package com.todolist.todolistback.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="user_account")
public class User {
    @JsonView(Views.Simplified.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @OneToMany(mappedBy = "creator")
    private List<Project> createdProjects;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "user_projects", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projects;
    
    @JsonView(Views.Simplified.class)
    @Column(unique=true)
    private String username;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @SuppressWarnings("unused")
    private User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
