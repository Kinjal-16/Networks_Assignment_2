import java.io.IOException;
import java.net.*;
import java .util.*;
public class Controller extends Thread{
    HashMap<String,HashMap<String,ArrayList<ArrayList<String>>>> map;
    HashMap<InetAddress,String> Router;
    String in;
    String out;
    DatagramSocket soc;
    InetAddress addr;
    Terminal ob = new Terminal("Controller");
    @Override
    public void run()
    {
        try {
            soc= new DatagramSocket(51510,InetAddress.getByName("Controller"));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        while(true)
        {
            try {
                checkFAddress();
                //sleep(10000);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    void checkFAddress() throws IOException {

        byte [] buf = new byte[1024];
        DatagramPacket pck = new DatagramPacket(buf,buf.length);
        ob.println("Waiting for a packet");
        soc.receive(pck);
        ArrayList <String> list = new ArrayList<String>();
        String str = new String(pck.getData(), 0, pck.getLength());
        ob.println("New Packet Received");
        switch(buf[0]) {
            case 1:
            if (map.containsKey(str.substring(2, (((int) buf[1]) + 2)))) {

                addr = pck.getAddress();
                String r = Router.get(addr);
                ob.println("Fetching the In and Out addresses for "+r);
                HashMap<String, ArrayList<ArrayList<String>>> tempMap = map.get(str.substring(2, (((int) buf[1]) + 2)));
                ArrayList<ArrayList<String>> graph = tempMap.get("E1");
                ArrayList<String> str2 = new ArrayList<String>();
                for (int i = 0; i < 3; i++) {
                    str2 = graph.get(i);
                    if (str2.get(0) == r) {
                        in = str2.get(1);
                        out = str2.get(2);

                        break;

                    }


                }
                ob.println("Sending the In and Out addresses");
                send();


            }
            break;
            case 4:
                addr = pck.getAddress();
                String r = Router.get(addr);
                ob.println(r+": "+str.substring(2,(((int)buf[1])+2)));
                packets obj = new packets();
                DatagramPacket pck2 = obj.createPacket("Controller's Running",(byte)5);
                pck2.setPort(51510);
                pck2.setAddress(addr);
                soc.send(pck2);
                ob.println("Response sent");
                break;
            case 3:
                addr = pck.getAddress();
                String r2 = Router.get(addr);
                ob.println("Ack-"+r2+": "+"Received");
        }







    }
    void send() throws IOException {

        String str[] = {in,out};
        packets ob = new packets();
        DatagramPacket pck = ob.createPacket(str);
        pck.setPort(51510);
        pck.setAddress(addr);
        soc.send(pck);
        this.ob.println("Required packet sent");
    }
    void RouterMap() throws UnknownHostException {
        Router = new HashMap<InetAddress,String>();
        InetAddress ip4 = InetAddress.getByName("E1");
        Router.put(ip4,"E1");
        InetAddress ip1 = InetAddress.getByName("Router1");
        Router.put(ip1,"Router1");
        InetAddress ip2 = InetAddress.getByName("Router2");
        Router.put(ip2,"Router2");
        InetAddress ip3 = InetAddress.getByName("Router3");
        Router.put(ip3,"Router3");
        InetAddress ip5 = InetAddress.getByName("E2");
        Router.put(ip5,"E2");

    }
    void createFTable()
    {
        map = new HashMap<String,HashMap<String,ArrayList<ArrayList<String>>>>();
        int RowCount = 3;
        ArrayList<ArrayList<String>> graph = new ArrayList<>(RowCount);
        for(int i=0;i<RowCount;i++)
        {
            graph.add(new ArrayList<>());
        }
        graph.get(0).add("Router1");
        graph.get(0).add("E1");
        graph.get(0).add("Router2");
        graph.get(1).add("Router2");
        graph.get(1).add("Router1");
        graph.get(1).add("Router3");
        graph.get(2).add("Router3");
        graph.get(2).add("Router2");
        graph.get(2).add("E2");
        HashMap<String,ArrayList<ArrayList<String>>> temMap = new HashMap<String,ArrayList<ArrayList<String>>>();
        temMap.put("E1",graph);
        map.put("Trinity",temMap);

    }
    public static void main(String args[]) throws UnknownHostException {

        Controller ob = new Controller();
        ob.createFTable();
        ob.RouterMap();
        ob.start();
    }
}
