package ThinkingPhylosophers;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Demo {
    public static void main(String args[]) throws InterruptedException {
        DiningPhilosophers.runTest();
    }
}
class DiningPhilosophers {
    private static Random random = new Random(System.currentTimeMillis());
    private Semaphore[] forks = new Semaphore[5];

    public DiningPhilosophers() {
        forks[0] = new Semaphore(1);
        forks[1] = new Semaphore(1);
        forks[2] = new Semaphore(1);
        forks[3] = new Semaphore(1);
        forks[4] = new Semaphore(1);
    }

    public void lifecycleOfPhilosopher(int id) throws InterruptedException {

        while (true) {
            think();
            eat(id);
        }
    }

    void think() throws InterruptedException {
        Thread.sleep(random.nextInt(50));
    }

    void eat(int id) throws InterruptedException {
        // TODO: 3/29/20 Synchronize
        System.out.println("Philosopher " + id + " is trying to eat.");
        if ( id == 0 ) {
            forks[id].acquire();
            if ( forks[forks.length-1].availablePermits() > 0 ){
                forks[forks.length-1].acquire();
                System.out.println("Philosopher " + id + " is eating.");
                forks[forks.length-1].release();
                forks[id].release();
                System.out.println("Philosopher " + id + " has finished eating.");
            }
            else{
                System.err.println("Philosopher " + id + " cannot eat!");
                forks[id].release();
            }
        }
        else{
            forks[id].acquire();
            if( forks[forks.length%id].availablePermits() > 0 ){
                forks[forks.length%id].acquire();
                System.out.println("Philosopher " + id + " is eating.");
                forks[forks.length%id].release();
                forks[id].release();
                System.out.println("Philosopher " + id + " has finished eating.");
            }
            else{
                System.err.println("Philosopher " + id + " cannot eat!");
                forks[id].release();
            }
        }
    }

    static void runPhilosopher(DiningPhilosophers dp, int id) {
        try {
            dp.lifecycleOfPhilosopher(id);
        } catch (InterruptedException ie) {

        }
    }

    public static void runTest() throws InterruptedException {
        final DiningPhilosophers dp = new DiningPhilosophers();

        Thread p1 = new Thread(new Runnable() {

            public void run() {
                runPhilosopher(dp, 0);
            }
        });

        Thread p2 = new Thread(new Runnable() {

            public void run() {
                runPhilosopher(dp, 1);
            }
        });

        Thread p3 = new Thread(new Runnable() {

            public void run() {
                runPhilosopher(dp, 2);
            }
        });

        Thread p4 = new Thread(new Runnable() {

            public void run() {
                runPhilosopher(dp, 3);
            }
        });

        Thread p5 = new Thread(new Runnable() {

            public void run() {
                runPhilosopher(dp, 4);
            }
        });

        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();

        p1.join();
        p2.join();
        p3.join();
        p4.join();
        p5.join();
    }
}