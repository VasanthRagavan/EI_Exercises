import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ScheduleFacade {
    private static final Logger LOGGER = LoggerUtil.getLogger(ScheduleFacade.class.getName());
    private final ScheduleManager manager;

    public ScheduleFacade(ScheduleManager manager) {
        this.manager = manager;
    }

    public void handleAddTaskInteractive(Scanner scanner) {
        try {
            System.out.print("Description: ");
            String desc = scanner.nextLine();
            System.out.print("Start (HH:mm): ");
            String start = scanner.nextLine();
            System.out.print("End (HH:mm): ");
            String end = scanner.nextLine();
            System.out.print("Priority (High/Medium/Low): ");
            String pr = scanner.nextLine();

            Task t = TaskFactory.createTask(desc, start, end, pr);
            manager.addTask(t);
            System.out.println("Task added successfully.");
        } catch (InvalidTimeException | TaskConflictException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Failed to add task: " + e.getMessage());
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    public void handleRemoveTaskInteractive(Scanner scanner) {
        try {
            handleViewTasks();
            System.out.print("Enter Task ID to remove: ");
            String id = scanner.nextLine();
            manager.removeTask(id);
            System.out.println("Task removed successfully.");
        } catch (TaskNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Remove failed: " + e.getMessage());
            System.out.println("Unexpected error.");
        }
    }

    public void handleViewTasks() {
        List<Task> tasks = manager.viewTasksSorted();
        if (tasks.isEmpty()) {
            System.out.println("No tasks scheduled for the day.");
            return;
        }
        System.out.println("Scheduled Tasks:");
        for (Task t : tasks) {
            System.out.println(t.getId() + " | " + t.toString());
        }
    }

    public void handleEditTaskInteractive(Scanner scanner) {
        try {
            handleViewTasks();
            System.out.print("Enter Task ID to edit: ");
            String id = scanner.nextLine();
            System.out.print("New Description (leave blank to keep): ");
            String desc = scanner.nextLine();
            System.out.print("New Start (HH:mm) or blank: ");
            String start = scanner.nextLine();
            System.out.print("New End (HH:mm) or blank: ");
            String end = scanner.nextLine();
            System.out.print("New Priority (High/Medium/Low) or blank: ");
            String pr = scanner.nextLine();

            Priority newP = pr.trim().isEmpty() ? null : Priority.fromString(pr);
            manager.editTask(id,
                    desc.trim().isEmpty() ? null : desc,
                    start.trim().isEmpty() ? null : start,
                    end.trim().isEmpty() ? null : end,
                    newP);
            System.out.println("Task edited successfully.");
        } catch (TaskNotFoundException | InvalidTimeException | TaskConflictException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Edit failed: " + e.getMessage());
            System.out.println("Unexpected error.");
        }
    }

    public void handleMarkCompletedInteractive(Scanner scanner) {
        try {
            handleViewTasks();
            System.out.print("Enter Task ID to mark completed: ");
            String id = scanner.nextLine();
            manager.markCompleted(id);
            System.out.println("Task marked completed.");
        } catch (TaskNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.severe("Mark completed failed: " + e.getMessage());
            System.out.println("Unexpected error.");
        }
    }

    public void handleViewByPriorityInteractive(Scanner scanner) {
        System.out.print("Enter priority (High/Medium/Low): ");
        String pr = scanner.nextLine();
        Priority p = Priority.fromString(pr);
        List<Task> list = manager.viewByPriority(p);
        if (list.isEmpty()) {
            System.out.println("No tasks with priority " + p);
            return;
        }
        for (Task t : list) System.out.println(t.getId() + " | " + t.toString());
    }

    public void handleSwitchStrategyInteractive(Scanner scanner) {
        System.out.print("Choose strategy: 1) Reject 2) Reschedule : ");
        String s = scanner.nextLine();
        if ("1".equals(s.trim())) {
            manager.setConflictStrategy(new RejectStrategy());
            System.out.println("Set to Reject strategy.");
        } else {
            manager.setConflictStrategy(new RescheduleStrategy());
            System.out.println("Set to Reschedule strategy.");
        }
    }
}
