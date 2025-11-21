package com.example.deckservice.service;

import com.example.deckservice.model.Deck;
import com.example.deckservice.repository.DeckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeckService {

    private final DeckRepository deckRepository;

    public List<Deck> getDecksByUser(String userId) {
        return deckRepository.findByUserId(userId);
    }

    public Deck getDeck(String deckId, String userId) {
        return deckRepository.findByIdAndUserId(deckId, userId)
                .orElseThrow(() -> new RuntimeException("Mazo no encontrado"));
    }

    public Deck createDeck(Deck.CreateDeckRequest request) {
        if (deckRepository.existsByUserIdAndDeckName(request.getUserId(), request.getDeckName())) {
            throw new RuntimeException("Ya tienes un mazo con ese nombre");
        }
        
        Deck newDeck = new Deck();
        newDeck.setUserId(request.getUserId());
        newDeck.setDeckName(request.getDeckName());
        newDeck.setCards(request.getCards() != null ? request.getCards() : new ArrayList<>());
        newDeck.setCreatedAt(LocalDateTime.now());
        newDeck.setUpdatedAt(LocalDateTime.now());
        
        return deckRepository.save(newDeck);
    }

    public Deck updateDeck(String deckId, String userId, Deck.UpdateDeckRequest updates) {
        Deck deck = getDeck(deckId, userId);
        
        if (updates.getDeckName() != null) {
            if (!updates.getDeckName().equals(deck.getDeckName()) && 
                deckRepository.existsByUserIdAndDeckName(userId, updates.getDeckName())) {
                throw new RuntimeException("Ya tienes un mazo con ese nombre");
            }
            deck.setDeckName(updates.getDeckName());
        }
        
        if (updates.getCards() != null) {
            deck.setCards(updates.getCards());
        }
        
        deck.setUpdatedAt(LocalDateTime.now());
        return deckRepository.save(deck);
    }

    public void deleteDeck(String deckId, String userId) {
        Deck deck = deckRepository.findByIdAndUserId(deckId, userId)
                .orElseThrow(() -> new RuntimeException("Mazo no encontrado"));
        deckRepository.delete(deck);
    }

    public Deck addCardToDeck(String deckId, String userId, String cardId) {
        Deck deck = getDeck(deckId, userId);
        
        if (!deck.getCards().contains(cardId)) {
            deck.getCards().add(cardId);
            deck.setUpdatedAt(LocalDateTime.now());
            return deckRepository.save(deck);
        }
        
        return deck;
    }

    public Deck removeCardFromDeck(String deckId, String userId, String cardId) {
        Deck deck = getDeck(deckId, userId);
        deck.getCards().remove(cardId);
        deck.setUpdatedAt(LocalDateTime.now());
        return deckRepository.save(deck);
    }
}