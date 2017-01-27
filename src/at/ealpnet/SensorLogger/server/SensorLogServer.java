package at.ealpnet.SensorLogger.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author Patrick Schmidt
 * @version 1.0
 */
public class SensorLogServer {

    private DatagramSocket serverSocket;

    private int sensorUuid;

    public SensorLogServer() throws SocketException {
        serverSocket=new DatagramSocket(9999);
        sensorUuid=0;
    }

    public void run(){
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket;
        String remoteData;

        System.out.println("Startet Server on Port: " + serverSocket.getLocalPort());

        while(true) {
            receivePacket=new DatagramPacket(receiveData,receiveData.length);
            try {
                serverSocket.receive(receivePacket);
                remoteData=new String(receivePacket.getData());
                remoteData=remoteData.substring(0,receivePacket.getLength());

                //Init probe with uniqe ID
                if(remoteData.startsWith("NAMERQST")){
                    sensorUuid++;

                    String sensorName="P" + sensorUuid;
                    byte[] sendData = sensorName.getBytes();

                    System.out.println("Name " + sensorUuid + " for " + receivePacket.getSocketAddress().toString().substring(1));
                    DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,receivePacket.getSocketAddress());
                    serverSocket.send(sendPacket);
                } else {
                    System.out.println(remoteData);
                }
            } catch (IOException e) {
                System.err.println("Error receiving Packet: " + e);
            }

        }
    }
}