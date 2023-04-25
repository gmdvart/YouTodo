package com.example.todoapp;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoapp.adapter.TodoListAdapter;
import com.example.todoapp.model.TodoModel;

import java.util.List;

public final class BindingsAdapters {
    @BindingAdapter("itemList")
    public void bindRecyclerView(RecyclerView recyclerView, @Nullable List<TodoModel> todos) {
        TodoListAdapter adapter = (TodoListAdapter) recyclerView.getAdapter();
        if (adapter != null && todos != null) {
                adapter.submitList(todos);
        }
    }

    @BindingAdapter("emptyText")
    public void bindEmptyText(TextView textView, int todoSize) {
        if (todoSize == 0) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(R.string.todos_empty);
        } else {
            textView.setVisibility(View.GONE);
        }
    }
}
