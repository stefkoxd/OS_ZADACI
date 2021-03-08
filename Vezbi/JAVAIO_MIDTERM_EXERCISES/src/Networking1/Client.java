package Networking1;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Client extends Thread {

    private int numFiles;
    private String fileUrl;

    Client(){
        numFiles = 0;
        fileUrl = ".";
    }

    @Override
    public void run() {
        ls20KB(this.fileUrl);
        try {
            Socket socket = new Socket("localhost", 4498);

            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeInt(numFiles);

            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ls20KB(String url){

        File currentDirectory = new File(url);
        File []files = currentDirectory.listFiles();

        for (File file: files ){
            if ( file.length() < (20*1024) ){
                this.numFiles++;
            }
        }

        for ( File file: files ){
            if ( file.isDirectory() ){
                ls20KB(file.getAbsolutePath());
            }
        }

    }

}
