package Basics_Reading_Writing;

import java.io.*;
import java.util.Arrays;

public class MainClass {

    public static void readBytes(String url) throws IOException {
        File file = new File(url);
        InputStream inputStream = new FileInputStream(file);
        int readByte = inputStream.read();
        while ( readByte!=-1 ){
            System.out.print("Read: " + readByte + "; ");
            readByte = inputStream.read();
        }
        inputStream.close();
    }

    public static void readBytesWithBuffer(String url) throws IOException {
        File file = new File(url);
        InputStream inputStream = new FileInputStream(file);

        byte []readingBuffer = new byte[5];
        inputStream.read(readingBuffer);

        System.out.println("First three characters are: " + Arrays.toString(readingBuffer));
        inputStream.close();
    }

    public static void readBytesCharOriented(String url) throws IOException {
        File file = new File(url);
        InputStream inputStream = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(inputStream);

        char []charBuffer = new char[5];

        //so inputstreamreader moze da se citaat characters preku inputstream

        reader.read(charBuffer);

        System.out.println(charBuffer);
        reader.close();
        inputStream.close();
    }

    public static void writeBytes(String url,byte[]bytesToWrite) throws IOException {
        File file = new File(url);
        OutputStream outputStream = new FileOutputStream(file);

        outputStream.write(bytesToWrite);
        outputStream.close();
    }

    public static void writeCharsOutputStream(String url, char []charsToWrite) throws IOException {
        OutputStream outputStream = new FileOutputStream(new File(url));
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);

        writer.write(charsToWrite);
        writer.close();
    }

    public static void readPrimitives(String url) throws IOException {
        DataInputStream inputStream = new DataInputStream(new FileInputStream(new File(url)));
        System.out.println(inputStream.readFloat());
        inputStream.close();
    }

    public static void writePrimitives(String url) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(new File(url)));
        /*dataOutputStream.writeUTF("Some string");
        dataOutputStream.writeInt(15);
        dataOutputStream.writeChar('c');
        dataOutputStream.writeFloat((float) 3.14);*/
        dataOutputStream.writeFloat(145.12f);
        //etc
        dataOutputStream.close();
    }

    public static void bufferedReading(String url) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(url)));
        String readLine = reader.readLine();
        StringBuilder stringBuilder = new StringBuilder();
        while (readLine!=null){
            stringBuilder.append(readLine);
            readLine = reader.readLine();
        }
        System.out.println(stringBuilder);
        reader.close();
    }

    public static void bufferedWriting(String url, String stringToWrite) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(url), true));
        writer.write(stringToWrite);
        writer.close();
    }

    public static void redirectSTDOUT(String url) throws FileNotFoundException {
        File file = new File(url);
        PrintStream printStream = new PrintStream(new FileOutputStream(new File(url)));
        System.setOut(printStream);
    }
    public static void readFromLargeFile(String url) throws IOException {
        File file = new File(url);
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

        randomAccessFile.seek(10);
        System.out.println(randomAccessFile.readLine());

        randomAccessFile.close();
    }

    public static void moveFile(String src, String destination){
        File currentFile = new File(src);
        File destinationFile = new File(destination);

        currentFile.renameTo(new File(destinationFile.getAbsolutePath() + "\\" + currentFile.getName()));
    }

    public static void main(String[] args) throws IOException {
        String url = "src\\Basics_Reading_Writing\\demoFile.txt";
        moveFile(url, "out");
    }

}
