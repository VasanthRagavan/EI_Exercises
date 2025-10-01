package chat;

public class PrivateChatManager {

    public static void sendPrivate(User from, User to, String content) {
        String formatted = "[Private] " + from.getUsername() + ": " + content;
        to.getWriter().println(formatted);
        from.getWriter().println(formatted);
    }
}
