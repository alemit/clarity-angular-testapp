package com.github.clarityangulartestapp.repository;

import org.springframework.data.repository.CrudRepository;

import com.github.clarityangulartestapp.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByUsername(String username);
}
