
package com.mycompany.simpletaskscheduler;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

public class Scheduler {

    // Logger to record activities
    public static final Logger LOG = Logger.getLogger(Scheduler.class.getName());

    // List to store all tasks
    private List<Task> tasks = new CopyOnWriteArrayList<>();

    // Thread pool to run scheduled tasks
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    // Constructor to set up the logger
    public Scheduler() {
        try {
            FileHandler fh = new FileHandler("scheduler.log", true);
            fh.setFormatter(new SimpleFormatter());
            LOG.setUseParentHandlers(false);
            LOG.addHandler(fh);
        } catch (IOException e) { System.err.println("Logger error: " + e.getMessage()); }
    }

    // Add a new task to the list
    public void addTask(Task t) {
        tasks.add(t);
        LOG.info("Added: " + t);
    }

    // Remove a task using its name
    public boolean removeTask(String name) {
        boolean removed = tasks.removeIf(t -> t.getName().equalsIgnoreCase(name));
        if (removed) LOG.info("Removed: " + name);
        else LOG.warning("Remove failed: " + name);
        return removed;
    }

    // Search for a task using recursion
    public Task searchTask(String name, int index) {

        // Stop searching if the end of the list is reached
        if (index >= tasks.size()) return null;

        // Return the task if the name matches
        if (tasks.get(index).getName().equalsIgnoreCase(name)) return tasks.get(index);

        // Search the next task
        return searchTask(name, index + 1);
    }

    // Sort tasks by priority from lowest to highest
    public void sortTasks() {
        List<Task> sorted = new CopyOnWriteArrayList<>(tasks);
        sorted.sort(Comparator.comparingInt(Task::getPriority));
        tasks = sorted;
        LOG.info("Tasks sorted.");
    }

    // Save tasks to a file
    public void saveTasks(String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(new ArrayList<>(tasks));
            LOG.info("Saved to " + filename);
        }
    }

    // Load previously saved tasks from a file
    @SuppressWarnings("unchecked")
    public void loadTasks(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            tasks = new CopyOnWriteArrayList<>((List<Task>) in.readObject());
            LOG.info("Loaded from " + filename);
        }
    }

    // Run tasks at their scheduled time
    public void runTasks() {

        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();

        // Go through each task in the list
        for (Task t : tasks) {

            // Calculate how long to wait before running
            long delay = Math.max(0, Duration.between(now, t.getScheduledTime()).getSeconds());

            // Schedule the task to run after the delay
            executor.schedule(() -> {
                System.out.println("Running: " + t.getName() + " at " + LocalDateTime.now());

                // Simulate running the task for one second
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

                // Record when the task finishes
                LOG.info("Finished: " + t.getName());

            }, delay, TimeUnit.SECONDS);
        }

        LOG.info("Tasks scheduled.");
    }

    // Stop the scheduler from accepting new tasks
    public void shutdown() {
        executor.shutdown();
        LOG.info("Shut down.");
    }

    // Display all tasks
    public void showTasks() { tasks.forEach(System.out::println); }

    // Return a read-only copy of the task list
    public List<Task> getTasks() { return Collections.unmodifiableList(new ArrayList<>(tasks)); }
}
