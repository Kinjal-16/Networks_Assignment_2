import java.io.IOException;
import java.net.*;
import java.util.*;
public class Sender extends Thread {
    DatagramSocket soc;
    Terminal ob = new Terminal("Sender");
    public void run()
    {
        try {

            soc = new DatagramSocket(50001,InetAddress.getByName("E1"));


        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

        while(true)
        {
            try {
                Scanner sc = new Scanner(System.in);
                ob.println("Press Enter to send a packet");
                String temp = ob.read("Input: ");
                send();
                System.out.println(soc.getLocalSocketAddress());

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    void send() throws IOException {

        packets pck = new packets();
        DatagramPacket packet = pck.createPacket("Trinity");
        packet.setPort(51510);
        InetAddress ip = InetAddress.getByName("Router1");

        packet.setAddress(ip);

        ob.println("Sending packet");

        soc.send(packet);
    }
    public static void main(String args[])
    {
        Sender ob = new Sender();
        ob.start();
    }

}
