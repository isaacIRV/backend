package com.example.deckservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "decks")
public class Deck {
    
    @Id
    private String id;
    
    @NotBlank(message = "El userId es obligatorio")
    private String userId;
    
    @NotBlank(message = "El nombre del mazo es obligatorio")
    @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
    private String deckName;
    
    private List<String> cards = new ArrayList<>();
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Estadísticas del mazo (opcional)
    private int wins = 0;
    private int losses = 0;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDeckRequest {
        @NotBlank(message = "El userId es obligatorio")
        private String userId;
        
        @NotBlank(message = "El nombre del mazo es obligatorio")
        private String deckName;
        
        private List<String> cards = new ArrayList<>();
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateDeckRequest {
        private String deckName;
        private List<String> cards;
    }
}