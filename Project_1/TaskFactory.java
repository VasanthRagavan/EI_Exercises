public class TaskFactory {
    public static Task createTask(String description, String startHHmm, String endHHmm, String priorityStr) throws InvalidTimeException {
        TaskBuilder builder = new TaskBuilder();
        builder.setDescription(description)
               .setStart(startHHmm)
               .setEnd(endHHmm)
               .setPriority(Priority.fromString(priorityStr));
        return builder.build();
    }
}
