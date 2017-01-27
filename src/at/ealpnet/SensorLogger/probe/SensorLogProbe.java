package at.ealpnet.SensorLogger.probe;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author Patrick Schmidt
 * @version 1.0
 */
public class SensorLogProbe {

    private InetAddress server;
    private DatagramSocket socket;

    private String sensorName;

    public SensorLogProbe(InetAddress server){
        this.server=server;
    }

    public void run() {
        while(true){
            if(socket==null){
                try {
                    socket=new DatagramSocket();
                } catch (SocketException e) {
                    System.err.println("Error creating Socket: " + e);
                }
            }

            if(sensorName==null){
                byte[] sendData=new String("NAMERQST").getBytes();
                byte[] recvData=new byte[4];

                try {
                    System.out.print("Requesting name from server... ");
                    DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,server,9999);
                    socket.send(sendPacket);

                    DatagramPacket recvPacket= new DatagramPacket(recvData,recvData.length);
                    socket.receive(recvPacket);

                    sensorName=new String(recvPacket.getData());
                    sensorName=sensorName.substring(0,recvPacket.getLength());

                    System.out.println(sensorName);
                } catch (IOException e) {
                    System.err.println("Error requesting Sensor ID: " + e);
                }
            }

            if(sensorName!=null){
                byte[] data = getSensorData();

                try {
                    System.out.println("Send: " + new String(data));
                    DatagramPacket packet = new DatagramPacket(data,data.length,server,9999);
                    socket.send(packet);
                } catch (IOException e) {
                    System.err.println("Error sending packet: " + e);
                }

            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {}
        }
    }

    private byte[] getSensorData(){
        String data="[" + sensorName + "](time) " + String.valueOf(System.currentTimeMillis());
        return data.getBytes();
    }
}