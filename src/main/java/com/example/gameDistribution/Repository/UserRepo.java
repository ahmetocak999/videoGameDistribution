package com.example.gameDistribution.Repository;

import com.example.gameDistribution.Entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<UserEntity,String> {
    Optional<UserEntity> findByName(String userName);

    Page<UserEntity> findAll(Pageable pageable);
}
