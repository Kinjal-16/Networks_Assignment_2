import java.io.IOException;
import java.net.*;

public class Receiver extends Thread{
    DatagramSocket soc;
    Terminal ob = new Terminal("Receiver");
    public void run()
    {
        try {
            soc= new DatagramSocket(51510,InetAddress.getByName("E2"));
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        while(true) {
            try {
                receive();
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
    void receive() throws IOException {
        byte []buf = new byte[1280];
        DatagramPacket pck = new DatagramPacket(buf,buf.length);

        ob.println("Socket running at "+soc.getLocalSocketAddress());
        soc.receive(pck);

        String str = new String(pck.getData(),0,pck.getLength());
        ob.println("Received: "+str.substring(2,((int)buf[1]+2)));

    }
    public static void main(String args[]) throws InterruptedException {
        Receiver ob = new Receiver();

            ob.start();
    }
}
