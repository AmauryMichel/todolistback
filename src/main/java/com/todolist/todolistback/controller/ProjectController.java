package com.todolist.todolistback.controller;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.todolistback.entity.NoteGroup;
import com.todolist.todolistback.entity.Project;
import com.todolist.todolistback.entity.User;
import com.todolist.todolistback.repository.ProjectRepository;
import com.todolist.todolistback.repository.UserRepository;

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

    @PutMapping("/{id}/addMember")
    public ResponseEntity<?> addMember(@PathVariable long id, @RequestBody User newUser) {
        Project project = projectRepository.findById(id);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Project does not exist");
        }
        if (!userRepository.existsById(newUser.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
        }

        try {
            int operation = project.addMember(newUser);
            if (operation == -1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Member is already in project");
            }

            projectRepository.save(project);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding member to project");
        }
    }

    @PutMapping("/{id}/removeMember")
    public ResponseEntity<?> removeMember(@PathVariable long id, @RequestBody User newUser) {
        Project project = projectRepository.findById(id);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Project does not exist");
        }
        if (!userRepository.existsById(newUser.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
        }

        try {
            int operation = project.removeMember(newUser);
            if (operation == -1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Member doesn't belong to project");
            }
            projectRepository.save(project);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error adding member to project");
        }
    }
}
