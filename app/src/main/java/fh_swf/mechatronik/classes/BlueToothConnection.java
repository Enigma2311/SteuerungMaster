package fh_swf.mechatronik.classes;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;
import fh_swf.mechatronik.model.OptionsModel;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Hilfs-Klasse zur Herstellung einer Bluetooth-Verbindung.
 *
 * Created by Fabian Schäfer on 04.04.2017.
 */
public class BlueToothConnection extends Thread{

    private BluetoothAdapter mBluetoothAdapter;  // Initialisierung eines Bluetooth-Adapter-Objekts, welches zur Herstellung einer Bluetooth-Verbindung benötigt wird.
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");   // Zuweisung der StandardUUID (Universally Unique Identifier) für eine Verbindung zu einem Mikrocontroller.
    private BluetoothDevice blueDevice;          // Digitales Bluetooth-Device, notwendig für die Datenübertragung.
    private BluetoothSocket blueSocket;          // Socket für die Herstellung der Bluettooth-Verbindung.
    private ConnectedBluetoothThread connected;  // Separater Thread der zur Datenübertragung via Bluetooth dient und den Hauptthread entlastet.
    private OptionsModel optionsData;            // Objekt der Optionsdaten, notwendig um die Übertragungszeitfrequenz zu erhalten.
    private BluetoothReceiver receiver;          // receiver-Objekt für den Datenempfang vom Endgerät.
    private Context mainContext;                 // Kontext der Applikation zur Anzeige von Fehlermeldungen.
    private boolean success = true;              // Hilfsvariable zur Verbindungsprüfung.

    /**
     *
     * Konstruktor der eine Liste von gekoppelten Geräten entgegen nimmt.
     * Dient zur Initialisierung der benötigten Variablen.
     *
     * @param ctx
     *
     * Liste mit gekoppelten Geräten.
     *
     */

    public BlueToothConnection(Context ctx)
    {
        mBluetoothAdapter = null;
        blueDevice = null;
        blueSocket = null;
        this.optionsData = OptionsModel.getInstance();
        mainContext = ctx;
    }


    /**
     * Ausführungsmethode des Threads, welche die Methoden zur Verbindungsherstellung aufruft.
     */

    @Override
    public void run()
    {
        createDeviceAndConnect();
        writeManager();
    }

    /**
     *  Methode zur Erstellung eines digitalen Geräts, welches intern benötigt wird, um die Verbindung zu einem realen Gerät aufzubauen.
     *  Nach der Erstellung wird unter Verwendung der MAC-Adresse des Zielgeräts versucht eine Bluetooth-Verbindung aufzubauen.
     */

    private void createDeviceAndConnect()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();                   // Erstellung des Bluetooth-Adapters

        try {
            blueDevice = mBluetoothAdapter.getRemoteDevice(optionsData.getBluetoothAddress());       // Erstellung des digitalen Mobilgeräts.
        }
        catch (IllegalArgumentException illEx)
        {
            Toast.makeText(mainContext, "Fehler in der Bluetooth Adresse, bitte prüfen!", Toast.LENGTH_LONG).show();
            success = false;
            return;
        }
        try {
            blueSocket = createBluetoothSocket(blueDevice);                         // Erstellung des BluetoothSocket.
        } catch (IOException e) {
            success = false;
            e.printStackTrace();

        }

        try {
            blueSocket = (BluetoothSocket) blueDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(blueDevice, 1);  // Erstellung des Sockets.
            blueSocket.connect();       // Erstellung der Verbindung.
            writeManager();
        } catch (IOException e) {
            //Toast.makeText(mainContext, "Verbindung konnte nicht hergestellt werden. Prüfen Sie ob das Zielgerät erreichbar und Bluetooth aktiviert ist!",Toast.LENGTH_LONG).show();
            e.printStackTrace();
            try {

                blueSocket.connect();
                return;

            } catch (IOException e1) {
                Toast.makeText(mainContext, "Verbindung konnte nicht hergestellt werden. Prüfen Sie ob das Zielgerät erreichbar und Bluetooth aktiviert ist!",Toast.LENGTH_LONG).show();
                success = false;
                e1.printStackTrace();
            }

            try {
                blueSocket.close();
            } catch (IOException e1) {
                Toast.makeText(mainContext, "Verbindung konnte nicht hergestellt werden. Prüfen Sie ob das Zielgerät erreichbar und Bluetooth aktiviert ist!",Toast.LENGTH_LONG).show();
                success = false;
                e1.printStackTrace();
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            success = false;
            Toast.makeText(mainContext, "Verbindung konnte nicht hergestellt werden. Prüfen Sie ob das Zielgerät erreichbar und Bluetooth aktiviert ist!",Toast.LENGTH_LONG).show();
        }


    }

    /**
     *
     * Es wird ein BluetoothSocket unter Nutzung der UUID erstellt und zurück gegeben.
     *
     * @param device
     *
     * Digitales Gerät welches den Socket erstellt.
     *
     * @return
     *
     * Der erstellte Bluetooth-Socket.
     *
     * @throws IOException
     *
     * Exception für den Fall das die Erstellung fehlschlägt.
     *
     */


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);

    }

    /**
     * Methode zur Verwaltung der Datenübertragung.
     * Die Daten werden nach einem eingestellten Intervall vorbereitet, dies geschieht durch Nutzung eines Timers, und dann über
     * den "ConnectedBluetoothThread" an das verbundene Gerät übetragen.
     */

    private void writeManager() {

        if (blueSocket.isConnected()) {
            connected = new ConnectedBluetoothThread(blueSocket);
            receiver = new BluetoothReceiver(blueSocket);
            connected.start();
            receiver.start();
            }
    }


    /**
     * Getter für den BluetoothSocket
     * @return
     *
     * BluetoothSocket-Objekt.
     */

   public BluetoothSocket getBlueSocket() {
        return blueSocket;
    }

    public void btDisconnect()
    {
        connected.cancel();
        receiver.cancel();
        if(blueSocket != null) {
            try {

                blueSocket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        connected = null;
        receiver = null;

    }

    /**
     * Getter für die success-Variable.
     * @return
     * Wert der Variable success.
     */

    public boolean getSuccess()
    {
        return success;
    }
}
