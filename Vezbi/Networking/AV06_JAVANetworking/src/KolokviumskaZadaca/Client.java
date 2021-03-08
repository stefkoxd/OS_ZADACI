package KolokviumskaZadaca;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

class fileFilter implements FilenameFilter {
    @Override
    public boolean accept(File file, String s) {
        return (s.endsWith(".txt") || s.endsWith(".dat")) && (file.length() < (512*1024));
    }
}

public class Client extends Thread{

    private ArrayList<String> files;
    private String currentDirectory;

    Client(String currentDirectory){
        this.currentDirectory = currentDirectory;
        this.files = new ArrayList<String>();
    }

    @Override
    public void run() {
        try {
            findTxtDatFiles(currentDirectory);
            fileTxtOutput("D:\\FINKI MATERIJALI\\Cetvrt Semestar\\Operativni Sistemi\\Vezbi\\Networking\\AV06_JAVANetworking\\src\\KolokviumskaZadaca\\files.csv");
            File csvFile = new File("files.csv");

            //ovie podatoci treba da se pratat do serverot
            long lastModified = csvFile.lastModified();
            long length = csvFile.length();
            Socket socket = new Socket("localhost", 3398);

            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeLong(length);
            outputStream.writeLong(lastModified);


            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findTxtDatFiles(String directoryPath) throws IOException {
        File currentDir = new File(directoryPath);
        FilenameFilter filter = new fileFilter();
        File []files = currentDir.listFiles(filter);

        assert files != null;
        for ( File file: files){
            this.files.add(file.getAbsolutePath() + "," + file.length());
        }

        files = currentDir.listFiles();

        assert files != null;
        for ( File file: files){
            if ( file.isDirectory() ){
                findTxtDatFiles(file.getAbsolutePath());
            }
        }

    }

    public void fileTxtOutput(String fileUrl) throws IOException {
        File file = new File(fileUrl);

        if ( !file.exists() ){
            file.createNewFile();
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file ,true));

        for ( String string: this.files ){
            writer.write(string);
            writer.newLine();
        }

        writer.close();
    }
}
