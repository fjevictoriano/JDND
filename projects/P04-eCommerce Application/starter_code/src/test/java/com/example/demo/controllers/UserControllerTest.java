package com.example.demo.controllers;


import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Spy
    private UserRepository userRepository;
    @Spy
    private CartRepository cartRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserController userController;


    @Before
    public void setup() {
        User user = new User();
        user.setId(1);
        user.setUsername("username");
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.findByUsername("username")).willReturn(user);
    }

    @Test
    public void testFindById(){
        ResponseEntity<User> response = userController.findById(1L);
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(1, user.getId());
        assertEquals("username", user.getUsername());


    }

    @Test
    public void testFindByUserName() {
        ResponseEntity<User> response = userController.findByUserName("username");
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(1, user.getId());
        assertEquals("username", user.getUsername());

    }

    @Test
    public void testCreateUser(){
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setPassword("abcdefghipass");
        userRequest.setConfirmPassword("abcdefghipass");
        ResponseEntity<User> response = userController.createUser(userRequest);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        verify(userRepository, times(1)).save(any(User.class));
        verify(cartRepository, times(1)).save(any(Cart.class));

    }
    @Test
    public void testCreateUserWhenUserHasBadPasswordLength(){
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setPassword("pass");
        ResponseEntity<User> response = userController.createUser(userRequest);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        verify(userRepository, times(0)).save(any(User.class));
        verify(cartRepository, times(0)).save(any(Cart.class));

    }
    @Test
    public void testCreateUserWhenPasswordAndConfirmPasswordAreNotEqual(){
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setPassword("abcdefghipass");
        userRequest.setConfirmPassword("a5cdefghip4ss");
        ResponseEntity<User> response = userController.createUser(userRequest);
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        verify(userRepository, times(0)).save(any(User.class));
        verify(cartRepository, times(0)).save(any(Cart.class));

    }
}