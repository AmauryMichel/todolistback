package com.todolist.todolistback.entity;

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
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "notegroup_id")
    private NoteGroup group;

    private int noteOrderNumber;
    private String name;
    private Boolean completed = false;

    @SuppressWarnings("unused")
    public Note() {}

    public Note(User author, NoteGroup group, int noteOrderNumber, String name) {
        this.author = author;
        this.group = group;
        this.noteOrderNumber = noteOrderNumber;
        this.name = name;
    }    
}