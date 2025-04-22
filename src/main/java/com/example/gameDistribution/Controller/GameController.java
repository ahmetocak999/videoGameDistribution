package com.example.gameDistribution.Controller;

import com.example.gameDistribution.DTO.GameDTO;
import com.example.gameDistribution.Service.GameService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {
    @Autowired
    GameService gameService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/all")
    public ResponseEntity<Page<GameDTO>> getGamesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(gameService.getGamesPaginated(pageable));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/my-games")
    public ResponseEntity<List<GameDTO>> getMyGames(HttpServletRequest request) {
        return gameService.getMyGames(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("id/{id}")
    public ResponseEntity<GameDTO>getGameById(@PathVariable String id){
        return gameService.getGameById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<String>addGame(@RequestBody GameDTO gameDTO){

        gameService.addGame(gameDTO);
        return ResponseEntity.ok("Game is added.");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGameById(@PathVariable String id){
        boolean deleted = gameService.deleteGameById(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/toggle-rating/{id}")
    public ResponseEntity<Void> toggleRating(@PathVariable String id) {
        boolean updated = gameService.toggleRating(id);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/toggle-comment/{id}")
    public ResponseEntity<Void> toggleComment(@PathVariable String id) {
        boolean updated = gameService.toggleComment(id);
        return updated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{title}")
    public ResponseEntity<GameDTO> getGameDetails(@PathVariable String title){
        return gameService.getGameDetails(title)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/add-to-library")
    public ResponseEntity<String> addGameToLibrary(HttpServletRequest request, @RequestParam String gameTitle) {
        return gameService.addGameToLibrary(request, gameTitle);
    }

}
