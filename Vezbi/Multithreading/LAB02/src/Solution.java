import java.util.concurrent.Semaphore;

class BlockingQueue<T> {

    T[] contents;
    int capacity;

    public BlockingQueue(int capacity) {
        contents = (T[]) new Object[capacity];
        this.capacity = capacity;
    }

    public void enqueue(T item, int i) {
        contents[i] = item;
    }

    public T dequeue() {
        return contents[capacity-1];
    }

    public int getCapacity() {
        return capacity;
    }
}

class Threads extends Thread{
    private String function;

    public static BlockingQueue<Integer> queue = new BlockingQueue<>(10);
    private static Semaphore mutex = new Semaphore(1);
    private static Semaphore emptySpaces = new Semaphore(queue.getCapacity());
    private static Semaphore fullSpaces = new Semaphore(0);
    private static int i = 0;

    public static int count = 0;

    Threads(String function){
        this.function = function;
    }

    @Override
    public void run(){
        while (i < 100) {
            if (function.equals("enter")) {
                //proverka dali ima mesto, ako nema uspij go ovoj proces i neka bide razbuden koga ke se oslobodi barem edno mesto
                try {
                    emptySpaces.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    mutex.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queue.enqueue(1, count++);

                //ako bil uspan drugiot proces deka prazen bil baferot razbudi go
                fullSpaces.release();
                System.out.println("Count after inserting an element: " + count);
                mutex.release();
            } else if (function.equals("remove")) {
                //proverka dali e prazen baferot, ako e prazen uspij go ovoj proces
                try {
                    fullSpaces.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    mutex.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queue.dequeue();
                count--;

                //ako bil poln baferot razbudi go uspaniot proces
                emptySpaces.release();
                System.out.println("Count after taking out one element: " + count);
                mutex.release();
            }
            i++;
        }
    }
}

public class Solution {
    public static void main(String []args) throws InterruptedException {
        Threads enterThread = new Threads("enter");
        Threads removeThread = new Threads("remove");

        enterThread.start();
        removeThread.start();

        enterThread.join();
        removeThread.join();

        System.out.println("Capacity of buffer is:" + (Threads.queue.getCapacity()-Threads.count) );
    }
}
