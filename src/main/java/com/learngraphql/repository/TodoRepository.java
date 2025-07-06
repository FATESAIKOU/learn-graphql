package com.learngraphql.repository;

import com.learngraphql.entity.Todo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

  List<Todo> findByCompleted(Boolean completed);

  List<Todo> findByTitleContainingIgnoreCase(String title);

  List<Todo> findAllByOrderByCreatedAtDesc();
}
