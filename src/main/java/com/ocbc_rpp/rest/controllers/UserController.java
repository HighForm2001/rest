package com.ocbc_rpp.rest.controllers;

import com.ocbc_rpp.rest.repositories.UserRepository;
import com.ocbc_rpp.rest.models.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository repository;
    public UserController(UserRepository repository){
        this.repository = repository;
    }

    @GetMapping
    public List<User> all(){
        return repository.findAll();
    }

    @PostMapping
    public User newUser(@RequestBody User user){

        repository.save(user);
        return user;
    }
}
