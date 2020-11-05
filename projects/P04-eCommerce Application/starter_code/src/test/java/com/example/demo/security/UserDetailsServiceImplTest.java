package com.example.demo.security;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Before
    public void setUp() {
        User user = new User();
        user.setId(1);
        user.setUsername("username");
        user.setPassword("password");
        given(userRepository.findByUsername("username")).willReturn(user);
    }

    @Test
    public void testLoadUserByUsername() {
        UserDetails user = userDetailsService.loadUserByUsername("username");
        assertNotNull(user);
        assertEquals("username", user.getUsername());
    }
}