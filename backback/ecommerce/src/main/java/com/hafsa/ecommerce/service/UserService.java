package com.hafsa.ecommerce.service;

import com.hafsa.ecommerce.dto.UserDTO;
import com.hafsa.ecommerce.model.User;
import com.hafsa.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User registerUser(UserDTO userDTO) {
        User user = new User();
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        return userRepository.save(user);
    }


    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id : " + id));
    }


    public User getCurrentUser() {
        String dummyEmail = "dummy@example.com";

        // On recherche l'utilisateur par email
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmail(dummyEmail));

        if (existingUser.isPresent()) {
            // S'il existe, on le retourne
            return existingUser.get();
        } else {
            // Sinon, on crée un nouvel utilisateur
            User user = new User();
            user.setEmail(dummyEmail);
            user.setUsername(dummyEmail);
            return userRepository.save(user);
        }
    }
}

