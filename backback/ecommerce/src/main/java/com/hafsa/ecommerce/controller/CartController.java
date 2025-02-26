package com.hafsa.ecommerce.controller;

import com.hafsa.ecommerce.dto.CartItemDTO;
import com.hafsa.ecommerce.model.Cart;
import com.hafsa.ecommerce.model.CartItem;
import com.hafsa.ecommerce.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin(origins = "*")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<Cart> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @PostMapping("/items")
    public ResponseEntity<CartItem> addToCart(@RequestBody CartItemDTO cartItemDTO) {
        return ResponseEntity.ok(cartService.addToCart(cartItemDTO));
    }

    @PutMapping("/items/{id}/quantity")
    public ResponseEntity<CartItem> updateQuantity(
            @PathVariable Long id,
            @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateQuantity(id, quantity));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long id) {
        cartService.removeFromCart(id);
        return ResponseEntity.ok().build();
    }
}