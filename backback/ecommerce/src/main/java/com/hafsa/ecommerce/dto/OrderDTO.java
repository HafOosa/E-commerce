package com.hafsa.ecommerce.dto;

import lombok.Data;

import java.util.Collection;
import java.util.List;
@Data
public class OrderDTO {
    private Long userId;
    private List<OrderItemDTO> orderItems;
    private Double totalAmount;


}
