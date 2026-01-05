package com.todolist.todolistback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @JsonIncludeProperties(value = {"id"})
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "notegroup_id")
    private NoteGroup group;

    private String text;

    private Integer noteOrderNumber = 0;
    private Boolean completed = false;

    public Note() {}

    public Note(User author, NoteGroup group, Integer noteOrderNumber, String text) {
        this.author = author;
        this.group = group;
        this.noteOrderNumber = noteOrderNumber;
        this.text = text;
    }    
}