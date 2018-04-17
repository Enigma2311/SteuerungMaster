package fh_swf.mechatronik;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * Klasse für die Datenübertragung per Wireless-Network.
 *
 * Created by Fabian Schäfer on 23.04.2017.
 */

class WiFiConnection {

    private ConnectedWiFiThread wifiConnect;   // Instanz-Objekt für den Thread der WLAN-Übertragung
    private OptionsModel optionsData;          // Objekt für die Optionsdaten zum Zugriff auf das Übertragungsintervall.


    /**
     * Konstruktor der die Variablen/Objekte initialisiert.
     */

    WiFiConnection()
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

        wifiConnect = new ConnectedWiFiThread(optionsData.getPort(), optionsData.getIpAddress());

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                wifiConnect.setDataToBeSendWiFi(MainModel.dataToStringForTransfer());
                wifiConnect.run();
            }
        },0, optionsData.getTransmissionTime());

    }





}
