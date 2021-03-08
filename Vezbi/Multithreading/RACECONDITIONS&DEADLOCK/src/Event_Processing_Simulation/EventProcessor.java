package Event_Processing_Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class EventProcessor extends Thread {

    public static Random random = new Random();
    static List<EventGenerator> scheduled = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        List<EventProcessor> processors = new ArrayList<>();
        // TODO: kreirajte 20 Processor i startuvajte gi vo pozadina
        for (int i = 0; i < 20; i++) {
            EventProcessor p = new EventProcessor();
            processors.add(p);
            //TODO: startuvajte go vo pozadina
            p.start();
        }

        for (int i = 0; i < 100; i++) {
            EventGenerator eventGenerator = new EventGenerator();
            //TODO: startuvajte go eventGenerator-ot
            eventGenerator.start();
        }

        for (int i = 0; i < 20; i++) {
            EventProcessor p = processors.get(i);
            // TODO: Cekajte 20000ms za Processor-ot p da zavrsi
            p.join(20000);
            // TODO: ispisete go statusot od izvrsuvanjeto
        }

        for ( EventProcessor processor: processors ){
            if ( processor.isAlive() ){
                System.err.println("Terminated processing");
                processor.interrupt();
            }
        }
        System.out.println("Finished processing.");

    }

    public static Semaphore canProcess = new Semaphore(0);

    @Override
    public void run() {
        try {
            canProcess.acquire(5);

            process();

            waitForProcessing.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static Semaphore waitForProcessing = new Semaphore(0);

    public static void process() throws InterruptedException {
        // TODO: pocekajte 5 novi nastani
        System.out.println("processing event");
    }

}


class EventGenerator extends Thread {

    public Integer duration;

    private static Semaphore semaphore = new Semaphore(5);
    private static int generatedEvents = 0;
    private static Semaphore generatedEventsSemaphore = new Semaphore(1);

    public EventGenerator() throws InterruptedException {
        this.duration = EventProcessor.random.nextInt(1000);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            semaphore.acquire();
            generate();
            EventProcessor.canProcess.release();

            generatedEventsSemaphore.acquire();
            generatedEvents++;
            if (generatedEvents % 5 == 0){
                EventProcessor.waitForProcessing.acquire();
            }
            generatedEventsSemaphore.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        semaphore.release();
    }

    /**
     * Ne smee da bide povikan paralelno kaj poveke od 5 generatori
     */
    public static void generate() throws InterruptedException {
        generatedEventsSemaphore.acquire();
        System.out.println("Generating event: " + (generatedEvents+1));
        generatedEventsSemaphore.release();
    }
}