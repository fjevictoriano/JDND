package com.example.demo.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JWTAuthorizationFilterTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private AuthenticationManager authenticationManager;
    private FilterChain filterChain;
    private final String TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9" +
            ".eyJzdWIiOiJGZXJuYW5kbyIsImV4cCI6MTYwNTM4MTIyNX0" +
            ".rVduzTfRckkdWDl3VUoR2CBOzUX5BlEDReheX31CW2D-r7GhQUUV1O494t46eWMazr4VwA0NqU1kEfG0ZGjLKA";

    private JWTAuthorizationFilter authorizationFilter;

    @Before
    public void setUp() throws Exception {
        this.authenticationManager = mock(AuthenticationManager.class);
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.filterChain = mock(FilterChain.class);
        this.authorizationFilter = new JWTAuthorizationFilter(authenticationManager);
        when(request.getHeader(SecurityConstants.HEADER_STRING)).thenReturn(TOKEN);
    }

    @Test
    public void testDoFilterInternal() throws IOException, ServletException {
        authorizationFilter.doFilterInternal(request,response, filterChain);
    }
}