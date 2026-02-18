package com.todolist.todolistback.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/{id}/set-completed")
    public ResponseEntity<?> setNoteGroupCompleted(@PathVariable long id, @RequestBody boolean completed) {
        NoteGroup noteGroup = noteGroupRepository.findById(id);
        if (noteGroup == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Note does not exist");
        }

        try {
            noteGroup.setCompleted(completed);
            noteGroupRepository.save(noteGroup);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating note");
        }
    }

    @PutMapping("/{id}/edit-text")
    public ResponseEntity<?> editNoteGroupName(@PathVariable long id, @RequestBody String text) {
        NoteGroup noteGroup = noteGroupRepository.findById(id);
        if (noteGroup == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Note does not exist");
        }

        try {
            noteGroup.setName(text);
            noteGroupRepository.save(noteGroup);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating note");
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteNoteGroup(@PathVariable long id) {
        NoteGroup noteGroup = noteGroupRepository.findById(id);
        if (noteGroup == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Note does not exist");
        }

        try {
            noteGroupRepository.delete(noteGroup);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting note");
        }
    }
}
