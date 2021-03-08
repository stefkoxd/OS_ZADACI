import java.io.*;
import java.util.List;

class textFilter implements FilenameFilter{

    @Override
    public boolean accept(File file, String s) {
        return s.endsWith(".docx") || s.endsWith(".xlsx");
    }
}

class Filter implements FileFilter{

    @Override
    public boolean accept(File file) {
        return file.getName().endsWith(".txt") && file.canWrite();
    }

}


public class mainClass {
    public static void main(String[] args) throws IOException {
        //moving a file from one place to another
        /*File fileToMove = new File("document.docx");
        File destination = new File("out\\" + fileToMove.getName());
        fileToMove.renameTo(destination);*/

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        /*String from = br.readLine();
        String to = br.readLine();
        findRW(from,to);*/


        /*====DEL 1====*/
        /*String from = br.readLine();
        String to = br.readLine();
        moveWritableTxtFiles(from,to);
        */

        /*====DEL 2====*/
        /*String source = br.readLine();
        long elementLength = Long.parseLong(br.readLine());
        List <byte []> data = new ArrayList<>();
        deserializeData(source,data,elementLength);
        *//*============TEST EXAMPLES============*/
        /*
        System.out.println("First implementation: ");
        for ( byte[] d: data ){
            System.out.println(Arrays.toString(d));
        }
        data = new ArrayList<>();
        deserializeDataII(source,data,elementLength);
        System.out.println("Second implementation: ");
        for ( byte[] d: data ){
            System.out.println(Arrays.toString(d));
        }*/

        /*====DEL 3====*/
        /*String src = br.readLine();
        String dest = br.readLine();
        invertLargeFile(src,dest);*/
    }


    /*====DEL 3====*/
    public static void invertLargeFile(String source, String destination) throws IOException {
        File src = new File(source);
        File dest = new File(destination);
        RandomAccessFile read = new RandomAccessFile(src, "r");
        RandomAccessFile write = new RandomAccessFile(dest, "rw");
        for (int i = (int) (src.length()-1) ; i >= 0 ; i-- ){
            read.seek(i);
            write.write(read.read());
        }
        read.close();
        write.close();
    }

    /*====DEL 2====*/
    public static void deserializeData(String source, List<byte[]> data, long elementLength) throws IOException {
        File file = new File(source);
        if ( !file.exists() ) throw new FileNotFoundException("Doesn't exist");
        InputStream inputStream = new FileInputStream(file);
        int read = inputStream.read();
        byte[] bytes = new byte[(int) elementLength];
        int i = 0;
        while(read != -1) {
            if ( i == elementLength ){
                i = 0;
                data.add(bytes);
                bytes = new byte[(int) elementLength];
            }
            else {
                bytes[i] = (byte) read;
                i++;
                read = inputStream.read();
            }
        }
        data.add(bytes);
        inputStream.close();
    }

    /*====DEL 2.1(Implementacija so RAF, istiot rezultat se dobiva)====*/
    public static void deserializeDataII(String source, List<byte[]> data, long elementLength)throws IOException{
        RandomAccessFile reader = new RandomAccessFile(new File(source), "r");
        int count = 0;
        byte []bytes = new byte[(int)elementLength];
        while(reader.read() != -1 ){
            //se postavuva na pozicija od koja ke cita bajti
            reader.seek(count);

            //se citaat prvite elementLength bajti i se smestuvaat vo nizata bytes
            reader.read(bytes);

            //nizata se dodava vo listata od bytes
            data.add(bytes);

            //nizata se kreira od pocetok
            bytes = new byte[(int) elementLength];

            count+=elementLength;
        }
        reader.close();
    }


    /*====DEL 1====*/
    public static void moveWritableTxtFiles(String from, String to) throws FileNotFoundException {
        File src = new File(from);
        if ( !src.exists() ) throw new FileNotFoundException("Does not exist");
        else{
            File destination = new File(to);
            if ( !destination.exists() ) destination.mkdir();
            FileFilter filter = new Filter();
            File []filesInCurrentDir = src.listFiles(filter);
            assert filesInCurrentDir != null;
            for ( File f: filesInCurrentDir ){
                System.out.println(f);
                File dest = new File(to + "\\" + f.getName());
                f.renameTo(dest);
            }
        }
    }



    public static void findRW(String in, String out) throws IOException {
        File currentDir = new File(in);
        File dirTo = new File(out);

        FilenameFilter filter = new textFilter();
        File []filteredFiles = currentDir.listFiles(filter);

        assert filteredFiles!=null;

        for ( File f : filteredFiles ){
            if ( f.getName().endsWith(".docx") ){
                File newDir = new File(dirTo.getAbsolutePath() + "\\brisenje");
                if ( !newDir.exists() ){
                    newDir.mkdir();
                }
                File destination = new File(newDir + "\\" + f.getName());
                f.renameTo(destination);
                File txtFile = new File(dirTo.getAbsolutePath() + "\\files.txt");
                if ( !txtFile.exists() ){
                    txtFile.createNewFile();
                }
                String stringToWrite = "Name: " + f.getName() + "\nPath: " + f.getAbsolutePath();
                writeToFile(txtFile, stringToWrite);
            }
            else{
                File txtFile = new File(dirTo.getAbsolutePath() + "\\rw.txt");
                if ( !txtFile.exists() ){
                    txtFile.createNewFile();
                }
                String stringToWrite = "Name: " + f.getName() + "\nPath: " + f.getAbsolutePath();
                writeToFile(txtFile, stringToWrite);
            }
        }

        File []allFiles = currentDir.listFiles();
        assert allFiles!=null;
        for ( File f : allFiles ){
            if ( f.isDirectory() ){
                findRW(f.getAbsolutePath(), out);
            }
        }
    }

