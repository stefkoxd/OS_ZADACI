package MoveWritableFiles;

import java.io.*;
import java.util.List;

public class Demo {

    static class fileFilter implements FilenameFilter{
        @Override
        public boolean accept(File file, String s) {
            return s.endsWith(".txt") && file.canWrite();
        }
    }

    public static void moveWritableFiles( String from, String to ){
        File currentDirectory = new File(from);
        if ( !currentDirectory.exists() ) {
            System.out.println("Does not exist.");
        }
        else{
            File destDir = new File(to);
            if ( !destDir.exists() ) {
                destDir.mkdir();
            }
            FilenameFilter filter = new fileFilter();
            File []files = currentDirectory.listFiles(filter);
            assert files != null;
            for ( File file: files ){
                file.renameTo(new File(destDir.getAbsolutePath() + "\\" + file.getName()));
            }
            files = currentDirectory.listFiles();
            assert files != null;
            for ( File file : files ){
                if ( file.isDirectory() ){
                    moveWritableFiles(file.getAbsolutePath(), to);
                }
            }
        }
    }


    public static void deserializeData(String source, List<byte[]> data, long elementLength) throws IOException {
        File file = new File(source);
        byte[] readBytes = new byte[(int) elementLength];
        InputStream inputStream = new FileInputStream(file);
        byte readByte = (byte) inputStream.read();
        int i = 0;
        while (readByte!=-1){
            readBytes[i] = readByte;
            if( i == elementLength-1 ){
                data.add(readBytes);
                readBytes = new byte[(int) elementLength];
                i = 0;
            }
            readByte = (byte) inputStream.read();
        }
        inputStream.close();
    }

    public static void deserializeDataRAF(String source, List<byte[]> data, long elementLength) throws IOException {
        RandomAccessFile file = new RandomAccessFile(source, "r");
        byte[] bytes = new byte[(int)elementLength];
        int counter = 0;

        while (counter<file.length()){
            file.seek(counter);
            file.read(bytes);
            data.add(bytes);
            counter+=elementLength;
        }

        file.close();
    }

    public static void invertLargeFile(String source, String destination) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(source, "r");
        RandomAccessFile destinationFile = new RandomAccessFile(destination, "rw");

        for (int i = ((int) raf.length())-1; i >= 0 ; i-- ){
            raf.seek(i);
            char readChar = (char)raf.read();
            destinationFile.writeChars(String.valueOf(readChar));
        }
        raf.close();
        destinationFile.close();
    }


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        invertLargeFile(br.readLine(), br.readLine());
    }
}
