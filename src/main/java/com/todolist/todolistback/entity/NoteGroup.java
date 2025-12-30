package com.todolist.todolistback.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class NoteGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "group")
    private List<Note> notes;

    private int groupOrderNumber;
    private String name;
    private Boolean completed = false;

    @SuppressWarnings("unused")
    public NoteGroup() {}

    public NoteGroup(Project project, int groupOrderNumber, String name) {
        this.project = project;
        this.groupOrderNumber = groupOrderNumber;
        this.name = name;
    }
}