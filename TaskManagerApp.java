import java.util.*;
import java.io.*;
import java.sql.Date;

class Task implements Serializable {
    private String title, description, category;
    private Date dueDate;
    private int priority;
    private boolean completed;
    private List<String> notes;    
    private String recurrence;      

    public Task(String title, String description, Date dueDate, String category, int priority, String recurrence) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
        this.priority = priority;
        this.completed = false;
        this.notes = new ArrayList<>();
        this.recurrence = recurrence;
    }

    
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Date getDueDate() { return dueDate; }
    public int getPriority() { return priority; }
    public boolean isCompleted() { return completed; }
    public List<String> getNotes() { return notes; }
    public String getRecurrence() { return recurrence; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setRecurrence(String recurrence) { this.recurrence = recurrence; }
    public void setNotes(List<String> notes) { this.notes = notes; }

    public void display() {
        System.out.printf("Title: %s\nDesc: %s\nDue: %s\nCat: %s\nPriority: %d\nStatus: %s\nRecurrence: %s\n",
                title, description, dueDate, category, priority, completed ? "Completed" : "Pending", recurrence);
        if (!notes.isEmpty()) {
            System.out.println("Notes/Subtasks:");
            int i = 1;
            for (String note : notes)
                System.out.println("  " + (i++) + ". " + note);
        }
    }

    public void markComplete() { completed = true; }
    public void markIncomplete() { completed = false; }
}

public class TaskManagerApp {
    private static List<Task> tasks = new ArrayList<>();
    private static String file = "tasks.ser";
    private static List<String> categories = new ArrayList<>(Arrays.asList("Work", "Home", "Personal", "Misc"));
    private static List<String> recOptions = Arrays.asList("NONE", "DAILY", "WEEKLY", "MONTHLY");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        loadTasks();
        dashboard();
        showReminders();

