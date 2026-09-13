package com.example.rep3.service;

import com.example.rep3.dao.TaskDAO;
import com.example.rep3.model.Task;
import com.example.rep3.model.TaskCategory;
import com.example.rep3.model.TaskStatus;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskService {

    private final TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public List<Task> getAllTasks() {
        return taskDAO.findAll();
    }

    public Task createTask(
            String title,
            String description,
            String executor,
            LocalDate deadline,
            TaskStatus status,
            TaskCategory category
    ) {
        validateTaskData(
                title,
                executor,
                deadline,
                status,
                category
        );

        int id = generateId();

        Task task = new Task(
                id,
                title,
                description,
                executor,
                deadline,
                status,
                category
        );

        taskDAO.add(task);

        return task;
    }

    public void updateTask(
            int id,
            String title,
            String description,
            String executor,
            LocalDate deadline,
            TaskStatus status,
            TaskCategory category
    ) {
        validateTaskData(
                title,
                executor,
                deadline,
                status,
                category
        );

        Task task = getTaskById(id);

        task.setTitle(title);
        task.setDescription(description);
        task.setExecutor(executor);
        task.setDeadline(deadline);
        task.setStatus(status);
        task.setCategory(category);

        taskDAO.update(task);
    }

    public void deleteTask(int id) {
        getTaskById(id);
        taskDAO.delete(id);
    }

    public void markDone(int id) {
        Task task = getTaskById(id);

        task.setStatus(TaskStatus.DONE);

        taskDAO.update(task);
    }

    public Task getTaskById(int id) {
        Optional<Task> task = taskDAO.findById(id);

        if (task.isEmpty()) {
            throw new IllegalArgumentException(
                    "Задача с ID " + id + " не найдена"
            );
        }

        return task.get();
    }

    public List<Task> search(
            String executor,
            TaskStatus status,
            TaskCategory category
    ) {
        return taskDAO.findAll()
                .stream()
                .filter(task ->
                        executor == null
                                || executor.isBlank()
                                || task.getExecutor()
                                .toLowerCase()
                                .contains(executor.toLowerCase())
                )
                .filter(task ->
                        status == null
                                || task.getStatus() == status
                )
                .filter(task ->
                        category == null
                                || task.getCategory() == category
                )
                .collect(Collectors.toList());
    }

    public List<Task> sortByDeadline() {
        return taskDAO.findAll()
                .stream()
                .sorted(Comparator.comparing(Task::getDeadline))
                .collect(Collectors.toList());
    }

    public List<Task> sortByExecutor() {
        return taskDAO.findAll()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Task::getExecutor,
                                String.CASE_INSENSITIVE_ORDER
                        )
                )
                .collect(Collectors.toList());
    }

    public List<Task> sortByStatus() {
        return taskDAO.findAll()
                .stream()
                .sorted(
                        Comparator.comparing(Task::getStatus)
                )
                .collect(Collectors.toList());
    }

    public List<Task> sortByTitle() {
        return taskDAO.findAll()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Task::getTitle,
                                String.CASE_INSENSITIVE_ORDER
                        )
                )
                .collect(Collectors.toList());
    }

    public List<Task> getOverdueTasks() {
        LocalDate today = LocalDate.now();

        return taskDAO.findAll()
                .stream()
                .filter(task ->
                        task.getDeadline().isBefore(today)
                )
                .filter(task ->
                        task.getStatus() != TaskStatus.DONE
                )
                .collect(Collectors.toList());
    }

    private int generateId() {
        return taskDAO.findAll()
                .stream()
                .mapToInt(Task::getId)
                .max()
                .orElse(0) + 1;
    }

    private void validateTaskData(
            String title,
            String executor,
            LocalDate deadline,
            TaskStatus status,
            TaskCategory category
    ) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Название задачи не может быть пустым"
            );
        }

        if (executor == null || executor.isBlank()) {
            throw new IllegalArgumentException(
                    "Исполнитель не может быть пустым"
            );
        }

        if (deadline == null) {
            throw new IllegalArgumentException(
                    "Необходимо указать срок выполнения"
            );
        }

        if (status == null) {
            throw new IllegalArgumentException(
                    "Необходимо выбрать статус"
            );
        }

        if (category == null) {
            throw new IllegalArgumentException(
                    "Необходимо выбрать категорию"
            );
        }
    }
}