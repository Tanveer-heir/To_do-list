# Java CLI Task Manager

A robust, feature-rich **Task Manager** built in Java for the command line.  
Easily manage tasks, deadlines, priorities, categories, notes, and recurring schedules—all with saving/loading support.

---

## Features

- **Add, View, Edit, Delete Tasks**
- **Set Priority (1–5) and Due Dates**
- **Categories:** Built-in and user-defined
- **Search and Sort:** By date, category, status, priority
- **Mark Complete/Incompleted**
- **Recurring Tasks:** (Daily, Weekly, Monthly)
- **Notes/Subtasks** per task
- **Reminders for Due/Overdue Tasks**
- **Export/Import Tasks (CSV)**
- **Statistics/Dashboard:** Shows summary of task status
- **Persistent Data:** Tasks saved between runs

---

## How to Run

1. **Clone or download the code**.
2. **Compile:**
   _If using Programiz or online compiler, paste everything into a single file named `Main.java`, with your main class as `public class Main`._
3. **Run:**

---

## Menu Overview

When you run the application, you'll see:

Choose options by entering the menu number.

---

## Task Structure

Each task includes:

- Title, Description
- Category, Priority, Due Date
- Notes/Subtasks (optional)
- Status: Pending or Completed
- Recurrence: NONE, DAILY, WEEKLY, MONTHLY

---

## Saving & Loading

- **On exit**, tasks are automatically saved to `tasks.ser`
- You can **import or export** all tasks to/from `tasks.csv` for sharing and backup

---

## Customization

- **Categories:** Add/remove categories in the menu
- **Recurrence:** Choose frequency when creating/editing a task
- **Notes/Subtasks:** Add multiple for each task

---

## Sample Usage

1. Add a new task
2. Set priority and due date
3. Mark as complete when done
4. Edit notes/subtasks
5. Export tasks for sharing

---

## Advanced Features

- Soon: Multi-user, GUI (Swing/JavaFX), cloud sync  
  _(request these by opening an issue or messaging the author!)_

---

## Requirements

- Java 8+
- Any CLI/Terminal
- No external dependencies

---

## Credits

Developed by Tanveer Singh

---

## License

This project is licensed under the MIT License.

---

## Contributing

Pull requests, suggestions, and issues are welcome!


