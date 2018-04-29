package com.github.clarityangulartestapp.controller;

import java.io.UnsupportedEncodingException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.clarityangulartestapp.error.InvalidFieldValidationException;
import com.github.clarityangulartestapp.model.User;
import com.github.clarityangulartestapp.security.CookieManager;
import com.github.clarityangulartestapp.security.JwtTokenManager;
import com.github.clarityangulartestapp.service.UserService;

@RestController
public class UserController implements PublicController {

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenManager jwtManager;

    @Autowired
    CookieManager cookieManager;

    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@Valid @RequestBody User user)
            throws UnsupportedEncodingException, InvalidFieldValidationException {
        User createUser = userService.createUser(user);
        HttpHeaders headers = cookieManager.createCookieHeaders(jwtManager.generateToken(user.getUsername()), false);
        return new ResponseEntity<User>(createUser, headers, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/users/authentication", method = RequestMethod.POST)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody User user)
            throws UnsupportedEncodingException, InvalidFieldValidationException {
        User authenticateUser = userService.authenticate(user);
        HttpHeaders headers = cookieManager.createCookieHeaders(jwtManager.generateToken(user.getUsername()), false);
        return new ResponseEntity<User>(authenticateUser, headers, HttpStatus.OK);
    }

    @RequestMapping(path = "/users/logout", method = RequestMethod.POST)
    public <T> ResponseEntity<?> logout() {
        return new ResponseEntity<T>(cookieManager.createCookieHeaders("", true), HttpStatus.NO_CONTENT);
    }
}
