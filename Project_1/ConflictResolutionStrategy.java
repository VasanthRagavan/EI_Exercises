public interface ConflictResolutionStrategy {
    void resolveConflict(ScheduleManager manager, Task incoming, Task existing) throws TaskConflictException;
}
