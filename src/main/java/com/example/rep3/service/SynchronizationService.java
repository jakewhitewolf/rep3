package com.example.rep3.service;

import com.example.rep3.dao.TaskDAO;
import com.example.rep3.model.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SynchronizationService {

    private final List<TaskDAO> sources;

    public SynchronizationService(List<TaskDAO> sources) {
        this.sources = sources;
    }

    public void synchronize() {
        Map<Integer, Task> latestTasks = new HashMap<>();

        for (TaskDAO source : sources) {
            for (Task task : source.findAll()) {
                Task existing = latestTasks.get(task.getId());

                if (existing == null ||
                        task.getUpdatedAt().isAfter(existing.getUpdatedAt())) {
                    latestTasks.put(task.getId(), task);
                }
            }
        }

        for (TaskDAO source : sources) {
            for (Task task : latestTasks.values()) {
                Optional<Task> existing = source.findById(task.getId());

                if (existing.isEmpty()) {
                    source.add(task);
                } else if (task.getUpdatedAt()
                        .isAfter(existing.get().getUpdatedAt())) {
                    source.update(task);
                }
            }
        }
    }
}