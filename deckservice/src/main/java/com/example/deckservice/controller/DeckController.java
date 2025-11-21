package com.example.deckservice.controller;

import com.example.deckservice.model.Deck;
import com.example.deckservice.service.DeckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/decks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DeckController {

    private final DeckService deckService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Deck>> getUserDecks(@PathVariable String userId) {
        List<Deck> decks = deckService.getDecksByUser(userId);
        return ResponseEntity.ok(decks);
    }

    @GetMapping("/{deckId}")
    public ResponseEntity<Deck> getDeck(@PathVariable String deckId, @RequestHeader String userId) {
        Deck deck = deckService.getDeck(deckId, userId);
        return ResponseEntity.ok(deck);
    }

    @PostMapping
    public ResponseEntity<?> createDeck(@Valid @RequestBody Deck.CreateDeckRequest request) {
        try {
            Deck deck = deckService.createDeck(request);
            return ResponseEntity.ok(deck);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{deckId}")
    public ResponseEntity<?> updateDeck(@PathVariable String deckId, 
                                      @RequestHeader String userId,
                                      @Valid @RequestBody Deck.UpdateDeckRequest updates) {
        try {
            Deck deck = deckService.updateDeck(deckId, userId, updates);
            return ResponseEntity.ok(deck);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{deckId}")
    public ResponseEntity<?> deleteDeck(@PathVariable String deckId, @RequestHeader String userId) {
        try {
            deckService.deleteDeck(deckId, userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Mazo eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/{deckId}/cards/{cardId}")
    public ResponseEntity<?> addCardToDeck(@PathVariable String deckId, 
                                         @PathVariable String cardId,
                                         @RequestHeader String userId) {
        try {
            Deck deck = deckService.addCardToDeck(deckId, userId, cardId);
            return ResponseEntity.ok(deck);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/{deckId}/cards/{cardId}")
    public ResponseEntity<?> removeCardFromDeck(@PathVariable String deckId, 
                                              @PathVariable String cardId,
                                              @RequestHeader String userId) {
        try {
            Deck deck = deckService.removeCardFromDeck(deckId, userId, cardId);
            return ResponseEntity.ok(deck);
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