package fh_swf.mechatronik.classes;

import fh_swf.mechatronik.model.OptionsModel;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * Klasse für die Datenübertragung per Wireless-Network.
 *
 * Created by Fabian Schäfer on 23.04.2017.
 */

public class WiFiConnection {

    private ConnectedWiFiThread wifiConnect;   // Instanz-Objekt für den Thread der WLAN-Übertragung
    private OptionsModel optionsData;          // Objekt für die Optionsdaten zum Zugriff auf das Übertragungsintervall.
    private WifiReceiver receiver;
    private DatagramSocket udpSocket;

    /**
     * Konstruktor der die Variablen/Objekte initialisiert.
     */

    public WiFiConnection()
    {
        optionsData = OptionsModel.getInstance();
        wifiWriterManager();
    }

    /**
     * Methode zur Verwaltung der Datenübertragung.
     * Die Daten werden nach einem eingestellten Intervall vorbereitet, dies geschieht durch Nutzung eines Timers, und dann über
     * den "ConnectedWiFiThread" an das verbundene Gerät übetragen.
     */

    private void wifiWriterManager() {

        try {
            udpSocket = new DatagramSocket(optionsData.getPort());
        } catch (SocketException e) {
            e.printStackTrace();
        }

        wifiConnect = new ConnectedWiFiThread(udpSocket, optionsData.getIpAddress());

        try {
            receiver = new WifiReceiver(udpSocket);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        if(receiver != null) {
            receiver.start();
        }

//      wifiConnect.setDataToBeSendWiFi(MainModel.dataToStringForTransfer());
        if(wifiConnect != null) {
            wifiConnect.start();
        }




    }

    public ConnectedWiFiThread getWifiConnect() {
        return wifiConnect;
    }

    public void stop() {
        wifiConnect.cancel();
//        try {
//            receiver.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        receiver.cancel();
//        try {
//            wifiConnect.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        wifiConnect = null;
        receiver = null;
    }




}
