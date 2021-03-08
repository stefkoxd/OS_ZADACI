package PartyRoom;

import pkg.ProblemExecution;
import pkg.TemplateThread;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class PartySolution {

    private static boolean isDeanInRoom;
    private static Semaphore deanInRoomSemaphore;
    private static int studentsWaiting;
    private static Semaphore studentsWaitingSemaphore;
    private static int numStudents;
    private static Semaphore numStudentsSemaphore;
    private static Semaphore waitForDeanToExit;
    private static Semaphore dancing;
    private static Semaphore waitFor50Students;
    private static Semaphore waitForStudentsToLeave;


    public static void init() {
        waitForStudentsToLeave = new Semaphore(0);
        waitFor50Students = new Semaphore(0);
        dancing = new Semaphore(0);
        waitForDeanToExit = new Semaphore(0);
        numStudents = 0;
        numStudentsSemaphore = new Semaphore(1);
        studentsWaitingSemaphore = new Semaphore(1);
        studentsWaiting = 0;
        isDeanInRoom = false;
        deanInRoomSemaphore = new Semaphore(1);
    }

    static class Student extends TemplateThread {

        public Student(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            deanInRoomSemaphore.acquire();

            if ( isDeanInRoom ){
                deanInRoomSemaphore.release();

                studentsWaitingSemaphore.acquire();
                studentsWaiting++;
                studentsWaitingSemaphore.release();

                waitForDeanToExit.acquire();
            }
            else
                deanInRoomSemaphore.release();

            numStudentsSemaphore.acquire();
            numStudents++;
            if ( numStudents == 50 ){
                waitFor50Students.release();

                deanInRoomSemaphore.acquire();
                isDeanInRoom = true;
                deanInRoomSemaphore.release();
            }
            numStudentsSemaphore.release();

            state.studentEnter();
            state.dance();

            dancing.acquire();


            numStudentsSemaphore.acquire();
            numStudents--;
            if ( numStudents == 0 ){
                state.studentLeave();
                numStudentsSemaphore.release();
                waitForStudentsToLeave.release();

            }
            else {
                state.studentLeave();
                numStudentsSemaphore.release();
            }
        }

    }

    static class Dean extends TemplateThread {

        public Dean(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            numStudentsSemaphore.acquire();

            if (numStudents == 0 ){

                numStudentsSemaphore.release();

                deanInRoomSemaphore.acquire();
                isDeanInRoom = true;
                deanInRoomSemaphore.release();

                state.deanEnter();
                state.conductSearch();
                state.deanLeave();

                studentsWaitingSemaphore.acquire();
                waitForDeanToExit.release(studentsWaiting);
                studentsWaiting = 0;
                studentsWaitingSemaphore.release();

            }
            else{
                numStudentsSemaphore.release();

                waitFor50Students.acquire();

                dancing.release(50);
                waitForStudentsToLeave.acquire();
                state.deanLeave();

                waitForDeanToExit.release(studentsWaiting);
                studentsWaiting = 0;
                studentsWaitingSemaphore.release();

            }
            deanInRoomSemaphore.acquire();
            isDeanInRoom = false;
            deanInRoomSemaphore.release();
        }

    }

    static PartyState state = new PartyState();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            run();
        }
    }

    public static void run() {
        try {
            int numRuns = 1;
            int numScenarios = 100;

            HashSet<Thread> threads = new HashSet<Thread>();

            for (int i = 0; i < numScenarios; i++) {
                Student s = new Student(numRuns);
                threads.add(s);

            }
            threads.add(new Dean(numRuns));

            init();

            ProblemExecution.start(threads, state);
            System.out.println(new Date().getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
