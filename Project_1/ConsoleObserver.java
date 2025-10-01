public class ConsoleObserver implements ScheduleObserver {
    @Override
    public void update(String event, Task task) {
        String msg = String.format("[Observer] Event: %s | Task: %s", event, task.toString());
        System.out.println(msg);
    }
}
