package main;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner sc;

    public ChatClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        sc = new Scanner(System.in);

        // Thread to listen to server messages
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    System.out.println(msg);
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server.");
            }
        }).start();

        startClient();
    }

    private void startClient() {
        try {
            // Get username
            System.out.print("Enter username: ");
            String username = sc.nextLine();
            out.println(username);

            while (true) {
                printMenu();
                String option = sc.nextLine().trim();

                switch (option) {
                    case "1": // Create Room
                        System.out.print("Enter Room ID: ");
                        String roomId = sc.nextLine();
                        System.out.print("Enter Password (leave blank for none): ");
                        String password = sc.nextLine();
                        if (password.isEmpty()) password = "null";
                        System.out.print("Enter Max Users (0 = no limit): ");
                        String maxUsers = sc.nextLine();
                        if (maxUsers.isEmpty()) maxUsers = "0";
                        out.println("CREATE " + roomId + " " + password + " " + maxUsers);
                        break;

                    case "2": // Join Room
                        System.out.print("Enter Room ID: ");
                        String joinId = sc.nextLine();
                        System.out.print("Enter Password (leave blank for none): ");
                        String joinPw = sc.nextLine();
                        if (joinPw.isEmpty()) joinPw = "null";
                        out.println("JOIN " + joinId + " " + joinPw);
                        break;

                    case "3": // Room Message
                        System.out.print("Enter message: ");
                        String roomMsg = sc.nextLine();
                        out.println("ROOMMSG " + roomMsg);
                        break;

                    case "4": // Private Message
                        System.out.print("Enter username to message: ");
                        String target = sc.nextLine();
                        System.out.print("Enter message: ");
                        String privMsg = sc.nextLine();
                        out.println("PRIVMSG " + target + " " + privMsg);
                        break;

                    case "5": // List Rooms
                        out.println("LISTROOMS");
                        break;

                    case "6": // Show active users in room
                        System.out.print("Enter Room ID: ");
                        String rId = sc.nextLine();
                        out.println("ACTIVEUSERS " + rId);
                        break;

                    case "7": // Exit
                        out.println("EXIT");
                        socket.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid option, try again!");
                }
            }

        } catch (IOException e) {
            System.out.println("Connection lost: " + e.getMessage());
        }
    }

    private void printMenu() {
        System.out.println("\n=== Menu ===");
        System.out.println("1. Create Room");
        System.out.println("2. Join Room");
        System.out.println("3. Send Room Message");
        System.out.println("4. Send Private Message");
        System.out.println("5. List Rooms");
        System.out.println("6. Show Active Users in Room");
        System.out.println("7. Exit");
        System.out.print("Select option (1-7): ");
    }

    public static void main(String[] args) throws IOException {
        new ChatClient("localhost", 12345);
    }
}
