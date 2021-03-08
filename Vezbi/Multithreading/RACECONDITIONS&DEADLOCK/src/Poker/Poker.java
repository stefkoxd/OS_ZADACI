package Poker;

import pkg.ProblemExecution;
import pkg.TemplateThread;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Poker {
    static PokerState state = new PokerState();

    private static Semaphore canSeat;
    private static int numPlayers;
    private static Semaphore numPlayersSemaphore;
    private static Semaphore canDeal;

    public static void init(){
        canSeat = new Semaphore(6);
        numPlayers = 0;
        numPlayersSemaphore = new Semaphore(1);
        canDeal = new Semaphore(0);
    }

    static class Player extends TemplateThread{
        public Player(int numRuns) {
            super(numRuns);
        }

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void execute() throws InterruptedException {
            canSeat.acquire();
            state.playerSeat();

            numPlayersSemaphore.acquire();
            numPlayers++;
            if(numPlayers == 6){
                state.dealCards();
                canDeal.release(6);
            }
            numPlayersSemaphore.release();
            canDeal.acquire();


            state.play();


            numPlayersSemaphore.acquire();
            numPlayers--;
            if ( numPlayers == 0 ){
                state.endRound();
                numPlayersSemaphore.release();
                canSeat.release(6);
            }
            else
                numPlayersSemaphore.release();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            run();
        }
    }

    public static void run() {
        try {
            int numRuns = 1;
            int numIterations = 1200;

            HashSet<Thread> threads = new HashSet<Thread>();

            for (int i = 0; i < numIterations; i++) {
                Player c = new Player(numRuns);
                threads.add(c);
            }

            init();

            ProblemExecution.start(threads, state);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
