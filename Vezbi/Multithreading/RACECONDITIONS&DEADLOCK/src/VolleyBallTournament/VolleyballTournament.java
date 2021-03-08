package VolleyBallTournament;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class VolleyballTournament {
    
    public static void main(String[] args) throws InterruptedException {
        init();
        HashSet<Player> threads = new HashSet<>();
        for (int i = 0; i < 60; i++) {
            Player p = new Player();
            threads.add(p);
        }
        // run all threads in background
        for ( Player player: threads ){
            player.start();
        }
        for ( Player player: threads ){
            player.join();
        }
        int []a = new int[5];


        // after all of them are started, wait each of them to finish for maximum 2_000 ms

        for( Player player: threads ){
            if ( player.isAlive() ){
                System.err.println("Possible deadlock!");
                player.interrupt();
                return;
            }
        }

        System.out.println("Tournament finished.");

    }

    private static Semaphore canEnter;
    private static Semaphore canEnterLockerRoom;
    private static int numPlayersInside;
    private static Semaphore numPlayersSemaphore;
    private static int dressedPlayers;
    private static Semaphore dressedPlayersSemaphore;
    private static Semaphore waitForEveryone;

    public static void init(){
        waitForEveryone = new Semaphore(0);
        dressedPlayers = 0;
        dressedPlayersSemaphore = new Semaphore(1);
        canEnterLockerRoom = new Semaphore(4);
        canEnter = new Semaphore(12);
        numPlayersSemaphore = new Semaphore(1);
        numPlayersInside = 0;
    }

    static class Player extends Thread{

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {
            // at most 12 players should print this in parallel
            canEnter.acquire();
            System.out.println("Player inside.");
            // at most 4 players may enter in the dressing room in parallel

            canEnterLockerRoom.acquire();
            System.out.println("In dressing room.");
            Thread.sleep(10);// this represent the dressing time

            dressedPlayersSemaphore.acquire();
            dressedPlayers++;
            if ( dressedPlayers == 4 ){
                dressedPlayers = 0;
                waitForEveryone.release(4);
                canEnterLockerRoom.release(4);
            }
            dressedPlayersSemaphore.release();
            waitForEveryone.acquire();

            // after all players are ready, they should start with the game together
            System.out.println("Game started.");
            Thread.sleep(100);// this represent the game duration
            System.out.println("Player done.");
            // only one player should print the next line, representing that the game has finished

            numPlayersSemaphore.acquire();
            numPlayersInside++;
            if( numPlayersInside == 12 ) {
                numPlayersInside = 0;
                canEnter.release(12);
                System.out.println("Game finished.");
            }
            numPlayersSemaphore.release();

        }
    }


}
