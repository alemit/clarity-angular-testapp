package com.github.clarityangulartestapp.security;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.clarityangulartestapp.error.UnathorizedAccessException;

@Component
public class JwtTokenManager {

    @Value(value = "${jwt.secret}")
    private String jwtSecret;

    public String generateToken(String username) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
        Map<String, Object> headerClaims = new HashMap<>();
        return JWT.create().withExpiresAt(getExpirationTime()).withHeader(headerClaims).withClaim("username", username).sign(algorithm);
    }

    public Map<String, Claim> validateToken(String token)
            throws UnathorizedAccessException, UnsupportedEncodingException, JWTVerificationException {
        if (token != null) {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if (decodedJWT.getExpiresAt().compareTo(new Date()) > 0) {
                return decodedJWT.getClaims();
            } else {
                throw new UnathorizedAccessException("Token expired");
            }
        } else {
            throw new UnathorizedAccessException("Missing token");
        }
    }

    private Date getExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 15);
        return calendar.getTime();
    }
}
