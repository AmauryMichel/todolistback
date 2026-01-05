package com.todolist.todolistback.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todolist.todolistback.repository.NoteGroupRepository;
import com.todolist.todolistback.repository.ProjectRepository;
import com.todolist.todolistback.entity.NoteGroup;

@RestController
@RequestMapping("/notegroup")
public class NoteGroupController {
    private final NoteGroupRepository noteGroupRepository;
    private final ProjectRepository projectRepository;

    public NoteGroupController(NoteGroupRepository noteGroupRepository, ProjectRepository projectRepository) {
        this.noteGroupRepository = noteGroupRepository;
        this.projectRepository = projectRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNoteGroup(@RequestBody NoteGroup noteGroup) {
        if (!projectRepository.existsById(noteGroup.getProject().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Creator not valid");
        }

        try {
            noteGroupRepository.save(noteGroup);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating note group");
        }
    }
}
