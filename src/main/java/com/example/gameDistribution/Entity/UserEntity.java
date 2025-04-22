package com.example.gameDistribution.Entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class UserEntity {

    @Id
    private String id;

    private String name;
    private double totalPlayTime = 0;
    private double averageRating = 0;
    private String mostPlayedGame = null;
    private String authId;

    private Map<String, Double> playedGames = new HashMap<>();

    private Map<String, Integer> ratedGames = new HashMap<>();

    private List<CommentEntity> comments = new ArrayList<>();

    private Set<String> libraryGames = new HashSet<>();


    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public double getTotalPlayTime() {
        return totalPlayTime;
    }
    public void setTotalPlayTime(double totalPlayTime) {
        this.totalPlayTime = totalPlayTime;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
    public String getMostPlayedGame() {
        return mostPlayedGame;
    }

    public void setMostPlayedGame(String mostPlayedGame) {
        this.mostPlayedGame = mostPlayedGame;
    }

    public Map<String, Double> getPlayedGames() {
        return playedGames;
    }

    public void setPlayedGames(Map<String, Double> playedGames) {
        this.playedGames = playedGames;
    }

    public Map<String, Integer> getRatedGames() {
        return ratedGames;
    }

    public void setRatedGames(Map<String, Integer> ratedGames) {
        this.ratedGames = ratedGames;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public String getAuthId(){
        return authId;
    }
    public void setAuthId(String authId){
        this.authId = authId;
    }
    public Set<String> getLibraryGames(){
        return libraryGames;
    }
    public void setLibraryGames(Set<String> libraryGames){
        this.libraryGames = libraryGames;
    }


}
