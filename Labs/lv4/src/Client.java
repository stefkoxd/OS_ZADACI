import java.io.*;
import java.net.Socket;

public class Client{
    public int port;

    Client(int port){
        this.port = port;
    }

    public void execute() throws IOException, InterruptedException {
        while(true) {
            Socket communicationSocket = new Socket("localhost", this.port);

            DataOutputStream outputStream = new DataOutputStream(communicationSocket.getOutputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String message = reader.readLine();

            if ( message.equals("q") ) {
                reader.close();
                outputStream.close();
                break;
            }

            outputStream.writeUTF(message);

            DataInputStream inputStream = new DataInputStream(communicationSocket.getInputStream());
            System.out.println(inputStream.readUTF());
            inputStream.close();
            outputStream.close();
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Client client = new Client(4444);
        client.execute();
    }

}
