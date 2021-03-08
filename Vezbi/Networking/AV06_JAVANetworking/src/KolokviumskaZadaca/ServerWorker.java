package KolokviumskaZadaca;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
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
            long length = inputStream.readLong();
            long lastModification = inputStream.readLong();

            String result = socket.getInetAddress().getHostAddress() + " " + socket.getPort() + " " + length + " " + lastModification;
            BufferedWriter writer = new BufferedWriter(new FileWriter(server.getLocalFile(), true));

            synchronized (ServerWorker.class) {
                writer.write(result);
                writer.newLine();
            }

            writer.flush();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
