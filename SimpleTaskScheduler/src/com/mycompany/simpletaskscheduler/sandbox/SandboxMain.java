/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.simpletaskscheduler.sandbox;

/**
 *
 * @author tsholanangtsokela
 */

import com.mycompany.simpletaskscheduler.Scheduler;

public class SandboxMain {

    public static void main(String[] args) {

        Scheduler scheduler = new Scheduler();

        System.out.println("=== SANDBOX TEST ===");

        // Test 1: Check if the scheduler starts empty
        System.out.println("Test 1: Empty Scheduler");

        if (scheduler.getTasks().isEmpty()) {
            System.out.println("PASS: Scheduler is empty");
        } else {
            System.out.println("FAIL: Scheduler is not empty");
        }

        // Test 2: Search for a task that does not exist
        System.out.println("Test 2: Search Missing Task");

        if (scheduler.searchTask("Homework", 0) == null) {
            System.out.println("PASS: Missing task not found");
        } else {
            System.out.println("FAIL: Unexpected task found");
        }

        // Test 3: Remove a task that does not exist
        System.out.println("Test 3: Remove Missing Task");

        if (!scheduler.removeTask("Homework")) {
            System.out.println("PASS: Missing task not removed");
        } else {
            System.out.println("FAIL: Unexpected removal");
        }
        
        // Test 4: Add a task
        System.out.println("Test 4: Add Task");

        scheduler.addTask(
            new com.mycompany.simpletaskscheduler.Task("Homework", 3)
        );

        if (scheduler.getTasks().size() == 1) {
            System.out.println("PASS: Task added successfully");
        } else {
            System.out.println("FAIL: Task was not added");
        }

        // Test 5: Search for an existing task
        System.out.println("Test 5: Search Existing Task");

        if (scheduler.searchTask("Homework", 0) != null) {
            System.out.println("PASS: Task found");
        } else {
            System.out.println("FAIL: Task not found");
        }

        // Test 6: Remove an existing task
        System.out.println("Test 6: Remove Existing Task");

        boolean removed = scheduler.removeTask("Homework");

        if (removed && scheduler.getTasks().isEmpty()) {
            System.out.println("PASS: Task removed successfully");
        } else {
            System.out.println("FAIL: Task was not removed");
        }
        
        // Test 7: Sort tasks by priority
        System.out.println("Test 7: Sort Tasks");

        scheduler.addTask(
            new com.mycompany.simpletaskscheduler.Task("Study", 8)
        );

        scheduler.addTask(
            new com.mycompany.simpletaskscheduler.Task("Exercise", 2)
        );

        scheduler.addTask(
            new com.mycompany.simpletaskscheduler.Task("Assignment", 5)
        );

        scheduler.sortTasks();

        if (scheduler.getTasks().get(0).getPriority() == 2
                && scheduler.getTasks().get(1).getPriority() == 5
                && scheduler.getTasks().get(2).getPriority() == 8) {

            System.out.println("PASS: Tasks sorted correctly");

        } else {
            System.out.println("FAIL: Tasks not sorted correctly");
        }
        
        // Test 8: Save and Load Tasks
        System.out.println("Test 8: Save and Load Tasks");

        try {
            // Save the existing tasks
            scheduler.saveTasks("sandbox_tasks.dat");

            // Create a new scheduler
            Scheduler loadedScheduler = new Scheduler();

            try {
                // Load the saved tasks
                loadedScheduler.loadTasks("sandbox_tasks.dat");

                // Compare saved and loaded tasks
                if (loadedScheduler.getTasks().size() == 3
                        && loadedScheduler.searchTask("Study", 0) != null
                        && loadedScheduler.searchTask("Exercise", 0) != null
                        && loadedScheduler.searchTask("Assignment", 0) != null) {

                    System.out.println("PASS: Tasks saved and loaded");

                } else {
                    System.out.println("FAIL: Tasks not loaded correctly");
                }
            } finally {
                loadedScheduler.shutdown();
            }

        } catch (Exception e) {
            System.out.println("FAIL: " + e.getMessage());
        }
        
        // Test 9: Run a scheduled task
        System.out.println("Test 9: Run Scheduled Task");

        Scheduler runScheduler = new Scheduler();

        try {
            runScheduler.addTask(
                new com.mycompany.simpletaskscheduler.Task(
                    "Sandbox Test",
                    1,
                    java.time.LocalDateTime.now().plusSeconds(2)
                )
            );

            runScheduler.runTasks();

            // Wait for the scheduled task to execute
            Thread.sleep(4000);

            System.out.println(
                "CHECK: Look for the Running message above"
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("FAIL: Test interrupted");
        } finally {
            runScheduler.shutdown();
        }
        
        // Test 10: Invalid Task Name
        System.out.println("Test 10: Invalid Task Name");

        try {
            new com.mycompany.simpletaskscheduler.Task(
                "Homework123", 3
            );

            System.out.println(
                "FAIL: Invalid task name was accepted"
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                "PASS: Invalid task name rejected"
            );

        }
        
        // Test 11: Invalid Task Priority
        System.out.println("Test 11: Invalid Task Priority");

        try {
            new com.mycompany.simpletaskscheduler.Task(
                "Homework", 15
            );

            System.out.println(
                "FAIL: Invalid priority was accepted"
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                "PASS: Invalid priority rejected"
            );

        }
        
        // Test 12: Invalid Scheduled Time
        System.out.println("Test 12: Invalid Scheduled Time");

        try {
            new com.mycompany.simpletaskscheduler.Task(
                "Homework",
                3,
                null
            );

            System.out.println(
                "FAIL: Invalid scheduled time accepted"
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                "PASS: Invalid scheduled time rejected"
            );

        }

        scheduler.shutdown();

        System.out.println("=== TESTING COMPLETE ===");
    }
}
