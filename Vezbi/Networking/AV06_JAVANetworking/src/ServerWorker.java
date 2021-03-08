import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

public class ServerWorker extends Thread {
    private Socket socket;
    private BufferedWriter writer;
    private String fileUrl;
    private DataInputStream inputStream;

    ServerWorker(Socket socket, String fileUrl) throws IOException {
        this.socket = socket;
        this.fileUrl = fileUrl;
        writer = new BufferedWriter(new FileWriter(fileUrl, true));
    }

    @Override
    public void run() {
        try {
            this.inputStream = new DataInputStream(socket.getInputStream());
            int numFiles = inputStream.readInt();
            synchronized (ServerWorker.class) {
                writer.write(String.format("%s %d %d", socket.getInetAddress().getHostAddress(), socket.getPort(), numFiles));
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
