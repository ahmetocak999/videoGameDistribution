package com.example.gameDistribution.Service;

import com.example.gameDistribution.DTO.CommentDTO;
import com.example.gameDistribution.DTO.UserDTO;
import com.example.gameDistribution.Entity.AuthEntity;
import com.example.gameDistribution.Entity.CommentEntity;
import com.example.gameDistribution.Entity.GameEntity;
import com.example.gameDistribution.Entity.UserEntity;
import com.example.gameDistribution.Mapper.CommentMapper;
import com.example.gameDistribution.Mapper.UserMapper;
import com.example.gameDistribution.Repository.AuthRepo;
import com.example.gameDistribution.Repository.GameRepo;
import com.example.gameDistribution.Repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRepo userRepo;
    @Autowired
    GameRepo gameRepo;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthRepo authRepo;

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepo.findAll(pageable).map(userMapper::toDTO);
    }

    public void addUser(UserDTO userDTO) {
        userRepo.save(userMapper.toEntity(userDTO));
    }

    public boolean deleteUserById(String userId) {
        Optional<UserEntity> userOpt = userRepo.findById(userId);

        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();

            // 1. Kullanıcının oyunlara etkisini temizle
            for (Map.Entry<String, Double> entry : user.getPlayedGames().entrySet()) {
                String gameId = entry.getKey();
                double userPlayTime = entry.getValue();

                gameRepo.findById(gameId).ifPresent(game -> {
                    game.setPlayTime(game.getPlayTime() - userPlayTime);
                    gameRepo.save(game);
                });
            }

            for (Map.Entry<String, Integer> entry : user.getRatedGames().entrySet()) {
                String gameId = entry.getKey();

                gameRepo.findById(gameId).ifPresent(game -> {
                    game.getUserRatings().remove(user.getId());

                    // 🔁 Ağırlıklı ortalama hesaplama
                    double totalWeightedRating = 0.0;

                    for (Map.Entry<String, Integer> ratingEntry : game.getUserRatings().entrySet()) {
                        String raterUserId = ratingEntry.getKey();
                        int ratingValue = ratingEntry.getValue();

                        double raterPlayTime = userRepo.findById(raterUserId)
                                .map(u -> u.getPlayedGames().getOrDefault(game.getId(), 0.0))
                                .orElse(0.0);

                        totalWeightedRating += raterPlayTime * ratingValue;
                    }

                    double newWeightedRating = game.getPlayTime() > 0
                            ? totalWeightedRating / game.getPlayTime()
                            : 0.0;

                    game.setRating(newWeightedRating);
                    gameRepo.save(game);
                });
            }

            for (CommentEntity comment : user.getComments()) {
                String gameId = comment.getGameId();
                gameRepo.findById(gameId).ifPresent(game -> {
                    game.getAllComments().removeIf(c -> c.getUserId().equals(user.getId()));
                    gameRepo.save(game);
                });
            }

            // 2. AuthEntity'yi sil
            if (user.getAuthId() != null) {
                authRepo.deleteById(user.getAuthId());
            }

            // 3. UserEntity'yi sil
            userRepo.deleteById(userId);
            return true;

        } else {
            // Eğer User yok ama belki AuthEntity varsa...
            Optional<AuthEntity> authOpt = authRepo.findById(userId);
            if (authOpt.isPresent()) {
                authRepo.deleteById(userId);
                return true;
            }
        }

        return false;
    }




    public boolean playGame(HttpServletRequest request, String gameTitle, double hours) {
        String token = request.getHeader("Authorization").substring(7);
        String userName = jwtService.extractUsername(token);

        Optional<UserEntity> userOpt = userRepo.findByName(userName);
        Optional<GameEntity> gameOpt = gameRepo.findByTitle(gameTitle);

        if (userOpt.isPresent() && gameOpt.isPresent()) {
            UserEntity user = userOpt.get();
            GameEntity game = gameOpt.get();

            // ✅ Eğer daha önce oynanmadıysa ilk defa başlat
            user.getPlayedGames().putIfAbsent(game.getId(), 0.0);

            Map<String, Double> playedGames = user.getPlayedGames();
            double previous = playedGames.getOrDefault(game.getId(), 0.0);
            double updated = previous + hours;
            playedGames.put(game.getId(), updated);

            user.setTotalPlayTime(user.getTotalPlayTime() + hours);

            String mostPlayed = playedGames.entrySet().stream()
                    .filter(entry -> user.getLibraryGames().contains(entry.getKey())) // ✅ sadece library'deki oyunlar
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);

            user.setMostPlayedGame(mostPlayed);

            game.setPlayTime(game.getPlayTime() + hours);

            userRepo.save(user);
            gameRepo.save(game);
            return true;
        }

        return false;
    }



    public boolean rateGame(HttpServletRequest request, String gameTitle, int rating) {
        String token = request.getHeader("Authorization").substring(7); // "Bearer " kısmını çıkar
        String userName = jwtService.extractUsername(token);  // Kullanıcı adını çıkar

        Optional<UserEntity> userOpt = userRepo.findByName(userName);
        Optional<GameEntity> gameOpt = gameRepo.findByTitle(gameTitle);

        if (userOpt.isEmpty() || gameOpt.isEmpty()) return false;

        UserEntity user = userOpt.get();
        GameEntity game = gameOpt.get();

        if (!game.isRatingEnabled()) {
            return false; // ❌ Puanlama devre dışıysa işlemi durdur
        }




        double playTime = user.getPlayedGames().getOrDefault(game.getId(), 0.0);
        if (playTime < 1.0) return false;


        // ✅ Kullanıcının rating'i ekleniyor/güncelleniyor
        user.getRatedGames().put(game.getId(), rating);
        game.getUserRatings().put(user.getId(), rating);

        // ✅ Weighted Average Hesabı (Doğru Formül)
        double totalWeightedRating = 0.0;

        for (Map.Entry<String, Integer> entry : game.getUserRatings().entrySet()) {
            String raterUserId = entry.getKey();
            int userRating = entry.getValue();

            double userPlayTime = userRepo.findById(raterUserId)
                    .map(u -> u.getPlayedGames().getOrDefault(game.getId(), 0.0))
                    .orElse(0.0);

            totalWeightedRating += userPlayTime * userRating;
        }

        double newGameRating = game.getPlayTime() > 0 ? totalWeightedRating / game.getPlayTime() : 0.0;
        game.setRating(newGameRating);

        // ✅ Kullanıcının Ortalama Rating'i
        double newAvgRating = user.getRatedGames().values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
        user.setAverageRating(newAvgRating);

        // ✅ Kaydet
        userRepo.save(user);
        gameRepo.save(game);

        return true;
    }


    public boolean commentGame(HttpServletRequest request, String gameTitle, String content) {
        String token = request.getHeader("Authorization").substring(7); // "Bearer " kısmını kes
        String userName = jwtService.extractUsername(token); // Username çıkar

        Optional<UserEntity> userOpt = userRepo.findByName(userName);
        Optional<GameEntity> gameOpt = gameRepo.findByTitle(gameTitle);

        if (userOpt.isPresent() && gameOpt.isPresent()) {
            UserEntity user = userOpt.get();
            GameEntity game = gameOpt.get();

            if (!game.isCommentEnabled()) {
                return false; // ❌ Yorum devre dışıysa işlemi durdur
            }


            double playTime = user.getPlayedGames().getOrDefault(game.getId(), 0.0);
            if (playTime < 1.0) return false;

            // Önceki yorumları sil (update davranışı için)
            user.getComments().removeIf(c -> c.getGameId().equals(game.getId()));
            game.getAllComments().removeIf(c -> c.getUserId().equals(user.getId()));

            // Yeni yorum oluştur
            CommentEntity comment = new CommentEntity();
            comment.setUserId(user.getId());
            comment.setUserName(user.getName());
            comment.setGameId(game.getId());
            comment.setGameName(game.getTitle());
            comment.setContent(content);
            comment.setPlayTime(playTime);

            // Yorumları ekle
            user.getComments().add(comment);
            game.getAllComments().add(comment);

            // Yorumları oynama süresine göre sırala
            user.getComments().sort((c1, c2) -> Double.compare(
                    user.getPlayedGames().getOrDefault(c2.getGameId(), 0.0),
                    user.getPlayedGames().getOrDefault(c1.getGameId(), 0.0)
            ));

            userRepo.save(user);
            gameRepo.save(game);

            return true;
        }
        return false;
    }



    public Optional<UserDTO> getUserDetails(String username) {
        return userRepo.findByName(username)
                .map(user -> {
                    UserDTO userDTO = userMapper.toDTO(user);

                    // ⭐ Most Played Game'in sadece library'de varsa adını getir
                    String mostPlayedId = user.getMostPlayedGame();
                    if (mostPlayedId != null && user.getLibraryGames().contains(mostPlayedId)) {
                        gameRepo.findById(mostPlayedId)
                                .map(GameEntity::getTitle)
                                .ifPresent(userDTO::setMostPlayedGame);
                    } else {
                        userDTO.setMostPlayedGame(null); // ❗ Library'de yoksa null göster
                    }

                    // 💬 Yorumları DTO’ya çevir
                    List<CommentDTO> commentDTOs = user.getComments().stream()
                            .map(commentMapper::toDTO)
                            .toList();
                    userDTO.setComments(commentDTOs);

                    return userDTO;
                });
    }


    public boolean removeGameFromUser(HttpServletRequest request, String gameTitle) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);

        Optional<UserEntity> userOpt = userRepo.findByName(username);
        Optional<GameEntity> gameOpt = gameRepo.findByTitle(gameTitle);

        if (userOpt.isPresent() && gameOpt.isPresent()) {
            UserEntity user = userOpt.get();
            GameEntity game = gameOpt.get();

            String gameId = game.getId();

            // ✅ 0. Kütüphaneden kaldır (EKLENEN)
            user.getLibraryGames().remove(gameId);

            // 1. Play time'ı sil
            Double removedTimeObj = user.getPlayedGames().remove(gameId);
            double removedTime = (removedTimeObj != null) ? removedTimeObj : 0.0;
            user.setTotalPlayTime(user.getTotalPlayTime() - removedTime);

            // 2. Rating'i kaldır ve Game rating'i güncelle
            Integer removedRating = user.getRatedGames().remove(gameId);
            if (removedRating != null) {
                game.getUserRatings().remove(user.getId());

                double totalWeightedRating = 0.0;
                double totalPlayTime = 0.0;
                for (Map.Entry<String, Integer> entry : game.getUserRatings().entrySet()) {
                    String userId = entry.getKey();
                    int rating = entry.getValue();

                    double userPlayTime = userRepo.findById(userId)
                            .map(u -> u.getPlayedGames().getOrDefault(gameId, 0.0))
                            .orElse(0.0);

                    totalWeightedRating += rating * userPlayTime;
                    totalPlayTime += userPlayTime;
                }

                double newGameRating = totalPlayTime > 0 ? totalWeightedRating / totalPlayTime : 0.0;
                game.setRating(newGameRating);
            }

            // 3. Yorumu kaldır
            user.getComments().removeIf(c -> c.getGameId().equals(gameId));
            game.getAllComments().removeIf(c -> c.getUserId().equals(user.getId()));

            // 4. Most played game tekrar hesapla
            String mostPlayed = user.getPlayedGames().entrySet().stream()
                    .filter(entry -> user.getLibraryGames().contains(entry.getKey())) // sadece kütüphanedekiler
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);

            user.setMostPlayedGame(mostPlayed);

            // 5. Oyunun toplam playtime'ı azalt
            game.setPlayTime(game.getPlayTime() - removedTime);

            userRepo.save(user);
            gameRepo.save(game);

            return true;
        }

        return false;
    }


}