        while (true) {
            System.out.println("\n--- Task Manager ---");
            System.out.println("1.Add 2.View 3.Edit 4.Delete 5.Mark Complete 6.Mark Incomplete\n" +
                    "7.Save 8.Search 9.Sort 10.ExportCSV 11.ImportCSV\n" +
                    "12.Categories 13.Stats 0.Exit");
            int op = getIntInput(sc, "Choice: ");

            switch (op) {
                case 1: addTask(sc); break;
                case 2: showTasks(tasks); break;
                case 3: editTask(sc); break;
                case 4: deleteTask(sc); break;
                case 5: setComplete(sc, true); break;
                case 6: setComplete(sc, false); break;
                case 7: saveTasks(); break;
                case 8: searchTasks(sc); break;
                case 9: sortMenu(sc); break;
                case 10: exportCSV(); break;
                case 11: importCSV(); break;
                case 12: categoryMenu(sc); break;
                case 13: dashboard(); break;
                case 0:
                    saveTasks();
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid.");
            }
        }
    }

    
    static int getIntInput(Scanner sc, String msg) {
        int r = -1;
        while (true) {
            System.out.print(msg);
            try {
                r = Integer.parseInt(sc.nextLine());
                break;
            } catch (Exception e) { System.out.println("Invalid!"); }
        }
        return r;
    }
    static Date getDateInput(Scanner sc, String msg) {
        Date d = null;
        while (d == null) {
            System.out.print(msg);
            String du = sc.nextLine();
            try { d = java.sql.Date.valueOf(du); }
            catch (Exception ex) { System.out.println("Invalid date. Format: YYYY-MM-DD."); }
        }
        return d;
    }

    
    static void addTask(Scanner sc) {
        System.out.print("Title: "); String t = sc.nextLine();
        System.out.print("Desc: "); String d = sc.nextLine();
        Date due = getDateInput(sc, "Due YYYY-MM-DD: ");
        System.out.println("Categories: " + categories);
        System.out.print("Category: "); String c = sc.nextLine();
        if (!categories.contains(c)) categories.add(c);

        int pr;
        do {
            pr = getIntInput(sc, "Priority (1-5): ");
        } while (pr < 1 || pr > 5);

        // Recurrence
        System.out.println("Recurrence (NONE, DAILY, WEEKLY, MONTHLY): ");
        String r = sc.nextLine().trim().toUpperCase();
        if (!recOptions.contains(r)) r = "NONE";

        Task task = new Task(t, d, due, c, pr, r);

       
        System.out.println("Add notes/subtasks? [y/n]");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            List<String> notes = new ArrayList<>();
            while (true) {
                System.out.print("Enter note (or blank to stop): ");
                String note = sc.nextLine();
                if (note.isEmpty()) break;
                notes.add(note);
            }
            task.setNotes(notes);
        }
        tasks.add(task);
        System.out.println("Added.");
    }

    static void showTasks(List<Task> list) {
        if (list.isEmpty()) { System.out.println("No tasks."); return; }
        int idx = 1;
        for (Task t : list) {
            System.out.print(idx++ + ". ");
            t.display();
            System.out.println();
        }
    }

    static void editTask(Scanner sc) {
        showTasks(tasks);
        int idx = getIntInput(sc, "Edit task #: ");
        if (idx > 0 && idx <= tasks.size()) {
            Task t = tasks.get(idx - 1);

            System.out.print("New Title (cur: " + t.getTitle() + "): ");
            String nt = sc.nextLine();
            if (!nt.isEmpty()) t.setTitle(nt);

            System.out.print("New Desc (cur: " + t.getDescription() + "): ");
            String nd = sc.nextLine();
            if (!nd.isEmpty()) t.setDescription(nd);

            Date ndate = getDateInput(sc, "New Due (cur: " + t.getDueDate() + ") YYYY-MM-DD [skip for no change]: ");
            if (ndate != null) t.setDueDate(ndate);

            System.out.print("New Category (cur: " + t.getCategory() + "): ");
            String nc = sc.nextLine();
            if (!nc.isEmpty()) {
                t.setCategory(nc); if (!categories.contains(nc)) categories.add(nc);
            }

            System.out.print("New Priority (cur: " + t.getPriority() + "): ");
            String npr = sc.nextLine();
            if (!npr.isEmpty())
                try { t.setPriority(Integer.parseInt(npr)); } catch (Exception ignored) {}

            System.out.print("Recurrence (cur: " + t.getRecurrence() + "): ");
            String nr = sc.nextLine().toUpperCase();
            if (!nr.isEmpty() && recOptions.contains(nr)) t.setRecurrence(nr);

            System.out.print("Edit notes/subtasks? [y/n]: ");
            if (sc.nextLine().equalsIgnoreCase("y")) {
                List<String> notes = new ArrayList<>();
                while (true) {
                    System.out.print("Enter note (or blank to stop): ");
                    String note = sc.nextLine();
                    if (note.isEmpty()) break;
                    notes.add(note);
                }
                t.setNotes(notes);
            }

            System.out.println("Edited.");
        } else System.out.println("Invalid task number.");
    }

    static void deleteTask(Scanner sc) {
        showTasks(tasks);
        int idx = getIntInput(sc, "Delete #: ");
        if (idx > 0 && idx <= tasks.size()) {
            tasks.remove(idx - 1);
            System.out.println("Deleted.");
        } else System.out.println("Invalid task number.");
    }

    static void setComplete(Scanner sc, boolean complete) {
        showTasks(tasks);
        int idx = getIntInput(sc, (complete ? "Complete #: " : "Mark incomplete #: "));
        if (idx > 0 && idx <= tasks.size()) {
            if (complete) {
                Task t = tasks.get(idx - 1);
                t.markComplete();
                // Add recurrence if needed
                if (!t.getRecurrence().equals("NONE")) {
                    Date newDue = new Date(nextDueDate(t.getDueDate(), t.getRecurrence()));
                    Task recurring = new Task(
                            t.getTitle(), t.getDescription(), newDue, t.getCategory(), t.getPriority(), t.getRecurrence()
                    );
                    recurring.setNotes(t.getNotes());
                    tasks.add(recurring);
                    System.out.println("Added recurring task for next " + t.getRecurrence().toLowerCase() + ".");
                }
                System.out.println("Marked complete.");
            } else {
                tasks.get(idx - 1).markIncomplete();
                System.out.println("Marked as incomplete.");
            }
        } else System.out.println("Invalid task number.");
    }
    static long nextDueDate(Date current, String rec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(current);
        switch (rec) {
            case "DAILY": cal.add(Calendar.DATE, 1); break;
            case "WEEKLY": cal.add(Calendar.DATE, 7); break;
            case "MONTHLY": cal.add(Calendar.MONTH, 1); break;
        }
        return cal.getTimeInMillis();
    }

    static void searchTasks(Scanner sc) {
        System.out.print("Search keyword: ");
        String kw = sc.nextLine();
        List<Task> found = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getTitle().toLowerCase().contains(kw.toLowerCase()) ||
                    t.getDescription().toLowerCase().contains(kw.toLowerCase())) {
                found.add(t);
            }
        }
        if (found.isEmpty()) System.out.println("No matching tasks found.");
        else showTasks(found);
    }

    static void sortMenu(Scanner sc) {
        System.out.println("Sort By: 1.Due Date 2.Priority 3.Category 4.Status");
        int op = getIntInput(sc, "Sort by #: ");
        switch (op) {
            case 1: tasks.sort(Comparator.comparing(Task::getDueDate)); break;
            case 2: tasks.sort(Comparator.comparing(Task::getPriority)); break;
            case 3: tasks.sort(Comparator.comparing(Task::getCategory)); break;
            case 4: tasks.sort(Comparator.comparing(Task::isCompleted)); break;
            default: return;
        }
        System.out.println("Sorted.");
        showTasks(tasks);
    }


    static void exportCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("tasks.csv"))) {
            for (Task t : tasks) {
                pw.printf("\"%s\",\"%s\",\"%s\",\"%s\",%d,%b,\"%s\",\"%s\"\n",
                        t.getTitle().replace("\"","\"\""),
                        t.getDescription().replace("\"","\"\""),
                        t.getDueDate(), t.getCategory(),
                        t.getPriority(), t.isCompleted(), t.getRecurrence(),
                        String.join("~", t.getNotes()).replace("\"", "\"\""));
            }
            System.out.println("Exported to tasks.csv");
        } catch (IOException e) { System.out.println("Export error: " + e.getMessage()); }
    }
    static void importCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("tasks.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split by "," but handle commas in quotes
                List<String> parts = new ArrayList<>();
                boolean inQuote = false; StringBuilder sb = new StringBuilder();
                for (char c : line.toCharArray()) {
                    if (c == '\"') inQuote = !inQuote;
                    else if (c == ',' && !inQuote) { parts.add(sb.toString().replace("\"\"", "\"")); sb = new StringBuilder(); }
                    else sb.append(c);
                }
                parts.add(sb.toString().replace("\"\"", "\""));
                if (parts.size() < 8) continue;
                String title = parts.get(0), desc = parts.get(1), category = parts.get(3), rec = parts.get(6);
                Date due = java.sql.Date.valueOf(parts.get(2));
                int pr = Integer.parseInt(parts.get(4));
                boolean comp = Boolean.parseBoolean(parts.get(5));
                List<String> notes = Arrays.asList(parts.get(7).split("~"));
                Task t = new Task(title, desc, due, category, pr, rec);
                t.setNotes(notes);
                if (comp) t.markComplete();
                tasks.add(t);
            }
            System.out.println("Imported from tasks.csv");
        } catch (Exception e) { System.out.println("Import error: " + e.getMessage()); }
    }

 
    static void categoryMenu(Scanner sc) {
        while (true) {
            System.out.println("\nCategories: " + categories);
            System.out.println("1.Add 2.Delete 0.Back");
            int op = getIntInput(sc, "Option #: ");
            if (op == 1) {
                System.out.print("New category: ");
                String nc = sc.nextLine();
                if (!categories.contains(nc)) categories.add(nc);
            } else if (op == 2) {
                System.out.print("Delete which? ");
                String cat = sc.nextLine();
                categories.remove(cat);
            } else break;
        }
    }

    static void dashboard() {
        long total = tasks.size();
        long completed = tasks.stream().filter(Task::isCompleted).count();
        long overdue = tasks.stream().filter(t -> !t.isCompleted() && t.getDueDate().before(new Date(System.currentTimeMillis()))).count();
        System.out.println("Dashboard:\nTotal tasks: " + total + " | Completed: " + completed + " | Overdue: " + overdue);
    }
    static void showReminders() {
        Date now = new Date(System.currentTimeMillis());
        for (Task t : tasks) {
            long diff = t.getDueDate().getTime() - now.getTime();
            long daysLeft = diff / (1000*60*60*24);
            if (!t.isCompleted() && (daysLeft < 3 || t.getDueDate().before(now))) {
                System.out.println("*** Reminder: Task '" + t.getTitle() + "' is due in " + daysLeft + " days!");
            }
        }
    }

    static void saveTasks() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(tasks);
            System.out.println("Saved.");
        } catch (Exception e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    static void loadTasks() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            tasks = (List<Task>) in.readObject();
            System.out.println("Loaded " + tasks.size() + " tasks.");
        } catch (FileNotFoundException e) {
            System.out.println("No saved tasks found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }
}

