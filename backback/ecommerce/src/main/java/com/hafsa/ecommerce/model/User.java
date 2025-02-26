package com.hafsa.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)

    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public void setUsername(String dummyUser) {
    }


    public enum Role {
        ADMIN, CUSTOMER
    }
}
