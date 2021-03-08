package SiO2;

import java.util.concurrent.Semaphore;

public class SiO2 {
    public static int NUM_RUN = 50;

    private static Semaphore siSemaphore;
    private static Semaphore oSemaphore;
    private static Semaphore siHere;
    private static Semaphore oHere;
    private static Semaphore ready;

    public static void init() {
        siSemaphore = new Semaphore(1);
        oSemaphore = new Semaphore(2);
        siHere = new Semaphore(0);
        oHere = new Semaphore(0);
        ready = new Semaphore(0);
    }

    public static class Si extends Thread {

        public void bond() {
            System.out.println("Si is bonding now.");
        }

        @Override
        public void run() {
            for (int i=0;i<NUM_RUN;i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void execute() throws InterruptedException {
            siSemaphore.acquire();

            siHere.release(2);
            oHere.acquire(2);

            ready.release(2);

            bond();

            siSemaphore.release();
        }

    }

    public static class O extends Thread {

        public void execute() throws InterruptedException {
            oSemaphore.acquire(1);

            siHere.acquire();
            oHere.release();
            ready.acquire();

            bond();

            oSemaphore.release();
        }

        public void bond() {
            System.out.println("O is bonding now.");
        }


        @Override
        public void run() {
            for (int i=0;i<NUM_RUN;i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
