package com.example.gameDistribution.Service;

import com.example.gameDistribution.DTO.GameDTO;
import com.example.gameDistribution.Entity.GameEntity;
import com.example.gameDistribution.Mapper.GameMapper;
import com.example.gameDistribution.Repository.GameRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    GameMapper gameMapper;
    @Autowired
    GameRepo gameRepo;

    public List<GameDTO> getAllGames() {
        return gameRepo.findAll().stream().map(gameMapper::toDTO).toList();

    }

    public GameEntity addGame(GameDTO gameDTO) {
        GameEntity game = gameRepo.save(gameMapper.toEntity(gameDTO));
        return game;
    }

    public Optional<GameDTO> getGameById(String id) {
        return gameRepo.findById(id)
                .map(gameMapper::toDTO);
    }

    public boolean deleteGameById(String id) {
        if(gameRepo.existsById(id)){
            gameRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean disableRatingAndComment(String id) {
        Optional<GameEntity> optionalGame = gameRepo.findById(id);
        if (optionalGame.isPresent()) {
            GameEntity game = optionalGame.get();
            game.setRatingEnabled(false);
            game.setCommentEnabled(false);
            gameRepo.save(game);
            return true;
        } else {
            return false;
        }
    }

    public boolean enableRatingAndComment(String id) {
        Optional<GameEntity> optionalGame = gameRepo.findById(id);
        if (optionalGame.isPresent()) {
            GameEntity game = optionalGame.get();
            game.setRatingEnabled(true);
            game.setCommentEnabled(true);
            gameRepo.save(game);
            return true;
        } else {
            return false;
        }
    }


}
