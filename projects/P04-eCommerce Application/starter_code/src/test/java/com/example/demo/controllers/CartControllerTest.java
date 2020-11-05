package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartController cartController;


    @Before
    public void setUp(){
        Cart cart = new Cart();
        User user = new User();
        user.setId(1);
        user.setUsername("username");
        user.setCart(cart);
        Item item = new Item();
        item.setDescription("desc");
        item.setId(1L);
        item.setName("itemName");
        item.setPrice(new BigDecimal(34));
        given(userRepository.findByUsername("username")).willReturn(user);
        given(itemRepository.findById(1L)).willReturn(Optional.of(item));
    }

    @Test
    public void testAddToCart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(1);
        request.setUsername("username");
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        verify(cartRepository, times(1)).save(any(Cart.class));
        assertEquals(1, response.getBody().getItems().size());

    }
    @Test
    public void testAddToCartWhenUserNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(1);
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        verify(cartRepository, times(0)).save(any(Cart.class));

    }

    @Test
    public void testAddToCartWhenItemNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("username");
        request.setItemId(5);
        request.setQuantity(1);
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        verify(cartRepository, times(0)).save(any(Cart.class));

    }

    @Test
    public void testRemoveFromCart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("username");
        request.setItemId(1);
        request.setQuantity(1);
        ResponseEntity<Cart> response = cartController.removeFromcart(request);


        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        verify(cartRepository, times(1)).save(any(Cart.class));
        assertTrue(response.getBody().getItems().isEmpty());

    }


    @Test
    public void testRemoveFromCartWhenUserNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(1);
        request.setQuantity(1);
        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        verify(cartRepository, times(0)).save(any(Cart.class));

    }


    @Test
    public void testRemoveFromCarWhenItemNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("username");
        request.setItemId(5);
        request.setQuantity(1);
        ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        verify(cartRepository, times(0)).save(any(Cart.class));

    }
}