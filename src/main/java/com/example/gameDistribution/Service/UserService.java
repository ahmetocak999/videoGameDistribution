package com.example.gameDistribution.Service;

import com.example.gameDistribution.DTO.UserDTO;
import com.example.gameDistribution.Entity.CommentEntity;
import com.example.gameDistribution.Entity.GameEntity;
import com.example.gameDistribution.Entity.UserEntity;
import com.example.gameDistribution.Mapper.UserMapper;
import com.example.gameDistribution.Repository.GameRepo;
import com.example.gameDistribution.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream().map(userMapper::toDTO).toList();
    }

    public void addUser(UserDTO userDTO) {
        userRepo.save(userMapper.toEntity(userDTO));
    }

    public boolean deleteUserById(String id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean playGame(String userId, String gameId, double hours) {
        Optional<GameEntity> gameopt = gameRepo.findById(gameId);
        Optional<UserEntity> useropt = userRepo.findById(userId);

        if(gameopt.isPresent() && useropt.isPresent()){
            GameEntity game = gameopt.get();
            UserEntity user = useropt.get();

            Map<String,Double> playedGames = user.getPlayedGames();
            double previous = playedGames.getOrDefault(gameId,0.0);
            double updatedPlayTime = previous + hours;
            playedGames.put(gameId,updatedPlayTime);

            user.setTotalPlayTime(user.getTotalPlayTime() + hours);

            String mostPlayedGameId = playedGames.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
            user.setMostPlayedGame(mostPlayedGameId);

            game.setPlayTime(game.getPlayTime() + hours);

            userRepo.save(user);
            gameRepo.save(game);

            return true;

        }
        return false;
    }

    public boolean rateGame(String userId, String gameId, int rating) {
        Optional<UserEntity> userOpt = userRepo.findById(userId);
        Optional<GameEntity> gameOpt = gameRepo.findById(gameId);

        if(userOpt.isPresent() && gameOpt.isPresent()) {
            UserEntity user = userOpt.get();
            GameEntity game = gameOpt.get();

            if (!user.getPlayedGames().containsKey(gameId) ||
                    user.getPlayedGames().get(gameId) < 1.0) {
                return false;
            }

            user.getRatedGames().put(gameId,rating);
            game.getUserRatings().put(userId,rating);

            double weightedSum = 0;
            double totalPlayTime = 0;

            for (Map.Entry<String, Integer> entry : game.getUserRatings().entrySet()) {
                String uid = entry.getKey();
                int r = entry.getValue();
                Optional<UserEntity> u = userRepo.findById(uid);

                if(u.isPresent()) {
                    double userPlayTime = u.get().getPlayedGames().getOrDefault(gameId,0.0);
                    weightedSum += userPlayTime * r;
                    totalPlayTime += userPlayTime;
                }
            }
            if (totalPlayTime > 0){
                double averageRating = weightedSum/totalPlayTime;
                game.setRating(averageRating);
            }
            double userRatingSum = user.getRatedGames().values().stream()
                    .mapToInt(Integer::intValue).sum();
            int ratingCount = user.getRatedGames().size();
            user.setAverageRating(userRatingSum / (double) ratingCount);

            userRepo.save(user);
            gameRepo.save(game);

            return true;
        }

        return false;
    }

    public boolean commentGame(String userId, String gameId, String content) {

        Optional<UserEntity> userOpt = userRepo.findById(userId);
        Optional<GameEntity> gameOpt = gameRepo.findById(gameId);

        if(userOpt.isPresent() && gameOpt.isPresent()) {
            UserEntity user = userOpt.get();
            GameEntity game = gameOpt.get();

            double playTime = user.getPlayedGames().getOrDefault(gameId,0.0);
            if(playTime < 1.0){
                return false;
            }

            user.getComments().removeIf(c -> c.getGameId().equals(gameId));
            game.getAllComments().removeIf(c -> c.getUserId().equals(userId));

            CommentEntity comment = new CommentEntity();
            comment.setUserId(user.getId());
            comment.setUserName(user.getName());
            comment.setGameId(game.getId());
            comment.setGameName(game.getName());
            comment.setContent(content);
            comment.setPlayTime(playTime);

            user.getComments().add(comment);
            game.getAllComments().add(comment);

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
}
