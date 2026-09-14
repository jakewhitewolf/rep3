package com.example.rep3.dao;

import com.example.rep3.model.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileTaskDAO implements TaskDAO {

    private final Path filePath;

    public FileTaskDAO(String filePath) {
        this.filePath = Path.of(filePath);
        createFileIfNeeded();
    }

    @Override
    public List<Task> findAll() {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            if (Files.size(filePath) == 0) {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла задач", e);
        }

        try (ObjectInputStream input =
                     new ObjectInputStream(
                             Files.newInputStream(filePath)
                     )) {

            Object data = input.readObject();

            @SuppressWarnings("unchecked")
            List<Task> tasks = (List<Task>) data;

            return new ArrayList<>(tasks);

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(
                    "Ошибка загрузки задач из файла",
                    e
            );
        }
    }

    @Override
    public Optional<Task> findById(int id) {
        return findAll()
                .stream()
                .filter(task -> task.getId() == id)
                .findFirst();
    }

    @Override
    public void add(Task task) {
        List<Task> tasks = findAll();

        tasks.add(task);

        saveAll(tasks);
    }

    @Override
    public void update(Task task) {
        List<Task> tasks = findAll();

        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.set(i, task);
                saveAll(tasks);
                return;
            }
        }

        throw new RuntimeException(
                "Задача с ID " + task.getId() + " не найдена"
        );
    }

    @Override
    public void delete(int id) {
        List<Task> tasks = findAll();

        boolean removed =
                tasks.removeIf(task -> task.getId() == id);

        if (removed) {
            saveAll(tasks);
        }
    }

    private void saveAll(List<Task> tasks) {
        try {
            Path parent = filePath.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            try (ObjectOutputStream output =
                         new ObjectOutputStream(
                                 Files.newOutputStream(filePath)
                         )) {

                output.writeObject(tasks);
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Ошибка сохранения задач в файл",
                    e
            );
        }
    }

    private void createFileIfNeeded() {
        try {
            Path parent = filePath.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

        } catch (IOException e) {
            throw new RuntimeException(
                    "Не удалось создать файл задач",
                    e
            );
        }
    }
}