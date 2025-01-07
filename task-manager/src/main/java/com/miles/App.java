package com.miles;

import java.util.Scanner;

public final class App {

    private App() {
    }

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);
        String command;

        System.out.println("Welcome to the Task Manager!");
        while (true) {
            System.out.print("Enter command (add, update, delete, list, exit): ");
            command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "add":
                    taskManager.addTask(scanner);
                    break;
                case "update":
                    taskManager.updateTask(scanner);
                    break;
                case "delete":
                    taskManager.deleteTask(scanner);
                    break;
                case "list":
                    taskManager.listTasks(scanner);
                    break;
                case "exit":
                    System.out.println("Exiting Task Manager. Goodbye!");
                    scanner.close();
                    break;
                default:
                    System.out.println("Unknown command. Please try again.");
            }
        }
    }
}
