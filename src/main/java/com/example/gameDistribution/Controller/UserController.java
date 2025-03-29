package com.example.gameDistribution.Controller;

import com.example.gameDistribution.DTO.UserDTO;
import com.example.gameDistribution.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());

    }
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO){
        userService.addUser(userDTO);
        return ResponseEntity.ok("User is added.");

    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id){
        boolean deleted = userService.deleteUserById(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
    @PostMapping("/{userId}/play/{gameId}")
    public ResponseEntity<String> playGame(@PathVariable String userId,
                                           @PathVariable String gameId,
                                           @RequestParam double hours){
        boolean updated = userService.playGame(userId,gameId,hours);
        return updated ? ResponseEntity.ok("Playtime updated")
                : ResponseEntity.notFound().build();
    }
    @PostMapping("/{userId}/rate/{gameId}")
    public ResponseEntity<String> rateGame(@PathVariable String userId,
                                           @PathVariable String gameId,
                                           @RequestParam int rating){

        boolean success = userService.rateGame(userId,gameId,rating);
        return success ? ResponseEntity.ok("Rate updated.")
                : ResponseEntity.badRequest().body("You must play the game for at least 1 hour.");
    }

    @PostMapping("/{userId}/comment/{gameId}")
    public ResponseEntity<String> commentGame(@PathVariable String userId,
                                              @PathVariable String gameId,
                                              @RequestParam String content){
        boolean success = userService.commentGame(userId,gameId,content);
        return success ? ResponseEntity.ok("Comment submitted")
                : ResponseEntity.badRequest().body("You must play the game for at least 1 hour.");
    }
}
