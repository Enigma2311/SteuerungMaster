package fh_swf.mechatronik;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.net.*;

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


    /**
     * Konstruktor der einen Port und eine IP-Adresse übernimmt und zuweist, sowie die anderen Objekte initalisiert.
     *
     * @param serverPort Der von Nutzer für das Zielgerät angegeben Port.
     * @param ipAddress  Die von einem Nutzer für das Zielgerät angegebene IP-Adresse.
     */

    ConnectedWiFiThread(int serverPort, String ipAddress) {

        dataToBeSendWiFi = null;
        server_port = serverPort;
        try {
            udpSocket = new DatagramSocket(serverPort);         // Erstellung des DatagramSockets für die UDP-Übertragung
            local = InetAddress.getByName(ipAddress);           // Umwandlung der übergebenen IP-Adresse in das für die UDP-Datenpakete benötigte Format.
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

    }


    /**
     * run-Methode des Threads für die Ausführung der Datenübertragung.
     */

    @Override
    public void run() {
        write(dataToBeSendWiFi);
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

    void setDataToBeSendWiFi(String dataToBeSendWiFi) {
        this.dataToBeSendWiFi = dataToBeSendWiFi;
    }

    private void receiveVideo() {



    }

}