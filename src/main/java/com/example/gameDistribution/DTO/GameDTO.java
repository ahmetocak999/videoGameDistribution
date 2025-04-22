package com.example.gameDistribution.DTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {

    private String id;

    private String title;


    private String genre;

    private double rating;

    private String photo;

    private double playTime;


    private boolean ratingEnabled;

    private boolean commentEnabled;

    private List<CommentDTO> allComments;

    private Map<String, String> optionalFields;

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

    public List<CommentDTO> getAllComments() {
        return allComments;
    }

    public void setAllComments(List<CommentDTO> allComments) {
        this.allComments = allComments;
    }

    public Map<String, String> getOptionalFields() {
        return optionalFields;
    }

    public void setOptionalFields(Map<String, String> optionalFields) {
        this.optionalFields = optionalFields;
    }
    public double getPlayTime(){
        return playTime;
    }
    public void setPlayTime(double playTime){
        this.playTime = playTime;
    }





}
