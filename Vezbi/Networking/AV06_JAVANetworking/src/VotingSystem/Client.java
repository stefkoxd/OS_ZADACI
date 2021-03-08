package VotingSystem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class Client extends Thread{

    private String clientId;
    private int port;
    private Random rand;

    public Client(int port,String clientId){
        this.port = port;
        rand = new Random();
        this.clientId = clientId;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", port);
            System.out.println("Connected to server!");

            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            String candidates = inputStream.readUTF();
            System.out.println("Reading candidates");

            System.out.print(candidates);

            int vote = rand.nextInt(4);
            DataOutputStream voteStream = new DataOutputStream(socket.getOutputStream());
            voteStream.writeInt(vote);
            System.out.println("Vote by client " + this.clientId + " has been entered!");

            inputStream.close();
            voteStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}