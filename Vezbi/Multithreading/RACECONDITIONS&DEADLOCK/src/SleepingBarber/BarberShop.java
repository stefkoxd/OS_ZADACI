package SleepingBarber;

import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Customer extends Thread{
    private String id;
    Customer(String id){
        this.id = id;
    }
    @Override
    public void run() {
        try {
            BarberShop.customerComesIn(id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class BarberShop {
    private static int waitingCustomers = 0;

    private static Semaphore mutex = new Semaphore(1);
    private static Lock waitingCustomersLock = new ReentrantLock();

    static void customerComesIn(String id) throws InterruptedException {
        // TODO: 3/29/20 Synchronize this method, invoked by a Customer thread
        //proveri kolku lugje ima vo studioto ( maksimum moze da ima 6, 5 cekaat 1 se shisa )
        waitingCustomersLock.lock();
        if ( waitingCustomers < 6 ){
            waitingCustomers++;
            waitingCustomersLock.unlock();

            //proveri dali mozes da se shisash, ako ne suspendiraj se i cekaj.
            mutex.acquire();
            barber(id);

            //otkako ke zavrsis namali go brojot na lugje vo studioto sto ke bidat
            waitingCustomersLock.lock();
            waitingCustomers--;
            waitingCustomersLock.unlock();

            //pusti go mutex-ot i napusti go studioto
            mutex.release();
        }
        //ako ima vekje 6 lugje, pusti go lock-ot i napusti go studioto
        else if ( waitingCustomers == 6 ){
            waitingCustomersLock.unlock();
        }
    }

    static void barber(String id) throws InterruptedException {
        // TODO: 3/29/20 Synchronize this method, invoked by Barber thread
        System.out.println("Serving " + id + ".");
    }

    public static void main(String[] args) throws InterruptedException {
        // TODO: 3/29/20 Synchronize the scenario
        HashSet<Thread> threads = new HashSet<>();
        for ( int i = 0 ; i < 100 ; i++ ){
            Customer customer = new Customer("Customer " + i);
            threads.add(customer);
        }
        for ( Thread t: threads){
            t.start();
        }
        for ( Thread t: threads ){
            t.join(1000);
        }
    }
}
