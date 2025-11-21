package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado: " + username));
    }

    public User registerUser(User.RegisterRequest request) {
        // Verificar si el usuario ya existe
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("El usuario ya existe: " + request.getUsername());
        }
        
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("El email ya está en uso: " + request.getEmail());
        }
        
        // Crear nuevo usuario
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword()); // En producción, encriptar
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(newUser);
    }

    public User authenticateUser(User.LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Usuario o contraseña incorrectos"));
        
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }
        
        // Actualizar última conexión
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User updateUser(String username, Map<String, Object> updates) {
        User user = getUserByUsername(username);
        
        // Validar y actualizar campos permitidos
        if (updates.containsKey("email")) {
            String newEmail = (String) updates.get("email");
            
            // Verificar si el nuevo email ya existe (y no es del mismo usuario)
            if (!newEmail.equals(user.getEmail()) && userRepository.existsByEmail(newEmail)) {
                throw new UserAlreadyExistsException("El email ya está en uso: " + newEmail);
            }
            user.setEmail(newEmail);
        }
        
        // MEJORA: Permitir actualizar más campos del juego TCG
        if (updates.containsKey("coins")) {
            user.setCoins((Integer) updates.get("coins"));
        }
        
        if (updates.containsKey("wins")) {
            user.setWins((Integer) updates.get("wins"));
        }
        
        if (updates.containsKey("losses")) {
            user.setLosses((Integer) updates.get("losses"));
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    // MEJORA: Método DELETE con validación
    public void delete(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    // NUEVO: Método para agregar monedas a un usuario
    public User addCoins(String username, int coins) {
        User user = getUserByUsername(username);
        user.setCoins(user.getCoins() + coins);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    // NUEVO: Método para actualizar estadísticas de juego
    public User updateGameStats(String username, boolean won) {
        User user = getUserByUsername(username);
        
        if (won) {
            user.setWins(user.getWins() + 1);
            user.setCoins(user.getCoins() + 50); // Recompensa por ganar
        } else {
            user.setLosses(user.getLosses() + 1);
            user.setCoins(user.getCoins() + 10); // Recompensa por participar
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
}