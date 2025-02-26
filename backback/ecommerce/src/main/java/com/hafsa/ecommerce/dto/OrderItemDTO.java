package com.hafsa.ecommerce.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long productId;
    private Integer quantity;
    private Double unitPrice;
}
