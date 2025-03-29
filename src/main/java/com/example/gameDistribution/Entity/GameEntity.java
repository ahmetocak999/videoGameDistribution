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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Document(collection = "games")
public class GameEntity {
    @Id
    private String id;
    private String title;
    private String name;
    private String genre;
    private double rating = 0;

    private boolean ratingEnabled = true;
    private boolean commentEnabled = true;

    private double playTime = 0;

    private Map<String, Integer> userRatings = new HashMap<>();

    private List<CommentEntity> allComments = new ArrayList<>();





}
