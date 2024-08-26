package com.firstapp.todolistapp;

// MainActivity.java
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText taskInput;
    private Button addButton;
    private RecyclerView taskList;
    private TaskAdapter taskAdapter;
    private List<Task> tasks;
    private static final String PREFS_NAME = "task_prefs";
    private static final String KEY_TASKS = "tasks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskInput = findViewById(R.id.task_input);
        addButton = findViewById(R.id.add_button);
        taskList = findViewById(R.id.task_list);

        tasks = new ArrayList<>();
        taskAdapter = new TaskAdapter(tasks, new TaskAdapter.OnTaskActionListener() {
            @Override
            public void onEditTask(Task task) {
                showEditTaskDialog(task);
            }

            @Override
            public void onDeleteTask(Task task, int position) {
                deleteTask(position);
            }
        });
        taskList.setLayoutManager(new LinearLayoutManager(this));
        taskList.setAdapter(taskAdapter);

        addButton.setOnClickListener(v -> addTask());

        // Load tasks from storage
        loadTasks();
    }

    private void addTask() {
        String description = taskInput.getText().toString();
        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter a task", Toast.LENGTH_SHORT).show();
            return;
        }

        Task task = new Task(description);
        tasks.add(task);
        taskAdapter.notifyItemInserted(tasks.size() - 1);
        taskInput.setText(""); // Clear input field

        // Save tasks to storage
        saveTasks();
    }

    private void showEditTaskDialog(Task task) {
        final EditText editTaskInput = new EditText(this);
        editTaskInput.setText(task.getDescription());

        new AlertDialog.Builder(this)
                .setTitle("Edit Task")
                .setView(editTaskInput)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newDescription = editTaskInput.getText().toString();
                    if (!newDescription.isEmpty()) {
                        task.setDescription(newDescription);
                        taskAdapter.notifyDataSetChanged(); // Refresh the RecyclerView
                        saveTasks(); // Save updated tasks
                    } else {
                        Toast.makeText(MainActivity.this, "Task description cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteTask(int position) {
        tasks.remove(position);
        taskAdapter.notifyItemRemoved(position);
        taskAdapter.notifyItemRangeChanged(position, tasks.size()); // Update item positions
        saveTasks(); // Save updated tasks
    }

    private void saveTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tasks);
        editor.putString(KEY_TASKS, json);
        editor.apply();
    }

    private void loadTasks() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_TASKS, null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        tasks = gson.fromJson(json, type);

        if (tasks == null) {
            tasks = new ArrayList<>();
        }

        taskAdapter = new TaskAdapter(tasks, new TaskAdapter.OnTaskActionListener() {
            @Override
            public void onEditTask(Task task) {
                showEditTaskDialog(task);
            }

            @Override
            public void onDeleteTask(Task task, int position) {
                deleteTask(position);
            }
        });
        taskList.setAdapter(taskAdapter);
    }
}



