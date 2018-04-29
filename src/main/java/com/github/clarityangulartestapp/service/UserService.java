package com.github.clarityangulartestapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.clarityangulartestapp.error.InvalidFieldValidationException;
import com.github.clarityangulartestapp.error.ValidationErrorField;
import com.github.clarityangulartestapp.model.User;
import com.github.clarityangulartestapp.repository.UserRepository;

@Service
public class UserService {

    private static final String MASK = "*****";

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User createUser(User user) throws InvalidFieldValidationException {
        User userFound = userRepository.findUserByUsername(user.getUsername());
        if (userFound != null) {
            throw new InvalidFieldValidationException("Invalid name", new ValidationErrorField("user", "name", "Invalid name"));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        savedUser.setPassword(MASK);
        return savedUser;
    }

    public User authenticate(User user) throws InvalidFieldValidationException {
        User userFound = userRepository.findUserByUsername(user.getUsername());
        if (userFound == null) {
            throw new InvalidFieldValidationException("Invalid username or password",
                    new ValidationErrorField("user", "name or password", "Invalid name or password"));
        }

        if (!passwordEncoder.matches(user.getPassword(), userFound.getPassword())) {
            throw new InvalidFieldValidationException("Invalid username or password",
                    new ValidationErrorField("user", "name or password", "Invalid name or password"));
        }

        userFound.setPassword(MASK);
        return userFound;
    }
}
