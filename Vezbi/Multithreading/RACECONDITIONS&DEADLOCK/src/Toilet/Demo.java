package Toilet;


import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demo {

    public static class Toilet {

        public void vlezi() {
            System.out.println("Vleguva...");
        }

        public void izlezi() {
            System.out.println("Izleguva...");
        }

    }

    private static int numM;
    private static int numF;

    private static Lock numFLock;
    private static Lock numMLock;
    private static Semaphore ms;
    private static Semaphore fs;

    public static void init() {
        numM = 0;
        numF = 0;
        numFLock = new ReentrantLock();
        numMLock = new ReentrantLock();
        ms = new Semaphore(0);
        fs = new Semaphore(0);
    }


    public static void main(String[] args) throws InterruptedException {
        init();
        HashSet<Thread> threads = new HashSet<>();
        Toilet toilet = new Toilet();
        for ( int i = 0; i < 1500; i++ ){
            Man man = new Man(toilet);
            Woman woman = new Woman(toilet);
            threads.add(man);
            threads.add(woman);
        }
        for ( Thread t: threads){
            t.start();
        }
        for ( Thread t: threads ){
            t.join();
        }
    }

    public static class Man extends Thread{

        private Toilet toilet;

        public Man(Toilet toilet) {
            this.toilet = toilet;
        }

        public void enter() throws InterruptedException {
            numFLock.lock();
            if(numF > 0 ){
                numFLock.unlock();
                fs.acquire();
            }
            else
                numFLock.unlock();

            numMLock.lock();
            numM++;
            numMLock.unlock();

            toilet.vlezi();
        }

        public void exit() throws InterruptedException {
            numMLock.lock();
            numM--;
            if ( numM == 0 ){
                ms.release(ms.getQueueLength());
            }
            toilet.izlezi();
            numMLock.unlock();
        }

        @Override
        public void run() {
            try {
                enter();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                exit();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Woman extends Thread{

        private Toilet toilet;

        public Woman(Toilet toilet) {
            this.toilet = toilet;
        }

        public void enter() throws InterruptedException {
            numMLock.lock();
            if ( numM > 0 ){
                numMLock.unlock();
                ms.acquire();
            }
            else
                numMLock.unlock();
            numFLock.lock();
            numF++;
            numFLock.unlock();

            toilet.vlezi();
        }

        public void exit() throws InterruptedException {
            numFLock.lock();
            numF--;
            if ( numF == 0 ){
                fs.release(fs.getQueueLength());
            }
            toilet.izlezi();
            numFLock.unlock();
        }

        @Override
        public void run() {
            try {
                enter();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                exit();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
