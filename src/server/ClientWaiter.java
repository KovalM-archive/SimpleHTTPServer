package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientWaiter implements Runnable {
    private Socket socket;
    private Thread thread;

    public ClientWaiter() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        ServerWorkPanel serverWorkPanel = new ServerWorkPanel();
        serverWorkPanel.getTextArea().append("Server start work\n\n");
        try {
            ServerSocket serverSocket = new ServerSocket(8070);
            while (true){
                socket = serverSocket.accept();
                new SimpleHTTPServer(socket, serverWorkPanel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
