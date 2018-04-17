package fh_swf.mechatronik;

import android.bluetooth.BluetoothSocket;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * Thread-Auslagerung der Bluetooth-Datenübertragung.
 *
 * Created by Fabian Schäfer on 16.04.2017.
 */

public class ConnectedBluetoothThread extends Thread {

    private final OutputStream mOutstream; // Output-Stream für die Übersendung eines Datensatzes.

    private String dataToBeSend;    // Datensatz der gesendet werden soll.

    /**
     * Konstruktor zu Initialisierung der benötigten Variablen.
     *
     * @param socket
     *
     * Bluetooth-Socket der die Verbindung zum Endgerät enthält.
     *
     */

    ConnectedBluetoothThread(BluetoothSocket socket)
    {
        OutputStream tmpOut = null;
        dataToBeSend = null;

        try {
            tmpOut = socket.getOutputStream();  //Erstellung des Output-Streams zur Datenübertragung

        } catch (IOException e) {
            e.printStackTrace();
        }


        mOutstream = tmpOut;
    }

    /**
     * Run-Methode des Threads, welche die Daten in diesem separaten Thread überträgt.
     */

    public void run(){

            write(dataToBeSend);
    }

    /**
     * Es wird der zu Übertragende String in Bytes zerlegt und in einem Byte-Array gespeichert. Dieses Byte-Array wird
     * gesendet und auf dem Endgerät wieder als String zusammengesetzt.
     *
     * @param input
     *
     * Der zu übertragende Datensatz.
     */

    private void write(String input){

        byte[] msgBuffer = input.getBytes();

        try {
            mOutstream.write(msgBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Setter für den zu übertragenden Datensatz-String.
     *
     * @param dataToBeSend
     *
     * Der zu übertragende Datensatz.
     */

    void setDataToBeSend(String dataToBeSend) {
        this.dataToBeSend = dataToBeSend;
    }
}
