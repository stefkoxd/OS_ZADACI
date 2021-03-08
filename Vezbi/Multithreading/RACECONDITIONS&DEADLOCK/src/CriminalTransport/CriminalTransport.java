package CriminalTransport;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class CriminalTransport {

    private static int numPolicemen;
    private static int numCriminals;
    private static Semaphore criminalSemaphore;
    private static Semaphore policemanSemaphore;
    private static Semaphore policeManWait;
    private static Semaphore numCriminalsSemaphore;
    private static Semaphore criminalWait;

    private static void init(){
        criminalSemaphore = new Semaphore(1);
        policemanSemaphore = new Semaphore(1);
        policeManWait = new Semaphore(0);
        numCriminalsSemaphore = new Semaphore(1);
        numCriminals = 0;
        criminalWait = new Semaphore(0);
    }

    static class Policeman extends Thread{

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {
            // waits until it is valid to enter the car
            policemanSemaphore.acquire();
            System.out.println("Policeman enters in the car");
            //sleep here
            policeManWait.acquire();



            // when the four passengers are inside, one policeman prints the starting command
            System.out.println("Start driving.");
            Thread.sleep(100);
            // one policeman prints the this command to notice that everyone can exit
            System.out.println("Arrived.");
            // the exit from the car is allowed after the "Arrived." message is printed
            System.out.println("Policeman exits from the car");
        }

    }



    static class Criminal extends Thread{
        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {
            // waits until it is valid to enter the car
            criminalSemaphore.acquire();
            System.out.println("Criminal enters in the car");
            numCriminalsSemaphore.acquire();
            numCriminals++;
            if ( numCriminals >= 1 ){
                policeManWait.release();
            }
            numCriminalsSemaphore.release();
            criminalSemaphore.release();
            criminalWait.acquire();

            Thread.sleep(100);
            // the exit from the car is allowed after the "Arrived." message is printed
            System.out.println("Criminal exits from the car");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        init();
        HashSet<Thread> threads = new HashSet<Thread>();
        for (int i = 0; i < 60; i++) {
            Policeman red = new Policeman();
            threads.add(red);
            Criminal green = new Criminal();
            threads.add(green);
        }
        // run all threads in background
        for ( Thread t: threads){
            t.start();
        }
        // after all of them are started, wait each of them to finish for maximum 1_000 ms
        for ( Thread t: threads){
            t.join(1000);
        }
        // for each thread, terminate it if it is not finished
        for ( Thread t: threads){
            if ( t.isAlive() ){
                System.err.println("Possible deadlock");
                t.interrupt();
            }
        }
    }
}


