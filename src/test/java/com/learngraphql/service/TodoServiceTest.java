package com.learngraphql.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.learngraphql.dto.CreateTodoInput;
import com.learngraphql.dto.UpdateTodoInput;
import com.learngraphql.entity.Todo;
import com.learngraphql.repository.TodoRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

  @Mock private TodoRepository todoRepository;

  @InjectMocks private TodoService todoService;

  private Todo testTodo;
  private Todo completedTodo;

  @BeforeEach
  void setUp() {
    testTodo = new Todo("測試任務", "這是一個測試任務");
    testTodo.setId(1L);
    testTodo.setCreatedAt(LocalDateTime.now());
    testTodo.setUpdatedAt(LocalDateTime.now());

    completedTodo = new Todo("完成的任務", "這是已完成的任務");
    completedTodo.setId(2L);
    completedTodo.setCompleted(true);
    completedTodo.setCreatedAt(LocalDateTime.now());
    completedTodo.setUpdatedAt(LocalDateTime.now());
  }

  @Test
  void getAllTodos_shouldReturnAllTodosOrderedByCreatedAtDesc() {
    // Given
    List<Todo> expectedTodos = Arrays.asList(testTodo, completedTodo);
    when(todoRepository.findAllByOrderByCreatedAtDesc()).thenReturn(expectedTodos);

    // When
    List<Todo> actualTodos = todoService.getAllTodos();

    // Then
    assertThat(actualTodos).hasSize(2);
    assertThat(actualTodos).containsExactly(testTodo, completedTodo);
    verify(todoRepository).findAllByOrderByCreatedAtDesc();
  }

  @Test
  void getTodoById_shouldReturnTodoWhenExists() {
    // Given
    when(todoRepository.findById(1L)).thenReturn(Optional.of(testTodo));

    // When
    Optional<Todo> result = todoService.getTodoById(1L);

    // Then
    assertThat(result).isPresent();
    assertThat(result.get()).isEqualTo(testTodo);
    verify(todoRepository).findById(1L);
  }

  @Test
  void getTodoById_shouldReturnEmptyWhenNotExists() {
    // Given
    when(todoRepository.findById(999L)).thenReturn(Optional.empty());

    // When
    Optional<Todo> result = todoService.getTodoById(999L);

    // Then
    assertThat(result).isEmpty();
    verify(todoRepository).findById(999L);
  }

  @Test
  void getTodosByStatus_shouldReturnCompletedTodos() {
    // Given
    List<Todo> completedTodos = Arrays.asList(completedTodo);
    when(todoRepository.findByCompleted(true)).thenReturn(completedTodos);

    // When
    List<Todo> result = todoService.getTodosByStatus(true);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getCompleted()).isTrue();
    verify(todoRepository).findByCompleted(true);
  }

  @Test
  void getTodosByStatus_shouldReturnIncompleteTodos() {
    // Given
    List<Todo> incompleteTodos = Arrays.asList(testTodo);
    when(todoRepository.findByCompleted(false)).thenReturn(incompleteTodos);

    // When
    List<Todo> result = todoService.getTodosByStatus(false);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getCompleted()).isFalse();
    verify(todoRepository).findByCompleted(false);
  }

  @Test
  void createTodo_shouldCreateAndReturnNewTodo() {
    // Given
    CreateTodoInput input = new CreateTodoInput("新任務", "新任務描述");
    Todo savedTodo = new Todo(input.title(), input.description());
    savedTodo.setId(3L);

    when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

    // When
    Todo result = todoService.createTodo(input);

    // Then
    assertThat(result.getId()).isEqualTo(3L);
    assertThat(result.getTitle()).isEqualTo("新任務");
    assertThat(result.getDescription()).isEqualTo("新任務描述");
    assertThat(result.getCompleted()).isFalse();
    verify(todoRepository).save(any(Todo.class));
  }

  @Test
  void createTodo_shouldCreateTodoWithNullDescription() {
    // Given
    CreateTodoInput input = new CreateTodoInput("新任務", null);
    Todo savedTodo = new Todo(input.title(), input.description());
    savedTodo.setId(3L);

    when(todoRepository.save(any(Todo.class))).thenReturn(savedTodo);

    // When
    Todo result = todoService.createTodo(input);

    // Then
    assertThat(result.getTitle()).isEqualTo("新任務");
    assertThat(result.getDescription()).isNull();
    verify(todoRepository).save(any(Todo.class));
  }

  @Test
  void updateTodo_shouldUpdateExistingTodo() {
    // Given
    UpdateTodoInput input = new UpdateTodoInput("更新的標題", "更新的描述", true);
    when(todoRepository.findById(1L)).thenReturn(Optional.of(testTodo));
    when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

    // When
    Optional<Todo> result = todoService.updateTodo(1L, input);

    // Then
    assertThat(result).isPresent();
    assertThat(testTodo.getTitle()).isEqualTo("更新的標題");
    assertThat(testTodo.getDescription()).isEqualTo("更新的描述");
    assertThat(testTodo.getCompleted()).isTrue();
    verify(todoRepository).findById(1L);
    verify(todoRepository).save(testTodo);
  }

  @Test
  void updateTodo_shouldUpdateOnlyProvidedFields() {
    // Given
    UpdateTodoInput input = new UpdateTodoInput("只更新標題", null, null);
    String originalDescription = testTodo.getDescription();
    Boolean originalCompleted = testTodo.getCompleted();

    when(todoRepository.findById(1L)).thenReturn(Optional.of(testTodo));
    when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

    // When
    Optional<Todo> result = todoService.updateTodo(1L, input);

    // Then
    assertThat(result).isPresent();
    assertThat(testTodo.getTitle()).isEqualTo("只更新標題");
    assertThat(testTodo.getDescription()).isEqualTo(originalDescription);
    assertThat(testTodo.getCompleted()).isEqualTo(originalCompleted);
    verify(todoRepository).save(testTodo);
  }

  @Test
  void updateTodo_shouldReturnEmptyWhenTodoNotExists() {
    // Given
    UpdateTodoInput input = new UpdateTodoInput("標題", "描述", true);
    when(todoRepository.findById(999L)).thenReturn(Optional.empty());

    // When
    Optional<Todo> result = todoService.updateTodo(999L, input);

    // Then
    assertThat(result).isEmpty();
    verify(todoRepository).findById(999L);
    verify(todoRepository, never()).save(any(Todo.class));
  }

  @Test
  void toggleTodo_shouldToggleCompletionStatus() {
    // Given
    boolean originalStatus = testTodo.getCompleted();
    when(todoRepository.findById(1L)).thenReturn(Optional.of(testTodo));
    when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

    // When
    Optional<Todo> result = todoService.toggleTodo(1L);

    // Then
    assertThat(result).isPresent();
    assertThat(testTodo.getCompleted()).isEqualTo(!originalStatus);
    verify(todoRepository).findById(1L);
    verify(todoRepository).save(testTodo);
  }

  @Test
  void toggleTodo_shouldReturnEmptyWhenTodoNotExists() {
    // Given
    when(todoRepository.findById(999L)).thenReturn(Optional.empty());

    // When
    Optional<Todo> result = todoService.toggleTodo(999L);

    // Then
    assertThat(result).isEmpty();
    verify(todoRepository).findById(999L);
    verify(todoRepository, never()).save(any(Todo.class));
  }

  @Test
  void deleteTodo_shouldReturnTrueWhenTodoExists() {
    // Given
    when(todoRepository.existsById(1L)).thenReturn(true);

    // When
    boolean result = todoService.deleteTodo(1L);

    // Then
    assertThat(result).isTrue();
    verify(todoRepository).existsById(1L);
    verify(todoRepository).deleteById(1L);
  }

  @Test
  void deleteTodo_shouldReturnFalseWhenTodoNotExists() {
    // Given
    when(todoRepository.existsById(999L)).thenReturn(false);

    // When
    boolean result = todoService.deleteTodo(999L);

    // Then
    assertThat(result).isFalse();
    verify(todoRepository).existsById(999L);
    verify(todoRepository, never()).deleteById(anyLong());
  }
}
