package main;

import chat.*;
import utils.Logger;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer {

    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private ExecutorService pool = Executors.newCachedThreadPool();
    private ChatRoomManager manager = ChatRoomManager.getInstance();

    public ChatServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
        Logger.log("Server started on port " + PORT);
    }

    public void start() {
        try {
            while (true) {
                Socket client = serverSocket.accept();
                pool.execute(new ClientHandler(client));
            }
        } catch (IOException e) {
            Logger.error("Server error: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {
        new ChatServer().start();
    }

    private class ClientHandler implements Runnable {

        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private User user;
        private ChatRoom currentRoom;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }

        @Override
        public void run() {
            try {
                out.println("Enter username:");
                String uname = in.readLine();
                while (uname == null || uname.trim().isEmpty()) {
                    out.println("Username cannot be empty. Enter username:");
                    uname = in.readLine();
                }
                user = new User(uname, out);

                String line;
                while ((line = in.readLine()) != null) {
                    String[] parts = line.split(" ", 2);
                    String cmd = parts[0].toUpperCase();

                    switch (cmd) {
                        case "CREATE":
                            handleCreate(parts);
                            break;
                        case "JOIN":
                            handleJoin(parts);
                            break;
                        case "ROOMMSG":
                            handleRoomMsg(parts);
                            break;
                        case "PRIVMSG":
                            handlePrivMsg(parts);
                            break;
                        case "LISTROOMS":
                            out.println("ROOMS: " + manager.getAllRooms().keySet());
                            break;
                        case "EXIT":
                            socket.close();
                            return;
                        default:
                            out.println("Unknown command!");
                    }
                }

            } catch (Exception e) {
                Logger.error("ClientHandler error: " + e.getMessage());
            }
        }

        private void handleCreate(String[] parts) {
            try {
                if (parts.length < 2) {
                    out.println("Usage: CREATE roomId password maxUsers");
                    return;
                }
                String[] args = parts[1].split(" ");
                String roomId = args[0];
                String password = args[1].equals("null") ? null : args[1];
                int max = Integer.parseInt(args[2]);
                manager.createRoom(roomId, password, max);
                out.println("Room created: " + roomId);
            } catch (Exception e) {
                out.println("Error: " + e.getMessage());
            }
        }

        private void handleJoin(String[] parts) {
            try {
                if (parts.length < 2) {
                    out.println("Usage: JOIN roomId password");
                    return;
                }
                String[] args = parts[1].split(" ");
                String roomId = args[0];
                String password = args[1].equals("null") ? null : args[1];
                ChatRoom room = manager.getRoom(roomId);
                if (room == null) {
                    out.println("Room does not exist!");
                    return;
                }
                room.joinRoom(user, password);
                currentRoom = room;
                out.println("Joined room: " + roomId);
            } catch (Exception e) {
                out.println("Error: " + e.getMessage());
            }
        }

        private void handleRoomMsg(String[] parts) {
            if (currentRoom == null) {
                out.println("Join a room first!");
                return;
            }
            if (parts.length < 2) {
                out.println("Usage: ROOMMSG message");
                return;
            }
            currentRoom.sendMessage(user, parts[1]);
        }

        private void handlePrivMsg(String[] parts) {
            if (parts.length < 2) {
                out.println("Usage: PRIVMSG username message");
                return;
            }
            String[] args = parts[1].split(" ", 2);
            String target = args[0];
            String msg = args[1];

            boolean found = false;
            for (ChatRoom r : manager.getAllRooms().values()) {
                for (User u : r.getActiveUsers()) {
                    if (u.getUsername().equals(target)) {
                        PrivateChatManager.sendPrivate(user, u, msg);
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            if (!found) out.println("User not found!");
        }
    }
}
