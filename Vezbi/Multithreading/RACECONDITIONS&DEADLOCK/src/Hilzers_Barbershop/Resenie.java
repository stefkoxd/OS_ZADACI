package Hilzers_Barbershop;

import pkg.ProblemExecution;
import pkg.TemplateThread;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Resenie {
    static State state;

    // TODO: Definicija na globalni promenlivi i semafori
    private static Semaphore canEnterBarbershop;
    private static Semaphore canSitOnSofa;
    private static Semaphore freeBarber;
    private static Semaphore cutHairSemaphore;
    private static Semaphore acceptPaymentSemaphore;
    private static Semaphore canExit;

    public static void init(int numBarbers) {
        canEnterBarbershop = new Semaphore(20);
        canSitOnSofa = new Semaphore(4);
        freeBarber = new Semaphore(5);
        cutHairSemaphore = new Semaphore(0);
        acceptPaymentSemaphore = new Semaphore(0);
        canExit = new Semaphore(0);
    }

    public static class Barber extends TemplateThread {
        public int barberId;

        public Barber(int numRuns, int id) {
            super(numRuns);
            barberId = id;
        }

        @Override
        public void execute() throws InterruptedException {
            cutHairSemaphore.acquire();
            state.cutHair();

            acceptPaymentSemaphore.acquire();
            state.acceptPayment();
            canExit.release();
        }
    }

    public static class Customer extends TemplateThread {
        public int custId;

        public Customer(int numRuns, int cId) {
            super(numRuns);
            this.custId = cId;
        }

        @Override
        public void execute() throws InterruptedException {
            canEnterBarbershop.acquire();
            state.enterShop();
            canSitOnSofa.acquire();
            state.sitOnSofa();

            freeBarber.acquire();
            state.sitInBarberChair();

            canSitOnSofa.release();

            state.getHairCut();

            cutHairSemaphore.release();
            freeBarber.release();

            freeBarber.acquire();
            state.pay();
            acceptPaymentSemaphore.release();
            canExit.acquire();

            state.exitShop();
            freeBarber.release();
            canEnterBarbershop.release();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            System.out.print((i + 1) + ": ");
            run();
        }
    }

    public static void run() {
        try {
            int numBarbers = 5; // Max: 15
            int numCustomers = 10 * numBarbers;

            state = new State(numBarbers, numCustomers);
            HashSet<Thread> threads = new HashSet<Thread>();

            for (int i = 0; i < numBarbers; i++) {
                Barber b = new Barber(Math.max(numCustomers / numBarbers, 1), i);
                threads.add(b);
            }
            for (int i = 0; i < numCustomers; i++) {
                Customer cust = new Customer(1, i);
                threads.add(cust);
            }

            init(numBarbers);

            ProblemExecution.start(threads, state);
            System.out.println(new Date().getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
