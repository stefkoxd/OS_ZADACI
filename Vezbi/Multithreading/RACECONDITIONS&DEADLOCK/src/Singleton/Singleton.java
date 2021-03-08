package Singleton;

import java.util.HashSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class thread extends Thread{
    private String id;
    thread(String id){
        this.id = id;
    }
    @Override
    public void run() {
        Singleton.getInstance(id);
    }
}

public class Singleton {
    private static volatile Singleton singleton;

    private static Lock lock = new ReentrantLock();
    private static int numInstances = 0;

    private Singleton() {

    }

    public static Singleton getInstance(String id) {
        // TODO: 3/29/20 Synchronize this
        System.out.println(id + " is trying to instance a singleton");
        lock.lock();
        if ( numInstances == 0 ){
            System.out.println(id + " has successfully instanced a singleton");
            singleton = new Singleton();
            numInstances++;
            lock.unlock();
        }
        else{
            System.out.println(id + " can't instance a new singleton because a singleton instance already exists!");
            lock.unlock();
        }
        return singleton;
    }

    public static void main(String[] args) throws InterruptedException {
        // TODO: 3/29/20 Simulate the scenario when multiple threads call the method getInstance
        HashSet<Thread> threads = new HashSet<>();
        for ( int i = 0; i < 1000 ; i++ ){
            thread t = new thread("Thread " + i);
            threads.add(t);
        }
        for ( Thread t: threads) {
            t.start();
        }
        for ( Thread t: threads){
            t.join(1000);
        }
    }

}
