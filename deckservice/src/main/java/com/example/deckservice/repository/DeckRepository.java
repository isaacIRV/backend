package com.example.deckservice.repository;

import com.example.deckservice.model.Deck;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeckRepository extends MongoRepository<Deck, String> {
    List<Deck> findByUserId(String userId);
    Optional<Deck> findByIdAndUserId(String id, String userId);
    boolean existsByUserIdAndDeckName(String userId, String deckName);
    void deleteByIdAndUserId(String id, String userId);
}