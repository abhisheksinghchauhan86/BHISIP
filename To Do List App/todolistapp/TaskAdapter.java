package com.firstapp.todolistapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final List<Task> taskList;
    private final OnTaskActionListener onTaskActionListener;

    public TaskAdapter(List<Task> taskList, OnTaskActionListener onTaskActionListener) {
        this.taskList = taskList;
        this.onTaskActionListener = onTaskActionListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskTextView.setText(" " + (position + 1) + ": " + task.getDescription());
        holder.editButton.setOnClickListener(v -> onTaskActionListener.onEditTask(task));
        holder.deleteButton.setOnClickListener(v -> onTaskActionListener.onDeleteTask(task, position));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        final TextView taskTextView;
        final Button editButton;
        final Button deleteButton;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.task_text);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    public interface OnTaskActionListener {
        void onEditTask(Task task);
        void onDeleteTask(Task task, int position);
    }
}





