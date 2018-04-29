package com.github.clarityangulartestapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.annotation.RequestScope;

import com.github.clarityangulartestapp.model.User;

@SpringBootApplication
public class ClarityAngularTestappApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClarityAngularTestappApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @RequestScope
    public User requestUser() {
        return new User();
    }
}
