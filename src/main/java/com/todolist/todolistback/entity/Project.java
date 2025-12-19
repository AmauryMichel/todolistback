package com.todolist.todolistback.entity;

import java.util.List;

import jakarta.persistence.*;

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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
