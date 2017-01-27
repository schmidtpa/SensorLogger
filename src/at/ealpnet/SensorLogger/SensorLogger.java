package at.ealpnet.SensorLogger;

import at.ealpnet.SensorLogger.probe.SensorLogProbe;
import at.ealpnet.SensorLogger.server.SensorLogServer;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author Patrick Schmidt
 * @version 1.0
 */
public class SensorLogger {

    private static Scanner scanner;
    private static String line;

    public static void main(String[] args) {
        scanner=new Scanner(System.in);

        //Client or Server
        do{
            System.out.println("(s)erver or (p)robe?");
            line=scanner.nextLine().toLowerCase();
        }while(!line.equals("p") && !line.equals("s"));

        //Starting Server
        if(line.equals("s")){
            try {
                new SensorLogServer().run();
            } catch (SocketException e) {
                System.out.println("Error starting server: " + e);
            }
        }

        //Starting Probe
        if(line.equals("p")){
            InetAddress server=null;

            do{
                System.out.println("Server Hostname?");
                line=scanner.nextLine().toLowerCase();

                try {
                    server = InetAddress.getByName(line);
                } catch (UnknownHostException e) {
                    System.out.println("Unkown Hostname: " + line);
                }

            }while(server==null);
            new SensorLogProbe(server).run();
        }
    }

}