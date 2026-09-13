package com.example.rep3.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {

    private int id;
    private String title;
    private String description;
    private String executor;
    private LocalDate deadline;
    private TaskStatus status;
    private TaskCategory category;
    private LocalDateTime updatedAt;

    public Task(int id,
                String title,
                String description,
                String executor,
                LocalDate deadline,
                TaskStatus status,
                TaskCategory category) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.executor = executor;
        this.deadline = deadline;
        this.status = status;
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getExecutor() {
        return executor;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public TaskCategory getCategory() {
        return category;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setTitle(String title) {
        this.title = title;
        updateTime();
    }

    public void setDescription(String description) {
        this.description = description;
        updateTime();
    }

    public void setExecutor(String executor) {
        this.executor = executor;
        updateTime();
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
        updateTime();
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
        updateTime();
    }

    public void setCategory(TaskCategory category) {
        this.category = category;
        updateTime();
    }

    private void updateTime() {
        updatedAt = LocalDateTime.now();
    }
}