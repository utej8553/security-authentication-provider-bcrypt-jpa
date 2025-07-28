package com.example.demo.Service;

import com.example.demo.Repository.UserRepository;
import com.example.demo.model.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public user saveUser(user user) {
        user.setPassword(encoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        return repo.save(user);
    }
}
