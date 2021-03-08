package Networking1;

import java.io.*;
import java.net.Socket;

public class ServerWorker extends Thread{

    private Socket socket;
    private Server server;
    ServerWorker(Socket socket, Server server){
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            int readNumFiles = inputStream.readInt();
            String data = socket.getInetAddress().getHostAddress() + " " + socket.getPort() + " " + readNumFiles + "\n";

            synchronized ( ServerWorker.class ){
                BufferedWriter writer = new BufferedWriter(new FileWriter(server.getFile(), true));
                writer.write(data);
                writer.close();
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
