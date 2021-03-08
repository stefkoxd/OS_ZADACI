package Kolokviumska2;

import java.io.*;
import java.net.Socket;

public class ServerWorker extends Thread {
    private Socket socket;
    private Server server;

    public ServerWorker(Socket socket, Server server){
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            String length = inputStream.readUTF();
            String filesWithoutPermission = inputStream.readUTF();

            synchronized (ServerWorker.class) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File(server.getFileUrl()), true));
                writer.write("IP: " + socket.getInetAddress().getHostAddress() + "; Port: " + socket.getPort() + ":\n");
                writer.write("Length of files with permission: " + length + "KB\n" + "Number of files without permission: " + filesWithoutPermission);
                writer.newLine();
                writer.flush();
            }

            System.out.println("ServerWorker has finished writing the data.");

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
