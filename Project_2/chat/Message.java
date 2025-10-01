package chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private String sender;
    private String content;
    private LocalDateTime timestamp;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getFormattedMessage() {
        return "[" + timestamp.format(DateTimeFormatter.ofPattern("HH:mm")) + "] " + sender + ": " + content;
    }
}
