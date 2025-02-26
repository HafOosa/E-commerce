package com.hafsa.ecommerce.repository;

import com.hafsa.ecommerce.model.Cart;
import com.hafsa.ecommerce.model.CartItem;
import com.hafsa.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

     static CartItem save(CartItem cartItem) {

        return cartItem;
    }

    Optional<Cart> findByUser(User user);
}
