package com.example.gameDistribution.Mapper;

import com.example.gameDistribution.DTO.GameDTO;
import com.example.gameDistribution.Entity.GameEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GameMapper {

    @Autowired
    private CommentMapper commentMapper;

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
        gameDTO.setPhoto(game.getPhoto());
        gameDTO.setPlayTime(game.getPlayTime());
        gameDTO.setOptionalFields(game.getOptionalFields());
        gameDTO.setAllComments(
                game.getAllComments().stream()
                        .map(commentMapper::toDTO)
                        .collect(Collectors.toList()));

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
        gameEntity.setOptionalFields(gameDTO.getOptionalFields());
        gameEntity.setPlayTime(gameDTO.getPlayTime());
        gameEntity.setAllComments(gameDTO.getAllComments()!= null ?
                gameDTO.getAllComments().stream()
                        .map(commentMapper::toEntity)
                        .collect(Collectors.toList()): null);
        gameEntity.setPhoto(gameDTO.getPhoto());
        return gameEntity;
    }
}
