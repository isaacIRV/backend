package com.example.demo.controller;

import com.example.demo.service.UserService;
import com.example.demo.model.User;
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
            response.put("createdAt", user.getCreatedAt());
            
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
            response.put("coins", updatedUser.getCoins());
            response.put("updatedAt", updatedUser.getUpdatedAt());
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    // NUEVO: Endpoint DELETE para eliminar usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.delete(id);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Usuario eliminado exitosamente");
            response.put("id", id);
            
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    // NUEVO: Endpoint para obtener todos los usuarios (solo desarrollo)
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            var users = userService.getAll();
            
            Map<String, Object> response = new HashMap<>();
            response.put("total", users.size());
            response.put("users", users);
            
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