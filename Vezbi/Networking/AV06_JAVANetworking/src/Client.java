import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread{
    String fileUrl;
    Socket socket;
    InetAddress address;

    Client(String fileUrl) throws UnknownHostException {
        this.fileUrl = fileUrl;
        this.address = InetAddress.getByName("localhost");
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, 4677);
            int numFiles = findFiles();
            DataOutputStream stream = new DataOutputStream(socket.getOutputStream());
            stream.writeInt(numFiles);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int findFiles(){
        File file = new File(fileUrl);
        File []files = file.listFiles();
        int numFiles = 0;

        assert files != null;
        for ( File f: files){
            if ( f.length() < (20*1000) ){
                numFiles++;
            }
        }

        return numFiles;
    }
}
