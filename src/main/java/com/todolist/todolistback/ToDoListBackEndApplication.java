package com.todolist.todolistback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan("com.todolist.todolistback.user")
public class ToDoListBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToDoListBackEndApplication.class, args);
	}
}