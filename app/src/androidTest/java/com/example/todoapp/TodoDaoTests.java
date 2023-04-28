package com.example.todoapp;

import android.content.Context;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.example.todoapp.data.database.Todo;
import com.example.todoapp.data.database.TodoDao;
import com.example.todoapp.data.database.TodoDatabase;
import com.example.todoapp.utils.LiveDataInterceptor;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TodoDaoTests {
    private static final String FIRST_TODO_TITLE = "First Title";
    private static final String SECOND_TODO_TITLE = "Second Title";
    private static final String TODO_UPDATED_TITLE = "Updated Title";

    private Todo firstTodo;
    private Todo secondTodo;

    private TodoDatabase database;
    private TodoDao dao;
    private final LiveDataInterceptor<List<Todo>> interceptor = new LiveDataInterceptor<>();

    @Before
    public void initializeTodos() {
        firstTodo = new Todo(FIRST_TODO_TITLE, false);
        firstTodo.setId(1);

        secondTodo = new Todo(SECOND_TODO_TITLE, false);
        secondTodo.setId(2);
    }

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, TodoDatabase.class).build();
        dao = database.getDao();
    }

    @After
    public void closeDb() throws IOException {
        database.close();
    }

    @Test
    public void database_addTodos_validateTodoSize() throws TimeoutException {
        dao.insert(firstTodo);
        List<Todo> todoItemList = interceptor.getOrAwaitValueFrom(dao.getAll());
        Assert.assertEquals(1, todoItemList.size());

        dao.insert(secondTodo);
        todoItemList = interceptor.getOrAwaitValueFrom(dao.getAll());
        Assert.assertEquals(2, todoItemList.size());
    }

    @Test
    public void database_deleteTodos_validateTodoSize() throws TimeoutException {
        dao.insert(firstTodo);
        dao.insert(secondTodo);
        List<Todo> todoItemList = interceptor.getOrAwaitValueFrom(dao.getAll());
        Assert.assertEquals(2, todoItemList.size());

        dao.delete(firstTodo);
        todoItemList = interceptor.getOrAwaitValueFrom(dao.getAll());
        Assert.assertEquals(1, todoItemList.size());

        dao.delete(secondTodo);
        todoItemList = interceptor.getOrAwaitValueFrom(dao.getAll());
        Assert.assertEquals(0, todoItemList.size());
    }
    
    @Test
    public void database_updateTodo_todoValueChanged() throws TimeoutException {
        dao.insert(firstTodo);
        Todo todoItem = interceptor.getOrAwaitValueFrom(dao.getAll()).get(0);
        Assert.assertEquals(FIRST_TODO_TITLE, todoItem.getTitle());

        updateTodo();
        todoItem = interceptor.getOrAwaitValueFrom(dao.getAll()).get(0);
        Assert.assertEquals(TODO_UPDATED_TITLE, todoItem.getTitle());
    }

    private void updateTodo() {
        int id = firstTodo.getId();
        firstTodo = new Todo(TODO_UPDATED_TITLE, firstTodo.getDone());
        firstTodo.setId(id);
        dao.update(firstTodo);
    }
}
