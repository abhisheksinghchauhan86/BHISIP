package com.firstapp.todolistapp;
public class Task {
    private static int nextId = 1; // Static variable to keep track of the next ID
    private int taskNumber;
    private String description;
    private boolean isCompleted;

    // Constructor
    public Task(String description) {
        this.taskNumber = nextId++;
        this.description = description;
        this.isCompleted = false;
    }

    // Getters and Setters
    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString() {
        return "Task #" + taskNumber + ": " + description + (isCompleted ? " (Completed)" : " (Pending)");
    }
}
