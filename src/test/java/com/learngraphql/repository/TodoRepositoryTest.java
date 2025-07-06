package com.learngraphql.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.learngraphql.BaseIntegrationTest;
import com.learngraphql.entity.Todo;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class TodoRepositoryTest extends BaseIntegrationTest {

  @Autowired private TodoRepository todoRepository;

  private Todo completedTodo;
  private Todo incompleteTodo;

  @BeforeEach
  void setUp() {
    // 清理現有資料
    todoRepository.deleteAll();

    // 創建測試資料
    completedTodo = new Todo("完成的任務", "這是一個已完成的任務");
    completedTodo.setCompleted(true);
    completedTodo = todoRepository.save(completedTodo);

    incompleteTodo = new Todo("未完成的任務", "這是一個未完成的任務");
    incompleteTodo.setCompleted(false);
    incompleteTodo = todoRepository.save(incompleteTodo);

    Todo anotherTodo = new Todo("GraphQL 學習", "學習 GraphQL 相關技術");
    anotherTodo.setCompleted(false);
    todoRepository.save(anotherTodo);
  }

  @Test
  void testFindByCompleted_shouldReturnCompletedTodos() {
    // When
    List<Todo> completedTodos = todoRepository.findByCompleted(true);

    // Then
    assertThat(completedTodos).hasSize(1);
    assertThat(completedTodos.get(0).getTitle()).isEqualTo("完成的任務");
    assertThat(completedTodos.get(0).getCompleted()).isTrue();
  }

  @Test
  void testFindByCompleted_shouldReturnIncompleteTodos() {
    // When
    List<Todo> incompleteTodos = todoRepository.findByCompleted(false);

    // Then
    assertThat(incompleteTodos).hasSize(2);
    assertThat(incompleteTodos).extracting(Todo::getCompleted).allMatch(completed -> !completed);
  }

  @Test
  void testFindByTitleContainingIgnoreCase_shouldReturnMatchingTodos() {
    // When
    List<Todo> foundTodos = todoRepository.findByTitleContainingIgnoreCase("graphql");

    // Then
    assertThat(foundTodos).hasSize(1);
    assertThat(foundTodos.get(0).getTitle()).isEqualTo("GraphQL 學習");
  }

  @Test
  void testFindByTitleContainingIgnoreCase_shouldReturnEmptyWhenNoMatch() {
    // When
    List<Todo> foundTodos = todoRepository.findByTitleContainingIgnoreCase("不存在的任務");

    // Then
    assertThat(foundTodos).isEmpty();
  }

  @Test
  void testFindAllByOrderByCreatedAtDesc_shouldReturnTodosInDescendingOrder() {
    // When
    List<Todo> allTodos = todoRepository.findAllByOrderByCreatedAtDesc();

    // Then
    assertThat(allTodos).hasSize(3);
    // 因為資料是按順序創建的，最後創建的應該在最前面
    for (int i = 0; i < allTodos.size() - 1; i++) {
      assertThat(allTodos.get(i).getCreatedAt())
          .isAfterOrEqualTo(allTodos.get(i + 1).getCreatedAt());
    }
  }

  @Test
  void testSave_shouldPersistTodo() {
    // Given
    Todo newTodo = new Todo("新測試任務", "測試保存功能");

    // When
    Todo savedTodo = todoRepository.save(newTodo);

    // Then
    assertThat(savedTodo.getId()).isNotNull();
    assertThat(savedTodo.getTitle()).isEqualTo("新測試任務");
    assertThat(savedTodo.getDescription()).isEqualTo("測試保存功能");
    assertThat(savedTodo.getCompleted()).isFalse();
    assertThat(savedTodo.getCreatedAt()).isNotNull();
    assertThat(savedTodo.getUpdatedAt()).isNotNull();
  }

  @Test
  void testDeleteById_shouldRemoveTodo() {
    // Given
    Long todoId = completedTodo.getId();
    assertThat(todoRepository.existsById(todoId)).isTrue();

    // When
    todoRepository.deleteById(todoId);

    // Then
    assertThat(todoRepository.existsById(todoId)).isFalse();
    assertThat(todoRepository.findAll()).hasSize(2);
  }
}
