package com.hafsa.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime orderDate = LocalDateTime.now(); // Initialisation automatique

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING; // Statut par défaut

    private Double totalAmount = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    // Méthode utilitaire pour ajouter un item
    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }
    // Méthode pour obtenir les items
    public List<OrderItem> getItems() {
        return this.orderItems;
    }

    // Méthode utile pour ajouter un item
    public void addItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    // Méthode utile pour supprimer un item
    public void removeItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
    }

    public enum OrderStatus {
        PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
    }
}