package Rollercoaster;

import pkg.ProblemExecution;
import pkg.TemplateThread;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class RollerCoasterClass {

    private static int numPassengers;
    private static Semaphore passengersSemaphore;
    private static Semaphore canRide;
    private static Semaphore canEnterCar;
    private static Semaphore canStart;
    private static Semaphore canExitCar;
    private static Semaphore carMutex;
    private static Semaphore sValidate;

    public static void init(){
        carMutex = new Semaphore(1);
        canExitCar = new Semaphore(0);
        numPassengers = 0;
        passengersSemaphore = new Semaphore(1);
        canRide = new Semaphore(10);
        canEnterCar = new Semaphore(0);
        canStart = new Semaphore(0);
        sValidate = new Semaphore(0);
    }

    public static class Car extends TemplateThread {

        public Car(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            carMutex.acquire();
            state.load();
            canEnterCar.release(10);

            canStart.acquire(10);
            state.run();


            state.unload();
            canExitCar.release(10);

            sValidate.acquire();
            state.validate();

            carMutex.release();
        }
    }

    public static class Passenger extends TemplateThread{

        public Passenger(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            canRide.acquire();

            passengersSemaphore.acquire();
            numPassengers++;
            passengersSemaphore.release();

            canEnterCar.acquire();
            state.board();

            canStart.release(1);


            canExitCar.acquire();
            state.unboard();

            passengersSemaphore.acquire();
            numPassengers--;
            if (numPassengers == 0){
                sValidate.release();
                canRide.release(10);
            }
            passengersSemaphore.release();
        }
    }

    static RollerCoasterState state = new RollerCoasterState();

    public static void main(String[] args) {
        for(int i=0;i<10;i++){
            run();
        }
    }

    public static void run(){
        try{
            int numRuns=1;
            int numScenarios=100;
            HashSet<Thread> threads=new HashSet<Thread>();

            for(int i=0;i<numScenarios;i++){
                Passenger p=new Passenger(numRuns);
                threads.add(p);
                if(i%10==0){
                    Car c=new Car(numRuns);
                    threads.add(c);
                }
            }

            init();

            ProblemExecution.start(threads, state);
            System.out.println(new Date().getTime());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }
}
