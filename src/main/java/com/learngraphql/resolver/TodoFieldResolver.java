package com.learngraphql.resolver;

import com.learngraphql.entity.Todo;
import java.time.format.DateTimeFormatter;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TodoFieldResolver {

  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @SchemaMapping(typeName = "Todo", field = "createdAt")
  public String createdAt(Todo todo) {
    return todo.getCreatedAt().format(FORMATTER);
  }

  @SchemaMapping(typeName = "Todo", field = "updatedAt")
  public String updatedAt(Todo todo) {
    return todo.getUpdatedAt().format(FORMATTER);
  }
}
