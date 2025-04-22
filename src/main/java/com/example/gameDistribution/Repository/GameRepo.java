package com.example.gameDistribution.Repository;

import com.example.gameDistribution.Entity.GameEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepo extends MongoRepository<GameEntity,String> {
    Optional<GameEntity> findByTitle(String gameTitle);

    Page<GameEntity> findAll(Pageable pageable);

    Optional<GameEntity> findByTitleIgnoreCase(String title);

}
