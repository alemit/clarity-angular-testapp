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

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        super();
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) throws InvalidFieldValidationException {
        User userFound = userRepository.findUserByUsername(user.getUsername());
        if (userFound != null) {
            throw new InvalidFieldValidationException("Invalid username", new ValidationErrorField("user", "username", "Invalid username"));
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
                    new ValidationErrorField("user", "username or password", "Invalid username or password"));
        }

        if (!passwordEncoder.matches(user.getPassword(), userFound.getPassword())) {
            throw new InvalidFieldValidationException("Invalid username or password",
                    new ValidationErrorField("user", "username or password", "Invalid username or password"));
        }

        userFound.setPassword(MASK);
        return userFound;
    }
}
