package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;

    @Before
    public void setUp(){
        User user = new User();
        user.setId(1);
        user.setUsername("username");
        Item item = new Item();
        item.setDescription("desc");
        item.setId(1L);
        item.setName("itemName");
        item.setPrice(new BigDecimal(34));
        Cart cart = new Cart();
        cart.setItems(Collections.singletonList(item));
        cart.setTotal(new BigDecimal(1));
        cart.setUser(user);
        user.setCart(cart);
        given(userRepository.findByUsername("username")).willReturn(user);
        given(orderRepository.findByUser(user))
                .willReturn(Collections.singletonList(UserOrder.createFromCart(cart)));

    }


    @Test
    public void submit() {
        ResponseEntity<UserOrder> response =  orderController.submit("username");
        assertNotNull(response.getBody());

        verify(orderRepository, times(1)).save(any(UserOrder.class));
    }

    @Test
    public void getOrdersForUser() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("username");
        List<UserOrder> orders = response.getBody();
        assertNotNull(orders);
        assertFalse(orders.isEmpty());

    }
}