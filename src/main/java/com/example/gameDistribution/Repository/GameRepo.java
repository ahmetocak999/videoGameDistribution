package com.example.gameDistribution.Repository;

import com.example.gameDistribution.Entity.GameEntity;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepo extends MongoRepository<GameEntity,String> {
}
