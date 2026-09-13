package com.example.rep3.dao;

import com.example.rep3.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CollectionTaskDAO implements TaskDAO {

    private final List<Task> tasks = new ArrayList<>();

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    @Override
    public Optional<Task> findById(int id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst();
    }

    @Override
    public void add(Task task) {
        tasks.add(task);
    }

    @Override
    public void update(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.set(i, task);
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        tasks.removeIf(task -> task.getId() == id);
    }
}