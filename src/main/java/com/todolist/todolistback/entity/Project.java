package com.todolist.todolistback.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Project {
    @JsonView(Views.Simplified.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JsonView(Views.Simplified.class)
    @JsonIncludeProperties(value = {"id", "username"})
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToMany
    @JsonView(Views.Detailed.class)
    @JsonIncludeProperties(value = {"id", "username"})
    @JoinTable(
        name = "user_projects", 
        joinColumns = @JoinColumn(name = "project_id"), 
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> projectMembers;

    @JsonView(Views.Detailed.class)
    @OneToMany(mappedBy = "project")
    private List<NoteGroup> noteGroups;
    
    @JsonView(Views.Simplified.class)
    private String projectName;

    @SuppressWarnings("unused")
    private Project() {}

    public Project(User creator, String projectName) {
        this.creator = creator;
        this.projectName = projectName;
        this.projectMembers = new ArrayList<User>();
        addMember(creator);
    }

    public void addMember(User user) {
        if (this.projectMembers.contains(user)) return;
        
        this.projectMembers.add(user);
    }

    public void removeMember(User user) {
        int index = this.projectMembers.indexOf(user);
        if (index == -1) return;

        this.projectMembers.remove(index);
    }
}
