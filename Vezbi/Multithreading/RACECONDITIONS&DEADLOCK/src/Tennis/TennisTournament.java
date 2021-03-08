package Tennis;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class TennisTournament {

    private static Semaphore greenPlayers;
    private static Semaphore redPlayers;
    private static int numPlayersOnField;
    private static Semaphore numPlayersSemaphore;
    private static Semaphore canPlay;


    public static void init(){
        greenPlayers = new Semaphore(2);
        redPlayers = new Semaphore(2);
        numPlayersOnField = 0;
        numPlayersSemaphore = new Semaphore(1);
        canPlay = new Semaphore(0);
    }

    public static class GreenPlayer extends Thread{

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {
            System.out.println("Green player ready");
            greenPlayers.acquire();
            System.out.println("Green player enters field");

            numPlayersSemaphore.acquire();
            numPlayersOnField++;
            if ( numPlayersOnField == 4 ) {
                canPlay.release(4);
            }
            numPlayersSemaphore.release();
            canPlay.acquire();


            System.out.println("Match started");
            Thread.sleep(200);
            System.out.println("Green player finished playing");


            numPlayersSemaphore.acquire();
            numPlayersOnField--;
            if ( numPlayersOnField == 0 ){
                // TODO: only one player calls the next line per match
                System.out.println("Match finished");
                numPlayersSemaphore.release();
                greenPlayers.release(2);
                redPlayers.release(2);
            }
            else
                numPlayersSemaphore.release();
        }

    }

    public static class RedPlayer extends Thread{

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {

            System.out.println("Red player ready");
            redPlayers.acquire();
            System.out.println("Red player enters field");

            numPlayersSemaphore.acquire();
            numPlayersOnField++;
            if ( numPlayersOnField == 4 ) {
                canPlay.release(4);
            }
            numPlayersSemaphore.release();
            canPlay.acquire();


            System.out.println("Match started");
            Thread.sleep(200);
            System.out.println("Red player finished playing");


            numPlayersSemaphore.acquire();
            numPlayersOnField--;
            if ( numPlayersOnField == 0 ){
                // TODO: only one player calls the next line per match
                System.out.println("Match finished");
                numPlayersSemaphore.release();
                greenPlayers.release(2);
                redPlayers.release(2);
            }
            else
                numPlayersSemaphore.release();
        }

    }


    public static void main(String[] args) throws InterruptedException {
        init();
        HashSet<Thread> threads = new HashSet<Thread>();
        for (int i = 0; i < 30; i++) {
            RedPlayer red = new RedPlayer();
            threads.add(red);
            GreenPlayer green = new GreenPlayer();
            threads.add(green);
        }
        // start 30 red and 30 green players in background
        for ( Thread thread : threads) {
            thread.start();
        }
        // after all of them are started, wait each of them to finish for 1_000 ms
        for ( Thread thread : threads) {
            thread.join(1000);
        }

        // after the waiting for each of the players is done, check the one that are not finished and terminate them
        for ( Thread thread: threads){
            if ( thread.isAlive() ){
                System.err.println("Possible deadlock");
                thread.interrupt();
            }
        }
    }

}
