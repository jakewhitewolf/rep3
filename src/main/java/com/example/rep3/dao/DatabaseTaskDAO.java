package com.example.rep3.dao;

import com.example.rep3.model.Task;
import com.example.rep3.model.TaskCategory;
import com.example.rep3.model.TaskStatus;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseTaskDAO implements TaskDAO {

    private final String url;

    public DatabaseTaskDAO(String url) {
        this.url = url;
        createTable();
    }

    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();

        String sql = "SELECT * FROM tasks";

        try (
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)
        ) {

            while (resultSet.next()) {
                tasks.add(readTask(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка загрузки задач из базы данных",
                    e
            );
        }

        return tasks;
    }

    @Override
    public Optional<Task> findById(int id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";

        try (
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return Optional.of(
                            readTask(resultSet)
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка поиска задачи в базе данных",
                    e
            );
        }

        return Optional.empty();
    }

    @Override
    public void add(Task task) {
        String sql = """
                INSERT INTO tasks
                (id, title, description, executor, deadline, status, category, updated_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, task.getId());
            statement.setString(2, task.getTitle());
            statement.setString(3, task.getDescription());
            statement.setString(4, task.getExecutor());
            statement.setString(5, task.getDeadline().toString());
            statement.setString(6, task.getStatus().name());
            statement.setString(7, task.getCategory().name());
            statement.setString(8, task.getUpdatedAt().toString());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка добавления задачи в базу данных",
                    e
            );
        }
    }

    @Override
    public void update(Task task) {
        String sql = """
                UPDATE tasks
                SET title = ?,
                    description = ?,
                    executor = ?,
                    deadline = ?,
                    status = ?,
                    category = ?,
                    updated_at = ?
                WHERE id = ?
                """;

        try (
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.setString(3, task.getExecutor());
            statement.setString(4, task.getDeadline().toString());
            statement.setString(5, task.getStatus().name());
            statement.setString(6, task.getCategory().name());
            statement.setString(7, task.getUpdatedAt().toString());
            statement.setInt(8, task.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка обновления задачи в базе данных",
                    e
            );
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (
                Connection connection = DriverManager.getConnection(url);
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка удаления задачи из базы данных",
                    e
            );
        }
    }

    private void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY,
                    title TEXT NOT NULL,
                    description TEXT,
                    executor TEXT NOT NULL,
                    deadline TEXT NOT NULL,
                    status TEXT NOT NULL,
                    category TEXT NOT NULL,
                    updated_at TEXT NOT NULL
                )
                """;

        try (
                Connection connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement()
        ) {

            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Ошибка создания таблицы tasks",
                    e
            );
        }
    }

    private Task readTask(ResultSet resultSet)
            throws SQLException {

        return new Task(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getString("executor"),
                LocalDate.parse(
                        resultSet.getString("deadline")
                ),
                TaskStatus.valueOf(
                        resultSet.getString("status")
                ),
                TaskCategory.valueOf(
                        resultSet.getString("category")
                ),
                LocalDateTime.parse(
                        resultSet.getString("updated_at")
                )
        );
    }
}