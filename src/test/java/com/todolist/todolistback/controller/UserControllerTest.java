package com.todolist.todolistback.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.todolist.todolistback.entity.Project;
import com.todolist.todolistback.entity.User;
import com.todolist.todolistback.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @MockitoBean
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    User user1 = new User("admin", "root");
    User user2 = new User("user2", "pass");
    Project project1 = new Project(user1, "project1", null);

    List<User> users = new ArrayList<>(Arrays.asList(user1, user2));
    List<Project> projects = new ArrayList<>(Arrays.asList(project1));


    @Test
    void testGetUsers() throws Exception {

        when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getUserProjects() throws Exception {        
        user1.setProjects(projects);

        when(userRepository.findById(1)).thenReturn(user1);

        mockMvc.perform(get("/users/1/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
