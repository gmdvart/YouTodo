package com.example.todoapp.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Todo todo);

    @Update
    void update(Todo todo);

    @Delete
    void delete(Todo todo);

    @Query("SELECT * FROM todos ORDER BY id ASC")
    LiveData<List<Todo>> getAll();
}
