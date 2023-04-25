package com.example.todoapp.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.todoapp.R;
import com.example.todoapp.TodoApplication;
import com.example.todoapp.TodoConstants;
import com.example.todoapp.adapter.TodoListAdapter;
import com.example.todoapp.data.database.Todo;
import com.example.todoapp.databinding.FragmentTodoBinding;
import com.example.todoapp.model.TodoModel;
import com.example.todoapp.viewmodel.TodoViewModel;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TodoFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    private FragmentTodoBinding binding;
    private TodoViewModel viewModel;
    private TodoModel longClickedTodo;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTodoBinding.inflate(getLayoutInflater(), container, false);

        TodoApplication application = (TodoApplication) (requireActivity().getApplication());
        viewModel = new TodoViewModel.TodoViewModelFactory(application.container.getTodoRepository()).create(TodoViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView todoRecyclerView = binding.todoRecyclerView;
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        TodoListAdapter adapter = new TodoListAdapter(new TodoListAdapter.OnTodoClickListener() {
            @Override
            public void onLongTodoClick(TodoModel todo, View view) {
                longClickedTodo = todo;
                showPopupMenu(view);
            }
            @Override
            public void onCheckTodo(TodoModel todo, Boolean isDone) {
                viewModel.updateTodo(todo.getId(), todo.getTitle(), isDone);
            }
        });
        todoRecyclerView.setAdapter(adapter);

        viewModel.getAllTodos().observe(this.getViewLifecycleOwner(), new Observer<List<TodoModel>>() {
            @Override
            public void onChanged(List<TodoModel> todos) {
                adapter.submitList(todos);
            }
        });
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(requireContext(), view);
        popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
        popupMenu.inflate(R.menu.todo_menu);
        popupMenu.show();
    }

    private void showEditDialog() {
        Gson gson = new Gson();
        String todo = gson.toJson(longClickedTodo);

        Bundle transactionData = new Bundle();
        transactionData.putString(TodoConstants.KEY_DIALOG_TITLE, getString(R.string.edit_todo));
        transactionData.putString(TodoConstants.KEY_DIALOG_POSITIVE_TEXT, getString(R.string.edit));
        transactionData.putString(TodoConstants.KEY_DIALOG_TODO, todo);

        TodoAddDialog dialog = new TodoAddDialog();
        dialog.setArguments(transactionData);
        dialog.show(requireActivity().getSupportFragmentManager(), "");
    }

    private void showDeleteDialog(TodoModel todo) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
        dialog.setTitle(getString(R.string.delete_warning));
        dialog.setNegativeButton(getString(R.string.no), null);
        dialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Todo todoToDelete = new Todo(todo.getTitle(), todo.getDone());
                todoToDelete.setId(todo.getId());
                viewModel.deleteTodo(todoToDelete);
            }
        });
        dialog.create();
        dialog.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_edit) {
            showEditDialog();
        } else {
            showDeleteDialog(longClickedTodo);
        }
        return true;
    }
}