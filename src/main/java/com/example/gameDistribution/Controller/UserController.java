package com.example.gameDistribution.Controller;

import com.example.gameDistribution.DTO.UserDTO;
import com.example.gameDistribution.Service.JwtService;
import com.example.gameDistribution.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Page<UserDTO>> getAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size)
    {
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username){
        return userService.getUserDetails(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO){
        userService.addUser(userDTO);
        return ResponseEntity.ok("User is added.");

    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id){
        boolean deleted = userService.deleteUserById(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/play")
    public ResponseEntity<String> playGame(@RequestParam String gameTitle,
                                           @RequestParam double hours,
                                           HttpServletRequest request) {
        boolean updated = userService.playGame(request, gameTitle, hours);
        return updated ? ResponseEntity.ok("Playtime updated")
                : ResponseEntity.notFound().build();
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/rate")
    public ResponseEntity<String> rateGame(@RequestParam String gameTitle,
                                           @RequestParam int rating,
                                           HttpServletRequest request) {

        boolean success = userService.rateGame(request, gameTitle, rating);

        return success ? ResponseEntity.ok("Rate updated.")
                : ResponseEntity.badRequest().body("You must play the game for at least 1 hour.");
    }



    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/comment")
    public ResponseEntity<String> commentGame(@RequestParam String gameTitle,
                                              @RequestParam String content,
                                              HttpServletRequest request) {
        boolean success = userService.commentGame(request, gameTitle, content);
        return success ? ResponseEntity.ok("Comment submitted")
                : ResponseEntity.badRequest().body("You must play the game for at least 1 hour.");
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        return userService.getUserDetails(authentication.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/remove-from-user")
    public ResponseEntity<String> removeGameFromUser(@RequestParam String gameTitle, HttpServletRequest request) {
        boolean removed = userService.removeGameFromUser(request, gameTitle);
        return removed
                ? ResponseEntity.ok("Game removed from your library.")
                : ResponseEntity.badRequest().body("Failed to remove game from your library.");
    }



}
