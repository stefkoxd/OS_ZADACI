package Gym;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Gym {
    private static Semaphore canEnter;
    private static Semaphore lockerRoom;
    private static int numPeople;
    private static Semaphore numPeopleSemaphore;

    public static void init(){
        canEnter = new Semaphore(12);
        lockerRoom = new Semaphore(4);
        numPeopleSemaphore = new Semaphore(1);
        numPeople = 12;
    }

    static class Player extends Thread{
        private String id;
        Player(String id){
            this.id = id;
        }
        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        public void execute() throws InterruptedException {
            canEnter.acquire();
            lockerRoom.acquire();
            lockerRoom();
            lockerRoom.release();
            System.out.println("Player " + id + " is out of the locker room.");
            gymTime();
            numPeopleSemaphore.acquire();
            numPeople--;
            if ( numPeople == 0 ){
                numPeople = 12;
                gymFree();
            }
            numPeopleSemaphore.release();
        }

        public void lockerRoom(){
            System.out.println("Player "+ id + " is in the locker room.");
        }
        public void gymTime(){
            System.out.println("Player " + id + " is working out.");
        }
        public void gymFree(){
            System.out.println("Last player is getting out.");
            canEnter.release(12);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        HashSet<Player> players = new HashSet<>();
        init();
        for (int j = 0; j < 1200; j++) {
            players.add(new Player(Integer.toString(j)));
        }
        for (Player p : players) {
            p.start();
        }
        for (Player p : players) {
            p.join();
        }


    }
}
