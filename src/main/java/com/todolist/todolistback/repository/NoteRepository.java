package com.todolist.todolistback.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import com.todolist.todolistback.entity.Note;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long>{
    Note findById(long id);
}
