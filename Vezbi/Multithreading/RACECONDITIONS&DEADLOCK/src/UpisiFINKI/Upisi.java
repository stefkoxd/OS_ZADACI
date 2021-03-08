package UpisiFINKI;


import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Upisi {

    private static Semaphore comity;
    private static Semaphore students;
    private static Semaphore leaveDocuments;
    private static Semaphore signStudent;

    public static void init() {
        comity = new Semaphore(4);
        students = new Semaphore(4);
        leaveDocuments = new Semaphore(0);
        signStudent = new Semaphore(0);
    }

    public static class Clen extends Thread{

        private String id;
        Clen(String id){
            this.id = id;
        }


        public void execute() throws InterruptedException {
            int numStudents = 10;
            System.out.println(id + " is trying to get into the room for singing students.");
            comity.acquire();

            System.out.println(id + " is seated on his place.");
            while( numStudents > 0 ){
                System.out.println(id + " is waiting for a student to come.");
                leaveDocuments.acquire();

                System.out.println("A student has arrived at " + id);
                zapisi();
                System.out.println("A student has been signed by " + id);

                signStudent.release();

                numStudents--;
                System.out.println(id + " has " + numStudents + " students left.");
            }

            System.out.println(id + " has finished his job.");
            comity.release();
        }

        public void zapisi() {
            System.out.println(id + " is signing a student...");
        }

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Student extends Thread{

        private String id;

        Student(String id){
            this.id = id;
        }

        public void execute() throws InterruptedException {

            System.out.println(id + " is trying to get into the room.");

            students.acquire();
            System.out.println(id + " is in the room and wants to be signed up.");

            leaveDocuments.release();
            ostaviDokumenti();
            System.out.println(id + " has left his documents and is waiting to be signed.");
            signStudent.acquire();
            System.out.println(id + " has been signed and is leaving the room.");


            students.release();
        }

        public void ostaviDokumenti() {
            System.out.println(id + " is leaving his documents...");
        }

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        init();
        HashSet<Thread> threads = new HashSet<>();
        for ( int i = 0; i < 80; i++ ){
            Student student = new Student("Student " + i );
            threads.add(student);
        }
        for ( int i = 0 ; i < 8; i++ ){
            Clen comity = new Clen("Comity " + i);
            threads.add(comity);
        }
        for ( Thread t: threads){
            t.start();
        }
        for ( Thread t: threads){
            t.join(1000);
        }

    }

}
