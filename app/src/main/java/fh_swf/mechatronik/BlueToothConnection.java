package fh_swf.mechatronik;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
class BlueToothConnection {

    private BluetoothAdapter mBluetoothAdapter;  // Initialisierung eines Bluetooth-Adapter-Objekts, welches zur Herstellung einer Bluetooth-Verbindung benötigt wird.
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");   // Zuweisung der StandardUUID (Universally Unique Identifier) für eine Verbindung zu einem Mikrocontroller.
    private DeviceListActivity devices;          // Liste der per Bluetooth gekoppelten Geräte.
    private BluetoothDevice blueDevice;          // Digitales Bluetooth-Device, notwendig für die Datenübertragung.
    private BluetoothSocket blueSocket;          // Socket für die Herstellung der Bluettooth-Verbindung.
    private ConnectedBluetoothThread connected;  // Separater Thread der zur Datenübertragung via Bluetooth dient und den Hauptthread entlastet.
    private OptionsModel optionsData;            // Objekt der Optionsdaten, notwendig um die Übertragungszeitfrequenz zu erhalten.

    /**
     *
     * Konstruktor der eine Liste von gekoppelten Geräten entgegen nimmt.
     * Dient zur Initialisierung der benötigten Variablen.
     *
     * @param devices
     *
     * Liste mit gekoppelten Geräten.
     *
     */

    BlueToothConnection(DeviceListActivity devices)
    {
        this.devices = devices;
        mBluetoothAdapter = null;
        blueDevice = null;
        blueSocket = null;
        this.optionsData = OptionsModel.getInstance();

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
        blueDevice = mBluetoothAdapter.getRemoteDevice(devices.getAddress());       // Erstellung des digitalen Mobilgeräts.
        try {
            blueSocket = createBluetoothSocket(blueDevice);                         // Erstellung des BluetoothSocket.
        } catch (IOException e) {
            e.printStackTrace();

        }

        try {
            blueSocket = (BluetoothSocket) blueDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(blueDevice, 1);  // Erstellung des Sockets.
            blueSocket.connect();       // Erstellung der Verbindung.
        } catch (IOException e) {
            e.printStackTrace();
            try {

                blueSocket.connect();

            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                blueSocket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
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
        }
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run () {
                        connected.setDataToBeSend(MainModel.dataToStringForTransfer());
                        connected.run();
                    }
                },0,optionsData.getTransmissionTime());
            }


    /**
     * Getter für den BluetoothSocket
     * @return
     *
     * BluetoothSocket-Objekt.
     */

    BluetoothSocket getBlueSocket() {
        return blueSocket;
    }
}
