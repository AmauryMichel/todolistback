package com.todolist.todolistback.repository;

import org.springframework.stereotype.Repository;

import com.todolist.todolistback.entity.User;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
    User findByUsername(String username);
    boolean existsByUsername(String username);
}