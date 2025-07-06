package com.learngraphql.service;

import com.learngraphql.dto.CreateTodoInput;
import com.learngraphql.dto.UpdateTodoInput;
import com.learngraphql.entity.Todo;
import com.learngraphql.repository.TodoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TodoService {

  @Autowired private TodoRepository todoRepository;

  public List<Todo> getAllTodos() {
    return todoRepository.findAllByOrderByCreatedAtDesc();
  }

  public Optional<Todo> getTodoById(Long id) {
    return todoRepository.findById(id);
  }

  public List<Todo> getTodosByStatus(Boolean completed) {
    return todoRepository.findByCompleted(completed);
  }

  public Todo createTodo(CreateTodoInput input) {
    Todo todo = new Todo(input.title(), input.description());
    return todoRepository.save(todo);
  }

  public Optional<Todo> updateTodo(Long id, UpdateTodoInput input) {
    return todoRepository
        .findById(id)
        .map(
            todo -> {
              if (input.title() != null) {
                todo.setTitle(input.title());
              }
              if (input.description() != null) {
                todo.setDescription(input.description());
              }
              if (input.completed() != null) {
                todo.setCompleted(input.completed());
              }
              return todoRepository.save(todo);
            });
  }

  public Optional<Todo> toggleTodo(Long id) {
    return todoRepository
        .findById(id)
        .map(
            todo -> {
              todo.setCompleted(!todo.getCompleted());
              return todoRepository.save(todo);
            });
  }

  public boolean deleteTodo(Long id) {
    if (todoRepository.existsById(id)) {
      todoRepository.deleteById(id);
      return true;
    }
    return false;
  }
}
