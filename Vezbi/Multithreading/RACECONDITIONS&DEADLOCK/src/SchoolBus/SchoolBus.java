package SchoolBus;

import pkg.ProblemExecution;
import pkg.TemplateThread;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class SchoolBus {

    private static SchoolBusState state = new SchoolBusState();
    private static Semaphore studentsEnter;
    private static Semaphore busDriver;
    private static int numStudentsInBus;
    private static Semaphore numStudentsSemaphore;
    private static Semaphore waitForArrival;
    private static Semaphore driverWaiting;

    private static void init() {
        studentsEnter = new Semaphore(0);
        busDriver = new Semaphore(1);
        numStudentsInBus = 0;
        numStudentsSemaphore = new Semaphore(1);
        waitForArrival = new Semaphore(0);
        driverWaiting = new Semaphore(0);
    }

    static class Driver extends TemplateThread{

        public Driver(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            busDriver.acquire();
            state.driverEnter();
            studentsEnter.release(50);
            driverWaiting.acquire();

            state.busDeparture();
            state.busArrive();

            waitForArrival.release(50);
            driverWaiting.acquire();
            state.driverLeave();
            busDriver.release();
        }
    }


    static class Student extends TemplateThread{

        public Student(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            studentsEnter.acquire();

            state.studentEnter();
            numStudentsSemaphore.acquire();
            numStudentsInBus++;
            if( numStudentsInBus == 50 ) {
                driverWaiting.release();
            }
            numStudentsSemaphore.release();

            waitForArrival.acquire();
            numStudentsSemaphore.acquire();
            numStudentsInBus--;
            if ( numStudentsInBus == 0 ){
                state.studentLeave();
                numStudentsSemaphore.release();
                driverWaiting.release();
            }
            else{
                state.studentLeave();
                numStudentsSemaphore.release();
            }
        }

    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            run();
        }
    }

    public static void run() {
        try {
            int numRuns = 1;
            int numScenarios = 1000;
            HashSet<Thread> threads = new HashSet<Thread>();

            for (int i = 0; i < numScenarios; i++) {
                Student p = new Student(numRuns);
                threads.add(p);
                if (i % 50 == 0) {
                    Driver c = new Driver(numRuns);
                    threads.add(c);
                }
            }

            init();

            ProblemExecution.start(threads, state);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
