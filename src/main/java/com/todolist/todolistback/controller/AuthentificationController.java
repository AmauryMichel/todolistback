package com.todolist.todolistback.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.todolistback.security.JwtUtil;
import com.todolist.todolistback.user.User;
import com.todolist.todolistback.user.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthentificationController {
    private final UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder encoder;
    private JwtUtil jwtUtils;

    public AuthentificationController(UserRepository userRepository, AuthenticationManager authenticationManager,
            BCryptPasswordEncoder encoder, JwtUtil jwtUtils) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword()
                )
            );
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(jwtUtils.generateToken(userDetails.getUsername()));
        } catch (BadCredentialsException badCredentialsException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already taken");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        try {
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating account");
        }
    }
}
