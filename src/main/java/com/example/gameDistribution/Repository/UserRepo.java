package com.example.gameDistribution.Repository;

import com.example.gameDistribution.Entity.UserEntity;
import com.example.gameapp.Entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepo extends MongoRepository<UserEntity,String> {
}
