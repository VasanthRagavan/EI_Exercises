package utils;

public class Logger {
    public static void log(String msg) {
        System.out.println("[LOG] " + msg);
    }

    public static void error(String msg) {
        System.err.println("[ERROR] " + msg);
    }
}
