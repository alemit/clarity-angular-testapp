package com.github.clarityangulartestapp.security;

import static org.junit.Assert.assertNotNull;

import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.github.clarityangulartestapp.error.InvalidFieldValidationException;
import com.github.clarityangulartestapp.error.UnathorizedAccessException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTokenManagerTest {

    private static String JWT_TOKEN_EXPIRED = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiTmFtZSIsImV4cCI6MTUyNDg1NjQxN30.HDvH-w1UOwYrjW0oiOeATecM4zyYQaaQKtRX3KDZYnGBqQwqUzMckhTdvD3Doz9pYKpTIkQOVqMW7_H4gho3Ew";
    private static String JWT_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiTmFtZSIsImV4cCI6MTk5ODI0MTMzMX0.b1wHIesbgqMIG0mPa_V-KHB_-mQpNdkhgZsbForZxwx2gvF_nmmXL6JDBm9MVrwYeRgcKOPU39RN3qjnE8N7Xg";
    @Autowired
    JwtTokenManager jwtTokenManager;

    @Test
    public void givenUserName_whenGenerateToken_thenCreateJwtToken() throws InvalidFieldValidationException, UnsupportedEncodingException {
        String token = jwtTokenManager.generateToken("Name");
        assertNotNull(token);
    }

    @Test
    public void givenValidToken_whenValidateToken_thenCreateJwtToken() throws UnsupportedEncodingException, UnathorizedAccessException {
        jwtTokenManager.validateToken(JWT_TOKEN);
    }

    @Test(expected = TokenExpiredException.class)
    public void givenExpiredToken_whenValidateToken_thenThrowException() throws UnsupportedEncodingException, UnathorizedAccessException {
        jwtTokenManager.validateToken(JWT_TOKEN_EXPIRED);
    }

    @Test(expected = UnathorizedAccessException.class)
    public void givenNullToken_whenValidateToken_thenThrowException() throws UnsupportedEncodingException, UnathorizedAccessException {
        jwtTokenManager.validateToken(null);
    }

}
