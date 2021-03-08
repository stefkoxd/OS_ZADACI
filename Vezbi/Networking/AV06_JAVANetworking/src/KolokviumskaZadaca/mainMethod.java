package KolokviumskaZadaca;

import java.io.IOException;
import java.util.HashSet;

public class mainMethod {
    public static void main(String[] args) throws IOException {
        Server server = new Server(3398);
        HashSet<Client> clients = new HashSet<>();
        for ( int i = 0 ; i < 10; i++ ){
            Client client = new Client("D:\\FINKI MATERIJALI\\Cetvrt Semestar\\Operativni Sistemi\\Vezbi\\Networking\\AV06_JAVANetworking\\src\\KolokviumskaZadaca");
            clients.add(client);
        }
        server.start();

        for ( Client client: clients ){
            client.start();
        }


    }
}
