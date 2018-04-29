package com.github.clarityangulartestapp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user", schema="netinfo")
public class User {

    @Id
    @SequenceGenerator(name = "user_generator", sequenceName = "user_seq", allocationSize = 1)
    @GeneratedValue(generator = "user_generator", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @NotNull(message = "The field 'name' should not be empty")
    @Size(min = 3, max = 32, message = "Field 'name' must be between 3 and 32 symbols")
    @Pattern(message = "Invalid 'name' field format", regexp = "^[a-zA-Z0-9]{1}[a-zA-Z0-9_-]*$")
    @Column(name = "username")
    private String username;

    @NotNull(message = "The field 'password' should not be empty")
    @Size(min = 4, max = 72, message = "Field 'password' must be between 8 and 72 symbols")
    @Column(name = "password")
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
