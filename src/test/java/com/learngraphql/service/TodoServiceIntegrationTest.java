package com.learngraphql.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.learngraphql.BaseIntegrationTest;
import com.learngraphql.dto.CreateTodoInput;
import com.learngraphql.dto.UpdateTodoInput;
import com.learngraphql.entity.Todo;
import com.learngraphql.repository.TodoRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class TodoServiceIntegrationTest extends BaseIntegrationTest {

  @Autowired private TodoService todoService;

  @Autowired private TodoRepository todoRepository;

  @BeforeEach
  void setUp() {
    // 清理資料庫
    todoRepository.deleteAll();
  }

  @Test
  void createTodo_shouldPersistTodoSuccessfully() {
    // Given
    CreateTodoInput input = new CreateTodoInput("整合測試任務", "這是一個整合測試");

    // When
    Todo createdTodo = todoService.createTodo(input);

    // Then
    assertThat(createdTodo).isNotNull();
    assertThat(createdTodo.getId()).isNotNull();
    assertThat(createdTodo.getTitle()).isEqualTo("整合測試任務");
    assertThat(createdTodo.getDescription()).isEqualTo("這是一個整合測試");
    assertThat(createdTodo.getCompleted()).isFalse();
    assertThat(createdTodo.getCreatedAt()).isNotNull();
    assertThat(createdTodo.getUpdatedAt()).isNotNull();

    // 驗證資料庫中確實存在
    Optional<Todo> savedTodo = todoRepository.findById(createdTodo.getId());
    assertThat(savedTodo).isPresent();
    assertThat(savedTodo.get().getTitle()).isEqualTo("整合測試任務");
  }

  @Test
  void getAllTodos_shouldReturnAllTodosInCorrectOrder() {
    // Given - 創建多個 TODO
    CreateTodoInput input1 = new CreateTodoInput("第一個任務", "第一個描述");
    CreateTodoInput input2 = new CreateTodoInput("第二個任務", "第二個描述");

    Todo todo1 = todoService.createTodo(input1);
    Todo todo2 = todoService.createTodo(input2);

    // When
    List<Todo> allTodos = todoService.getAllTodos();

    // Then
    assertThat(allTodos).hasSize(2);
    // 應該按創建時間倒序排列，所以 todo2 應該在前
    assertThat(allTodos.get(0).getId()).isEqualTo(todo2.getId());
    assertThat(allTodos.get(1).getId()).isEqualTo(todo1.getId());
  }

  @Test
  void updateTodo_shouldUpdateExistingTodo() {
    // Given - 先創建一個 TODO
    CreateTodoInput createInput = new CreateTodoInput("原始標題", "原始描述");
    Todo originalTodo = todoService.createTodo(createInput);

    UpdateTodoInput updateInput = new UpdateTodoInput("更新後標題", "更新後描述", true);

    // When
    Optional<Todo> updatedTodo = todoService.updateTodo(originalTodo.getId(), updateInput);

    // Then
    assertThat(updatedTodo).isPresent();
    assertThat(updatedTodo.get().getTitle()).isEqualTo("更新後標題");
    assertThat(updatedTodo.get().getDescription()).isEqualTo("更新後描述");
    assertThat(updatedTodo.get().getCompleted()).isTrue();
    assertThat(updatedTodo.get().getUpdatedAt()).isAfter(updatedTodo.get().getCreatedAt());

    // 驗證資料庫中的資料確實被更新
    Optional<Todo> dbTodo = todoRepository.findById(originalTodo.getId());
    assertThat(dbTodo).isPresent();
    assertThat(dbTodo.get().getTitle()).isEqualTo("更新後標題");
    assertThat(dbTodo.get().getCompleted()).isTrue();
  }
}
