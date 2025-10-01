import java.util.List;
import java.util.logging.Logger;

public class RescheduleStrategy implements ConflictResolutionStrategy {
    private static final Logger LOGGER = LoggerUtil.getLogger(RescheduleStrategy.class.getName());

    @Override
    public void resolveConflict(ScheduleManager manager, Task incoming, Task existing) throws TaskConflictException {
      
        int duration = incoming.getEndMinutes() - incoming.getStartMinutes();
        int newStart = existing.getEndMinutes();
        int newEnd = newStart + duration;

        if (newEnd > 24 * 60) {
            throw new TaskConflictException("Cannot reschedule task; no time available in the day.");
        }

        try {
            Task rescheduled = new Task(incoming.getDescription(), newStart, newEnd, incoming.getPriority());

            
            List<Task> current = manager.viewTasksSorted();
            for (Task t : current) {
                if (t == existing) continue;
                if (t.getStartMinutes() < rescheduled.getEndMinutes() &&
                    rescheduled.getStartMinutes() < t.getEndMinutes()) {
                    throw new TaskConflictException("Reschedule would still conflict with " + t.getDescription());
                }
            }

            // Add rescheduled task
            manager.addTask(rescheduled);
            LOGGER.info("Incoming task rescheduled to " +
                    TimeParserAdapter.toHHmm(newStart) + "-" +
                    TimeParserAdapter.toHHmm(newEnd));

        } catch (InvalidTimeException e) {
            throw new TaskConflictException("Reschedule failed: " + e.getMessage());
        }
    }
}
