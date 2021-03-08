package Kolokviumska2;

import java.io.*;
import java.net.Socket;

public class Client extends Thread{

    static class fileFilter implements FilenameFilter{
        @Override
        public boolean accept(File file, String s) {
            return s.endsWith(".docx") || s.endsWith(".xlsx");
        }
    }

    private int port;
    private String in, out;
    private float lengthOfFiles;
    private int noPermissionFiles;


    Client(int port, String in, String out){
        this.port = port;
        this.in = in;
        this.out = out;
        lengthOfFiles = 0;
        noPermissionFiles = 0;
    }

    @Override
    public void run() {
        try {
            executeClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeClient() throws IOException {

        Socket socket = new Socket("localhost", port);
        System.out.println("Client has connected");
        findRW(in, out);
        System.out.println("Client has finished with finding the files.");

        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.writeUTF("Length of files: " + lengthOfFiles);
        outputStream.writeUTF("Number of files without permission: " + noPermissionFiles);

        System.out.println("Client has sent the data.");

        outputStream.close();
    }

    public void findRW(String in, String out) throws IOException {
        File currentDirectory = new File(in);
        FilenameFilter filter = new fileFilter();
        File []files = currentDirectory.listFiles(filter);

        for ( File file: files){
            if ( file.canRead() && file.canWrite() ){
                File destinationDir = new File(out + "\\toDelete");
                if (!destinationDir.exists()){
                    destinationDir.mkdir();
                }
                file.renameTo(new File(destinationDir.getAbsolutePath() + "\\" + file.getName()));
                lengthOfFiles += (((float)file.length())/1024);
            }
            else{
                File destinationTxt = new File(out + "\\rw.txt");
                if ( !destinationTxt.exists() ){
                    destinationTxt.createNewFile();
                }
                BufferedWriter writer = new BufferedWriter(new FileWriter(destinationTxt, true));
                writer.write("Can't read/write " + file.getAbsolutePath());
                noPermissionFiles++;
            }
        }

        files = currentDirectory.listFiles();
        for ( File file: files ){
            if ( file.isDirectory() ){
                findRW(file.getAbsolutePath(), out);
            }
        }
    }


}
