package com.todolist.todolistback.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User creator;

    @ManyToMany(mappedBy = "projects")
    private List<User> projectMembers;

    @OneToMany(mappedBy = "project")
    private List<NoteGroup> noteGroups;
    
    private String projectName;

    public Project(User creator, String projectName) {
        this.creator = creator;
        this.projectName = projectName;
    }
}
