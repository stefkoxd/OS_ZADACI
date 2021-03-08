package VotingSystem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class Client extends Thread {

    private Random randomNumber;

    Client(){
        randomNumber = new Random();
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", 5555);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());

            String readCandidates = inputStream.readUTF();

            int vote = randomNumber.nextInt(5);

            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeInt(vote);

            inputStream.close();
            outputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
