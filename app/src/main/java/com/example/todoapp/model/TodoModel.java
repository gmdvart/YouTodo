package com.example.todoapp.model;

import androidx.room.Insert;

import java.io.Serializable;

public class TodoModel implements Serializable {
    private final int id;
    private final String title;
    private final Boolean isDone;

    public TodoModel(int id, String title, Boolean isDone) {
        this.id = id;
        this.title = title;
        this.isDone = isDone;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public Boolean getDone() { return isDone; }
}
