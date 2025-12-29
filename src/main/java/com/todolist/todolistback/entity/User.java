package com.todolist.todolistback.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="user_account")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(mappedBy = "creator")
    private List<Project> createdProjects;

    @ManyToMany
    @JoinTable(
        name = "user_projects", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projects;
    
    @Column(unique=true)
    private String username;
    
    private String password;

    @SuppressWarnings("unused")
    private User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
