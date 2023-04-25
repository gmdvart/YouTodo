package com.example.todoapp.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.todoapp.data.database.Todo;
import com.example.todoapp.data.repository.TodoRepository;
import com.example.todoapp.model.TodoModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class TodoViewModel extends ViewModel {
    private final TodoRepository todoRepository;
    private final LiveData<List<TodoModel>> allTodos;

    TodoViewModel(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
        this.allTodos = Transformations.map(todoRepository.getAllTodos(), todos -> {
            return todos.stream().map(todo -> new TodoModel(todo.getId(), todo.getTitle(), todo.getDone())).collect(Collectors.toList());
        });
    }

    public LiveData<List<TodoModel>> getAllTodos() {
        return allTodos;
    }

    public void insertTodo(Todo todo) {
        todoRepository.insertTodo(todo);
    }

    public void updateTodo(int id, String title, Boolean isDone) {
        Todo updatedTodo = new Todo(title, isDone);
        updatedTodo.setId(id);
        todoRepository.updateTodo(updatedTodo);
    }

    public void deleteTodo(Todo todo) {
        todoRepository.deleteTodo(todo);
    }

    public static class TodoViewModelFactory implements ViewModelProvider.Factory {
        private final TodoRepository repository;
        public TodoViewModelFactory(TodoRepository repository) {
            this.repository = repository;
        }

        @SuppressWarnings("unchecked")
        @NotNull
        @Override
        public <T extends ViewModel> T create(@NotNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(TodoViewModel.class)) {
                return (T) new TodoViewModel(repository);
            } else {
                throw new IllegalArgumentException("Unknown View Model class");
            }
        }
    }
}
