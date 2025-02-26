package com.hafsa.ecommerce.controller;

import com.hafsa.ecommerce.dto.UserDTO;
import com.hafsa.ecommerce.model.User;
import com.hafsa.ecommerce.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/create")
    public User registerUser(@RequestBody UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    // Récupérer la liste de tous les utilisateurs
    @GetMapping("/getall")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    // Récupérer un utilisateur par son identifiant
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

}
