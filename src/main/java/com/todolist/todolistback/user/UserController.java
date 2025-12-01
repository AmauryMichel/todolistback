package com.todolist.todolistback.user;

import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<User> getUsers() {
        return (List<User>) userRepository.findAll();
    }

    @PostMapping
    void addUser(@RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}