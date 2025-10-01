import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class ScheduleManager {
    private static final Logger LOGGER = LoggerUtil.getLogger(ScheduleManager.class.getName());
    private static final ScheduleManager INSTANCE = new ScheduleManager();

    private final List<Task> tasks = new ArrayList<>();
    private final List<ScheduleObserver> observers = new CopyOnWriteArrayList<>();
    private ConflictResolutionStrategy conflictStrategy = new RejectStrategy();

    private ScheduleManager() { }

    public static ScheduleManager getInstance() {
        return INSTANCE;
    }

    public synchronized void addTask(Task t) throws TaskConflictException {
        if (t == null) throw new IllegalArgumentException("Task cannot be null");
        LOGGER.info("Attempting to add task: " + t.toString());
        // check for overlaps
        Task conflicting = findConflictingTask(t);
        if (conflicting != null) {
            LOGGER.warning("Conflict detected with task: " + conflicting.getId());
            // delegate to strategy
            conflictStrategy.resolveConflict(this, t, conflicting);
            // resolution may have added/rescheduled; if strategy chooses to reject it throws
            return;
        }
        tasks.add(t);
        Collections.sort(tasks);
        notifyObservers("TaskAdded", t);
        LOGGER.info("Task added: " + t.getId());
    }

    public synchronized void removeTask(String id) throws TaskNotFoundException {
        Task found = findById(id);
        if (found == null) throw new TaskNotFoundException("Task not found: " + id);
        tasks.remove(found);
        notifyObservers("TaskRemoved", found);
        LOGGER.info("Task removed: " + id);
    }

    public synchronized List<Task> viewTasksSorted() {
        List<Task> copy = new ArrayList<>(tasks);
        Collections.sort(copy);
        return copy;
    }

    public synchronized Task findById(String id) {
        for (Task t : tasks) {
            if (t.getId().equals(id)) return t;
        }
        return null;
    }

    private Task findConflictingTask(Task candidate) {
        for (Task t : tasks) {
            if (overlap(t.getStartMinutes(), t.getEndMinutes(), candidate.getStartMinutes(), candidate.getEndMinutes())) {
                return t;
            }
        }
        return null;
    }

    private boolean overlap(int s1, int e1, int s2, int e2) {
        return s1 < e2 && s2 < e1; // common interval
    }

    public synchronized void editTask(String id, String newDescription, String newStart, String newEnd, Priority newPriority) throws InvalidTimeException, TaskNotFoundException, TaskConflictException {
        Task existing = findById(id);
        if (existing == null) throw new TaskNotFoundException("Task not found: " + id);
        // create a temporary Task for overlap checks
        Task updated = new Task(
                newDescription != null ? newDescription : existing.getDescription(),
                newStart != null ? TimeParserAdapter.toMinutes(newStart) : existing.getStartMinutes(),
                newEnd != null ? TimeParserAdapter.toMinutes(newEnd) : existing.getEndMinutes(),
                newPriority != null ? newPriority : existing.getPriority()
        );
        // remove the existing temporarily
        tasks.remove(existing);
        Task conflict = findConflictingTask(updated);
        if (conflict != null) {
            // put back and throw
            tasks.add(existing);
            Collections.sort(tasks);
            throw new TaskConflictException("Edit conflict with task: " + conflict.getDescription());
        }
        // apply changes
        existing.setDescription(updated.getDescription());
        existing.setPriority(updated.getPriority());
        // note: start/end are final fields; in production you might make these mutable or recreate the task
        // for simplicity, create a new Task replacing the old one
        Task replacement = new Task(existing.getDescription(), updated.getStartMinutes(), updated.getEndMinutes(), existing.getPriority());
        tasks.remove(existing);
        tasks.add(replacement);
        Collections.sort(tasks);
        notifyObservers("TaskEdited", replacement);
    }

    public synchronized void markCompleted(String id) throws TaskNotFoundException, InvalidTimeException {
        Task t = findById(id);
        if (t == null) throw new TaskNotFoundException("Task not found: " + id);
        // We cannot mutate the original boolean if fields are final; we will reflect completion via replacement
        Task completed = new Task(t.getDescription(), t.getStartMinutes(), t.getEndMinutes(), t.getPriority());
        completed.markCompleted();
        tasks.remove(t);
        tasks.add(completed);
        Collections.sort(tasks);
        notifyObservers("TaskCompleted", completed);
    }

    public synchronized List<Task> viewByPriority(Priority p) {
        List<Task> res = new ArrayList<>();
        for (Task t : tasks) {
            if (t.getPriority() == p) res.add(t);
        }
        Collections.sort(res);
        return res;
    }

    public void registerObserver(ScheduleObserver obs) {
        if (obs != null) observers.add(obs);
    }

    public void unregisterObserver(ScheduleObserver obs) {
        observers.remove(obs);
    }

    private void notifyObservers(String event, Task t) {
        for (ScheduleObserver obs : observers) {
            try {
                obs.update(event, t);
            } catch (Exception e) {
                LOGGER.warning("Observer update failed: " + e.getMessage());
            }
        }
    }

    public void setConflictStrategy(ConflictResolutionStrategy strategy) {
        if (strategy != null) this.conflictStrategy = strategy;
    }

    public ConflictResolutionStrategy getConflictStrategy() {
        return this.conflictStrategy;
    }
}
