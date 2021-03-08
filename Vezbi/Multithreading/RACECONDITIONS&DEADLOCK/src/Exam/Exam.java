package Exam;

import pkg.ProblemExecution;
import pkg.TemplateThread;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Exam {

    private static Semaphore canTeacherEnter;
    private static Semaphore canStudentEnter;


    private static Semaphore canDistributeTests;
    private static Semaphore canStudentsLeave;
    private static Semaphore canProfessorLeave;

    public static void init() {
        canTeacherEnter = new Semaphore(1);
        canStudentEnter = new Semaphore(0);
        canDistributeTests = new Semaphore(0);
        canStudentsLeave = new Semaphore(0);
        canProfessorLeave = new Semaphore(0);
   }

    public static class Teacher extends TemplateThread {

        public Teacher(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            canTeacherEnter.acquire();
            state.teacherEnter();
            canStudentEnter.release(50);
            canDistributeTests.acquire(50);
            state.distributeTests();
            state.examEnd();
            canStudentsLeave.release(50);
            canProfessorLeave.acquire(50);
            state.teacherLeave();
            canTeacherEnter.release();
        }
    }

    public static class Student extends TemplateThread {

        public Student(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            canStudentEnter.acquire();
            state.studentEnter();
            canDistributeTests.release();
            canStudentsLeave.acquire();
            state.studentLeave();
            canProfessorLeave.release();
        }
    }

    static ExamState state = new ExamState();

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
                    Teacher c = new Teacher(numRuns);
                    threads.add(c);
                }
            }

            init();

            ProblemExecution.start(threads, state);
            System.out.println(new Date().getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
