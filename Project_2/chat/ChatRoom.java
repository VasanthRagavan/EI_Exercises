package chat;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatRoom {

    private String roomId;
    private String password;
    private int maxUsers;
    private List<User> activeUsers = new CopyOnWriteArrayList<>();
    private List<Message> messageHistory = new CopyOnWriteArrayList<>();
    private List<Observer> observers = new ArrayList<>();

    public ChatRoom(String roomId, String password, int maxUsers) {
        this.roomId = roomId;
        this.password = password;
        this.maxUsers = maxUsers;
    }

    public synchronized void joinRoom(User user, String pass) throws Exception {
        if (password != null && !password.equals(pass)) {
            throw new Exception("Incorrect password!");
        }
        if (maxUsers > 0 && activeUsers.size() >= maxUsers) {
            throw new Exception("Room is full!");
        }
        if (!activeUsers.contains(user)) {
            activeUsers.add(user);
            addObserver(new MessageObserver(user));
        }
    }

    public void sendMessage(User sender, String content) {
        Message msg = new Message(sender.getUsername(), content);
        messageHistory.add(msg);
        notifyObservers(msg);
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers(Message msg) {
        for (Observer obs : observers) {
            obs.update(msg);
        }
    }

    public List<User> getActiveUsers() {
        return activeUsers;
    }

    public List<Message> getMessageHistory() {
        return messageHistory;
    }

    public String getRoomId() {
        return roomId;
    }
}
