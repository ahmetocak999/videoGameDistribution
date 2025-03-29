package com.example.gameDistribution.Entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("collection = users")
public class UserEntity {
    @Id
    private String id;
    private String name;
    private double totalPlayTime = 0;
    private double averageRating = 0;
    private String mostPlayedGame = null;

    private Map<String, Double> playedGames = new HashMap<>();

    private Map<String, Integer> ratedGames = new HashMap<>();

    private List<CommentEntity> comments = new ArrayList<>();




}
