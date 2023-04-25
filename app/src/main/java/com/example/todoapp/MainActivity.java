package com.example.todoapp;

import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.example.todoapp.databinding.ActivityMainBinding;
import com.example.todoapp.ui.TodoAddDialog;
import com.example.todoapp.ui.TodoFragment;
import com.example.todoapp.viewmodel.TodoViewModel;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private TodoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TodoApplication application = (TodoApplication) getApplication();
        viewModel = new TodoViewModel.TodoViewModelFactory(application.container.getTodoRepository()).create(TodoViewModel.class);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, TodoFragment.class, null);
        fragmentTransaction.commit();

        binding.todoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTodoDialog();
            }
        });
    }

    private void showAddTodoDialog() {
        TodoAddDialog dialog = new TodoAddDialog();
        dialog.show(getSupportFragmentManager(), "");
    }
}

