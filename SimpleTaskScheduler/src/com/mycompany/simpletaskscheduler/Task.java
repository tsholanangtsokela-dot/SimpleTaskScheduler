
package com.mycompany.simpletaskscheduler;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    // Store the task details
    private final String name;
    private final int priority;
    private final LocalDateTime scheduledTime;

    // Create a task that runs immediately
    public Task(String name, int priority) {
        this(name, priority, LocalDateTime.now());
    }

    // Create a scheduled task and validate its details
    public Task(String name, int priority, LocalDateTime scheduledTime) {
        if (name == null || !Pattern.matches("[a-zA-Z ]+", name))
            throw new IllegalArgumentException("Name: letters/spaces only.");
        if (priority < 1 || priority > 10)
            throw new IllegalArgumentException("Priority must be a number.");
        if (scheduledTime == null)
            throw new IllegalArgumentException("Time cannot be null.");

        this.name = name;
        this.priority = priority;
        this.scheduledTime = scheduledTime;
    }

    // Return the task details
    public String getName() { return name; }
    public int getPriority() { return priority; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }

    // Display task information as text
    @Override
    public String toString() {
        return "Task: " + name + " | Priority: " + priority + " | Scheduled: " + scheduledTime;
    }
}
