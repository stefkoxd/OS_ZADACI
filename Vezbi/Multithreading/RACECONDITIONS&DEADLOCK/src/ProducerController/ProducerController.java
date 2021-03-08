package ProducerController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ProducerController {
    public static int NUM_RUN = 50;

    private static Semaphore lock = new Semaphore(1);
    private static int numControllers = 0;
    private static Semaphore numControllersLock = new Semaphore(1);
    private static Semaphore canCheck = new Semaphore(10);

    public static void init() {

    }

    public static class Buffer {

        public void produce() {
            System.out.println("Producer is producing...");
        }

        public void check() {
            System.out.println("Controller is checking...");
        }
    }

    public static class Producer extends Thread {
        private final Buffer buffer;

        public Producer(Buffer b) {
            this.buffer = b;
        }

        public void execute() throws InterruptedException {
            lock.acquire();
            buffer.produce();
            lock.release();
        }

        @Override
        public void run() {
            for (int i = 0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Controller extends Thread {

        private final Buffer buffer;

        public Controller(Buffer buffer) {
            this.buffer = buffer;
        }

        public void execute() throws InterruptedException {
            numControllersLock.acquire();
            if( numControllers == 0 ) {
                lock.acquire();
            }
            numControllers++;
            numControllersLock.release();

            canCheck.acquire();
            buffer.check();

            numControllersLock.acquire();
            numControllers--;
            canCheck.release();
            if ( numControllers == 0 ){
                lock.release();
            }
            numControllersLock.release();
        }

        @Override
        public void run() {
            for (int i = 0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Buffer buffer = new Buffer();
        Producer p = new Producer(buffer);
        List<Controller> controllers = new ArrayList<>();
        init();
        for (int i = 0; i < 10; i++) {
            controllers.add(new Controller(buffer));
        }
        p.start();
        for (int i = 0; i < 10; i++) {
            controllers.get(i).start();
        }
        for (int i = 0; i < 10; i++) {
            controllers.get(i).join();
        }

    }
}
