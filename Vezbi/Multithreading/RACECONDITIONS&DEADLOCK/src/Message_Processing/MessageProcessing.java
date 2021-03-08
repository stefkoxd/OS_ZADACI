package Message_Processing;

import java.util.HashSet;
import java.util.concurrent.Semaphore;


class Processor extends Thread{

    @Override
    public void run() {
        try {
            execute();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void execute() throws InterruptedException {

        int processedMessages=0;

        while(processedMessages < 50) {
            // wait for 5 ready messages in order to activate the processing
            MessageProcessing.canActivate.acquire();
            System.out.println("Activate processing");


            System.out.println("Request message");
            MessageProcessing.canSendMessage.release(5);


            // when the messasge is provided, process it
            MessageProcessing.provideMessageSemaphore.acquire(5);
            System.out.println("Process message");
            MessageProcessing.isDoneWithMessage.release(5);
            processedMessages+=5;
            Thread.sleep(200);


            // if there are no more ready messages, pause the processing
            System.out.println("Processing pause");
        }
    }

}

class MessageSource extends Thread{

    @Override
    public void run() {
        try {
            execute();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void execute() throws InterruptedException {
        Thread.sleep(50);

        System.out.println("Message ready");

        MessageProcessing.numMessagesSemaphore.acquire();
        MessageProcessing.numMessages++;
        if ( MessageProcessing.numMessages%5 == 0 ){
            MessageProcessing.canActivate.release();
        }
        MessageProcessing.numMessagesSemaphore.release();


        // wait until the processor requests the message
        MessageProcessing.canSendMessage.acquire();
        System.out.println("Provide message");
        MessageProcessing.provideMessageSemaphore.release();

        // wait until the processor is done with the processing of the message
        MessageProcessing.isDoneWithMessage.acquire();
        System.out.println("Message delivered. Leaving.");
    }

}


public class MessageProcessing {

    public static Semaphore canActivate;
    public static Semaphore canSendMessage;
    public static int numMessages;
    public static Semaphore numMessagesSemaphore;
    public static Semaphore isDoneWithMessage;
    public static Semaphore provideMessageSemaphore;

    public static void init(){
        canActivate = new Semaphore(0);
        canSendMessage = new Semaphore(0);
        numMessages = 0;
        numMessagesSemaphore = new Semaphore(1);
        isDoneWithMessage = new Semaphore(0);
        provideMessageSemaphore = new Semaphore(0);
    }


    public static void main(String[] args) throws InterruptedException {
        init();
        HashSet<Thread> threads = new HashSet<Thread>();
        for (int i = 0; i < 50; i++) {
            MessageSource ms = new MessageSource();
            threads.add(ms);
        }
        threads.add(new Processor());
        // start all threads in background
        for (Thread thread: threads){
            thread.start();
        }
        // after all of them are started, wait each of them to finish for 1_000 ms
        for (Thread thread: threads){
            thread.join(1000);
        }
        // after the waiting for each of the players is done, check the one that are not finished and terminate them
        for (Thread thread: threads){
            if ( thread.isAlive()){
                System.err.println("Possible deadlock");
                thread.interrupt();
            }
        }

    }

}
