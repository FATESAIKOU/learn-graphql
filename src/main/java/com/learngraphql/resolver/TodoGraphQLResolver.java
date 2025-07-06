package com.learngraphql.resolver;

import com.learngraphql.dto.CreateTodoInput;
import com.learngraphql.dto.UpdateTodoInput;
import com.learngraphql.entity.Todo;
import com.learngraphql.service.TodoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TodoGraphQLResolver {

  @Autowired private TodoService todoService;

  // Queries
  @QueryMapping
  public List<Todo> todos() {
    return todoService.getAllTodos();
  }

  @QueryMapping
  public Todo todo(@Argument String id) {
    return todoService.getTodoById(Long.parseLong(id)).orElse(null);
  }

  @QueryMapping
  public List<Todo> todosByStatus(@Argument Boolean completed) {
    return todoService.getTodosByStatus(completed);
  }

  @QueryMapping
  public String hello() {
    return "Hello from TODO GraphQL API!";
  }

  // Mutations
  @MutationMapping
  public Todo createTodo(@Argument CreateTodoInput input) {
    return todoService.createTodo(input);
  }

  @MutationMapping
  public Todo updateTodo(@Argument String id, @Argument UpdateTodoInput input) {
    return todoService.updateTodo(Long.parseLong(id), input).orElse(null);
  }

  @MutationMapping
  public Todo toggleTodo(@Argument String id) {
    return todoService.toggleTodo(Long.parseLong(id)).orElse(null);
  }

  @MutationMapping
  public Boolean deleteTodo(@Argument String id) {
    return todoService.deleteTodo(Long.parseLong(id));
  }
}
