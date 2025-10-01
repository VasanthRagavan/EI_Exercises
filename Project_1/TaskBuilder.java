public class TaskBuilder {
    private String description;
    private int startMinutes = -1;
    private int endMinutes = -1;
    private Priority priority = Priority.MEDIUM;

    public TaskBuilder setDescription(String d) { 
        this.description = d; 
        return this; 
    }

    public TaskBuilder setStart(String hhmm) throws InvalidTimeException { 
        this.startMinutes = TimeParserAdapter.toMinutes(hhmm); 
        return this; 
    }

    public TaskBuilder setEnd(String hhmm) throws InvalidTimeException { 
        this.endMinutes = TimeParserAdapter.toMinutes(hhmm); 
        return this; 
    }

    public TaskBuilder setPriority(Priority p) { 
        if (p != null) this.priority = p; 
        return this; 
    }

    public Task build() throws InvalidTimeException {
        if (description == null || startMinutes < 0 || endMinutes <= startMinutes) {
            throw new IllegalStateException("Incomplete/invalid task data");
        }
        return new Task(description, startMinutes, endMinutes, priority);
    }
}
