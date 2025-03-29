package com.example.gameDistribution.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String id;
    private String name;
    private double totalPlayTime;
    private double averageRating;
    private String mostPlayedGame;

    private List<CommentDTO> comments;

}
