package com.todolist.todolistback.user;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular to access this API
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @GetMapping
    public List<User> getUsers() {
        // return ResponseEntity.ok("Hello from Spring Boot!");
        return (List<User>) userRepository.findAll();
    }

    @PostMapping
    void addUser(@RequestBody User user) {
        userRepository.save(user);
    }
}