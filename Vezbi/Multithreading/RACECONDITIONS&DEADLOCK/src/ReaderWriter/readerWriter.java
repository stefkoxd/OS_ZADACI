package ReaderWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class readerWriter {
    private static Semaphore readers;

    static class Reader extends Thread{
        private String id;
        private File file;

        Reader(String id, File file){
            this.id = id;
            this.file = file;
        }

        @Override
        public void run(){
            System.out.println("Reader #" + this.id + " is trying to read!");
            try {
                readers.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                if ( reader.readLine() == null ) {
                    System.out.println("Reader #" + this.id + " has nothing to read!");
                    reader.close();
                    readers.release();
                    return;
                }else {
                    while (reader.readLine() != null) {
                        System.out.println("Reader #" + this.id + " just read: " + reader.readLine());
                    }
                    System.out.println("Reader #" + this.id + " has finished reading!");
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            readers.release();
        }

    }

    static class Writer extends Thread{
        private String id;
        private File file;
        private int numReaders;
        Writer(String id, File file, int numReaders){
            this.id = id;
            this.file = file;
            this.numReaders = numReaders;
        }
        @Override
        public void run(){

            System.out.println("Writer #" +this.id + " is trying to write");
            //ako brojot na permits kaj citacite e ednakov na nivniot broj toa znaci deka nikoj ne cita vo momentot
            while(readers.availablePermits()!=numReaders);

            readers.drainPermits();
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                    System.out.println("Writer #" + id + " is writing!");
                    writer.write("Writer #" + id + " wrote this!\n");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            readers.release(numReaders);
        }
    }

    public static void main(String []args) throws InterruptedException {
        readers = new Semaphore(10);
        File file = new File("D:\\FINKI MATERIJALI\\Cetvrt Semestar\\Operativni Sistemi\\Vezbi\\Multithreading\\RACECONDITIONS&DEADLOCK\\src\\ReaderWriter\\test.txt");
        List<Thread> threads = new ArrayList<>();

        for ( int i =  0; i < 10 ; i++ ){
            Reader reader = new Reader(Integer.toString(i), file);
            Writer writer = new Writer(Integer.toString(i), file, 10);
            threads.add(reader);
            threads.add(writer);
        }

        for (Thread thread: threads){
            thread.start();
        }

        for (Thread t: threads){
            t.join(1000);
        }

        for ( Thread t: threads ){
            if ( t.isAlive() ){
                System.err.println("NOT SYNCHRONIZED");
            }
        }

    }
}
