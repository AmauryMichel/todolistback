package com.todolist.todolistback.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.todolist.todolistback.entity.User;
import com.todolist.todolistback.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }
}