package com.hafsa.ecommerce.service;

import com.hafsa.ecommerce.dto.CartItemDTO;
import com.hafsa.ecommerce.model.Cart;
import com.hafsa.ecommerce.model.CartItem;
import com.hafsa.ecommerce.model.Product;
import com.hafsa.ecommerce.model.User;
import com.hafsa.ecommerce.repository.CartItemRepository;
import com.hafsa.ecommerce.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final ProductService productService;

    public Cart getCart() {
        User user = userService.getCurrentUser();
        return cartRepository.findByUser(user)
                .orElseGet(() -> createNewCart(user));
    }

    @Transactional
    public CartItem addToCart(CartItemDTO cartItemDTO) {
        Cart cart = getCart();
        Product product = productService.getProduct(cartItemDTO.getProductId());

        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }

        // Vérifier si le produit existe déjà dans le panier
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(cartItemDTO.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Mettre à jour la quantité si le produit existe déjà
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartItemDTO.getQuantity());
            return cartItemRepository.save(item);
        } else {
            // Créer un nouveau CartItem
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemDTO.getQuantity());
            cart.getItems().add(cartItem);
            return cartItemRepository.save(cartItem);
        }
    }

    @Transactional
    public void removeFromCart(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found"));

        // Vérifier que l'item appartient au panier de l'utilisateur actuel
        if (cartItem.getCart().getUser().equals(userService.getCurrentUser())) {
            cartItemRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Unauthorized to remove this cart item");
        }
    }

    @Transactional
    public CartItem updateQuantity(Long itemId, int quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found"));

        if (cartItem.getCart().getUser().equals(userService.getCurrentUser())) {
            cartItem.setQuantity(quantity);
            return cartItemRepository.save(cartItem);
        } else {
            throw new IllegalStateException("Unauthorized to update this cart item");
        }
    }

    public void clearCart() {
        Cart cart = getCart();
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private Cart createNewCart(User user) {
        Cart newCart = new Cart();
        newCart.setUser(user);
        return cartRepository.save(newCart);
    }

    public CartItemDTO mapCartItemToCartItemDTO(CartItem cartItem) {
        if (cartItem == null) {
            throw new IllegalArgumentException("CartItem cannot be null.");
        }

        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setProductId(cartItem.getProduct().getId());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        return cartItemDTO;
    }
}