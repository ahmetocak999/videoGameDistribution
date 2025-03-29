package com.example.gameDistribution.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameDTO {
    private String id;
    private String title;
    private String genre;
    private double rating;

    private boolean ratingEnabled;
    private boolean commentEnabled;

    private List<CommentDTO> allComments;


}
