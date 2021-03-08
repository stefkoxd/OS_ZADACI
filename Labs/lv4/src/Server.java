import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

    private int port;

    Server(int port){
        this.port = port;
    }

    public void execute() throws IOException {
        ServerSocket socket = new ServerSocket(this.port);
        Socket clientSocket;

        while (true){
            clientSocket = socket.accept();
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            System.out.println("Client said: " + inputStream.readUTF());

            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
            outputStream.writeUTF(new BufferedReader(new InputStreamReader(System.in)).readLine());

            outputStream.close();
            inputStream.close();
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(4444);
        server.execute();
    }
}
