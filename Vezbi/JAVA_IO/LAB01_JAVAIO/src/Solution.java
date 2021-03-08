//zadaca 1
/*public static final String filePath = "D:\\Data\\OS\\tmp";
    public static void main( String[]args ) throws FileNotFoundException {
        File file = new File(filePath);
        if ( !file.isDirectory() ){
            throw new FileNotFoundException();
        }
        File []files = file.listFiles();
        assert files!=null;
        for ( File f : files ){
            if ( f.isFile() ){
                if ( f.length() > 50 ){
                    System.out.print(f.getName() + " ");
                }
            }
        }
    }*/

//zadaca 2
/*public static void main(String[] args) throws IOException {
        String url = "D:\\FINKI MATERIJALI\\Cetvrt Semestar\\Operativni Sistemi\\Vezbi\\JAVA_IO\\LAB01_JAVAIO";
        File currentDir = new File(url);
        makeNewTxt(url);

    }

    public static void makeNewTxt(String url) throws IOException {
        File currentDir = new File(url);
        String name = currentDir.getName();
        File txt = new File(currentDir.getAbsolutePath() + "\\" + name + ".txt");

        if ( !txt.exists() ){
            txt.createNewFile();
        }

        File []files = currentDir.listFiles();
        for ( File f : files ){
            if ( f.isDirectory() ){
                makeNewTxt(f.getAbsolutePath());
            }
        }
    }*/

//zadaca 3
/*public static void main(String[] args) throws IOException {
        String source = "izvor.txt";
        String destination = "destinacija.txt";
        reverseData(source,  destination);
    }
    public static void reverseData(String src, String dest) throws IOException {
        File source = new File(src);
        File destination = new File(dest);
        BufferedReader reader = new BufferedReader(new FileReader(source));
        BufferedWriter writer = new BufferedWriter(new FileWriter(destination));
        StringBuilder dataRead = new StringBuilder();
        try {
            String s = reader.readLine();
            while(true){
                dataRead.append(s);
                s = reader.readLine();
                if ( s == null ) break;
                else dataRead.append("\n");
            }
        }finally {
            reader.close();
            dataRead = dataRead.reverse();
        }
        try {
            writer.write(dataRead.toString());
        }finally {
            writer.close();
        }
    }*/

//zadaca 1
/*Пронајдете го најголемиот документ во директориумот чија патека се наоѓа во filePath.  Резултатот прикажете го на стандарден излез.
Користете ги готовите класи од библиотеката Java IO. Целосното решение (Java кодот) на задачата внесете го како одговор.
*/
/*    public static void findLargestFile(String filePath){
        File file = new File(filePath);

        File []files = file.listFiles();

        assert files != null;
        File maxFile = new File(files[0].getAbsolutePath());
        for ( File f : files ){
            if ( f.length() > maxFile.length() ){
                maxFile = f;
            }
        }
        System.out.println("Name: " + maxFile.getName() + ", size: " + maxFile.length());
    }*/



//zadaca 2
/*Напишете Java програма која рекурзивно ги изменува сите документи во даден именик и неговото поддрво,
и ги пребарува сите .jpg или .bmp документи кои се менувани последните 7 дена.
Патеката на директориумот ја прима како влезен аргумент од командна линија.
Апсолутната патека, на документот кој го задоволува условот, ја печати  на екран.
 */
/*
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class nameFilter implements FilenameFilter{
    @Override
    public boolean accept(File file, String s) {
        Date date = new Date(file.lastModified());
        Date currentDay = new Date();
        return (s.endsWith(".jpg") || s.endsWith(".bmp")) && ((currentDay.getDate()-date.getDate()) <= 7);
    }
}

public class Solution {

    public static void findJPGBMP(String filePath, List<File>filesList){
        File currentDirectory = new File(filePath);

        FilenameFilter filter = new nameFilter();
        File []files = currentDirectory.listFiles(filter);

        assert files != null;

        for ( File f : files ){
            filesList.add(f);
        }

        File []directories = currentDirectory.listFiles();
        assert directories != null;
        for ( File f: directories ){
            if ( f.isDirectory() ){
                findJPGBMP(f.getAbsolutePath(), filesList);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String url = br.readLine();
        List<File> filesList = new ArrayList<>();

        findJPGBMP(url,filesList);

        for ( int i = 0 ; i < filesList.size(); i++ ){
            System.out.println(filesList.get(i).getAbsolutePath());
        }
    }
}
*/


//zadaca 6
/* Да се напише Java програма која со користење на I/O стримови ќе ја прочита содржината на датотеката izvor.txt,
линија по линија, а потоа за секоја линија ќе го запише бројот на повторувања на самогласки во истата.
Резултатот го запишува во празната датотека destinacija.txt.
 */
/*
import java.io.*;

public class Solution{

    public static boolean isVowel(char character){
        character = Character.toLowerCase(character);
        return character == 'a' || character == 'e' || character == 'i' || character == 'o' || character == 'u';
    }

    public static void readVowels() throws IOException {
        File fileToRead = new File("izvor.txt");
        File fileToWrite = new File("destinacija.txt");

        BufferedReader reader = new BufferedReader(new FileReader(fileToRead));
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWrite, true));

        String line = reader.readLine();
        int numVowels = 0;
        while ( line != null ){
            char []characters = line.toCharArray();
            for ( int i = 0 ; i < characters.length; i++ ){
                if ( isVowel(characters[i]) ) numVowels++;
            }
            writer.write((numVowels) + "\n");
            numVowels = 0;
            line = reader.readLine();
        }

        reader.close();
        writer.close();
    }

    public static void main(String []args) throws IOException {
        readVowels();
    }
}
*/





//READING AND WRITING FUNCTIONS
/*
import java.io.*;

public class Solution{

    public static void readBytes(String url) throws IOException {

        InputStream inputStream = new FileInputStream(new File(url));

        int readByte = inputStream.read();

        while (readByte!=-1){
            System.out.println(readByte);
            readByte = inputStream.read();
        }

        inputStream.close();

    }

    public static void writeBytes(String url) throws IOException {

        OutputStream outputStream = new FileOutputStream(new File(url));

        outputStream.write(100);


        outputStream.close();
    }


    public static void readPrimitives(String url) throws IOException {
        DataInputStream inputStream = new DataInputStream(new FileInputStream(new File(url)));

        System.out.println(inputStream.readInt());

        inputStream.close();
    }


    public static void bufferedReading(String url) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(url)));

        String readLine = reader.readLine();
        while ( readLine!=null ){
            System.out.println(readLine);
            readLine = reader.readLine();
        }

        reader.close();
    }

    public static void bufferedWriting(String url) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(url), true));

        writer.write("Random");

        writer.close();
    }


    public static void main(String[] args) throws IOException {

    }
}*/