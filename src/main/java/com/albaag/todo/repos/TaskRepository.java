package com.albaag.todo.repos;

import com.albaag.todo.model.Task;
import com.albaag.todo.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByAuthor(User author);

}
