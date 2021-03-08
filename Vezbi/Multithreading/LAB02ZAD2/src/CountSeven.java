public class CountSeven {
    public static class Thread1 extends Thread {
        public void run() {
            System.out.println("A");
            System.out.println("B");
        }
    }

    public static class Thread2 extends Thread {
        public void run() {
            System.out.println("1");
            System.out.println("2");
        }
    }

    public static class ThreadAB extends Thread{
        String stringToPrint;
        ThreadAB(String s){
            this.stringToPrint = s;
        }

        public void run(){
            System.out.println(this.stringToPrint);
        }
    }


    public static void main(String[] args) throws InterruptedException {
        ThreadAB t1 = new ThreadAB("ABCD");
        ThreadAB t2 = new ThreadAB("123");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}