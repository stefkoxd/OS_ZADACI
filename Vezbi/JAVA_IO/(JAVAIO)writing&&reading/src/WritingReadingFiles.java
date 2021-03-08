import java.io.*;
import java.util.Arrays;

public class WritingReadingFiles {
    public static void main(String[] args) throws IOException {
        //readBytes("test.txt");
        //writeBytes("test.txt");

        //bufferedReading("test.txt");
        //bufferedWriting("test.txt");

        //randomAccessReading("test.txt");
        //randomAccessWriting("test.txt");

        //redirectSTDOUT("test.txt");
        //System.out.println("This should be redirected");
    }

    private static void redirectSTDOUT(String url) throws FileNotFoundException {
        System.setOut(new PrintStream(new FileOutputStream(new File(url))));
    }

    private static void randomAccessWriting(String url) throws IOException {
        File file = new File(url);
        RandomAccessFile writer = new RandomAccessFile(file, "rw");

        writer.seek(5);

        writer.write(65);

        writer.close();
    }

    private static void randomAccessReading(String url) throws IOException {
        File file = new File(url);

        RandomAccessFile reader = new RandomAccessFile(file, "r");

        /*SAME SHIT
        System.out.println("file.length(): " + file.length());
        System.out.println("reader.length: " + reader.length());*/


        byte []bytes = new byte[(int) reader.length()];
        while (reader.read()!=-1){
            reader.read(bytes);
        }
        System.out.println(Arrays.toString(bytes));
        reader.close();
    }

    private static void bufferedWriting(String url) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(url), true));
        System.out.println("Enter a string to write to the file: ");
        String stringToWrite = br.readLine();
        writer.append("\n" + stringToWrite);
        writer.close();
    }

    private static void bufferedReading(String url) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(url)));
        String read = "";
        while ((read = reader.readLine()) != null ){
            System.out.println(read);
        }
        reader.close();
    }

    private static void writeBytes(String url) throws IOException {
        OutputStream outputStream = new FileOutputStream(new File(url));
        try{
            int a = 75;
            outputStream.write(a);
        }
        finally {
            outputStream.close();
        }
    }

    private static void readBytes(String url) throws IOException {
        InputStream inputStream = new FileInputStream(new File(url));
        try{
            int readByte = inputStream.read();
            while ( readByte!=-1 ){
                System.out.println(readByte);
                readByte = inputStream.read();
            }
        }
        finally {
            inputStream.close();
        }
    }
}