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

    @JsonView(Views.Simplified.class)
    private String description;

    @SuppressWarnings("unused")
    private Project() {}

    public Project(User creator, String projectName, String description) {
        this.creator = creator;
        this.projectName = projectName;
        this.projectMembers = new ArrayList<User>();
        this.description = description;
        addMember(creator);
    }

    public int addMember(User user) {
        int index = this.containsUser(user.getId());
        if (index != -1) return -1;
        
        this.projectMembers.add(user);
        return 0;
    }

    public int removeMember(User user) {
        int index = this.containsUser(user.getId());
        if (index == -1) return -1;

        this.projectMembers.remove(index);
        return 0;
    }

    private int containsUser(long userId) {
        for(int i = 0; i < this.projectMembers.size(); i++) {
            if (this.projectMembers.get(i).getId() == userId) {
                return i;
            }
        }
        return -1;
    }
}
