package com.example.todoapp.data.repository;

import androidx.lifecycle.LiveData;
import androidx.room.Database;
import com.example.todoapp.data.database.Todo;
import com.example.todoapp.data.database.TodoDao;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class TodoRepository {
    private final TodoDao todoDao;
    private final ExecutorService databaseWriteExecutor;

    public TodoRepository(TodoDao todoDao, ExecutorService databaseWriteExecutor) {
        this.todoDao = todoDao;
        this.databaseWriteExecutor = databaseWriteExecutor;
    }

    public void insertTodo(Todo todo) {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                todoDao.insert(todo);
            }
        });
    }
    public void updateTodo(Todo todo) {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                todoDao.update(todo);
            }

        });
    }
    public void deleteTodo(Todo todo) {
        databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                todoDao.delete(todo);
            }
        });
    }
    public LiveData<List<Todo>> getAllTodos() {
        return todoDao.getAll();
    }
}
