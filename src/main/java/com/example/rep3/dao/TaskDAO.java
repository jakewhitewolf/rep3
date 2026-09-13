package com.example.rep3.dao;

import com.example.rep3.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskDAO {

    List<Task> findAll();

    Optional<Task> findById(int id);

    void add(Task task);

    void update(Task task);

    void delete(int id);
}
