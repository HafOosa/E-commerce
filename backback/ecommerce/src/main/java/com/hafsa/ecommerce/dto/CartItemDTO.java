package com.hafsa.ecommerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemDTO {
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    // Champs additionnels pour l'affichage côté client
    private String productName;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String imageUrl;
    private Boolean inStock;
}