package com.example.gameDistribution.Repository;

import com.example.gameDistribution.Entity.AuthEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AuthRepo extends MongoRepository<AuthEntity, String> {
    Optional<AuthEntity> findByUsername(String username);

}
