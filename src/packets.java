import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;

public class packets {
    private byte netID = 1;
    private byte combination = 2;
    private byte Ack = 3;
    private byte Check = 4;
    private byte Response = 5;
    DatagramPacket createPacket(String str)
    {
        byte []buf = new byte[1280];
        buf[0]=netID;
        buf[1]=(byte)str.length();
        for(int i=0;i<str.length();i++)
            buf[i+2]=(byte)str.charAt(i);

        DatagramPacket pck = new DatagramPacket(buf, buf.length);
        return  pck;


    }
    DatagramPacket createPacket(String str,byte type)
    {
        byte []buf = new byte[1280];
        buf[0]=type;
        buf[1]=(byte)str.length();
        for(int i=0;i<str.length();i++)
            buf[i+2]=(byte)str.charAt(i);

        DatagramPacket pck = new DatagramPacket(buf, buf.length);
        return  pck;


    }
    DatagramPacket createPacket(String str[])
    {
        byte []buf = new byte[1280];
        int c=0;
        buf[c]=combination;
        c++;
        buf[c]=(byte)str.length;
        c++;
        for(int i=0;i<str.length;i++)
        {



            byte[] tmp=str[i].getBytes();

            Integer a = tmp.length;
            buf[c]= a.byteValue();
            c++;
            int temp = c;
            for(int j=0;j<tmp.length;j++)
            {

                buf[temp+j]=tmp[j];

                c++;
            }

        }
        String s2 = new String(buf,0,buf.length);

        DatagramPacket pck = new DatagramPacket(buf, buf.length);
        return  pck;

    }
}
