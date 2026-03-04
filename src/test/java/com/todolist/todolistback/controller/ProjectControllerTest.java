package com.todolist.todolistback.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.todolist.todolistback.entity.NoteGroup;
import com.todolist.todolistback.entity.Project;
import com.todolist.todolistback.entity.User;
import com.todolist.todolistback.repository.ProjectRepository;
import com.todolist.todolistback.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    JsonMapper jm = JsonMapper.builder().disable(MapperFeature.USE_ANNOTATIONS)
            .build();
    JsonMapper jmAnnotations = JsonMapper.builder().build();

    User user = new User("admin", "root");
    Project project = new Project(user, null, null);
    NoteGroup noteGroup = new NoteGroup(project, null, null);
    String newText = "newText";

    String baseUrl = "/projects";
    String urlCreate = "/create";
    String urlNotegroups = "/notegroups";

    @MockitoBean
    private ProjectRepository projectRepository;
    @MockitoBean
    private UserRepository userRepository;

    @InjectMocks
    private ProjectController projectController;

    @Nested
    class CreateNoteGroupTests {
        @Test
        void CreateNoteGroupSuccess() throws Exception {
            when(userRepository.existsById(any())).thenReturn(true);

            mockMvc.perform(post(baseUrl + urlCreate)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jm.writeValueAsString(project)))
                    .andExpect(status().isCreated());
        }

        @Test
        void CreateNoteGroupWrongUser() throws Exception {
            when(userRepository.existsById(user.getId())).thenReturn(false);

            mockMvc.perform(post(baseUrl + urlCreate)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jm.writeValueAsString(project)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void CreateNoteGroupException() throws Exception {
            when(userRepository.existsById(any())).thenReturn(true);
            when(projectRepository.save(any())).thenThrow(new DataAccessException("") {
            });

            mockMvc.perform(post(baseUrl + urlCreate)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jm.writeValueAsString(project)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    void testGetProject() throws Exception {
        when(projectRepository.findById(project.getId())).thenReturn(project);

        mockMvc.perform(get(baseUrl + "/" + project.getId()))
                .andExpect(content().json(jmAnnotations.writeValueAsString(project)));
    }

    @Test
    void testGetProjectNoteGroups() throws Exception {
        ArrayList<NoteGroup> list = new ArrayList<NoteGroup>();
        list.add(noteGroup);
        project.setNoteGroups(list);

        when(projectRepository.findById(project.getId())).thenReturn(project);

        mockMvc.perform(get(baseUrl + "/" + project.getId() + urlNotegroups))
                .andExpect(content().json(jmAnnotations.writeValueAsString(project.getNoteGroups())));
    }
}
