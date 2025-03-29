package com.example.gameDistribution.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CommentDTO {
    private String userId;
    private String userName;
    private String gameId;
    private String gameName;
    private String content;
    private Double playTime;
}
