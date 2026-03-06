package com.todolist.todolistback.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.todolist.todolistback.repository.NoteGroupRepository;
import com.todolist.todolistback.repository.ProjectRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
public class NoteGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    JsonMapper jm = JsonMapper.builder().disable(MapperFeature.USE_ANNOTATIONS)
            .build();

    @MockitoBean
    private NoteGroupRepository noteGroupRepository;
    @MockitoBean
    private ProjectRepository projectRepository;

    @InjectMocks
    private NoteGroupController noteGroupController;

    User user = new User("admin", "root");
    Project project = new Project(user, null, null);
    NoteGroup noteGroup = new NoteGroup(project, null, null);
    String newText = "newText";

    String baseUrl = "/notegroup";
    String urlCreate = "/create";
    String urlEdit = "/edit-text";
    String urlCompleted = "/set-completed";
    String urlDelete = "/delete";

    @Nested
    class CreateNoteGroupTests {
        @Test
        void CreateNoteGroupSuccess() throws Exception {
            when(projectRepository.existsById(any())).thenReturn(true);

            mockMvc.perform(post(baseUrl + urlCreate)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jm.writeValueAsString(noteGroup)))
                    .andExpect(status().isCreated());
        }

        @Test
        void CreateNoteGroupWrongProject() throws Exception {
            when(projectRepository.existsById(user.getId())).thenReturn(false);

            mockMvc.perform(post(baseUrl + urlCreate)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jm.writeValueAsString(noteGroup)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void CreateNoteGroupException() throws Exception {
            when(projectRepository.existsById(any())).thenReturn(true);
            when(noteGroupRepository.save(any())).thenThrow(new DataAccessException("") {
            });

            mockMvc.perform(post(baseUrl + urlCreate)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jm.writeValueAsString(noteGroup)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class SetNoteCompletedTests {
        @Test
        void setCompletedSuccess() throws Exception {
            when(noteGroupRepository.findById(noteGroup.getId())).thenReturn(noteGroup);

            mockMvc.perform(put(baseUrl + "/" + noteGroup.getId() + urlCompleted)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jm.writeValueAsString(true)))
                    .andExpect(status().isOk());
        }

        @Test
        void setCompletedWrongId() throws Exception {
            mockMvc.perform(put(baseUrl + "/" + noteGroup.getId() + urlCompleted)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jm.writeValueAsString(true)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void setCompletedException() throws Exception {
            when(noteGroupRepository.findById(noteGroup.getId())).thenReturn(noteGroup);
            when(noteGroupRepository.save(any())).thenThrow(new DataAccessException("") {
            });

            mockMvc.perform(put(baseUrl + "/" + noteGroup.getId() + urlCompleted)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jm.writeValueAsString(true)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class EditNoteTests {
        @Test
        void editNoteSuccess() throws Exception {
            when(noteGroupRepository.findById(noteGroup.getId())).thenReturn(noteGroup);

            mockMvc.perform(put(baseUrl + "/" + noteGroup.getId() + urlEdit)
                    .content(newText))
                    .andExpect(status().isOk());
        }

        @Test
        void editNoteWrongId() throws Exception {
            mockMvc.perform(put(baseUrl + "/" + noteGroup.getId() + urlEdit)
                    .content(newText))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void editNoteException() throws Exception {
            when(noteGroupRepository.findById(noteGroup.getId())).thenReturn(noteGroup);
            when(noteGroupRepository.save(any())).thenThrow(new DataAccessException("") {
            });

            mockMvc.perform(put(baseUrl + "/" + noteGroup.getId() + urlEdit)
                    .content(newText))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class DeleteNoteTests {
        @Test
        void deleteNoteSuccess() throws Exception {
            when(noteGroupRepository.findById(noteGroup.getId())).thenReturn(noteGroup);

            mockMvc.perform(delete(baseUrl + "/" + noteGroup.getId() + urlDelete))
                    .andExpect(status().isOk());
        }

        @Test
        void deleteNoteWrongId() throws Exception {
            mockMvc.perform(delete(baseUrl + "/" + noteGroup.getId() + urlDelete))

                    .andExpect(status().isBadRequest());
        }

        @Test
        void deleteNoteException() throws Exception {
            when(noteGroupRepository.findById(noteGroup.getId())).thenReturn(noteGroup);
            doThrow(new DataAccessException("") {
            }).when(noteGroupRepository).delete(any());

            mockMvc.perform(delete(baseUrl + "/" + noteGroup.getId() + urlDelete))
                    .andExpect(status().isBadRequest());
        }
    }
}
