package Pusaci;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Main {


    private static int itemOnTable;
    private static boolean isTableEmpty;
    private static Semaphore smokers;
    private static Semaphore agent;
    private static Semaphore semaphore;
    private static Semaphore tableEmptySemaphore;

    public static void init(){
        isTableEmpty = true;
        agent = new Semaphore(0);
        smokers = new Semaphore(0);
        semaphore = new Semaphore(0);
        tableEmptySemaphore = new Semaphore(1);
    }

    public static class Table{

        public void placeItems(){
            System.out.println("Agent is placing items...");
        }


    }

    public static class Agent extends Thread{
        Table table;
        Agent(Table table){
            this.table = table;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    placeItemsOnTable();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void placeItemsOnTable() throws InterruptedException {
        }
    }

    public static class Smoker extends Thread{
        int item;

        Smoker(int item) {
            this.item = item;
        }

        @Override
        public void run() {
            while(true){
                try {
                    checkTable();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void checkTable() throws InterruptedException {

        }

        public void smoke(){
            System.out.println("Smoker: " + item + "...");
        }

    }

    public static void main(String[] args) throws InterruptedException {
        init();
        Table table = new Table();
        Agent agent = new Agent(table);
        HashSet<Thread> threads = new HashSet<>();
        for ( int i = 0 ; i < 3; i++ ){
            Smoker smoker = new Smoker(i);
            threads.add(smoker);
        }
        threads.add(agent);
        for ( Thread t: threads){
            t.start();
        }
        for ( Thread t: threads){
            t.join();
        }

    }
}
