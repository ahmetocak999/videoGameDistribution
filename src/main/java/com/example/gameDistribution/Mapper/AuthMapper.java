package com.example.gameDistribution.Mapper;

import com.example.gameDistribution.DTO.AuthDTO;
import com.example.gameDistribution.Entity.AuthEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {
    public AuthEntity toEntity(AuthDTO authDTO){
        if(authDTO == null){
            return null;
        }
        AuthEntity entity = new AuthEntity();
        entity.setUsername(authDTO.getUsername());
        entity.setPassword(authDTO.getPassword());
        entity.setRole(authDTO.getRole());
        return entity;
    }

    public AuthDTO toDTO(AuthEntity entity){
        if(entity==null){
            return null;
        }
        AuthDTO authDTO = new AuthDTO();
        authDTO.setUsername(entity.getUsername());
        authDTO.setPassword(entity.getPassword());
        authDTO.setRole(entity.getRole());
        return authDTO;
    }
}
