package com.example.demo.controller;

import com.example.demo.service.UserService;
import com.example.demo.model.User;  // ← ¡AGREGA ESTA IMPORTACIÓN!
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserProfile(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            
            Map<String, Object> response = new HashMap<>();
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("coins", user.getCoins());
            response.put("wins", user.getWins());
            response.put("losses", user.getLosses());
            response.put("decks", user.getDecks());
            response.put("cardCollection", user.getCardCollection());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable String username,
            @RequestBody Map<String, Object> updates) {
        try {
            User updatedUser = userService.updateUser(username, updates);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Perfil actualizado exitosamente");
            response.put("username", updatedUser.getUsername());
            response.put("email", updatedUser.getEmail());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        return errorResponse;
    }
}