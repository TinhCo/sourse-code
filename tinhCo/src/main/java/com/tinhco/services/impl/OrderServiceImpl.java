package com.tinhco.services.impl;

import com.tinhco.auth.entities.User;
import com.tinhco.auth.repositories.UserRepository;
import com.tinhco.entities.Order;
import com.tinhco.exceptions.OrderNotFoundException;
import com.tinhco.repositories.OrderRepository;
import com.tinhco.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Order createOrder(Order order) {
        logger.info("Creating new order: {}", order);

        // Kiểm tra và thiết lập user
        if (order.getUser() != null) {
            User user = userRepository.findById(order.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user!"));
            order.setUser(user);
        }

        // Kiểm tra và thiết lập updatedBy
        if (order.getUpdatedBy() != null) {
            User updatedBy = userRepository.findById(order.getUpdatedBy().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user update!"));
            order.setUpdatedBy(updatedBy);
        }

        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        logger.info("Fetching all orders");
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        logger.info("Fetching order with id: {}", id);
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found for id: " + id));
    }

    @Override
    public Order updateOrder(Long id, Order order) {
        logger.info("Updating order with id: {}", id);
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found for id: " + id));

        existingOrder.setName(order.getName());
        existingOrder.setPhone(order.getPhone());
        existingOrder.setEmail(order.getEmail());
        existingOrder.setAddress(order.getAddress());
        existingOrder.setNote(order.getNote());
        existingOrder.setStatus(order.getStatus());

        // Kiểm tra và cập nhật user (nếu có)
        if (order.getUser() != null) {
            User user = userRepository.findById(order.getUser().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user!"));
            existingOrder.setUser(user);
        }

        // Kiểm tra và cập nhật updatedBy (nếu có)
        if (order.getUpdatedBy() != null) {
            User updatedBy = userRepository.findById(order.getUpdatedBy().getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user update!"));
            existingOrder.setUpdatedBy(updatedBy);
        }

        existingOrder.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        logger.info("Deleting order with id: {}", id);
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found for id: " + id);
        }
        orderRepository.deleteById(id);
    }
}
