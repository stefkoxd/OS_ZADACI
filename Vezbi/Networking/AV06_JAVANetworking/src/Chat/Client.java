package Chat;

import java.io.*;
import java.net.Socket;

public class Client {
    public void start() throws IOException {
        while(true) {
            //socket-ot prima host kako argument toa e vsusnost ili adresata ili hostname-ot na serverot so koj sakame da vospostavime vrska
            Socket communicationSocket = new Socket("localhost", 5555);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String readLine = br.readLine();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(communicationSocket.getOutputStream()));

            writer.write(readLine);
            writer.close();
        }
    }
}
