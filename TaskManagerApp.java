import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.Date;
import java.util.*;

class Task implements Serializable {
    private String title, description, category;
    private Date dueDate;
    private int priority;
    private boolean completed;
    private java.util.List<String> notes;
    private String recurrence;

    public Task(String title, String description, Date dueDate, String category, int priority, String recurrence) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.category = category;
        this.priority = priority;
        this.completed = false;
        this.notes = new java.util.ArrayList<>();
        this.recurrence = recurrence;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Date getDueDate() { return dueDate; }
    public int getPriority() { return priority; }
    public boolean isCompleted() { return completed; }
    public java.util.List<String> getNotes() { return notes; }
    public String getRecurrence() { return recurrence; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setRecurrence(String recurrence) { this.recurrence = recurrence; }
    public void setNotes(java.util.List<String> notes) { this.notes = notes; }

    public void markComplete() { completed = true; }
    public void markIncomplete() { completed = false; }
}

public class TaskManagerApp {
    private static java.util.ArrayList<Task> tasks = new java.util.ArrayList<>();
    private static String file = "tasks.ser";
    private static java.util.ArrayList<String> categories = new java.util.ArrayList<>(java.util.Arrays.asList("Work", "Home", "Personal", "Misc"));
    private static String[] recOptions = {"NONE", "DAILY", "WEEKLY", "MONTHLY"};

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TaskManagerGUI().setVisible(true));
    }

    // Load/save logic
    public static void saveTasks() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(tasks);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error saving: " + e.getMessage());
        }
    }
    @SuppressWarnings("unchecked")
    public static void loadTasks() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            tasks = (java.util.ArrayList<Task>) in.readObject();
        } catch (Exception e) {
            // File may not exist, ignore
        }
    }

    // Helper for next due date for recurrence
    public static Date nextDueDate(Date current, String rec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(current);
        switch (rec) {
            case "DAILY": cal.add(Calendar.DATE, 1); break;
            case "WEEKLY": cal.add(Calendar.DATE, 7); break;
            case "MONTHLY": cal.add(Calendar.MONTH, 1); break;
        }
        return new Date(cal.getTimeInMillis());
    }

    // -------- GUI ----------
    static class TaskManagerGUI extends JFrame {
        private DefaultTableModel model;
        private JTable table;
        private JLabel dashboard;

        public TaskManagerGUI() {
            setTitle("Java Task Manager");
            setSize(900, 500);
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            Main.loadTasks();

            // Table
            String[] columns = {"Title","Desc","Category","Due Date","Priority","Status","Recurrence","Notes/Subtasks"};
            model = new DefaultTableModel(columns, 0);
            table = new JTable(model);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(new JScrollPane(table), BorderLayout.CENTER);

            // Dashboard panel
            dashboard = new JLabel();
            refreshDashboard();
            panel.add(dashboard, BorderLayout.NORTH);

            add(panel, BorderLayout.CENTER);
            refreshTable();

            // Button Panel (Add/Edit/Delete/Mark/Save/Reload/Category)
            JPanel btnPanel = new JPanel();

            JButton addBtn = new JButton("Add");
            JButton editBtn = new JButton("Edit");
            JButton delBtn = new JButton("Delete");
            JButton markCompleteBtn = new JButton("Mark Complete");
            JButton markIncompBtn = new JButton("Mark Incomplete");
            JButton saveBtn = new JButton("Save");
            JButton reloadBtn = new JButton("Reload");
            JButton catBtn = new JButton("Categories");

            btnPanel.add(addBtn); btnPanel.add(editBtn); btnPanel.add(delBtn);
            btnPanel.add(markCompleteBtn); btnPanel.add(markIncompBtn);
            btnPanel.add(saveBtn); btnPanel.add(reloadBtn); btnPanel.add(catBtn);

            add(btnPanel, BorderLayout.SOUTH);

            // ---- Button Actions ----
            addBtn.addActionListener(e -> showTaskDialog(null));
            editBtn.addActionListener(e -> {
                int idx = table.getSelectedRow();
                if (idx == -1) showMsg("Select a task to edit.");
                else showTaskDialog(tasks.get(idx));
            });
            delBtn.addActionListener(e -> {
                int idx = table.getSelectedRow();
                if (idx == -1) showMsg("Select a task to delete.");
                else {
                    int confirm = JOptionPane.showConfirmDialog(this, "Delete selected task?");
                    if (confirm==JOptionPane.YES_OPTION) {
                        tasks.remove(idx);
                        refreshTable();
                        refreshDashboard();
                    }
                }
            });
            markCompleteBtn.addActionListener(e -> {
                int idx = table.getSelectedRow();
                if (idx==-1) showMsg("Select task to mark as complete.");
                else {
                    Task t = tasks.get(idx);
                    t.markComplete();
                    // Handle recurrence
                    if (!t.getRecurrence().equals("NONE")) {
                        Date newDue = Main.nextDueDate(t.getDueDate(), t.getRecurrence());
                        Task recurring = new Task(t.getTitle(), t.getDescription(), newDue, t.getCategory(), t.getPriority(), t.getRecurrence());
                        recurring.setNotes(t.getNotes());
                        tasks.add(recurring);
                        showMsg("Recurring task created for next " + t.getRecurrence());
                    }
                    refreshTable();
                    refreshDashboard();
                }
            });
            markIncompBtn.addActionListener(e -> {
                int idx = table.getSelectedRow();
                if (idx==-1) showMsg("Select task to mark as incomplete.");
                else {
                    tasks.get(idx).markIncomplete();
                    refreshTable();
                    refreshDashboard();
                }
            });
            saveBtn.addActionListener(e -> {
                Main.saveTasks();
                showMsg("Tasks saved.");
            });
            reloadBtn.addActionListener(e -> {
                Main.loadTasks();
                refreshTable();
                refreshDashboard();
                showMsg("Tasks reloaded.");
            });
            catBtn.addActionListener(e -> showCategoryDialog());

            // Show reminders on startup
            showReminders();
        }

        private void refreshDashboard() {
            long total = tasks.size();
            long completed = tasks.stream().filter(Task::isCompleted).count();
            long overdue = tasks.stream().filter(t -> !t.isCompleted() && t.getDueDate().before(new Date(System.currentTimeMillis()))).count();
            dashboard.setText("Total: " + total + " | Completed: " + completed + " | Overdue: " + overdue);
        }

        private void refreshTable() {
            model.setRowCount(0);
            for (Task t : tasks) {
                String notes = t.getNotes().isEmpty() ? "" : String.join(", ", t.getNotes());
                model.addRow(new Object[]{
                        t.getTitle(), t.getDescription(), t.getCategory(), t.getDueDate(),
                        t.getPriority(), t.isCompleted() ? "Completed" : "Pending", t.getRecurrence(), notes
                });
            }
        }

        private void showMsg(String msg) {
            JOptionPane.showMessageDialog(this, msg);
        }

        private void showTaskDialog(Task editing) {
            JTextField titleField = new JTextField(editing==null ? "" : editing.getTitle());
            JTextField descField = new JTextField(editing==null ? "" : editing.getDescription());
            JComboBox<String> catBox = new JComboBox<>(categories.toArray(new String[0]));
            if (editing!=null) catBox.setSelectedItem(editing.getCategory());
            JTextField dueField = new JTextField(editing==null ? "YYYY-MM-DD" : editing.getDueDate().toString());
            JComboBox<String> prioBox = new JComboBox<>(new String[]{"1","2","3","4","5"});
            if (editing!=null) prioBox.setSelectedItem(String.valueOf(editing.getPriority()));
            JComboBox<String> recBox = new JComboBox<>(recOptions);
            if (editing!=null) recBox.setSelectedItem(editing.getRecurrence());
            JTextField notesField = new JTextField(editing==null ? "" : String.join(";", editing.getNotes()));

            JPanel p = new JPanel(new GridLayout(0,2));
            p.add(new JLabel("Title:")); p.add(titleField);
            p.add(new JLabel("Description:")); p.add(descField);
            p.add(new JLabel("Category:")); p.add(catBox);
            p.add(new JLabel("Due Date:")); p.add(dueField);
            p.add(new JLabel("Priority:")); p.add(prioBox);
            p.add(new JLabel("Recurrence:")); p.add(recBox);
            p.add(new JLabel("Notes/Subtasks (semicolon separated):")); p.add(notesField);

            int res = JOptionPane.showConfirmDialog(this, p, editing==null?"Add Task":"Edit Task", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    String title = titleField.getText();
                    String desc = descField.getText();
                    String cat = (String)catBox.getSelectedItem();
                    Date due = Date.valueOf(dueField.getText());
                    int prio = Integer.parseInt((String)prioBox.getSelectedItem());
                    String rec = (String)recBox.getSelectedItem();
                    java.util.List<String> notes = new java.util.ArrayList<>();
                    String[] notesArr = notesField.getText().split(";");
                    for (String n : notesArr) if (!n.trim().isEmpty()) notes.add(n.trim());
                    if (editing==null) {
                        Task task = new Task(title, desc, due, cat, prio, rec);
                        task.setNotes(notes);
                        tasks.add(task);
                    } else {
                        editing.setTitle(title); editing.setDescription(desc);
                        editing.setCategory(cat); editing.setDueDate(due);
                        editing.setPriority(prio); editing.setRecurrence(rec);
                        editing.setNotes(notes);
                    }
                    refreshTable();
                    refreshDashboard();
                } catch (Exception ex) {
                    showMsg("Error: " + ex.getMessage());
                }
            }
        }

        private void showCategoryDialog() {
            JTextField newCat = new JTextField();
            JComboBox<String> catList = new JComboBox<>(categories.toArray(new String[0]));
            JButton addBtn = new JButton("Add");
            JButton delBtn = new JButton("Delete");

            JPanel p = new JPanel(new GridLayout(0,1));
            p.add(newCat); p.add(addBtn); p.add(catList); p.add(delBtn);

            JFrame catFrame = new JFrame("Manage Categories");
            catFrame.setSize(300,200);
            catFrame.add(p);
            catFrame.setLocationRelativeTo(this);

            addBtn.addActionListener(e -> {
                String c = newCat.getText().trim();
                if (!c.isEmpty() && !categories.contains(c)) {
                    categories.add(c); catList.addItem(c);
                    newCat.setText("");
                }
            });
            delBtn.addActionListener(e -> {
                if (catList.getSelectedItem()!=null) {
                    categories.remove(catList.getSelectedItem().toString());
                    catList.removeItem(catList.getSelectedItem());
                }
            });
            catFrame.setVisible(true);
        }

        private void showReminders() {
            Date now = new Date(System.currentTimeMillis());
            for (Task t : tasks) {
                long diff = t.getDueDate().getTime() - now.getTime();
                long daysLeft = diff / (1000*60*60*24);
                if (!t.isCompleted() && (daysLeft < 3 || t.getDueDate().before(now))) {
                    JOptionPane.showMessageDialog(this,
                            "*** Reminder: Task '" + t.getTitle() + "' is due in " + daysLeft + " days!");
                }
            }
        }
    }
}

