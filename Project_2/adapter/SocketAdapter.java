package adapter;

import java.io.PrintWriter;

public class SocketAdapter implements ClientAdapter {

    private PrintWriter writer;

    public SocketAdapter(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public void sendMessage(String msg) {
        writer.println(msg);
    }
}
