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

import com.todolist.todolistback.entity.Note;
import com.todolist.todolistback.repository.NoteGroupRepository;
import com.todolist.todolistback.repository.NoteRepository;
import com.todolist.todolistback.repository.UserRepository;

@RestController
@RequestMapping("/note")
public class NoteController {
    
    private final NoteRepository noteRepository;
    private final NoteGroupRepository noteGroupRepository;
    private final UserRepository userRepository;

    public NoteController(NoteRepository noteRepository, NoteGroupRepository noteGroupRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.noteGroupRepository = noteGroupRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createNote(@RequestBody Note note) {
        if (!userRepository.existsById(note.getAuthor().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Author not valid");
        }
        if (!noteGroupRepository.existsById(note.getGroup().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Note group not valid");
        }

        try {
            noteRepository.save(note);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating note");
        }
    }

    @PutMapping("/{id}/set-completed")
    public ResponseEntity<?> setNoteCompleted(@PathVariable long id, @RequestBody boolean completed) {
        Note note = noteRepository.findById(id);
        if (note == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Note does not exist");
        }

        try {
            note.setCompleted(completed);
            noteRepository.save(note);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating note");
        }
    }

    @PutMapping("/{id}/edit-text")
    public ResponseEntity<?> editNoteText(@PathVariable long id, @RequestBody String text) {
        Note note = noteRepository.findById(id);
        if (note == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Note does not exist");
        }

        try {
            note.setText(text);
            noteRepository.save(note);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating note");
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteNote(@PathVariable long id) {
        Note note = noteRepository.findById(id);
        if (note == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Note does not exist");
        }

        try {
            noteRepository.delete(note);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (DataAccessException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting note");
        }
    }
}
