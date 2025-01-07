# Task Manager

This is a command-line application for managing tasks. Users can add, update, delete tasks, mark tasks as in progress or done, and list tasks based on their status. The application stores task data in a JSON file for persistence.

## Features

- Add new tasks
- Update existing tasks
- Delete tasks
- Mark tasks as in progress or done
- List tasks by status (not done, in progress, done)

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Gradle

### Installation

1. Clone the repository:
   ```
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```
   cd task-manager
   ```
3. Build the project using Gradle:
   ```
   ./gradlew build
   ```

### Running the Application

To run the application, use the following command:
```
java -cp build/libs/task-manager.jar com.miles.App [command] [options]
```

### Commands

- `add [title] [description]`: Adds a new task.
- `update [id] [title] [description]`: Updates an existing task.
- `delete [id]`: Deletes a task.
- `mark [id] [status]`: Marks a task as in progress or done.
- `list [status]`: Lists tasks based on their status.

### Example Usage

```
java -cp build/libs/task-manager.jar com.miles.App add "Task 1" "Description of Task 1"
java -cp build/libs/task-manager.jar com.miles.App list NOT_DONE
```

## License

This project is licensed under the MIT License.