package com.example.todoapp.data.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "todos")
public class Todo {
    @PrimaryKey(autoGenerate = true) private int id = 0;
    private String title;
    private Boolean isDone;

    public Todo(String title, Boolean isDone) {
        this.title = title;
        this.isDone = isDone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getDone() {
        return isDone;
    }
}
