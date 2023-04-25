package com.example.todoapp.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoapp.databinding.TodoListItemBinding;
import com.example.todoapp.model.TodoModel;
import org.jetbrains.annotations.NotNull;

public class TodoListAdapter extends ListAdapter<TodoModel, TodoListAdapter.TodoViewHolder> {
    static class TodoViewHolder extends RecyclerView.ViewHolder {
        private final TodoListItemBinding binding;
        public final TodoListItemBinding getBinding() {
            return binding;
        }
        TodoViewHolder(TodoListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TodoModel todo) {
            binding.setTodo(todo);
            binding.todoTitle.setText(todo.getTitle());
            binding.todoStatus.setChecked(todo.getDone());
        }
    }

    private final OnTodoClickListener todoClickListener;
    public TodoListAdapter(OnTodoClickListener todoClickListener) {
        super(TodoListAdapter.DiffCallback);
        this.todoClickListener = todoClickListener;
    }

    @NotNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        TodoListItemBinding binding = TodoListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TodoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NotNull TodoViewHolder holder, int position) {
        TodoModel currentTodo = getItem(position);
        holder.binding.todoStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                todoClickListener.onCheckTodo(currentTodo, isChecked);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                todoClickListener.onLongTodoClick(currentTodo, holder.itemView);
                return true;
            }
        });
        holder.bind(currentTodo);
    }

    public static final DiffUtil.ItemCallback<TodoModel> DiffCallback = new DiffUtil.ItemCallback<TodoModel>() {
        @Override
        public boolean areItemsTheSame(@NotNull TodoModel oldItem, @NotNull TodoModel newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TodoModel oldItem, @NotNull TodoModel newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    };

    public interface OnTodoClickListener {
        void onLongTodoClick(TodoModel todo, View view);
        void onCheckTodo(TodoModel todo, Boolean isDone);
    }
}
