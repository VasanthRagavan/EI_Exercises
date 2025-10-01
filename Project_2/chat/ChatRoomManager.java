package chat;

import java.util.*;

public class ChatRoomManager {

    private static ChatRoomManager instance;
    private Map<String, ChatRoom> rooms = new HashMap<>();

    private ChatRoomManager() {}

    public static synchronized ChatRoomManager getInstance() {
        if (instance == null) instance = new ChatRoomManager();
        return instance;
    }

    public synchronized void createRoom(String roomId, String password, int maxUsers) throws Exception {
        if (rooms.containsKey(roomId)) throw new Exception("Room exists!");
        rooms.put(roomId, new ChatRoom(roomId, password, maxUsers));
    }

    public ChatRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }

    public Map<String, ChatRoom> getAllRooms() {
        return rooms;
    }
}
