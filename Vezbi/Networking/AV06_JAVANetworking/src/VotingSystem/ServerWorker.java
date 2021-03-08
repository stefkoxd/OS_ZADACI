package VotingSystem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class ServerWorker extends Thread {

    private Socket socket;
    private Server server;

    ServerWorker(Socket socket, Server server){
       this.socket = socket;
       this.server = server;
    }

    @Override
    public void run() {
        try {
            StringBuilder candidates = new StringBuilder();
            for (Candidate candidate: server.getHashMap().values()) {
                candidates.append("Name: ").append(candidate.getName()).append(", votes: ").append(candidate.getNumVotes()).append("\n");
            }

            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(candidates.toString());

            DataInputStream dataInputStreamReader = new DataInputStream(socket.getInputStream());
            int key = dataInputStreamReader.readInt();

            synchronized (Server.class){
                calculateVotePoints(server.getHashMap(), key);
            }

            outputStream.close();
            dataInputStreamReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        server.closeConnection();
    }

    private void calculateVotePoints(HashMap<Integer, Candidate> map, int key){
        Candidate candidate = map.get(key);
        candidate.incrementNumVotes();
        map.put(key, candidate);
    }

}
