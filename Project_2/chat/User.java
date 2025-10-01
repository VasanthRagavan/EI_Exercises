package chat;

import java.io.PrintWriter;

public class User {
    private String username;
    private PrintWriter writer;

    public User(String username, PrintWriter writer) {
        this.username = username;
        this.writer = writer;
    }

    public String getUsername() {
        return username;
    }

    public PrintWriter getWriter() {
        return writer;
    }
}
