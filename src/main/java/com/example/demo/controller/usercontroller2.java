package com.example.demo.controller;

import com.example.demo.Service.UserService;
import com.example.demo.model.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class usercontroller2 {
    @Autowired
    private UserService service;

    @PostMapping("register")
    public user register(@RequestBody user user) {
        return service.saveUser(user);
    }
}
