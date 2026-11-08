# Simple Task Scheduler

A Java-based desktop application developed by **Group 15** to help users create, manage, schedule, and organize tasks.

## Project Overview

The Simple Task Scheduler is a Java application developed using Object-Oriented Programming (OOP) principles. It provides a graphical user interface (GUI) built with Java Swing and supports task management, scheduling, searching, sorting, and file handling.

## Features

- Add tasks with a name, priority, and scheduled date and time.
- Display all stored tasks.
- Search for tasks using recursive searching.
- Sort tasks according to priority.
- Remove tasks by name.
- Save tasks to a file using Java serialization.
- Load previously saved tasks.
- Schedule tasks for execution using multithreading.
- Validate user input and handle invalid entries.
- Record application activities using logging.

## Technologies Used

- Java
- Java Swing
- Apache NetBeans IDE
- Java Collections Framework
- Java Serialization
- Java Executor Framework

## Project Structure

```text
SimpleTaskScheduler/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── mycompany/
│                   └── simpletaskscheduler/
│                       ├── Task.java
│                       ├── Scheduler.java
│                       ├── SchedulerGUI.java
│                       └── SimpleTaskScheduler.java
├── nbproject/
├── build.xml
└── README.md
```

The folder structure may differ depending on the NetBeans project configuration.

## Requirements

- Java Development Kit (JDK)
- Apache NetBeans IDE or another Java-compatible IDE

## How to Run the Application

1. Download or clone this repository.
2. Open Apache NetBeans.
3. Select **File → Open Project**.
4. Navigate to the SimpleTaskScheduler project folder.
5. Open the project.
6. Ensure that the required JDK is configured.
7. Run the `SchedulerGUI` class to launch the graphical interface.

## How to Use

**Add Task:** Enter the task name, priority (1–10), and scheduled date and time in the format `yyyy-MM-dd HH:mm`.

**Show Tasks:** Display all
