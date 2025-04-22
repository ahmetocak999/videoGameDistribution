package com.example.gameDistribution.Service;

import com.example.gameDistribution.DTO.CommentDTO;
import com.example.gameDistribution.DTO.GameDTO;
import com.example.gameDistribution.Entity.GameEntity;
import com.example.gameDistribution.Entity.UserEntity;
import com.example.gameDistribution.Mapper.CommentMapper;
import com.example.gameDistribution.Mapper.GameMapper;
import com.example.gameDistribution.Repository.GameRepo;

import com.example.gameDistribution.Repository.UserRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService {

    @Autowired
    GameMapper gameMapper;
    @Autowired
    GameRepo gameRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    JwtService jwtService;

    public Page<GameDTO> getGamesPaginated(Pageable pageable) {
        return gameRepo.findAll(pageable).map(gameMapper::toDTO);
    }


    public GameEntity addGame(GameDTO gameDTO) {
        if (gameRepo.findByTitle(gameDTO.getTitle()).isPresent()) {
            throw new IllegalArgumentException("A game with the same title already exists.");
        }

        GameEntity game = gameMapper.toEntity(gameDTO);

        // Başlangıç değerleri
        game.setRating(0);
        game.setPlayTime(0);
        game.setRatingEnabled(true);
        game.setCommentEnabled(true);
        game.setUserRatings(new HashMap<>());
        game.setAllComments(new ArrayList<>());

        return gameRepo.save(game);
    }


    public Optional<GameDTO> getGameById(String id) {
        return gameRepo.findById(id)
                .map(gameMapper::toDTO);
    }

    public boolean deleteGameById(String id) {
        Optional<GameEntity> optionalGame = gameRepo.findById(id);
        if (optionalGame.isPresent()) {
            GameEntity game = optionalGame.get();

            // 1. Oyun silinmeden önce kullanıcı verilerini güncelle
            List<UserEntity> allUsers = userRepo.findAll();
            for (UserEntity user : allUsers) {

                // Oyun bu kullanıcı tarafından oynanmış mı?
                if (user.getPlayedGames().containsKey(id)) {
                    // Yorumları temizle
                    user.getComments().removeIf(c -> c.getGameId().equals(id));

                    // Rating'i temizle
                    user.getRatedGames().remove(id);

                    // Most Played Game güncelle
                    Map<String, Double> playedGames = user.getPlayedGames();
                    playedGames.remove(id);

                    String newMostPlayed = playedGames.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .map(Map.Entry::getKey)
                            .orElse(null);
                    user.setMostPlayedGame(newMostPlayed);

                    // Ortalama rating güncelle
                    double newAvgRating = user.getRatedGames().values().stream()
                            .mapToInt(Integer::intValue)
                            .average().orElse(0.0);
                    user.setAverageRating(newAvgRating);

                    userRepo.save(user); // her kullanıcı için güncelle
                }
            }

            // 2. Game içindeki yorum ve rating'leri temizle
            game.setAllComments(new ArrayList<>());
            game.setUserRatings(new HashMap<>());

            // 3. Game'i sil
            gameRepo.delete(game);
            return true;
        }

        return false;
    }


    public boolean toggleRating(String id) {
        Optional<GameEntity> optionalGame = gameRepo.findById(id);
        if (optionalGame.isPresent()) {
            GameEntity game = optionalGame.get();
            game.setRatingEnabled(!game.isRatingEnabled());
            gameRepo.save(game);
            return true;
        }
        return false;
    }


    public boolean toggleComment(String id) {
        Optional<GameEntity> optionalGame = gameRepo.findById(id);
        if (optionalGame.isPresent()) {
            GameEntity game = optionalGame.get();
            game.setCommentEnabled(!game.isCommentEnabled());
            gameRepo.save(game);
            return true;
        }
        return false;
    }



    public Optional<GameDTO> getGameDetails(String title) {
        return gameRepo.findByTitle(title).map(game -> {
            GameDTO gameDTO = gameMapper.toDTO(game);
            List<CommentDTO> sortedComments = game.getAllComments().stream()
                    .sorted((c1, c2) -> Double.compare(c2.getPlayTime(), c1.getPlayTime()))
                    .map(commentMapper::toDTO)
                    .toList();
            gameDTO.setAllComments(sortedComments);
            return gameDTO;
        });
    }

    public ResponseEntity<List<GameDTO>> getMyGames(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);

        Optional<UserEntity> userOpt = userRepo.findByName(username);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            Set<String> ownedGameIds = user.getLibraryGames();

            List<GameDTO> ownedGames = gameRepo.findAllById(ownedGameIds).stream()
                    .map(game -> {
                        GameDTO dto = gameMapper.toDTO(game);
                        // ✅ Sadece bu kullanıcıya ait playTime setleniyor
                        dto.setPlayTime(user.getPlayedGames().getOrDefault(game.getId(), 0.0));
                        return dto;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ownedGames);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }



    public ResponseEntity<String> addGameToLibrary(HttpServletRequest request, String gameTitle) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            String username = jwtService.extractUsername(token);

            Optional<UserEntity> userOpt = userRepo.findByName(username);
            Optional<GameEntity> gameOpt = gameRepo.findByTitleIgnoreCase(gameTitle);


            if (userOpt.isPresent() && gameOpt.isPresent()) {
                UserEntity user = userOpt.get();
                GameEntity game = gameOpt.get();

                String gameId = game.getId();

                if (user.getLibraryGames() == null)
                    user.setLibraryGames(new HashSet<>());

                if (user.getLibraryGames().contains(gameId)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("You already added this game to your library.");
                }

                user.getLibraryGames().add(gameId);
                userRepo.save(user);

                return ResponseEntity.ok("Game added to your library.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Game or user not found.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal error: " + e.getMessage());
        }
    }



}
