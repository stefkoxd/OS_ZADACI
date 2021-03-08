package Networking1;

import java.util.ArrayList;

public class Demo {

    public static void main(String[] args) throws InterruptedException {

        Server server = new Server(4498);
        ArrayList<Client> clients = new ArrayList<Client>();

        server.start();

        for( int i = 0; i < 50; i++ ){
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
