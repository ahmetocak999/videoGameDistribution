package com.example.gameDistribution.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
    private String userId;
    private String userName;
    private String gameId;
    private String gameName;
    private String content;
    private Double playTime;
}
