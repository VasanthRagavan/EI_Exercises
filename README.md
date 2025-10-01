# EI_Exercises
# Astronaut Daily Schedule Organizer

## Overview
This is a **console-based Java application** to help astronauts manage their daily schedules efficiently. It allows users to add, remove, view, and manage tasks with priorities, while handling conflicts intelligently.

---

## Features

- Add tasks with description, start time, end time, and priority.
- Remove tasks.
- View all tasks sorted by start time.
- Validate that new tasks do not overlap with existing tasks.
- Handle conflicts using flexible strategies (Reschedule or Reject).
- Mark tasks as completed.
- View tasks by priority level.

---

## Design Patterns Used

| Pattern | Type | How Used |
|---------|------|----------|
| Builder | Creational | `TaskBuilder` simplifies creation of `Task` objects with multiple fields. |
| Strategy | Behavioral | `ConflictResolutionStrategy` interface allows conflict resolution to be switched at runtime (`RescheduleStrategy` or `RejectStrategy`). |
| Singleton  | Creational | `ScheduleManager` acts as the central manager of all tasks. |
| Observer  | Behavioral | Can be added to notify users of conflicts or updates (not implemented in current version). |

---

## Classes Overview

- **Task.java** – Represents a single task with description, start/end times, and priority.
- **TaskBuilder.java** – Builder pattern for easy task creation.
- **TimeParserAdapter.java** – Converts `"HH:MM"` strings to minutes and vice versa.
- **Priority.java** – Enum representing task priority (`HIGH`, `MEDIUM`, `LOW`).
- **ScheduleManager.java** – Manages all tasks and handles conflicts using strategies.
- **ConflictResolutionStrategy.java** – Interface for conflict handling strategies.
- **RescheduleStrategy.java** – Automatically reschedules conflicting tasks.
- **RejectStrategy.java** – Rejects conflicting tasks.
- **TaskConflictException.java** – Custom exception for task conflicts.
- **InvalidTimeException.java** – Exception for invalid time inputs.
- **Main.java** – Demo console program showcasing functionality.

---

# Java Multi-Terminal Chat Application

## Overview

This is a **console-based chat application** built in plain Java that allows multiple users to communicate in **real-time**.

The application supports:

* **Chat rooms** with optional password protection and user limits.
* **Private messaging** between users.
* **Message history** for each room.
* **Active users list** in each room.
* Multi-terminal support (clients can run in separate terminals).
* **Observer Pattern** for real-time message updates.
* **Adapter Pattern** for flexible client communication.
* **Exception handling** and logging for robustness.

---

## Features

1. **Room Management**

   * Create a room with optional **password** and **max users**.
   * Join existing rooms with the correct password.

2. **Messaging**

   * **Room messages:** send messages visible to all users in a room.
   * **Private messages:** send direct messages to a specific user.
   * **Message history:** new users joining a room can see past messages.

3. **User Management**

   * View **active users** in a room.
   * Exit the application cleanly.

---

## Design Patterns Used

* **Observer Pattern:** `MessageObserver` updates users in real-time whenever a new message is sent in a room.
* **Adapter Pattern:** `ClientAdapter` and `SocketAdapter` abstract message sending, allowing different communication protocols.
* **Singleton Pattern:** `ChatRoomManager` ensures a single instance manages all chat rooms.

---
