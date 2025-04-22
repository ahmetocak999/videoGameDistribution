package com.example.gameDistribution.Entity;

import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "games")
public class GameEntity {
    @Id
    private String id;
    private String title;

    private String genre;

    private double rating = 0;

    private String photo;


    private boolean ratingEnabled = true;

    private boolean commentEnabled = true;


    private double playTime = 0;


    private Map<String, Integer> userRatings = new HashMap<>();

    private List<CommentEntity> allComments = new ArrayList<>();

    private Map<String, String> optionalFields = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isRatingEnabled() {
        return ratingEnabled;
    }

    public void setRatingEnabled(boolean ratingEnabled) {
        this.ratingEnabled = ratingEnabled;
    }

    public boolean isCommentEnabled() {
        return commentEnabled;
    }

    public void setCommentEnabled(boolean commentEnabled) {
        this.commentEnabled = commentEnabled;
    }

    public double getPlayTime() {
        return playTime;
    }

    public void setPlayTime(double playTime) {
        this.playTime = playTime;
    }

    public Map<String, Integer> getUserRatings() {
        return userRatings;
    }

    public void setUserRatings(Map<String, Integer> userRatings) {
        this.userRatings = userRatings;
    }

    public List<CommentEntity> getAllComments() {
        return allComments;
    }

    public void setAllComments(List<CommentEntity> allComments) {
        this.allComments = allComments;
    }

    public Map<String, String> getOptionalFields() {
        return optionalFields;
    }

    public void setOptionalFields(Map<String, String> optionalFields) {
        this.optionalFields = optionalFields;
    }




}
