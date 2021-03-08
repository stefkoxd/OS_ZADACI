import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ThreadExecutor extends Thread{

    private String name;
    private Increment increment;

    public ThreadExecutor(String name, Increment increment){
        this.name = name;
        this.increment = increment;
    }

    @Override
    public void run(){

        for (int i = 0 ; i < 20; i++ ){
            System.out.println("Running thread is:" + this.name);
            try {
                increment.increment();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

class Increment{
    private int count = 0;
    //========================koristenje na lock:========================//
    private Lock lock = new ReentrantLock();
    public void safeIncrementLock(){
        lock.lock();
        count++;
        lock.unlock();
    }
    //=======================================================================//



    //========================koristenje na semafor:========================//
    //se koristi ako sakame povekje niski da imaat pristap do kriticniot region
    //kako argument vo konstruktorot se naveduva brojot na niski koi ke mozat da pristapat do
    //kriticniot region
    private Semaphore semaphore = new Semaphore(1);
    public void safeIncrementSemaphore() throws InterruptedException {

        semaphore.acquire(); // so semaphore.acquire niskata koja vleguva vo ovaa f-ja bara dozvola za dali ke moze da
        //go izvrsi kriticniot region
        count++;
        semaphore.release(); // se osloboduva pristapot
    }
    //=======================================================================//



    //========================koristenje na monitor:========================//
    public void safeIncrementSync(){
        synchronized (this){
            count++;
        }
    }
    //so synchronized f-ja se garantira deka samo edna niska ke ima pristap do sekoja f-ja sto e
    //od tipot synchronized se dodeka ne se zavrsi, ova e
    //na nivo na objekt (pr. public synchronized void increment(), drugiot primer e podolu)

    //ako imame klasa so povekje synchronized metodi i eden thread raboti na eden od tie synchronized metodi, togas site
    //synchronized metodi se onevozmozeni i nitu eden drug thread nema da moze da pristapi do niv se dodeka momentalniot
    //thread sto raboti na nekoja od tie synchronized metodi ne zavrsi

    //na nivo na klasa moze da se napravi promenlivata static i ovaa f-ja static, bidejki so static
    //f-cii i promenlivi sekoja instanca od ovaa klasa ke gi ima istite vrednosti na static promenlivata
    //se pravi synchronized(Increment.class), ova se koristi koga imame povekje instanci od ovaa klasa i sakame
    //da imaat sinhronizirani parametri
    //=======================================================================//


    //========================unsafe increment:========================//
    public void increment() throws InterruptedException {
        count++;
        //posle inkrementot niskata koja momentalno go pravi toj inkrement se blokira za 10 milisekundi,
        //bidejki 20 pati se povikuva ovaa f-ja vo run(), na dvete niski za celosno da zavrsat so inkrementiranje,
        //potrebni ke im bidat 200ms each bidejki tie se blokiraat za 10ms posle samiot inkrement.
        /*Thread.sleep(10);*/
    }
    //=======================================================================//


    public int getCount() {
        return count;
    }
}

public class AV3 {

    public static void main(String[] args) throws InterruptedException {
        Increment increment = new Increment();


        Thread executor = new ThreadExecutor("First", increment);
        Thread secondExecutor = new ThreadExecutor("Second", increment);


        //so povikuvanje na metodot start se kreira nova niska ( ako se povika run kodot sto e vo f-jata run ke se izvrsi vo
        // main niskata ) ---> executor.start();

        //paralelno izvrsuvanje na povekje niski, rezultatot na izvrsuvanje e nepredvidliv, rasporeduvacot e odgovoren za
        //rasporedot na izvrsuvanje
        executor.start();
        secondExecutor.start();


        //so join se blokira main metodot i se izvrsuva niskata koja go povikala join, main metodot prodolzuva otkako ke
        //zavrsi so izvrsuvanje niskata sto povikala .join(), vo slucaj niskata da predizvikuva blokiranje vo .join() f-jata
        //moze da se dade argument za kolku milisekundi ke i se dozvolat na niskata za da se izvrsi pa da prodolzi main-ot
        //executor.join(5) ---> imas 5 milisekundi vreme vo koe main-ot nema da te dopira, posle toa zavisi od rasporeduvacot
        executor.join();
        secondExecutor.join();




        //se proveruva dali niskata posle tie 5 milisekundi koi bile dodeleni dali seuste e ziva, vo ovoj slucaj ako e,
        //celosno se terminira so interrupt();
        /*if ( executor.isAlive() && secondExecutor.isAlive() ){
            executor.interrupt();
            secondExecutor.interrupt();
        }*/


        int count = increment.getCount();
        System.out.println("Result is: "+ count);

    }

}
