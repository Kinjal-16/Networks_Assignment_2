import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Router3 extends Thread
{
    static DatagramSocket soc;
    private Vector messages = new Vector();
    int MAX=1;
    int flag = 0;
    InetAddress addr;
    DatagramPacket ptemp;
    HashMap<String,ArrayList<ArrayList<InetAddress>>> map;
    ArrayList<ArrayList<InetAddress>> list=null;
    Terminal ob = new Terminal("Router3");
    public void run() {

        try {
            soc = new DatagramSocket(51510, InetAddress.getByName("Router3"));


            while (true) {
                receive();

                sleep(1000);


            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    void receive() throws IOException, InterruptedException {
        byte []buf = new byte[1280];
        DatagramPacket pck = new DatagramPacket(buf,buf.length);

        ob.println("Socket running at "+soc.getLocalSocketAddress());
        soc.receive(pck);

        ob.println("New Packet Received");
        buf=pck.getData();


        switch(buf[0]) {
            case 1:
                String strTemp = new String(pck.getData(),0,pck.getLength());
                String str = strTemp.substring(2,((int)buf[1]+2));

                ptemp = pck;
                addr = pck.getAddress();

                break;

            case 5:
                String str2 = new String(pck.getData(),0,pck.getLength());
                ob.println("Controller: "+str2.substring(2,((int)buf[1]+2)));
                flag =1;
                break;

            case 2:

                int c=1;
                int length = (int)buf[1];
                c++;
                String s2 = new String(buf,0,buf.length);

                String addr[]=new String[length];
                for(int i=0;i<length;i++)
                {
                    int ln = (int)s2.charAt(c);

                    c++;
                    addr[i]=s2.substring(c,(c+ln));
                    c=c+ln;

                }
                list.add(new ArrayList<>());

                ob.println("In"+addr[1]);
                ob.println("Out:"+addr[0]);

                list.get(0).add(InetAddress.getByName(addr[1]));
                list.get(0).add(InetAddress.getByName(addr[0]));
                SendAck(InetAddress.getByName("Controller"));


        }

        if(!list.isEmpty()) {
            for(int i=0;i<list.size();i++)
            {


                if(list.get(i).contains(addr))
                {
                    InetAddress addr=list.get(i).get(0);
                    Send(ptemp,addr);

                }
                else
                    ob.println("No forwarding address found");
            }


        }
        else
        {
            if(flag == 2)
                ob.println("Controller's not running");
            if(flag == 1) {
                ob.println("Fetching the forwarding address");

                packets ob = new packets();
                DatagramPacket packet = ob.createPacket("Trinity");
                packet.setPort(51510);
                InetAddress ip = InetAddress.getByName("Controller");

                packet.setAddress(ip);

                this.ob.println("Sending packet");

                soc.send(packet);
            }
            else
           {
                packets obj = new packets();
                DatagramPacket PacketHello = obj.createPacket("Hello",(byte)4);
                PacketHello.setAddress(InetAddress.getByName("Controller"));
                PacketHello.setPort(51510);
                ob.println("Sending Hello to Controller");
                soc.send(PacketHello);
                flag =2;
            }

        }

    }

    public synchronized void Send(DatagramPacket packet,InetAddress ip) throws IOException {
        packet.setPort(51510);


        packet.setAddress(ip);

        ob.println("Sending packet");

        soc.send(packet);
    }
    public synchronized void SendAck(InetAddress ip) throws IOException
    {
        packets obj = new packets();
        DatagramPacket pck = obj.createPacket("Ack",(byte)3);
        pck.setAddress(ip);
        pck.setPort(51510);
        soc.send(pck);
    }
    void createFTable() throws UnknownHostException {
        map=new HashMap<String,ArrayList<ArrayList<InetAddress>>>();
        list = new ArrayList<ArrayList<InetAddress>>();
        //list.add(new ArrayList<>());

        //list.get(0).add(InetAddress.getByName("sender"));
        //list.get(0).add(InetAddress.getByName("Router2"));
        map.put("Trinity",list);
        String str = "Trinity";
        ArrayList<ArrayList<InetAddress>> list2=map.get(str);


    }
    public static void main(String args[]) throws SocketException, UnknownHostException {
        Router3 ob = new Router3();
        ob.createFTable();
        ob.start();
    }

}
