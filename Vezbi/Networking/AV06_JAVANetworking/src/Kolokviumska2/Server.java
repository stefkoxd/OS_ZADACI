package Kolokviumska2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int port;
    private String fileUrl;

    Server(int port, String url) {
        this.port = port;
        this.fileUrl = url;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute() throws IOException {
        ServerSocket serverSocket = new ServerSocket(this.port);
        Socket socket;
        while(true){
            System.out.println("Server is waiting for connection...");
            socket = serverSocket.accept();
            System.out.println("Server has a connection established with a client...");
            ServerWorker worker = new ServerWorker(socket, this);
            worker.start();
        }
    }

}
