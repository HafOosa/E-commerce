// Correction pour Product.java
package com.hafsa.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;

    @JsonIgnore
    @ManyToOne  // Ajout de l'annotation manquante
    @JoinColumn(name = "category_id")
    private Category category;
}