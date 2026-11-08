
package com.mycompany.simpletaskscheduler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class SimpleTaskScheduler {
    private static final String FILE = "tasks.dat";
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Main method to start the program
    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        // Display the menu until the user exits
        do {
            System.out.println("\n1. Add  2. Show  3. Search  4. Save  5. Load  6. Run  7. Remove  0. Exit");
            System.out.print("Choice: ");
            if (!sc.hasNextInt()) { sc.nextLine(); continue; }
            choice = sc.nextInt(); sc.nextLine();

            // Perform the selected operation
            try {
                switch (choice) {
                    case 1:
                        // Add a new task
                        System.out.print("Name: "); String n = sc.nextLine();
                        System.out.print("Priority (1-10): "); int p = sc.nextInt(); sc.nextLine();
                        System.out.print("Time (yyyy-MM-dd HH:mm) or blank: "); String w = sc.nextLine().trim();
                        scheduler.addTask(new Task(n, p, w.isEmpty() ? LocalDateTime.now() : LocalDateTime.parse(w, FMT)));
                        break;
                    case 2: scheduler.sortTasks(); scheduler.showTasks(); break;
                    case 3:
                        // Search for a task
                        System.out.print("Search: ");
                        Task f = scheduler.searchTask(sc.nextLine(), 0);
                        System.out.println(f != null ? "Found: " + f : "Not found.");
                        break;
                    case 4: scheduler.saveTasks(FILE); break;
                    case 5: scheduler.loadTasks(FILE); break;
                    case 6: scheduler.runTasks(); System.out.println("Scheduled."); break;
                    case 7:
                        // Remove a task
                        System.out.print("Remove: ");
                        System.out.println(scheduler.removeTask(sc.nextLine()) ? "Removed." : "Not found.");
                        break;
                    case 0: scheduler.shutdown(); System.out.println("Exiting..."); break;
                    default: System.out.println("Invalid.");
                }
            } catch (Exception e) { System.out.println("Error: " + e.getMessage()); }
        } while (choice != 0);

        // Close the scanner
        sc.close();
    }
}
