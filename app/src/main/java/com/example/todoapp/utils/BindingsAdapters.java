package com.example.todoapp.utils;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoapp.adapter.TodoListAdapter;
import com.example.todoapp.constants.AnimatorConstants;
import com.example.todoapp.animations.CrossfadeAnimator;
import com.example.todoapp.model.TodoModel;

import java.util.List;

public final class BindingsAdapters {
    @BindingAdapter({"app:itemList"})
    public static void bindItemList(RecyclerView recyclerView, @Nullable List<TodoModel> todos) {
        TodoListAdapter adapter = (TodoListAdapter) recyclerView.getAdapter();
        if (adapter != null && todos != null) {
            adapter.submitList(todos);
        }
    }
}
