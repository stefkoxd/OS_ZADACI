import java.net.UnknownHostException;
import java.util.HashSet;

public class ClientStart {
    public static void main(String[] args) throws UnknownHostException {
        HashSet<Thread> clients = new HashSet<>();
        for( int i  = 0 ; i < 3 ; i++ ){
            Client client = new Client("test");
            clients.add(client);
        }
        for ( Thread thread: clients ){
            thread.start();
        }

    }
}
