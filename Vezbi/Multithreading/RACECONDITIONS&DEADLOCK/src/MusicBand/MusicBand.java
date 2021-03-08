package MusicBand;

import pkg.ProblemExecution;
import pkg.TemplateThread;

import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class MusicBand {

    private static int numPeopleOnStage;
    private static Semaphore numPeople;
    private static Semaphore guitarists;
    private static Semaphore singers;
    private static Semaphore waitForEval;

    private static void init() {
        guitarists = new Semaphore(3);
        singers = new Semaphore(2);
        numPeopleOnStage = 0;
        numPeople = new Semaphore(1);
        waitForEval = new Semaphore(0);
    }

    private static MusicBandState state = new MusicBandState();

    static class GuitarPlayer extends TemplateThread {

        public GuitarPlayer(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            guitarists.acquire();
            state.play();
            numPeople.acquire();
            numPeopleOnStage++;
            if ( numPeopleOnStage == 5 ){
                state.evaluate();
                numPeopleOnStage = 0;
                waitForEval.release(5);
                guitarists.release(3);
                singers.release(2);
            }
            numPeople.release();
            waitForEval.acquire();
        }
    }

    static class Singer extends TemplateThread{

        public Singer(int numRuns) {
            super(numRuns);
        }

        @Override
        public void execute() throws InterruptedException {
            singers.acquire();
            state.play();
            numPeople.acquire();
            numPeopleOnStage++;
            if ( numPeopleOnStage == 5 ){
                state.evaluate();
                numPeopleOnStage = 0;
                waitForEval.release(5);
                guitarists.release(3);
                singers.release(2);
            }
            numPeople.release();
            waitForEval.acquire();
        }
    }


    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            run();
        }
    }

    public static void run() {
        try {
            Scanner s = new Scanner(System.in);
            int numRuns = 1;
            int numIterations = 100;
            s.close();

            HashSet<Thread> threads = new HashSet<Thread>();

            for (int i = 0; i < numIterations; i++) {
                Singer singer = new Singer(numRuns);
                threads.add(singer);
                GuitarPlayer gp = new GuitarPlayer(numRuns);
                threads.add(gp);
                gp = new GuitarPlayer(numRuns);
                threads.add(gp);
                singer = new Singer(numRuns);
                threads.add(singer);
                gp = new GuitarPlayer(numRuns);
                threads.add(gp);
            }

            init();

            ProblemExecution.start(threads, state);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



}
