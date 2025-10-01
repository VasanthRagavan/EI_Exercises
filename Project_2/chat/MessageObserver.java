package chat;

public class MessageObserver implements Observer {
    private User user;

    public MessageObserver(User user) {
        this.user = user;
    }

    @Override
    public void update(Message message) {
        user.getWriter().println(message.getFormattedMessage());
    }
}
