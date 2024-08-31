package com.platform.ecommerce.orders.services;

import com.platform.ecommerce.cart.models.ShoppingCart;
import com.platform.ecommerce.cart.models.ShoppingCartItem;
import com.platform.ecommerce.cart.repositories.ShoppingCartItemRepository;
import com.platform.ecommerce.cart.repositories.ShoppingCartRepository;
import com.platform.ecommerce.exceptions.ResourceNotFoundException;
import com.platform.ecommerce.orders.models.*;
import com.platform.ecommerce.orders.payloads.OrderRequest;
import com.platform.ecommerce.orders.repositories.OrderItemRepository;
import com.platform.ecommerce.orders.repositories.OrderRepository;
import com.platform.ecommerce.orders.repositories.PaymentRepository;
import com.platform.ecommerce.products.models.Product;
import com.platform.ecommerce.products.repositories.ProductRepository;
import com.platform.ecommerce.users.models.Address;
import com.platform.ecommerce.users.models.User;
import com.platform.ecommerce.users.repositories.AddressRepository;
import com.platform.ecommerce.util.AuthUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingCartItemRepository shoppingCartItemRepository;

    @Override
    @Transactional
    public void placeOrder(OrderRequest orderRequest, Long addressId) {
        User user = authUtils.loggedInUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user);

        if (shoppingCart.getShoppingCartItems().isEmpty()) {
            throw new RuntimeException("No items added in cart");
        }

        Address address = addressRepository.findByAddressIdAndUser(addressId, user).orElseThrow(
                () -> new ResourceNotFoundException("Address not found with id: " + addressId)
        );

        // TODO check if product is available or not
        // TODO Update product quantity first to prevent double consuming

        double amount = total(shoppingCart);

        Payment payment = new Payment();
        payment.setPaymentMethod(orderRequest.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setAmount(amount);

        Payment savedPayment = paymentRepository.save(payment);

        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PROCESSING);
        order.setAddress(address);
        order.setPayment(savedPayment);
        order.setUser(user);
        order.setTotalAmount(amount);

        Order savedOrder = orderRepository.save(order);

        for (ShoppingCartItem cartItem : shoppingCart.getShoppingCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getSellingPrice());
            orderItem.setOrder(savedOrder);
            OrderItem savedOrderItem = orderItemRepository.save(orderItem);

            order.addOrderItem(savedOrderItem);
        }

        boolean paymentSuccessful = paymentService.processPayment(
                orderRequest.getPaymentMethod(),
                orderRequest.getPaymentInfo(),
                amount
        );

        if (!paymentSuccessful) {
            savedPayment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(savedPayment);
            savedOrder.setOrderStatus(OrderStatus.FAILED);
            orderRepository.save(savedOrder);
            throw new RuntimeException("Payment Failed");
        }

        savedPayment.setPaymentStatus(PaymentStatus.PAID);
        paymentRepository.save(savedPayment);
        savedOrder.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(savedOrder);

        shoppingCart.getShoppingCartItems().forEach(item -> {
            int quantity = item.getQuantity();
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - quantity);

            productRepository.save(product);
        });

        shoppingCart.getShoppingCartItems().clear();
        List<ShoppingCartItem> shoppingCartItems = shoppingCartItemRepository.findByShoppingCart(shoppingCart);

        shoppingCartItemRepository.deleteAllInBatch(shoppingCartItems);
    }

    private double total(ShoppingCart shoppingCart) {
        return shoppingCart.getShoppingCartItems().stream()
                .map(item -> item.getQuantity() * item.getProduct().getSellingPrice())
                .reduce(Double::sum)
                .get();
    }
}
