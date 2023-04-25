package com.example.todoapp;

import android.app.Application;
import com.example.todoapp.data.app.AppDataContainer;
import com.example.todoapp.data.app.TodoDataContainer;

public class TodoApplication extends Application {
    public AppDataContainer container;

    @Override
    public void onCreate() {
        super.onCreate();
        container = new TodoDataContainer(this);
    }
}
