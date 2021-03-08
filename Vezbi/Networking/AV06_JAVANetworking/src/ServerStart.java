import java.io.IOException;

public class ServerStart{
    public static void main(String[] args) throws IOException {
        String url = "result.txt";
        Server server = new Server(url);
        server.start();
    }
}