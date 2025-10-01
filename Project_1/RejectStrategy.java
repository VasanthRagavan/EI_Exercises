public class RejectStrategy implements ConflictResolutionStrategy {
    @Override
    public void resolveConflict(ScheduleManager manager, Task incoming, Task existing) throws TaskConflictException {
        throw new TaskConflictException("Task conflicts with existing task: " + existing.getDescription());
    }
}
