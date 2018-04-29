package com.github.clarityangulartestapp.security;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.github.clarityangulartestapp.model.User;

@SpringBootTest
public class JwtFilterTest {

    private JwtFilter jwtFilter;
    private Map<String, Claim> claims;
    
    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private JwtTokenManager jwtTokenManager;

    @Mock
    private CookieManager cookieManager;

    @Mock
    private FilterChain filterChain;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter out;

    @Mock
    private Claim claim;

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        claims = new HashMap<>();
        claims.put("username", claim);
        jwtFilter = new JwtFilter(applicationContext, jwtTokenManager, cookieManager);
        when(request.getServletPath()).thenReturn("/path");
        when(response.getWriter()).thenReturn(out);
        when(applicationContext.getBean(JwtFilter.REQUEST_BEAN)).thenReturn(new User());
        when(jwtTokenManager.validateToken("validToken")).thenReturn(claims);
    }

    @Test
    public void givenMissingToken_whenDoFilterInternal_thenUnauthorizedAccessResponse() throws ServletException, IOException {
        when(cookieManager.getCookieValue(request)).thenReturn(null);
        jwtFilter.doFilterInternal(request, response, filterChain);
        verify(out, times(1)).print(any(String.class));
    }

    @Test
    public void givenValidTokenWithoutName_whenDoFilterInternal_thenUnauthorizedAccessResponse() throws ServletException, IOException {
        when(cookieManager.getCookieValue(request)).thenReturn("validTokenWithoutName");
        when(claim.asString()).thenReturn(null);
        jwtFilter.doFilterInternal(request, response, filterChain);
        verify(out, times(1)).print(any(String.class));
    }
    
    @Test
    public void givenInvalidToken_whenDoFilterInternal_thenUnauthorizedAccessResponse() throws ServletException, IOException {
        when(cookieManager.getCookieValue(request)).thenReturn("invalidToken");
        when(jwtTokenManager.validateToken("invalidToken")).thenThrow(new JWTVerificationException("message"));
        jwtFilter.doFilterInternal(request, response, filterChain);
        verify(out, times(1)).print(any(String.class));
    }
    
    @Test
    public void givenValidToken_whenDoFilterInternal_thenSuccess() throws ServletException, IOException {
        when(cookieManager.getCookieValue(request)).thenReturn("validToken");
        when(jwtTokenManager.generateToken("validUsername")).thenReturn("validToken");
        when(claim.asString()).thenReturn("validUsername");
        jwtFilter.doFilterInternal(request, response, filterChain);
        verify(out, times(0)).print(any(String.class));
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
