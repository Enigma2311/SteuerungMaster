package fh_swf.mechatronik.classes;

import fh_swf.mechatronik.model.MainModel;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * Thread-Auslagerung für die UDP-Datenübertragung über eine Kabellose Verbindung.
 *
 * Created by Fabian Schäfer on 19.04.2017.
 */
public class ConnectedWiFiThread extends Thread {

    private int server_port;            // Port des Zielgeräts
    private DatagramSocket udpSocket;   // UDP-Socket für die Verbindungsherstellung.
    private InetAddress local;          // IP-Adresse an die die Daten gesendet werden.
    private String dataToBeSendWiFi;    // Datensatz der Übetragen wird.
    private ScheduledExecutorService scheduler; // Scheduler zur Wiederholung der Datenübertragung in einem Intervall.


    /**
     * Konstruktor der einen Port und eine IP-Adresse übernimmt und zuweist, sowie die anderen Objekte initalisiert.
     */

    ConnectedWiFiThread(DatagramSocket udp, String ip) {

        dataToBeSendWiFi = null;
        server_port = 1234;
        try {
            udpSocket = udp;         // Erstellung des DatagramSockets für die UDP-Übertragung
            local = InetAddress.getByName(ip);           // Umwandlung der übergebenen IP-Adresse in das für die UDP-Datenpakete benötigte Format.
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        scheduler = Executors.newScheduledThreadPool(1);

    }


    /**
     * run-Methode des Threads für die Ausführung der Datenübertragung.
     */

    @Override
    public void run() {

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                setDataToBeSendWiFi(MainModel.dataToStringForTransfer());
                write(dataToBeSendWiFi);
            }
        },0,1, TimeUnit.SECONDS);


    }

    /**
     * Methode welche den Daten-String in Bytes zerlegt und in einem Byte-Array speichert.
     * Das Byte-Array wird in eine UDP-Datenpaket verpackt und per UDP-Verbindung an das Zielgerät gesandt.
     *
     * @param data Der zu sendende Datensatz.
     */

    private void write(String data) {
        int msg_length = data.length();
        byte[] message = data.getBytes();
        DatagramPacket p = new DatagramPacket(message, msg_length, local, server_port);   //Erstellung eines UDP-Paketes, welches den Datensatz enthält.
        try {
            udpSocket.send(p);              //Übertragung der Daten.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setter für den zu übertragenden Datensatz-String.
     *
     * @param dataToBeSendWiFi Der zu übertragende Datensatz.
     */

    private void setDataToBeSendWiFi(String dataToBeSendWiFi) {
        this.dataToBeSendWiFi = dataToBeSendWiFi;
    }

    /**
     * Getter für das Socket.
     * @return
     * Socket mit dem gebundenen Port.
     */

    public DatagramSocket getUdpSocket() {
        return udpSocket;
    }

    /**
     * Methode um den scheduler zu beenden und den Thread sauber zu schließen.
     */

    void cancel()
    {
        scheduler.shutdown();
    }

}