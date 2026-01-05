package com.todolist.todolistback.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "group")
    private List<Note> notes;

    private String name;

    private Integer groupOrderNumber = 0;
    private Boolean completed = false;

    public NoteGroup() {}

    public NoteGroup(Project project, Integer groupOrderNumber, String name) {
        this.project = project;
        this.groupOrderNumber = groupOrderNumber;
        this.name = name;
        this.notes = new ArrayList<Note>();
    }
}