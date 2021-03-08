package VotingSystem;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class Server extends Thread{
    private int portNum;
    private HashMap<Integer, Candidate> candidateMap;
    private File resultFile;
    public static Semaphore maxClients = new Semaphore(100);


    Server(int portNum){
        this.portNum = portNum;
        candidateMap = new HashMap<>();
        candidateMap.put(0, new Candidate("NameOne - 0", 0));
        candidateMap.put(1, new Candidate("NameTwo - 1", 0));
        candidateMap.put(2, new Candidate("NameThree - 2", 0));
        candidateMap.put(3, new Candidate("NameFour - 3", 0));
        candidateMap.put(4, new Candidate("NameFive - 4", 0));
        resultFile = new File("result.txt");
    }

    public HashMap<Integer, Candidate> getCandidateMap() {
        return candidateMap;
    }

    public File getResultFile() {
        return resultFile;
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void voteReceived(){
        maxClients.release();
    }

    public void execute() throws IOException {
        ServerSocket serverSocket = new ServerSocket(this.portNum);
        Socket socket;
        while(true){
            socket = serverSocket.accept();
            ServerWorker worker = new ServerWorker(socket, this);
            worker.start();
        }
    }
}
