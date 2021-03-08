package Tribe_dinner;

import pkg.ProblemExecution;
import pkg.TemplateThread;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class TribeDinnerSolution {
    private static Semaphore canSitOnTable;
    private static Semaphore checkPot;
    private static Semaphore wakeUpChef;
    private static Semaphore waitForFood;


    public static void init() {
        waitForFood = new Semaphore(0);
        wakeUpChef = new Semaphore(0);
        canSitOnTable = new Semaphore(4);
        checkPot = new Semaphore(1);
    }

    public static class TribeMember extends TemplateThread {

        public TribeMember(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            canSitOnTable.acquire();


            checkPot.acquire();
            if(state.isPotEmpty()) {
                wakeUpChef.release();
                waitForFood.acquire();
            }
            state.fillPlate();
            checkPot.release();

            state.eat();

            canSitOnTable.release();
        }

    }

    public static class Chef extends TemplateThread {

        public Chef(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            wakeUpChef.acquire();
            state.cook();
            waitForFood.release();
        }

    }

    static SeptemberTribeDinnerState state = new SeptemberTribeDinnerState();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            run();
        }
    }

    public static void run() {
        try {
            int numRuns = 1;
            int numIterations = 150;

            HashSet<Thread> threads = new HashSet<Thread>();

            for (int i = 0; i < numIterations; i++) {
                TribeMember h = new TribeMember(numRuns);
                threads.add(h);
            }

            Chef chef = new Chef(10);
            threads.add(chef);

            init();

            ProblemExecution.start(threads, state);
            System.out.println(new Date().getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
