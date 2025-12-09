package com.todolist.todolistback.user;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{
    User findByUsername(String username);
    boolean existsByUsername(String username);
}