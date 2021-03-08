package Chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    BufferedReader reader;

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(5555);
        while(true){
            Socket socket = serverSocket.accept();
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String readLine = reader.readLine();
                while (readLine != null) {
                    System.out.println(readLine);
                    readLine = reader.readLine();
                }
            }
            finally {
                reader.close();
            }
        }
    }

}
