package com.learngraphql.dto;

public record UpdateTodoInput(String title, String description, Boolean completed) {}
