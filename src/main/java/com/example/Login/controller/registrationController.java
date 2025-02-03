package com.example.Login.controller;

import com.example.Login.model.MyAppUser;
import com.example.Login.repo.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class registrationController {

    @Autowired
    private MyAppUserRepository myAppUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "req/signup", consumes = "application/json")
    public MyAppUser createUser(@RequestBody MyAppUser newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return myAppUserRepository.save(newUser);
    }
}
