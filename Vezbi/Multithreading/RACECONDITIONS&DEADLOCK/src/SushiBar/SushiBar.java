package SushiBar;

import pkg.ProblemExecution;
import pkg.TemplateThread;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class SushiBar {

    private static SushiBarState state = new SushiBarState();

    private static Semaphore freeSpace;
    private static Semaphore waitForFood;

    private static Semaphore customers;
    private static int numCustomers;



    public static void init(){
        freeSpace = new Semaphore(6);
        customers = new Semaphore(1);
        numCustomers = 0;
        waitForFood = new Semaphore(0);
    }

    static class Customer extends TemplateThread {
        public Customer(int numRuns) {
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
        public void execute() throws InterruptedException {
            freeSpace.acquire();

            state.customerSeat();

            customers.acquire();
            numCustomers++;
            if ( numCustomers == 6 ){
                state.callWaiter();
                waitForFood.release(6);
            }
            customers.release();
            waitForFood.acquire();

            state.customerEat();


            customers.acquire();
            numCustomers--;
            if ( numCustomers == 0 ){
                state.eatingDone();
                freeSpace.release(6);
            }
            customers.release();

        }


    }

    public static void main(String[] args) {
        init();
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
                Customer c = new Customer(numRuns);
                threads.add(c);
            }

            init();

            ProblemExecution.start(threads, state);
            // System.out.println(new Date().getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


