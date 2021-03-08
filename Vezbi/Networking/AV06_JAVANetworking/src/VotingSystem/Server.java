package VotingSystem;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Server{
    private ServerSocket serverSocket;
    private HashMap<Integer, Candidate> hashMap;
    private static Semaphore maxConnections = new Semaphore(100);
    public HashMap<Integer, Candidate> getHashMap() {
        return hashMap;
    }

    Server(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
        hashMap = new HashMap<Integer, Candidate>();
        hashMap.put(0, new Candidate("NameOne - 0"));
        hashMap.put(1, new Candidate("NameTwo - 1"));
        hashMap.put(2, new Candidate("NameThree - 2"));
        hashMap.put(3, new Candidate("NameFour - 3"));
    }

    public void closeConnection(){
        maxConnections.release();
    }

    public void run() {
        Socket socket;
        while(true){
            try {
                maxConnections.acquire();
                socket = serverSocket.accept();
                ServerWorker worker = new ServerWorker(socket, this);
                worker.start();

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}