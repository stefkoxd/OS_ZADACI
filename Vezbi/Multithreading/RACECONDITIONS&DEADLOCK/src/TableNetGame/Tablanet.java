package TableNetGame;

import pkg.ProblemExecution;
import pkg.TemplateThread;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Tablanet {


    private static Semaphore canSeat;
    private static Semaphore canDeal;
    private static Semaphore areCardsDealt;
    private static Semaphore finishedPlaying;
    private static int totalPlayers;
    private static Semaphore totalPlayersSemaphore;
    private static Semaphore waitForOtherGroups;

    public static void init() {

        /*
        * if (totalPlayers == 20 ){
        *   state.endCycle();
        *   waitForOtherGroups.release(19);
        * }
        * else
        *   waitForOtherGroups.acquire();
        * */

        totalPlayers = 0;
        totalPlayersSemaphore = new Semaphore(1);
        waitForOtherGroups = new Semaphore(0);



        /*
        * finishedPlaying.release() at every player
        * finishedPlaying.acquire(4) at Dealer
        * state.nextGroup() at dealer after this
        * */

        finishedPlaying = new Semaphore(0);


        /*
        * areCardsDealt.acquire(1) at every player
        * areCardsDealt.release(4) at dealer
        * state.play() at every player after this
        * */

        areCardsDealt = new Semaphore(0);



        /*
        * canDeal.release(1) at every player
        * canDeal.acquire(4) at dealer
        * state.dealCards() at dealer after this
        * */

        canDeal =  new Semaphore(0);



        /*
        * canSeat.acquire() at every player, maximum 4 players can play at a time
        *
        * */

        canSeat = new Semaphore(4);
    }



    public static class Dealer extends TemplateThread {

        public Dealer(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            canDeal.acquire(4);

            state.dealCards();

            areCardsDealt.release(4);

            finishedPlaying.acquire(4);

            state.nextGroup();
            canSeat.release(4);
        }
    }

    public static class Player extends TemplateThread {

        public Player(int numRuns) {
            super(numRuns);
        }


        @Override
        public void execute() throws InterruptedException {
            canSeat.acquire();

            state.playerSeat();
            canDeal.release();

            areCardsDealt.acquire();

            state.play();
            finishedPlaying.release();

            totalPlayersSemaphore.acquire();
            totalPlayers++;
            if (totalPlayers == 20){
                totalPlayers = 0;
                state.endCycle();
                waitForOtherGroups.release(20);
            }
            totalPlayersSemaphore.release();
            waitForOtherGroups.acquire();
        }

    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            run();
        }
    }

    static TablanetState state = new TablanetState();

    public static void run() {
        try {
            int numCycles = 10;
            int numIterations = 20;

            HashSet<Thread> threads = new HashSet<Thread>();

            Dealer d = new Dealer(50);
            threads.add(d);
            for (int i = 0; i < numIterations; i++) {
                Player c = new Player(numCycles);
                threads.add(c);
            }

            init();

            ProblemExecution.start(threads, state);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
