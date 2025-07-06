package com.learngraphql.controller;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

  @Autowired private DataSource dataSource;

  @GetMapping("/health")
  public ResponseEntity<Map<String, Object>> health() {
    Map<String, Object> health = new HashMap<>();
    health.put("status", "UP");
    health.put("timestamp", System.currentTimeMillis());

    // Check database connectivity
    try (Connection connection = dataSource.getConnection()) {
      health.put("database", "UP");
    } catch (Exception e) {
      health.put("database", "DOWN");
      health.put("status", "DOWN");
    }

    return ResponseEntity.ok(health);
  }

  @GetMapping("/")
  public ResponseEntity<Map<String, String>> root() {
    Map<String, String> response = new HashMap<>();
    response.put("message", "Learn GraphQL API is running");
    response.put("graphql_endpoint", "/graphql");
    response.put("graphiql_endpoint", "/graphiql");
    return ResponseEntity.ok(response);
  }
}
