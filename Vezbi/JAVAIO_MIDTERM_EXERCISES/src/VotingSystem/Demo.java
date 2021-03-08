package VotingSystem;

import java.util.HashSet;

public class Demo {
    public static void main(String[] args) throws InterruptedException {
        Server server = new Server(5555);
        HashSet<Client> clients = new HashSet<>();

        server.start();

        for ( int i = 0; i < 500; i++ ){
            clients.add(new Client());
        }

        for(Client client: clients ){
            client.start();
        }

        for(Client client: clients ){
            client.join();
        }
    }
}