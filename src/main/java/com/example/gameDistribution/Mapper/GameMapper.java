package com.example.gameDistribution.Mapper;

import com.example.gameDistribution.DTO.GameDTO;
import com.example.gameDistribution.Entity.GameEntity;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    public GameDTO toDTO(GameEntity game){
        if(game==null){
            return null;
        }
        GameDTO gameDTO = new GameDTO();
        gameDTO.setId(game.getId());
        gameDTO.setGenre(game.getGenre());
        gameDTO.setTitle(game.getTitle());
        gameDTO.setRating(game.getRating());
        gameDTO.setRatingEnabled(game.isRatingEnabled());
        gameDTO.setCommentEnabled(game.isCommentEnabled());

        return gameDTO;
    }

    public GameEntity toEntity(GameDTO gameDTO){
        if(gameDTO==null){
            return null;
        }
        GameEntity gameEntity = new GameEntity();
        gameEntity.setId(gameDTO.getId());
        gameEntity.setGenre(gameDTO.getGenre());
        gameEntity.setTitle(gameDTO.getTitle());
        gameEntity.setRating(gameDTO.getRating());
        gameEntity.setRatingEnabled(gameDTO.isRatingEnabled());
        gameEntity.setCommentEnabled(gameDTO.isCommentEnabled());

        return gameEntity;
    }
}
