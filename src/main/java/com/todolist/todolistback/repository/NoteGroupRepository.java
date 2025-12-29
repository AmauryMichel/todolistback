package com.todolist.todolistback.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

import com.todolist.todolistback.entity.NoteGroup;

@Repository
public interface NoteGroupRepository extends CrudRepository<NoteGroup, Long>{

}
