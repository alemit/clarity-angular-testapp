package com.github.clarityangulartestapp.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.clarityangulartestapp.error.InvalidFieldValidationException;
import com.github.clarityangulartestapp.model.User;
import com.github.clarityangulartestapp.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    private static final String EXISTING_USER = "existing";
    private static final String NEW_USER = "newUser";

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    private User user;

    @Before
    public void setup() {
        user = new User();
        user.setId(1L);
        user.setPassword("pass");
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findUserByUsername(EXISTING_USER)).thenReturn(user);
        when(passwordEncoder.matches("pass", "pass")).thenReturn(true);
    }

    @Test
    public void givenValidUser_whenCreateUser_thenCreateSucess() throws InvalidFieldValidationException {
        user.setUsername(NEW_USER);
        User createdUser = userService.createUser(user);
        assertEquals(user.getUsername(), createdUser.getUsername());
    }

    @Test(expected = InvalidFieldValidationException.class)
    public void givenExistingUser_whenCreateUser_thenThrowException() throws InvalidFieldValidationException {
        user.setUsername(EXISTING_USER);
        userService.createUser(user);
    }

    @Test
    public void givenValidUser_whenAuthenticateeUser_thenSucess() throws InvalidFieldValidationException {
        user.setUsername(EXISTING_USER);
        User authenticatedUser = userService.authenticate(user);
        assertEquals(user.getUsername(), authenticatedUser.getUsername());
    }

    @Test(expected = InvalidFieldValidationException.class)
    public void givenNotExistingUser_whenAuthenticateeUser_thenThrowException() throws InvalidFieldValidationException {
        user.setUsername(NEW_USER);
        User authenticatedUser = userService.authenticate(user);
        assertEquals(user.getUsername(), authenticatedUser.getUsername());
    }

    @Test(expected = InvalidFieldValidationException.class)
    public void givenValidUserWithWrongPass_whenAuthenticateeUser_thenThrowException() throws InvalidFieldValidationException {
        user.setUsername(EXISTING_USER);
        user.setPassword("wrong");
        User authenticatedUser = userService.authenticate(user);
        assertEquals(user.getUsername(), authenticatedUser.getUsername());
    }
}
