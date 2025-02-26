package com.hafsa.ecommerce.service;

import com.hafsa.ecommerce.dto.OrderDTO;
import com.hafsa.ecommerce.dto.OrderItemDTO;
import com.hafsa.ecommerce.model.Order;
import com.hafsa.ecommerce.model.OrderItem;
import com.hafsa.ecommerce.model.User;
import com.hafsa.ecommerce.model.Product;
import com.hafsa.ecommerce.repository.OrderRepository;
import com.hafsa.ecommerce.repository.OrderItemRepository;
import com.hafsa.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;


    public OrderService(OrderRepository orderRepository,
                        ProductRepository productRepository,
                        OrderItemRepository orderItemRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.userService = userService;
    }

    public Order createOrder(OrderDTO orderDTO) {
        // Créer une nouvelle commande
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.OrderStatus.PENDING);
        order.setTotalAmount(orderDTO.getTotalAmount());

        // Sauvegarder d'abord la commande
        Order savedOrder = orderRepository.save(order);

        // Traiter les items de la commande
        if (orderDTO.getOrderItems() != null && !orderDTO.getOrderItems().isEmpty()) {
            orderDTO.getOrderItems().forEach(itemDTO -> {
                OrderItem orderItem = createOrderItem(itemDTO, savedOrder);
                savedOrder.addOrderItem(orderItem);
            });

            // Sauvegarder les items
            orderRepository.save(savedOrder);
        }

        return savedOrder;
    }

    private OrderItem createOrderItem(OrderItemDTO itemDTO, Order order) {
        // Vérifier si le produit existe
        Product product = productRepository.findById(itemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemDTO.getProductId()));

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(itemDTO.getQuantity());
        orderItem.setUnitPrice(itemDTO.getUnitPrice());

        return orderItem;
    }

    // Méthode pour obtenir toutes les commandes de l'utilisateur courant
    public List<Order> getAllOrders() {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }
        try {
            return orderRepository.findAllByUser(currentUser);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching orders: " + e.getMessage());
        }
    }

    @Transactional
    public Order updateOrder(Long id, OrderDTO orderDTO) {
        // Récupérer la commande existante
        Order order = getOrderById(id);
        if (order == null) {
            throw new RuntimeException("Order not found with id: " + id);
        }

        // Mettre à jour le montant total
        order.setTotalAmount(orderDTO.getTotalAmount());

        // Mettre à jour les items
        if (orderDTO.getOrderItems() != null) {
            // Supprimer les anciens items
            orderItemRepository.deleteByOrderId(id);
            order.getOrderItems().clear();

            // Créer et ajouter les nouveaux items
            orderDTO.getOrderItems().forEach(itemDTO -> {
                OrderItem newItem = createOrderItem(itemDTO, order);
                order.addOrderItem(newItem);
            });
        }

        // Sauvegarder et retourner la commande mise à jour
        return orderRepository.save(order);
    }

    // Méthode utilitaire pour obtenir une commande par ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }


    @Transactional
    public void deleteOrder(Long id) {
        // Vérifier si l'utilisateur est authentifié
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        // Récupérer et vérifier l'existence de la commande
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        // Vérifier si l'utilisateur a le droit de supprimer cette commande
        if (!order.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to delete this order");
        }

        try {
            // Supprimer d'abord les items associés
            orderItemRepository.deleteByOrderId(id);

            // Puis supprimer la commande
            orderRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting order: " + e.getMessage());
        }
    }
}