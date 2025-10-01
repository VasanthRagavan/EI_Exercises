import java.util.UUID;

public class Task implements Comparable<Task> {
    private final String id;
    private String description;
    private final int startMinutes;
    private final int endMinutes;
    private Priority priority;
    private boolean completed;

    public Task(String description, int startMinutes, int endMinutes, Priority priority) throws InvalidTimeException {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidTimeException("Task description is required.");
        }
        if (startMinutes < 0 || startMinutes >= 24 * 60) {
            throw new InvalidTimeException("Start time must be within 00:00–23:59.");
        }
        if (endMinutes <= 0 || endMinutes > 24 * 60) {
            throw new InvalidTimeException("End time must be within 00:00–24:00.");
        }
        if (endMinutes <= startMinutes) {
            throw new InvalidTimeException("End time must be after start time.");
        }
        if (priority == null) {
            throw new InvalidTimeException("Priority is required.");
        }

        this.id = UUID.randomUUID().toString();
        this.description = description.trim();
        this.startMinutes = startMinutes;
        this.endMinutes = endMinutes;
        this.priority = priority;
        this.completed = false;
    }

    public String getId() { return id; }
    public String getDescription() { return description; }
    public int getStartMinutes() { return startMinutes; }
    public int getEndMinutes() { return endMinutes; }
    public Priority getPriority() { return priority; }
    public boolean isCompleted() { return completed; }

    public void setDescription(String description) throws InvalidTimeException {
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidTimeException("Task description cannot be empty.");
        }
        this.description = description.trim();
    }

    public void setPriority(Priority p) throws InvalidTimeException {
        if (p == null) {
            throw new InvalidTimeException("Priority cannot be null.");
        }
        this.priority = p;
    }

    public void markCompleted() { this.completed = true; }

    @Override
    public int compareTo(Task o) {
        return Integer.compare(this.startMinutes, o.startMinutes);
    }

    @Override
    public String toString() {
        return String.format("%s - %s: %s [%s]%s",
                TimeParserAdapter.toHHmm(startMinutes),
                TimeParserAdapter.toHHmm(endMinutes),
                description,
                priority,
                completed ? " (Completed)" : "");
    }
}