    public static void writeToFile(File file, String data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        try{
            writer.write(data+"\n");
        }finally {
            writer.close();
        }
    }

    //IO FUNCTIONS (Reading and writing from/to files)

    public static void IO() throws IOException {
        File file = new File("test.txt");
        if ( !file.exists() ){
            file.createNewFile();
        }
        else {
            //IO using input/output streams; writing/reading byte by byte
            String url = file.getAbsolutePath();
            //reading byte by byte (InputStreamReader)
            // readBytes(url);

            //writing byte by byte(OutputStreamWriter)
            // writeBytes(url);

            //reading primitive types(DataInputStream)
            //readPrimitives(url);

            //writing primitives(DataOutputStream)
            // writePrimitives(url);

            //Buffered reading/writing (really useful for writing entire strings in files!)

            //buffered reading (citanje na cel string)
            //readBuffered(url);

            //buffered writing
            //writeBuffered(url);

            //appending a string to an already existing data in a file
            //appendBuffered(url);

            //Standard input/output, STDOUT moze da se manipulira da pisuva podatoci vo file a ne na ekran!
            //redirectSTDOutput(url);
            //after this function every time sout is called the result will be printed in the file which path is url, like the code below!
            //System.out.println("Hello world!");

            //reading from Standard input
            //readFromSTDIn();

            //RANDOM ACCESS FILE(Really useful when working with gigantic files!)
            //randomAccess(url);
        }
    }

    public static void readBytes(String url) throws IOException {
        File toRead = new File(url);
        InputStream inputStream = new FileInputStream(toRead);
        StringBuilder byteArray = new StringBuilder();
        try{
            int readByte = inputStream.read();
            while ( readByte!=-1 ){
                byteArray.append(readByte).append(" ");
                readByte = inputStream.read();
            }
        }finally {
            System.out.println(byteArray);
            inputStream.close();
        }
    }

    public static void writeBytes(String url) throws IOException {
        File file = new File(url);
        OutputStream writer = new FileOutputStream(file);
        try{
            writer.write(65);
        }
        finally {
            writer.close();
        }
    }

    public static void readPrimitives(String url) throws IOException {
        File file = new File(url);
        DataInputStream inputStream = new DataInputStream(new FileInputStream(file));
        //ima : readInt(), readDouble(), readFloat(), readChar() etc..
        System.out.println(inputStream.readInt());
        inputStream.close();
    }

    public static void writePrimitives(String url) throws IOException {
        File file = new File(url);
        DataOutputStream writer = new DataOutputStream(new FileOutputStream(file));
        //Ako se napise 3.14 ke se dobijat cudni znaci bidejki notepad koristi ASCII, a toa writeFloat()/double etc koristi UTF-8, ama ako se zapise int ke bide okej
        writer.writeInt(25);
        writer.close();
    }

    public static void readBuffered(String url) throws IOException{
        File file = new File(url);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null ){
            System.out.println(line);
        }
        reader.close();
    }

    public static void writeBuffered(String url) throws IOException{
        //Gi prebrisuva site podatoci vo file-ot i gi zamenuva so toa sto e navedeno podolu,
        //postoi nacin na koj se pravi append na
        // vekje postoeckata sodrzina bez istata da se prebrise (apppendBuffered)
        File file = new File(url);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write("This is yet another string.");
        bufferedWriter.close();
    }

    public static void appendBuffered(String url) throws IOException{
        File file = new File(url);
        //vo konstruktorot za FileWriter pokraj samiot file ima arg za dali e appendable, t.e dali ke se dodava sodrzina vo file-ot
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
        writer.write("\nBruh");
        writer.close();
    }

    public static void redirectSTDOutput(String url)throws IOException{
        File file = new File(url);
        PrintStream output = new PrintStream(new FileOutputStream(file));
        System.setOut(output);
    }

    public static void readFromSTDIn() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        System.out.println("Read data: " + s);
        br.close();
    }

    public static void randomAccess(String url)throws IOException{
        File file = new File(url);
        File file2 = new File("D:\\FINKI MATERIJALI\\Cetvrt Semestar\\Operativni Sistemi\\Vezbi\\JAVA_IO\\File_System_Vezbi\\file-rv.txt");
        //so ovaa f-ja ke se ispise sodrzinata od fileRead vo fileWrite so toa sto sodrzinata ke bide reversed
        RandomAccessFile fileRead = new RandomAccessFile(file, "r");
        RandomAccessFile fileWrite = new RandomAccessFile(file2, "rw");
        //.seek() e f-ja so koja moze da se pozicionirame na bilo koe mesto vo file-ot, prima integer kako argument
        //i toj integer e pozicijata na koja sakame da se naogjame
        for (int i = (int) (file.length()-1); i >= 0 ; i-- ){
            fileRead.seek(i);
            fileWrite.write(fileRead.read());
        }
        fileRead.close();
        fileWrite.close();
    }

}
