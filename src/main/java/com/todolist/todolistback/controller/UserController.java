package com.todolist.todolistback.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;
import com.todolist.todolistback.entity.Project;
import com.todolist.todolistback.entity.User;
import com.todolist.todolistback.entity.Views;
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

    @JsonView(Views.Simplified.class)
    @GetMapping("/{id}/projects")
    public List<Project> getUserProjects(@PathVariable long id) {
        User user = userRepository.findById(id);
        return user.getProjects();
    }
}