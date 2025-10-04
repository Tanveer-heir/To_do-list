# Java Task Manager (GUI Version)

A comprehensive **Task Management System** built in Java with a modern Swing GUI interface. This application helps you organize, track, and manage your tasks efficiently with features like recurring tasks, priority management, categories, and persistent data storage.

---

## ✨ Features

### Core Functionality
- **Add, Edit, Delete Tasks** with intuitive GUI dialogs
- **Priority System** (1-5 scale) for task importance
- **Due Date Management** with overdue task tracking
- **Task Categories** (Work, Home, Personal, Misc + custom categories)
- **Task Status** (Pending/Completed) with easy toggle
- **Notes/Subtasks** support for detailed task breakdown

### Advanced Features
- **Recurring Tasks** (Daily, Weekly, Monthly) - automatically creates new instances
- **Dashboard Statistics** - shows total, completed, and overdue tasks
- **Reminder System** - alerts for tasks due within 3 days or overdue
- **Persistent Storage** - tasks saved automatically using serialization
- **Category Management** - add/remove custom categories
- **Table View** - comprehensive display of all task information

---

## 🚀 How to Run

### Prerequisites
- Java 8 or higher installed on your system
- Any Java IDE (IntelliJ IDEA, Eclipse, NetBeans) or command line

### Installation & Execution

1. **Download/Clone** the code
2. **Save as** `TaskManagerApp.java` (or any preferred name)
3. **Compile:**

4. **Run:**


### For Online Compilers (like Programiz)
- Copy the entire code into a single file
- Ensure the main class is properly named for the platform
- Click Run

---

## 📋 How to Use

### Getting Started
1. **Launch** the application
2. The **dashboard** shows your current task statistics
3. **Reminders** will appear for any overdue or soon-due tasks

### Managing Tasks

#### Adding Tasks
1. Click **"Add"** button
2. Fill in the dialog:
- **Title & Description**
- **Category** (select existing or type new)
- **Due Date** (YYYY-MM-DD format)
- **Priority** (1-5, where 5 is highest)
- **Recurrence** (NONE, DAILY, WEEKLY, MONTHLY)
- **Notes/Subtasks** (semicolon-separated)
3. Click **OK** to save

#### Editing Tasks
1. **Select** a task in the table
2. Click **"Edit"** button
3. Modify fields as needed
4. Click **OK** to save changes

#### Completing Tasks
1. **Select** a task in the table
2. Click **"Mark Complete"**
3. For recurring tasks, a new instance is automatically created

#### Other Actions
- **Delete:** Select task → Click "Delete" → Confirm
- **Mark Incomplete:** Revert completed tasks to pending
- **Save:** Manually save tasks to file
- **Reload:** Refresh tasks from saved file

### Managing Categories
1. Click **"Categories"** button
2. **Add:** Type new category name → Click "Add"
3. **Delete:** Select category → Click "Delete"

---

## 📊 Task Information

Each task contains:

| Field | Description |
|-------|-------------|
| **Title** | Task name/summary |
| **Description** | Detailed task information |
| **Category** | Organization label (Work, Home, etc.) |
| **Due Date** | Target completion date |
| **Priority** | Importance level (1-5) |
| **Status** | Pending or Completed |
| **Recurrence** | Auto-repeat frequency |
| **Notes/Subtasks** | Additional details or checklist items |

---

## 🔄 Recurring Tasks

When you mark a recurring task as complete:
- The original task remains marked as completed
- A **new task** is automatically created with:
- Same title, description, category, priority, and notes
- **Updated due date** based on recurrence pattern:
 - **Daily:** +1 day
 - **Weekly:** +7 days  
 - **Monthly:** +1 month
- You'll see a confirmation message

---

## 💾 Data Storage

- Tasks are automatically saved to `tasks.ser` file
- **Persistent storage** means your data survives application restarts
- Use **Save/Reload** buttons for manual file operations
- File uses Java serialization for reliable data preservation

---

## ⚠️ Reminders & Alerts

The application shows reminders for:
- **Overdue tasks** (past due date)
- **Soon-due tasks** (within 3 days)
- **Recurring task creation** (when completing recurring tasks)

---

## 🛠️ Technical Details

- **Language:** Java (JDK 8+)
- **GUI Framework:** Java Swing
- **Data Storage:** Java Serialization (.ser files)
- **Architecture:** Object-oriented with Task model and GUI view
- **Dependencies:** None (uses built-in Java libraries)

---

## 🎯 Use Cases

Perfect for:
- **Students** managing assignments and deadlines
- **Professionals** tracking work projects and meetings
- **Personal** organization and household tasks
- **Small teams** needing simple task coordination
- Anyone wanting a **lightweight, offline task manager**

---

## 🚀 Future Enhancements

Potential additions:
- Export/Import to CSV/JSON formats
- Task search and filtering capabilities
- Task sorting by different criteria
- Multi-user support with authentication
- Cloud synchronization
- Mobile companion app
- Advanced reporting and analytics

---

## 📝 System Requirements

- **Operating System:** Windows, macOS, Linux (any Java-supported OS)
- **Memory:** Minimal (< 50MB RAM)
- **Storage:** < 5MB for application + data files
- **Java Version:** JDK/JRE 8 or higher

---

## 🐛 Troubleshooting

### Common Issues

1. **"Cannot find main class"**
- Ensure the file is named correctly
- Verify Java is properly installed

2. **Date format errors**
- Use YYYY-MM-DD format (e.g., 2025-10-04)

3. **Tasks not saving**
- Check write permissions in the application directory
- Manually click "Save" button

---

## 📄 License

This project is open-source. Feel free to use, modify, and distribute.

---

## 👨‍💻 Author

**Developed by:** Tanveer Singh
**Contact:** tanveerheir68@gmail.com  

---

## 🤝 Contributing

Contributions welcome! 
- Report bugs via issues
- Suggest features
- Submit pull requests
- Share feedback

---



