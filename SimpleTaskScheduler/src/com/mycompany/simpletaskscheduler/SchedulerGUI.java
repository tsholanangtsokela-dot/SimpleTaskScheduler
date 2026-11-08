
package com.mycompany.simpletaskscheduler;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SchedulerGUI extends JFrame {
    private static final String FILE = "tasks.dat";
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final Scheduler scheduler = new Scheduler();
    private final JTextArea output = new JTextArea();
    private final JTextArea logOutput = new JTextArea();

    // Set up the application window
    public SchedulerGUI() {
        setTitle("Simple Task Scheduler");
        setSize(700, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the buttons
        String[] btnNames = {"Add Task", "Show Tasks", "Search Task", "Save Tasks", "Load Tasks", "Run Tasks", "Remove Task", "Exit"};
        JPanel btnPanel = new JPanel(new GridLayout(8, 1, 5, 5));
        JButton[] buttons = new JButton[8];
        for (int i = 0; i < 8; i++) {
            buttons[i] = new JButton(btnNames[i]);
            btnPanel.add(buttons[i]);
        }

        // Set up the display areas
        output.setEditable(false);
        logOutput.setEditable(false);
        logOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logOutput.setBackground(new Color(240, 240, 240));
        JScrollPane logScroll = new JScrollPane(logOutput);
        logScroll.setPreferredSize(new Dimension(700, 120));
        logScroll.setBorder(BorderFactory.createTitledBorder("System Log (scheduler.log)"));

        add(btnPanel, BorderLayout.WEST);
        add(new JScrollPane(output), BorderLayout.CENTER);
        add(logScroll, BorderLayout.SOUTH);

        // Show log messages in the GUI
        Scheduler.LOG.addHandler(new java.util.logging.Handler() {
            public void publish(java.util.logging.LogRecord r) {
                SwingUtilities.invokeLater(() -> logOutput.append(r.getLevel() + ": " + r.getMessage() + "\n"));
            }
            public void flush() {}
            public void close() {}
        });

        
        // Add Task
        buttons[0].addActionListener(e -> {
            output.setText("");

            // Validate task name
            String name;
            while (true) {
                name = JOptionPane.showInputDialog(this, "Enter Task Name:");
                if (name == null) return;

                if (name.matches("[a-zA-Z ]+")) break;

                JOptionPane.showMessageDialog(this,
                        "Task name must contain letters and spaces only.",
                        "Invalid Name", JOptionPane.ERROR_MESSAGE);
            }

            // Validate priority
            int priority;
            while (true) {
                String prStr = JOptionPane.showInputDialog(this,
                        "Enter Priority (eg. 1, 2, 3):");
                if (prStr == null) return;

                try {
                    priority = Integer.parseInt(prStr.trim());

                    if (priority >= 1 && priority <= 10) break;

                } catch (NumberFormatException ex) {
                    // Invalid number will show the error below
                }

                JOptionPane.showMessageDialog(this,
                        "Priority must be a whole number.",
                        "Invalid Priority", JOptionPane.ERROR_MESSAGE);
            }

            // Validate scheduled time
            LocalDateTime time;
            while (true) {
                String timeStr = JOptionPane.showInputDialog(this,
                        "Enter Time (yyyy-MM-dd HH:mm):");
                if (timeStr == null) return;

                try {
                    time = timeStr.trim().isEmpty()
                            ? LocalDateTime.now()
                            : LocalDateTime.parse(timeStr.trim(), FORMAT);
                    break;

                } catch (java.time.format.DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid time. Use yyyy-MM-dd HH:mm\n"
                            + "Example: 2026-11-10 14:30",
                            "Invalid Time", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Add the validated task
            scheduler.addTask(new Task(name, priority, time));

            output.append("Task Added\n-----------------\n"
                    + "Name: " + name
                    + "\nPriority: " + priority
                    + "\nScheduled: " + time + "\n");
        });


        // Show Tasks
        buttons[1].addActionListener(e -> {
            output.setText("Tasks\n-----------------\n");
            scheduler.sortTasks();
            int c = 1;
            for (Task t : scheduler.getTasks()) {
                output.append(c++ + ". " + t.getName() + " | Priority: " + t.getPriority() + " | Scheduled: " + t.getScheduledTime() + "\n");
            }
            if (scheduler.getTasks().isEmpty()) output.append("No tasks found.");
        });

        // Search Task
        buttons[2].addActionListener(e -> {
            output.setText("");
            String name = JOptionPane.showInputDialog(this, "Enter task name:");
            if (name == null) return;
            Task found = scheduler.searchTask(name, 0);
            output.append("Search Result\n-----------------\n" + (found != null ? "Found: " + found : "Task not found."));
        });

        // Save Tasks
        buttons[3].addActionListener(e -> {
            output.setText("Save Tasks\n-----------------\n");
            try { scheduler.saveTasks(FILE); output.append("Tasks saved successfully."); }
            catch (Exception ex) { output.append("Error: " + ex.getMessage()); }
        });

        // Load Tasks
        buttons[4].addActionListener(e -> {
            output.setText("Load Tasks\n-----------------\n");
            try { scheduler.loadTasks(FILE); output.append("Tasks loaded successfully."); }
            catch (Exception ex) { output.append("Error: " + ex.getMessage()); }
        });

        // Run Tasks
        buttons[5].addActionListener(e -> {
            output.setText("Running Tasks\n-----------------\n");
            if (scheduler.getTasks().isEmpty()) { output.append("No tasks available."); return; }

            java.io.PrintStream original = System.out;
            System.setOut(new java.io.PrintStream(new java.io.OutputStream() {
                private StringBuilder buf = new StringBuilder();
                public void write(int b) {
                    if (b == '\n') {
                        String line = buf.toString().replaceAll(" on pool-1-thread-\\d+", "");
                        buf.setLength(0);
                        SwingUtilities.invokeLater(() -> output.append(line + "\n"));
                    } else buf.append((char) b);
                }
            }, true));

            scheduler.runTasks();
            output.append("Tasks scheduled. Live output will appear below.\n");
            new javax.swing.Timer(200, ev -> System.setOut(original)) {{ setRepeats(false); start(); }};
        });

        // Remove Task
        buttons[6].addActionListener(e -> {
            output.setText("");
            String name = JOptionPane.showInputDialog(this, "Enter task name to remove:");
            if (name == null) return;
            boolean ok = scheduler.removeTask(name);
            output.append("Remove Task\n-----------------\n" + (ok ? "Task removed." : "Task not found."));
        });

        // Exit
        buttons[7].addActionListener(e -> { scheduler.shutdown(); System.exit(0); });
    }

    // Start the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SchedulerGUI().setVisible(true));
    }
}
