package com.example.todoapp.data.app;

import com.example.todoapp.data.repository.TodoRepository;

public interface AppDataContainer {
    TodoRepository getTodoRepository();
}