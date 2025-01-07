package com.miles;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class TaskManager {

    private static final String TASKS_FILE = "src/main/resources/tasks.json";
    private List<Task> tasks;
    private Gson gson;

    public TaskManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.tasks = loadTasks();
    }

    private List<Task> loadTasks() {
        File file = new File(TASKS_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, new TypeToken<List<Task>>() {
            }.getType());
        } catch (IOException e) {
            System.err.println("[ERROR]: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveTasks() {
        try (FileWriter writer = new FileWriter(TASKS_FILE)) {
            gson.toJson(tasks, writer);
        } catch (IOException e) {
            System.err.println("[ERROR]: " + e.getMessage());
        }
    }

    public void addTask(Scanner scanner) {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        Task task = new Task(tasks.size() + 1, title, description, TaskStatus.NOT_DONE);
        tasks.add(task);
        saveTasks();
    }

    /**
     * Updates a task based on user input. Prompts the user for the task ID and
     * the update choice, then applies the update.
     */
    public void updateTask(Scanner scanner) {
        int id = getTaskId(scanner);
        Task task = findTaskById(id);

        if (task == null) {
            System.out.println("Task not found!");
            return;
        }

        int choice = getUpdateChoice(scanner);
        handleUpdateChoice(scanner, task, choice);
        saveTasks();
    }

    /**
     * Prompts the user to enter the task ID.
     *
     * @param scanner the Scanner object to read user input
     * @return the task ID entered by the user
     */
    private int getTaskId(Scanner scanner) {
        System.out.print("Enter task id: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        return id;
    }

    /**
     * Finds a task by its ID.
     *
     * @param id the ID of the task to find
     * @return the Task object if found, otherwise null
     */
    private Task findTaskById(int id) {
        return tasks.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    /**
     * Prompts the user to select an update choice.
     *
     * @param scanner the Scanner object to read user input
     * @return the update choice entered by the user
     */
    private int getUpdateChoice(Scanner scanner) {
        System.out.print("Select from list to update:\n1. Title\n2. Description\n3. Status\nEnter choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        return choice;
    }

    /**
     * Handles the update choice and applies the corresponding update to the
     * task.
     *
     * @param scanner the Scanner object to read user input
     * @param task the Task object to update
     * @param choice the update choice selected by the user
     */
    private void handleUpdateChoice(Scanner scanner, Task task, int choice) {
        switch (choice) {
            case 1:
                updateTitle(scanner, task);
                break;
            case 2:
                updateDescription(scanner, task);
                break;
            case 3:
                updateStatus(scanner, task);
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    /**
     * Updates the title of the task.
     *
     * @param scanner the Scanner object to read user input
     * @param task the Task object to update
     */
    private void updateTitle(Scanner scanner, Task task) {
        System.out.print("Enter new title: ");
        String title = scanner.nextLine();
        task.setTitle(title);
    }

    /**
     * Updates the description of the task.
     *
     * @param scanner the Scanner object to read user input
     * @param task the Task object to update
     */
    private void updateDescription(Scanner scanner, Task task) {
        System.out.print("Enter new description: ");
        String description = scanner.nextLine();
        task.setDescription(description);
    }

    /**
     * Updates the status of the task.
     *
     * @param scanner the Scanner object to read user input
     * @param task the Task object to update
     */
    private void updateStatus(Scanner scanner, Task task) {
        System.out.print("Select new status:\n1. Not Done\n2. In Progress\n3. Done\nEnter choice: ");
        int statusChoice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character
        switch (statusChoice) {
            case 1:
                task.setStatus(TaskStatus.NOT_DONE);
                break;
            case 2:
                task.setStatus(TaskStatus.IN_PROGRESS);
                break;
            case 3:
                task.setStatus(TaskStatus.DONE);
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    public void deleteTask(Scanner scanner) {
        int id = getTaskId(scanner);
        String title = tasks.stream().filter(task -> task.getId() == id).findFirst().get().getTitle();
        System.out.printf("Are you sure you want to delete task '%s'? (yes/no): ", title);
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            tasks.removeIf(task -> task.getId() == id);
        } else {
            System.out.println("Task deletion cancelled.");
        }
        saveTasks();
    }

    public void markTaskAsInProgress(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.setStatus(TaskStatus.IN_PROGRESS);
                saveTasks();
                return;
            }
        }
    }

    public void markTaskAsDone(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                task.setStatus(TaskStatus.DONE);
                saveTasks();
                return;
            }
        }
    }

    public List<Task> listTasks(Scanner scanner) {
        System.out.println("Choose from these statuses:\n1. Not Done\n2. In Progress\n3. Done\n4. All");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        TaskStatus status = null;
        switch (choice) {
            case 1:
                status = TaskStatus.NOT_DONE;
                break;
            case 2:
                status = TaskStatus.IN_PROGRESS;
                break;
            case 3:
                status = TaskStatus.DONE;
                break;
            case 4:
                break;
            default:
                throw new AssertionError();
        }
        scanner.nextLine(); // Consume the newline character
        List<Task> filteredTasks = new ArrayList<>();
        if (status == null) {
            for (Task task : tasks) {
                System.out.println("ID: " + task.getId() + ", Title: " + task.getTitle() + ", Description: "
                        + task.getDescription() + ", Status: " + task.getStatus());
            }
            return tasks;
        } else {
            return filterTasksByStatus(status, filteredTasks);
        }1
    }

    private List<Task> filterTasksByStatus(TaskStatus status, List<Task> filteredTasks) {
        for (Task task : tasks) {
            if (task.getStatus() == status) {
                filteredTasks.add(task);
            }
        }
        if (!filteredTasks.isEmpty()) {
            for (Task task : filteredTasks) {
                System.out.println("ID: " + task.getId() + ", Title: " + task.getTitle() + ", Description: "
                        + task.getDescription() + ", Status: " + task.getStatus());
            }
        } else {
            System.out.println("No tasks found with the selected status.");
        }
        return filteredTasks;
    }

}
