package com.example.gameDistribution.Mapper;

import com.example.gameDistribution.DTO.CommentDTO;
import com.example.gameDistribution.Entity.CommentEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public CommentDTO toDTO(CommentEntity comment){
        if(comment==null) {
            return null;
        }
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setUserId(comment.getUserId());
        commentDTO.setGameId(comment.getGameId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setGameName(comment.getGameName());
        commentDTO.setUserName(comment.getUserName());
        commentDTO.setPlayTime(comment.getPlayTime());

        return commentDTO;
    }

    public CommentEntity toEntity(CommentDTO commentDTO){
        if(commentDTO == null){
            return null;
        }
        CommentEntity comment = new CommentEntity();
        comment.setUserName(commentDTO.getUserName());
        comment.setUserId(commentDTO.getUserId());
        comment.setGameId(commentDTO.getGameId());
        comment.setContent(commentDTO.getContent());
        comment.setGameName(commentDTO.getGameName());
        comment.setPlayTime(commentDTO.getPlayTime());

        return comment;
    }
}
