package com.example.gameDistribution.Mapper;

import com.example.gameDistribution.DTO.UserDTO;
import com.example.gameDistribution.Entity.UserEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.stream.events.Comment;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    @Autowired
    CommentMapper commentMapper;

    public UserDTO toDTO(UserEntity userEntity){
        if (userEntity == null){
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setName(userEntity.getName());
        userDTO.setAverageRating(userEntity.getAverageRating());
        userDTO.setTotalPlayTime(userEntity.getTotalPlayTime());
        userDTO.setMostPlayedGame(userEntity.getMostPlayedGame());
        userDTO.setComments(userEntity.getComments().stream()
                .map(commentMapper::toDTO)
                .toList());
        userDTO.setLibraryGames(new HashSet<>(userEntity.getLibraryGames()));
        userDTO.setAuthId(userEntity.getAuthId());
        userDTO.setPlayedGames(userEntity.getPlayedGames() != null
                ? new HashMap<>(userEntity.getPlayedGames())
                : new HashMap<>());

        userDTO.setRatedGames(userEntity.getRatedGames() != null
                ? new HashMap<>(userEntity.getRatedGames())
                : new HashMap<>());



        return userDTO;
    }
    public UserEntity toEntity(UserDTO userDTO){
        if(userDTO == null){
            return null;
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDTO.getId());
        userEntity.setName(userDTO.getName());
        userEntity.setAverageRating(userDTO.getAverageRating());
        userEntity.setTotalPlayTime(userDTO.getTotalPlayTime());
        userEntity.setMostPlayedGame(userDTO.getMostPlayedGame());
        userEntity.setComments(userDTO.getComments().stream()
                .map(commentMapper::toEntity)
                .toList());
        userEntity.setLibraryGames(new HashSet<>(userDTO.getLibraryGames()));
        userEntity.setAuthId(userDTO.getAuthId());
        userEntity.setPlayedGames(userDTO.getPlayedGames() != null
                ? new HashMap<>(userDTO.getPlayedGames())
                : new HashMap<>());

        userEntity.setRatedGames(userDTO.getRatedGames() != null
                ? new HashMap<>(userDTO.getRatedGames())
                : new HashMap<>());


        return userEntity;
    }
}
