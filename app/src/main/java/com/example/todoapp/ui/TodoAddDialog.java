package com.example.todoapp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.fragment.app.DialogFragment;
import com.example.todoapp.R;
import com.example.todoapp.TodoApplication;
import com.example.todoapp.constants.TodoConstants;
import com.example.todoapp.data.database.Todo;
import com.example.todoapp.databinding.TodoAddDialogBinding;
import com.example.todoapp.model.TodoModel;
import com.example.todoapp.viewmodel.TodoViewModel;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TodoAddDialog extends DialogFragment {
    private TodoAddDialogBinding binding;
    private TodoViewModel viewModel;
    private TodoModel capturedTodo;


    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getLayoutInflater();
        binding = TodoAddDialogBinding.inflate(inflater, null, false);

        TodoApplication application = (TodoApplication) (requireActivity().getApplication());
        viewModel = new TodoViewModel.TodoViewModelFactory(application.container.getTodoRepository()).create(TodoViewModel.class);

        String dialogTitle = getString(R.string.create_todo);
        String positiveButtonText = getString(R.string.add);
        if (getArguments() != null) {
            dialogTitle = getArguments().getString(TodoConstants.KEY_DIALOG_TITLE);
            positiveButtonText = getArguments().getString(TodoConstants.KEY_DIALOG_POSITIVE_TEXT);

            Gson gson = new Gson();
            capturedTodo = gson.fromJson(getArguments().getString(TodoConstants.KEY_DIALOG_TODO), TodoModel.class);
            binding.todoInput.setText(capturedTodo.getTitle());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity())
            .setView(binding.getRoot())
            .setTitle(dialogTitle)
            .setNegativeButton(getString(R.string.cancel), null)
            .setPositiveButton(positiveButtonText, null);

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        AlertDialog dialog = (AlertDialog) requireDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputTitleText = Objects.requireNonNull(binding.todoInput.getText()).toString();
                if (inputTitleText.isEmpty()) {
                    setErrorOnInput(true);
                } else {
                    setErrorOnInput(false);
                    addTodo(inputTitleText);
                    dismiss();
                }
            }
        });
    }

    private void addTodo(String title) {
        if (capturedTodo == null) {
            Todo newTodo = new Todo(title, false);
            viewModel.insertTodo(newTodo);
        } else {
            viewModel.updateTodo(capturedTodo.getId(), title, capturedTodo.getDone());
        }
    }

    private void setErrorOnInput(Boolean isError) {
        if (isError) {
            binding.todoTextField.setErrorEnabled(true);
            binding.todoTextField.setError(getString(R.string.error_message));
        } else {
            binding.todoTextField.setErrorEnabled(false);
            binding.todoTextField.setError(null);
        }
    }
}
