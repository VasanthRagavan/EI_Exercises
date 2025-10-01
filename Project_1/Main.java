import java.util.Scanner;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = LoggerUtil.getLogger(Main.class.getName());

    public static void main(String[] args) {
        AppConfig config = AppConfig.getInstance();
        config.setRunning(true);

        ScheduleManager manager = ScheduleManager.getInstance();

        manager.registerObserver(new ConsoleObserver());

        manager.setConflictStrategy(new RejectStrategy());

        Scanner scanner = new Scanner(System.in);
        ScheduleFacade facade = new ScheduleFacade(manager);

        LOGGER.info("Astronaut Daily Schedule Organizer started.");

        while (config.isRunning()) {
            try {
                System.out.println();
                System.out.println("=== Astronaut Daily Schedule Organizer ===");
                System.out.println("1) Add Task");
                System.out.println("2) Remove Task");
                System.out.println("3) View Tasks (sorted)");
                System.out.println("4) Edit Task");
                System.out.println("5) Mark Task Completed");
                System.out.println("6) View by Priority");
                System.out.println("7) Switch Conflict Strategy (Reject/Reschedule)");
                System.out.println("8) Exit");
                System.out.print("Choose: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        facade.handleAddTaskInteractive(scanner);
                        break;
                    case "2":
                        facade.handleRemoveTaskInteractive(scanner);
                        break;
                    case "3":
                        facade.handleViewTasks();
                        break;
                    case "4":
                        facade.handleEditTaskInteractive(scanner);
                        break;
                    case "5":
                        facade.handleMarkCompletedInteractive(scanner);
                        break;
                    case "6":
                        facade.handleViewByPriorityInteractive(scanner);
                        break;
                    case "7":
                        facade.handleSwitchStrategyInteractive(scanner);
                        break;
                    case "8":
                        System.out.println("Exiting... Goodbye!");
                        config.setRunning(false);
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception ex) {
                LOGGER.severe("Unhandled exception in main loop: " + ex.getMessage());
                ex.printStackTrace(System.err);
            }
        }

        scanner.close();
        LOGGER.info("Application stopped.");
    }
}
