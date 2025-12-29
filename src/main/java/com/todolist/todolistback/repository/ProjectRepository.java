package com.todolist.todolistback.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import com.todolist.todolistback.entity.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long>{
    
}
