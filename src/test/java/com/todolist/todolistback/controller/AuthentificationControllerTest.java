package com.todolist.todolistback.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.todolist.todolistback.entity.User;
import com.todolist.todolistback.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
public class AuthentificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    JsonMapper jm = JsonMapper.builder().build();

    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthentificationController authentificationController;

    User user = new User("admin", "root");

    String baseUrl = "/auth";

    @Nested
    class loginTests {
        @Test
        void loginSuccess() throws Exception {
            Authentication auth = Mockito.mock(Authentication.class);
            UserDetails userDetails = Mockito.mock(UserDetails.class);

            when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
            when(authenticationManager.authenticate(any())).thenReturn(auth);
            when(auth.getPrincipal()).thenReturn(userDetails);
            when(userDetails.getUsername()).thenReturn(user.getPassword());

            mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jm.writeValueAsString(user)))                    
                        .andExpect(status().isAccepted());
        }

        @Test
        void loginException() throws Exception {
            when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(""){});

            mockMvc.perform(post(baseUrl + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jm.writeValueAsString(user)))                    
                        .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class registerTests {
        @Test
        void registerSuccess() throws Exception {
            when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
            when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

            mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jm.writeValueAsString(user)))                    
                        .andExpect(status().isCreated());
        }

        @Test
        void registerUsernameTaken() throws Exception {
            when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

            mockMvc.perform(post(baseUrl + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jm.writeValueAsString(user)))                    
                        .andExpect(status().isBadRequest());
        }

        @Test
        void registerException() throws Exception {
            when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
            when(userRepository.save(any())).thenThrow(new DataAccessException(""){});

            mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jm.writeValueAsString(user)))                    
                        .andExpect(status().isBadRequest());
        }
    }
}
