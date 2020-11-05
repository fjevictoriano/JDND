package com.example.demo.security;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JWTAuthenticationFilterTest {

    @Mock
    private AuthenticationManager authenticationManager;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @InjectMocks
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    private final String INPUT = "{\"username\":\"Fernando\",\"password\":\"abcdefd\"}";


    @Before
    public void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        InputStream inputStream = IOUtils.toInputStream(INPUT, "UTF-8");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new TestingAuthenticationToken(null, null));
        when(request.getInputStream()).thenReturn(new DelegatingServletInputStream(inputStream));

    }

    @Test
    public void testAttemptAuthentication() {
        Authentication authentication = jwtAuthenticationFilter.attemptAuthentication(request, response);
        assertNotNull(authentication);
        verify(authenticationManager, times(1))
                .authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testSuccessfulAuthentication() throws IOException, ServletException {
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("username");
        TestingAuthenticationToken auth = new TestingAuthenticationToken(user, null);
        jwtAuthenticationFilter.successfulAuthentication(request, response, null, auth);
        verify(response, times(1)).addHeader(anyString(),anyString());


    }
}