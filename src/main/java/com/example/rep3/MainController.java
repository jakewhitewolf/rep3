package com.example.rep3;

import com.example.rep3.dao.CollectionTaskDAO;
import com.example.rep3.model.Task;
import com.example.rep3.model.TaskCategory;
import com.example.rep3.model.TaskStatus;
import com.example.rep3.service.TaskService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class MainController {

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> statusFilter;

    @FXML
    private ComboBox<String> categoryFilter;

    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    private TableView<Task> taskTable;

    @FXML
    private TableColumn<Task, Integer> idColumn;

    @FXML
    private TableColumn<Task, String> titleColumn;

    @FXML
    private TableColumn<Task, String> executorColumn;

    @FXML
    private TableColumn<Task, LocalDate> deadlineColumn;

    @FXML
    private TableColumn<Task, TaskStatus> statusColumn;

    @FXML
    private TableColumn<Task, TaskCategory> categoryColumn;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField executorField;

    @FXML
    private DatePicker deadlinePicker;

    @FXML
    private ComboBox<TaskStatus> statusComboBox;

    @FXML
    private ComboBox<TaskCategory> categoryComboBox;

    @FXML
    private Label dataSourceLabel;

    @FXML
    private Label statusLabel;

    private TaskService taskService;

    private final ObservableList<Task> tableData =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        taskService = new TaskService(new CollectionTaskDAO());

        initializeTable();
        initializeControls();
        initializeSelection();

        refreshTable();

        dataSourceLabel.setText("Collection");
        statusLabel.setText("Готово");
    }

    private void initializeTable() {
        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        titleColumn.setCellValueFactory(
                new PropertyValueFactory<>("title")
        );

        executorColumn.setCellValueFactory(
                new PropertyValueFactory<>("executor")
        );

        deadlineColumn.setCellValueFactory(
                new PropertyValueFactory<>("deadline")
        );

        statusColumn.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category")
        );

        taskTable.setItems(tableData);
    }

    private void initializeControls() {
        statusComboBox.setItems(
                FXCollections.observableArrayList(
                        TaskStatus.values()
                )
        );

        categoryComboBox.setItems(
                FXCollections.observableArrayList(
                        TaskCategory.values()
                )
        );

        statusFilter.setItems(
                FXCollections.observableArrayList(
                        "Все",
                        "NEW",
                        "IN_PROGRESS",
                        "DONE"
                )
        );

        categoryFilter.setItems(
                FXCollections.observableArrayList(
                        "Все",
                        "WORK",
                        "STUDY",
                        "PERSONAL"
                )
        );

        sortComboBox.setItems(
                FXCollections.observableArrayList(
                        "По сроку выполнения",
                        "По исполнителю",
                        "По статусу",
                        "По названию"
                )
        );

        statusFilter.setValue("Все");
        categoryFilter.setValue("Все");

        statusComboBox.setValue(TaskStatus.NEW);
        categoryComboBox.setValue(TaskCategory.WORK);
    }

    private void initializeSelection() {
        taskTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldTask, newTask) -> {
                    if (newTask != null) {
                        fillForm(newTask);
                    }
                });
    }

    @FXML
    private void onAddTask() {
        try {
            String title = titleField.getText().trim();
            String description = descriptionArea.getText().trim();
            String executor = executorField.getText().trim();
            LocalDate deadline = deadlinePicker.getValue();
            TaskStatus status = statusComboBox.getValue();
            TaskCategory category = categoryComboBox.getValue();

            taskService.createTask(
                    title,
                    description,
                    executor,
                    deadline,
                    status,
                    category
            );

            refreshTable();
            clearForm();

            statusLabel.setText("Задача добавлена");

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onUpdateTask() {
        Task selectedTask =
                taskTable.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            showError("Выберите задачу для изменения");
            return;
        }

        try {
            taskService.updateTask(
                    selectedTask.getId(),
                    titleField.getText().trim(),
                    descriptionArea.getText().trim(),
                    executorField.getText().trim(),
                    deadlinePicker.getValue(),
                    statusComboBox.getValue(),
                    categoryComboBox.getValue()
            );

            refreshTable();
            clearForm();

            statusLabel.setText("Задача изменена");

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onDeleteTask() {
        Task selectedTask =
                taskTable.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            showError("Выберите задачу для удаления");
            return;
        }

        taskService.deleteTask(selectedTask.getId());

        refreshTable();
        clearForm();

        statusLabel.setText("Задача удалена");
    }

    @FXML
    private void onMarkDone() {
        Task selectedTask =
                taskTable.getSelectionModel().getSelectedItem();

        if (selectedTask == null) {
            showError("Выберите задачу");
            return;
        }

        taskService.markDone(selectedTask.getId());

        refreshTable();

        statusLabel.setText("Задача отмечена как выполненная");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Задача выполнена");
        alert.setHeaderText(selectedTask.getTitle());
        alert.setContentText("Статус задачи изменён на DONE");
        alert.showAndWait();
    }

    @FXML
    private void onSearch() {
        String executor = searchField.getText().trim();

        TaskStatus status = null;
        TaskCategory category = null;

        if (!"Все".equals(statusFilter.getValue())) {
            status = TaskStatus.valueOf(
                    statusFilter.getValue()
            );
        }

        if (!"Все".equals(categoryFilter.getValue())) {
            category = TaskCategory.valueOf(
                    categoryFilter.getValue()
            );
        }

        List<Task> result =
                taskService.search(
                        executor,
                        status,
                        category
                );

        tableData.setAll(result);

        statusLabel.setText(
                "Найдено задач: " + result.size()
        );
    }

    @FXML
    private void onResetFilters() {
        searchField.clear();

        statusFilter.setValue("Все");
        categoryFilter.setValue("Все");

        refreshTable();

        statusLabel.setText("Фильтры сброшены");
    }

    @FXML
    private void onSort() {
        String sort = sortComboBox.getValue();

        if (sort == null) {
            showError("Выберите критерий сортировки");
            return;
        }

        List<Task> tasks;

        switch (sort) {
            case "По сроку выполнения":
                tasks = taskService.sortByDeadline();
                break;

            case "По исполнителю":
                tasks = taskService.sortByExecutor();
                break;

            case "По статусу":
                tasks = taskService.sortByStatus();
                break;

            case "По названию":
                tasks = taskService.sortByTitle();
                break;

            default:
                return;
        }

        tableData.setAll(tasks);

        statusLabel.setText("Задачи отсортированы");
    }

    @FXML
    private void onRefresh() {
        refreshTable();
        statusLabel.setText("Список обновлён");
    }

    @FXML
    private void onClearForm() {
        clearForm();
    }

    @FXML
    private void onSynchronize() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Синхронизация");
        alert.setHeaderText("Синхронизация источников данных");
        alert.setContentText(
                "Функция станет доступна после подключения файлового источника и базы данных."
        );
        alert.showAndWait();
    }

    private void refreshTable() {
        tableData.setAll(
                taskService.getAllTasks()
        );
    }

    private void fillForm(Task task) {
        titleField.setText(task.getTitle());
        descriptionArea.setText(task.getDescription());
        executorField.setText(task.getExecutor());
        deadlinePicker.setValue(task.getDeadline());
        statusComboBox.setValue(task.getStatus());
        categoryComboBox.setValue(task.getCategory());
    }

    private void clearForm() {
        titleField.clear();
        descriptionArea.clear();
        executorField.clear();
        deadlinePicker.setValue(null);

        statusComboBox.setValue(TaskStatus.NEW);
        categoryComboBox.setValue(TaskCategory.WORK);

        taskTable.getSelectionModel().clearSelection();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Ошибка");
        alert.setHeaderText("Невозможно выполнить действие");
        alert.setContentText(message);

        alert.showAndWait();

        statusLabel.setText("Ошибка");
    }
}