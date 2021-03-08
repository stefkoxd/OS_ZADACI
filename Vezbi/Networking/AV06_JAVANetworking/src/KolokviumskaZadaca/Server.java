package KolokviumskaZadaca;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private ServerSocket serverSocket;
    private File localFile;

    public File getLocalFile() {
        return localFile;
    }

    Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        localFile = new File("D:\\FINKI MATERIJALI\\Cetvrt Semestar\\Operativni Sistemi\\Vezbi\\Networking\\AV06_JAVANetworking\\src\\KolokviumskaZadaca\\_data.txt");
        if ( !localFile.exists()) {
            localFile.createNewFile();
        }
    }

    @Override
    public void run() {
        Socket socket = new Socket();
        while(true){
            try {
                //waiting for a connection
                System.out.println("Listening...");
                socket = serverSocket.accept();
                System.out.println("Got a connection");
                //connection established, initiate comms using socket
                ServerWorker serverWorker = new ServerWorker(socket, this);
                //start working
                serverWorker.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
