package Zad2LV03;

import java.util.concurrent.Semaphore;

class H2OMachine {

    String[] molecule;
    int count;

    private static Semaphore oSemaphore = new Semaphore(1);
    private static Semaphore hSemaphore = new Semaphore(2);
    private static Semaphore oHere = new Semaphore(1);
    private static Semaphore hHere = new Semaphore(1);


    public H2OMachine() {
        molecule = new String[3];
        count = 0;
    }

    public void hydrogen() throws InterruptedException {
        // TODO: 3/29/20 synchronized logic here
        hSemaphore.acquire();
        oHere.acquire();
        hHere.release();
        System.out.println("The molecule H is formed");
        hSemaphore.release();
    }

    public void oxygen() throws InterruptedException {
        // TODO: 3/29/20 synchronized logic here
        oSemaphore.acquire();
        oHere.release(2);
        hHere.acquire(2);
        System.out.println("The molecule O is formed");
        oSemaphore.release();
    }
}
class H2OThread extends Thread {

    H2OMachine molecule;
    String atom;

    public H2OThread(H2OMachine molecule, String atom){
        this.molecule = molecule;
        this.atom = atom;
    }

    public void run() {
        if ("H".equals(atom)) {
            try {
                molecule.hydrogen();
            }
            catch (Exception e) {
            }
        }
        else if ("O".equals(atom)) {
            try {
                molecule.oxygen();
            }
            catch (Exception e) {
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {

        // TODO: 3/29/20 Simulate with multiple scenarios
        H2OMachine molecule = new H2OMachine();

        Thread t1 = new H2OThread(molecule,"H");
        Thread t2 = new H2OThread(molecule,"O");
        Thread t3 = new H2OThread(molecule,"H");
        Thread t4 = new H2OThread(molecule,"O");

        t2.start();
        t1.start();
        t4.start();
        t3.start();


    }
}
