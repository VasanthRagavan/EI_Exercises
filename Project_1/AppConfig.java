public class AppConfig {
    private static final AppConfig INSTANCE = new AppConfig();
    private volatile boolean running = false;

    private AppConfig() { }

    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
