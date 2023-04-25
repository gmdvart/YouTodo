package com.example.todoapp.data.app;

import android.content.Context;
import com.example.todoapp.data.database.TodoDatabase;
import com.example.todoapp.data.repository.TodoRepository;

public class TodoDataContainer implements AppDataContainer {
    private final Context context;

    public TodoDataContainer(final Context context) {
        this.context = context;
    }

    @Override
    public TodoRepository getTodoRepository() {
        return new TodoRepository(
                TodoDatabase.getDatabase(context).getDao(),
                TodoDatabase.databaseWriteExecutor
            );
    }
}
