import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private String fileUrl;

    Server(String fileUrl){
        this.fileUrl = fileUrl;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(4677);
        Socket communicationChannel;
        while(true){
            System.out.println("Listening...");
            communicationChannel = serverSocket.accept();
            System.out.println("Got connection!");
            ServerWorker worker = new ServerWorker(communicationChannel, fileUrl);
            worker.start();
        }
    }
}


