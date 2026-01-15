package com.todolist.todolistback.controller;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.todolist.todolistback.repository.ProjectRepository;
import com.todolist.todolistback.repository.UserRepository;

import com.todolist.todolistback.entity.NoteGroup;
import com.todolist.todolistback.entity.Project;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody Project project) {
        if (!userRepository.existsById(project.getCreator().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Creator not valid");
        }

        try {
            projectRepository.save(project);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating project");
        }
    }

    @GetMapping("/{id}")
    public Project getProject(@PathVariable long id) {
        Project project = projectRepository.findById(id);
        return project;
    }

    @GetMapping("/{id}/notegroups")
    public List<NoteGroup> getProjectNoteGroups(@PathVariable long id) {
        Project project = projectRepository.findById(id);
        return project.getNoteGroups();
    }
}
