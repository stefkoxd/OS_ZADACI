package Kolokviumska2;

public class Demo {
    public static void main(String[] args) {
        Server server = new Server(4444, "D:\\FINKI MATERIJALI\\Cetvrt Semestar\\Operativni Sistemi\\Vezbi\\Networking\\AV06_JAVANetworking\\serverDir\\data.txt");
        Client client = new Client(4444, ".", "out");
        server.start();
        client.start();
    }
}
