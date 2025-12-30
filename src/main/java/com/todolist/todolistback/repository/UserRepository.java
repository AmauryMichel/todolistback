package com.todolist.todolistback.repository;

import org.springframework.stereotype.Repository;

import org.springframework.data.repository.CrudRepository;

import com.todolist.todolistback.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
    User findById(long id);
    User findByUsername(String username);
    boolean existsByUsername(String username);
}