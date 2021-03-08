package Networking1;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private int numPort;
    private File file;

    Server(int numPort){
        this.numPort = numPort;
        this.file = new File("result.txt");
    }

    public File getFile() {
        return file;
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
        ServerSocket serverSocket = new ServerSocket(this.numPort);
        Socket socket;
        while (true){
            System.out.println("Waiting for connection");
            socket = serverSocket.accept();
            System.out.println("Got connection");
            ServerWorker worker = new ServerWorker(socket, this);
            worker.start();
        }
    }
}
