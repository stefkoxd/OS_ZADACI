import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//so monitor
class BlockingQueue<T> {

    List<T> contents;
    int capacity;


    public BlockingQueue(int capacity) {
        contents = new ArrayList<>();
        this.capacity = capacity;
    }

    public synchronized void enqueue(T item) throws InterruptedException {

        while ( contents.size() == capacity ){
            this.wait();

        }

        //dodava element
        contents.add(item);
        //gi izvestuva zaspienite consumers
        notifyAll();
    }

    public synchronized T dequeue() throws InterruptedException {
        T item;
        while ( contents.size() == 0 ){
            this.wait();
        }

        item = contents.remove(contents.size()-1);

        //notifyALL -> se izvestuvaat site zaspani niski
        notifyAll();
        return item;
    }

}

//so lock
class BlockingQueueLocked<T> {

    private Lock mutex = new ReentrantLock();

    List<T> contents;
    int capacity;

    public BlockingQueueLocked(int capacity) {
        contents = new ArrayList<T>();
        this.capacity = capacity;
    }

    public void enqueue(T item) {
        while(true){
            mutex.lock();
            if( contents.size() < capacity ){
                contents.add(item);
                mutex.unlock();
                break;
            }
            mutex.unlock();
        }
    }

    public T dequeue() {
        T item = null;

        while ( true ){
            mutex.lock();
            if ( contents.size() > 0 ) {
                item = contents.remove(contents.size() - 1);
                mutex.unlock();
                break;
            }
            mutex.unlock();
        }

        return item;
    }
}

//so semaphore
class BlockingQueueSemaphore<T> {

    List<T> contents;
    int capacity;

    private Semaphore producerSemaphore = new Semaphore(100);
    private Semaphore consumerSemaphore = new Semaphore(100);
    private Semaphore coordinator = new Semaphore(1);


    public BlockingQueueSemaphore(int capacity) {
        contents = new ArrayList<>();
        this.capacity = capacity;
    }

    public void enqueue(T item) throws InterruptedException {
        producerSemaphore.acquire();

        coordinator.acquire();
        while ( contents.size() == capacity ) {
            coordinator.release();

            Thread.sleep(100);

            coordinator.acquire();
        }


        contents.add(item);
        coordinator.release();
        consumerSemaphore.release();
    }

    public T dequeue() throws InterruptedException {
        T item = null;

        consumerSemaphore.acquire();

        coordinator.acquire();
        while( contents.size() == 0 ){
            coordinator.release();

            Thread.sleep(100);

            coordinator.acquire();
        }

        item = contents.remove(contents.size()-1);
        coordinator.release();
        producerSemaphore.release();

        return item;
    }
}

class Producer extends Thread{
    private String name;
    private BlockingQueueSemaphore<String> queue;

    public Producer(String name, BlockingQueueSemaphore<String> queue){
        this.name = name;
        this.queue = queue;
    }

    @Override
    public void run(){
        for ( int i = 0; i < 5; i++ ){
            System.out.println("Producer " + i + " is trying to produce");
            try {
                queue.enqueue("Product " + i + " from " + this.name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer extends Thread{

    private String name;
    private BlockingQueueSemaphore<String>  queue;

    public Consumer(String name, BlockingQueueSemaphore<String>  queue){
        this.name = name;
        this.queue = queue;
    }

    @Override
    public void run(){
        for ( int i = 0 ; i < 5; i++ ){
            System.out.println("Consumer " + i + " is trying to consume");
            String element = null;
            try {
                element = queue.dequeue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(element + " was consumed from " + name);
        }
    }
}

public class AV4 {
    public static void main(String []args) {
        BlockingQueueSemaphore<String> queue = new BlockingQueueSemaphore<>(10);

        HashSet<Thread> consumers = new HashSet<>();
        HashSet<Thread> producers = new HashSet<>();

        for ( int i = 0; i < 10; i++ ){
            Consumer consumer = new Consumer("Consumer " + i,  queue);
            consumers.add(consumer);
            Producer producer = new Producer("Producer "+ i, queue);
            producers.add(producer);
        }

        consumers.forEach(c -> c.start());
        producers.forEach( c -> c.start());

        consumers.forEach(c -> {
            try {
                c.join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producers.forEach( c -> {
            try {
                c.join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //validation for deadlock

        System.out.println("Successfully executed");
    }
}
