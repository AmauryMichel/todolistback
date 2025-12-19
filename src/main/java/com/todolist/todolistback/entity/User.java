package com.todolist.todolistback.entity;

import java.util.List;

import jakarta.persistence.*;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
