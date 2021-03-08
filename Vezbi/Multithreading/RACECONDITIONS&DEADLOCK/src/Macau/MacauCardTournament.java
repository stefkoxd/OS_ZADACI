package Macau;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.Semaphore;


public class MacauCardTournament {
    
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
        for ( Thread thread: threads){
            thread.start();
        }
        // after all of them are started, wait each of them to finish for 1_000 ms
        for ( Thread thread: threads){
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

    private static int num;
    private static Semaphore greenPlayer;
    private static Semaphore redPlayer;
    private static Semaphore waitForRed;
    private static Semaphore waitForGreen;
    private static int totalMatchesPlayed;
    private static Semaphore totalMatchesPlayedSemaphore;
    private static int lastPlayer;
    private static Semaphore lastPlayerSemaphore;
    private static Random random = new Random();
    private static int playerCounter;

    public static void init(){
        playerCounter = 0;
        lastPlayer = random.nextInt(5);
        lastPlayerSemaphore = new Semaphore(1);
        totalMatchesPlayed = 0;
        totalMatchesPlayedSemaphore = new Semaphore(1);
        greenPlayer = new Semaphore(2);
        redPlayer = new Semaphore(2);
        num = 1;
        waitForRed = new Semaphore(0);
        waitForGreen = new Semaphore(0);
    }

    static class GreenPlayer extends Thread {


        public void execute() throws InterruptedException {
            System.out.println("Green player ready");
            Thread.sleep(50);

            greenPlayer.acquire();
            System.out.println("Green player here");

            waitForGreen.release();
            waitForRed.acquire();


            // TODO: the following code should be executed 3 times
            totalMatchesPlayedSemaphore.acquire();
            totalMatchesPlayed++;
            if ( totalMatchesPlayed%10 == 0 )
                System.out.println("Game "+ num +" started");
            totalMatchesPlayedSemaphore.release();
            Thread.sleep(200);
            System.out.println("Green player finished game "+ num);


            // TODO: only one player calls the next line per game
            lastPlayerSemaphore.acquire();
            if ( playerCounter == lastPlayer ){
                playerCounter = 0;
                System.out.println("Game "+ num +" finished");
            }
            playerCounter++;
            lastPlayerSemaphore.release();

            // TODO: only one player calls the next line per match
            totalMatchesPlayedSemaphore.acquire();
            if ( totalMatchesPlayed%10 == 0 ) {
                num++;
                System.out.println("Match finished");
            }
            totalMatchesPlayedSemaphore.release();
            greenPlayer.release();
        }


        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class RedPlayer extends Thread{

        public void execute() throws InterruptedException {
            System.out.println("Red player ready");
            Thread.sleep(50);

            redPlayer.acquire();
            System.out.println("Red player here");

            waitForRed.release();
            waitForGreen.acquire();

            // TODO: the following code should be executed 3 times
            totalMatchesPlayedSemaphore.acquire();
            totalMatchesPlayed++;
            if ( totalMatchesPlayed%30 == 0 )
                System.out.println("Game "+ num +" started");
            totalMatchesPlayedSemaphore.release();
            Thread.sleep(200);
            System.out.println("Green player finished game "+ num);


            // TODO: only one player calls the next line per game
            lastPlayerSemaphore.acquire();
            if ( playerCounter == lastPlayer ){
                playerCounter = 0;
                System.out.println("Game "+ num +" finished");
            }
            else
                playerCounter++;
            lastPlayerSemaphore.release();

            // TODO: only one player calls the next line per match
            totalMatchesPlayedSemaphore.acquire();
            if ( totalMatchesPlayed%30 == 0 ) {
                num++;
                System.out.println("Match finished");
            }
            totalMatchesPlayedSemaphore.release();
            redPlayer.release();
        }

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}