package com.todolist.todolistback.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular to access this API
public class UserController {
    
    @GetMapping
    public ResponseEntity<String> getUsers() {
        return ResponseEntity.ok("Hello from Spring Boot!");
    }

}