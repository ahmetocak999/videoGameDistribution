package com.example.gameDistribution.DTO;

import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private String userId;
    private String userName;
    private String gameId;
    private String gameName;
    private String content;
    private Double playTime;

    public String getUserId(){
        return userId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getUserName(){
        return userName;
    }
    public void setUserName(String userName){
        this.userName = userName;
    }
    public String getGameId(){
        return gameId;
    }
    public void setGameId(String gameId){
        this.gameId = gameId;
    }
    public String getGameName(){
        return gameName;
    }
    public void setGameName(String gameName){
        this.gameName = gameName;
    }
    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }
    public Double getPlayTime(){
        return playTime;
    }
    public void setPlayTime(Double playTime){
        this.playTime = playTime;
    }


}
