package VotingSystem;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
            Server.maxClients.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        StringBuilder candidates = new StringBuilder();

        synchronized (ServerWorker.class){
            HashMap<Integer, Candidate> map = server.getCandidateMap();

            for(Map.Entry<Integer, Candidate> candidate : map.entrySet() ){
                candidates.append(candidate.getValue().getName() + " " + candidate.getValue().getNumVotes() + "\n");
            }

        }
        try {
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(String.valueOf(candidates));

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            int vote = inputStream.readInt();

            synchronized (ServerWorker.class){
                Candidate votedCandidate = server.getCandidateMap().get(vote);

                votedCandidate.incrementVotes();

                server.getCandidateMap().put(vote, votedCandidate);
            }

            synchronized (ServerWorker.class){
                BufferedWriter writer = new BufferedWriter(new FileWriter(server.getResultFile()));
                synchronized (ServerWorker.class){
                    candidates = new StringBuilder();
                    for(Map.Entry<Integer, Candidate> candidate : server.getCandidateMap().entrySet() ){
                        candidates.append(candidate.getValue().getName() + " " + candidate.getValue().getNumVotes() + "\n");
                    }
                }
                writer.write(candidates.toString());
                writer.close();
            }

            outputStream.close();
            inputStream.close();
            server.voteReceived();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
