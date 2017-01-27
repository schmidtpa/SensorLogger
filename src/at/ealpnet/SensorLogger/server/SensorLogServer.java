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

    public SensorLogServer() throws SocketException {
        serverSocket=new DatagramSocket(9999);
    }

    public void run(){
        byte[] receiveData = new byte[32];
        DatagramPacket receivePacket;
        String remoteData;

        System.out.println("Startet Server on Port: " + serverSocket.getLocalPort());

        while(true) {
            receivePacket=new DatagramPacket(receiveData,receiveData.length);
            try {
                serverSocket.receive(receivePacket);
                remoteData=new String(receivePacket.getData());
                System.out.println("Received: "+ remoteData);
            } catch (IOException e) {
                System.err.println("Error receiving Packet: " + e);
            }

        }
    }
}