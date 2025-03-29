package com.example.gameDistribution.Mapper;

import com.example.gameDistribution.DTO.UserDTO;
import com.example.gameDistribution.Entity.UserEntity;
import com.example.gameapp.DTO.UserDTO;
import com.example.gameapp.Entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
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
        return userEntity;
    }
}
