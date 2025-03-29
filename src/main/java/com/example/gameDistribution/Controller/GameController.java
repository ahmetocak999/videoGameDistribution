package com.example.gameDistribution.Controller;

import com.example.gameDistribution.DTO.GameDTO;
import com.example.gameDistribution.Service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    GameService gameService;

    @GetMapping("/all")
    public ResponseEntity<List<GameDTO>>getAllGames(){
        return ResponseEntity.ok(gameService.getAllGames());
    }
    @GetMapping("id/{id}")
    public ResponseEntity<GameDTO>getGameById(@PathVariable String id){
        return gameService.getGameById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping("/add")
    public ResponseEntity<String>addGame(@RequestBody GameDTO gameDTO){

        gameService.addGame(gameDTO);
        return ResponseEntity.ok("Game is added.");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGameById(@PathVariable String id){
        boolean deleted = gameService.deleteGameById(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/disable/{id}")
    public ResponseEntity<Void> disableRatingAndComment(@PathVariable String id){
        boolean updated = gameService.disableRatingAndComment(id);
        return updated ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/enable({id}")
    public ResponseEntity<Void> enableRatingAndComment(@PathVariable String id){
        boolean updated = gameService.enableRatingAndComment(id);
        return updated ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
