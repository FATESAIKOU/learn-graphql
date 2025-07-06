package com.learngraphql.service;

import com.learngraphql.entity.Todo;
import com.learngraphql.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TodoDataSeeder implements CommandLineRunner {

  @Autowired private TodoRepository todoRepository;

  @Override
  public void run(String... args) throws Exception {
    if (todoRepository.count() == 0) {
      seedSampleData();
    }
  }

  private void seedSampleData() {
    // Create sample todos
    Todo todo1 = new Todo("學習 GraphQL", "完成 GraphQL 教學課程和實作練習");
    Todo todo2 = new Todo("完成專案", "完成 Spring Boot + GraphQL TODO 應用程式");
    todo2.setCompleted(true);

    Todo todo3 = new Todo("買菜", "去超市買這週的食材");
    Todo todo4 = new Todo("運動", "每天至少運動 30 分鐘");
    Todo todo5 = new Todo("讀書", "閱讀技術書籍增進知識");

    todoRepository.save(todo1);
    todoRepository.save(todo2);
    todoRepository.save(todo3);
    todoRepository.save(todo4);
    todoRepository.save(todo5);

    System.out.println("範例 TODO 資料已成功建立！");
  }
}
