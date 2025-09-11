import java.util.*;
import java.io.*;

class Task implements Serializable {
    private String title, description, category;
    private Date dueDate;
    private int priority;
    private boolean completed;

    public Task(String title, String description, Date dueDate, String category, int priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
        this.priority = priority;
        this.completed = false;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public boolean isCompleted() { return completed; }
    public void markComplete() { completed = true; }

    public void display() {
        System.out.printf("Title: %s\nDesc: %s\nDue: %s\nCat: %s\nPriority: %d\nStatus: %s\n",
            title, description, dueDate, category, priority, completed ? "Completed" : "Pending");
    }
}

public class TaskManagerApp {
    private static List<Task> tasks = new ArrayList<>();
    private static String file = "tasks.ser";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        loadTasks();
        while (true) {
            System.out.println("\n--- Task Manager ---\n1.Add 2.View 3.Edit 4.Delete 5.Complete 6.Save 7.Search 0.Exit");
            int op = sc.nextInt(); sc.nextLine();
            switch (op) {
                case 1: addTask(sc); break;
                case 2: showTasks(); break;
                case 3: editTask(sc); break;
                case 4: deleteTask(sc); break;
                case 5: completeTask(sc); break;
                case 6: saveTasks(); break;
                case 7: searchTasks(sc); break;
                case 0: saveTasks(); System.exit(0);
            }
        }
    }

    static void addTask(Scanner sc) {
        System.out.print("Title: "); String t = sc.nextLine();
        System.out.print("Desc: "); String d = sc.nextLine();
        System.out.print("Due YYYY-MM-DD: "); String du = sc.nextLine();
        System.out.print("Category: "); String c = sc.nextLine();
        System.out.print("Priority (1-5): "); int pr = sc.nextInt(); sc.nextLine();
        Date due = java.sql.Date.valueOf(du);
        Task task = new Task(t, d, due, c, pr);
        tasks.add(task); System.out.println("Added.");
    }

    static void showTasks() {
        if (tasks.isEmpty()) { System.out.println("No tasks."); return; }
        int idx = 1;
        for (Task t : tasks) { System.out.print(idx++ + ". "); t.display(); System.out.println(); }
    }

    static void editTask(Scanner sc) {
        showTasks(); System.out.print("Edit task #: ");
        int idx = sc.nextInt(); sc.nextLine();
        if (idx > 0 && idx <= tasks.size()) {
            Task t = tasks.get(idx-1);
            System.out.print("New Title (cur: " + t.getTitle() + "): "); String nt = sc.nextLine();
            if (!nt.isEmpty()) t.setTitle(nt);
            System.out.print("New Desc (cur: " + t.getDescription() + "): "); String nd = sc.nextLine();
            if (!nd.isEmpty()) t.setDescription(nd);
            System.out.print("New Due (cur: " + t.getDueDate() + ") YYYY-MM-DD: "); String ndu = sc.nextLine();
            if (!ndu.isEmpty()) t.setDueDate(java.sql.Date.valueOf(ndu));
            System.out.print("New Category (cur: " + t.getCategory() + "): "); String nc = sc.nextLine();
            if (!nc.isEmpty()) t.setCategory(nc);
            System.out.print("New Priority (cur: " + t.getPriority() + "): "); String npr = sc.nextLine();
            if (!npr.isEmpty()) t.setPriority(Integer.parseInt(npr));
            System.out.println("Edited.");
        }
    }

    static void deleteTask(Scanner sc) {
        showTasks(); System.out.print("Delete #:"); int idx = sc.nextInt(); sc.nextLine();
        if (idx > 0 && idx <= tasks.size()) {
            tasks.remove(idx-1); System.out.println("Deleted.");
        }
    }

    static void completeTask(Scanner sc) {
        showTasks(); System.out.print("Complete #: ");
        int idx = sc.nextInt(); sc.nextLine();
        if (idx > 0 && idx <= tasks.size()) { tasks.get(idx-1).markComplete(); System.out.println("Marked complete."); }
    }

    static void searchTasks(Scanner sc) {
        System.out.print("Keyword: "); String kw = sc.nextLine();
        for (Task t : tasks)
            if (t.getTitle().contains(kw) || t.getDescription().contains(kw)) t.display();
    }

    static void saveTasks() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(tasks); System.out.println("Saved.");
        } catch (Exception e) { System.out.println("Error saving."); }
    }

    static void loadTasks() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            tasks = (List<Task>) in.readObject();
        } catch (Exception e) { /* file missing or first run */ }
    }
}
