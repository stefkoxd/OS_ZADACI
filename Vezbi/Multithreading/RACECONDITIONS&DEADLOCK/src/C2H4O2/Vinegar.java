package C2H4O2;

import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Vinegar {

    private static Semaphore cSemaphore = new Semaphore(2);
    private static Semaphore hSemaphore = new Semaphore(4);
    private static Semaphore oSemaphore = new Semaphore(2);

    private static Semaphore cHere = new Semaphore(0);
    private static Semaphore oHere = new Semaphore(0);
    private static Semaphore hHere = new Semaphore(0);

    private static int count = 0;
    private static Lock lock = new ReentrantLock();


    static class C extends Thread{

        public void execute() throws InterruptedException {
            cSemaphore.acquire();

            lock.lock();
            count++;
            lock.unlock();

            System.out.println("C here");
            cHere.release(6);

            hHere.acquire(4);
            oHere.acquire(2);


            System.out.println("Module bonding");
            Thread.sleep(100);

            lock.lock();
            count--;
            if (count == 0){
                System.out.println("Module created");
                lock.unlock();
                cSemaphore.release();
            }
            else {
                lock.unlock();
                System.out.println("C done");
                cSemaphore.release();
            }
        }

        public void run() {
            try {
                execute();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }

    }

    static class H extends Thread{
        public void execute() throws InterruptedException {
            hSemaphore.acquire();
            System.out.println("H here");

            lock.lock();
            count++;
            lock.unlock();

            hHere.release(4);

            cHere.acquire(2);
            oHere.acquire(2);

            System.out.println("Module bonding");
            Thread.sleep(100);

            lock.lock();
            count--;
            if (count == 0){
                System.out.println("Module created");
                lock.unlock();
                hSemaphore.release();
            }
            else {
                lock.unlock();
                System.out.println("H done");
                hSemaphore.release();
            }
        }

        public void run() {
            try {
                execute();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class O extends Thread{
        public void execute() throws InterruptedException {
            oSemaphore.acquire();
            System.out.println("O here");

            lock.lock();
            count++;
            lock.unlock();

            oHere.release(6);

            cHere.acquire(2);
            hHere.acquire(4);


            System.out.println("Module bonding");
            Thread.sleep(100);

            lock.lock();
            count--;
            if (count == 0){
                System.out.println("Module created");
                lock.unlock();
                oSemaphore.release();
            }
            else {
                lock.unlock();
                System.out.println("O done");
                oSemaphore.release();
            }
        }

        public void run() {
            try {
                execute();
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        HashSet<Thread> threads = new HashSet<>();

        for(int i=0;i<30;i++) {
            C c = new C();

            O o = new O();

            threads.add(c);

            threads.add(o);
        }
        for(int i=0;i<60;i++) {
            H h = new H();
            threads.add(h);
        }

        for(Thread t : threads) {
            t.start();
        }

        for(Thread t : threads) {
            t.join(2000);
        }
        for(Thread t : threads) {
            if(t.isAlive()) {
                t.interrupt();
                System.out.println("Possible Deadlock!");
            }
        }
        System.out.println("Process Finished.");
    }

}
