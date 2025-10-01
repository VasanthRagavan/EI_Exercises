package chat;

public class ConsoleAdapter implements Observer {

    private User user;

    public ConsoleAdapter(User user) {
        this.user = user;
    }

    @Override
    public void update(Message message) {
        System.out.println("[Room Update] " + message.getFormattedMessage());
    }

    public void sendMessage(ChatRoom room, String content) {
        room.sendMessage(user, content);
    }
}
