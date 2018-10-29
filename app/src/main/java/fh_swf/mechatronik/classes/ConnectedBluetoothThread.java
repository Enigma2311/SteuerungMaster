package fh_swf.mechatronik.classes;

import android.bluetooth.BluetoothSocket;
import fh_swf.mechatronik.model.MainModel;
import fh_swf.mechatronik.model.OptionsModel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * Thread-Auslagerung der Bluetooth-Datenübertragung.
 *
 * Created by Fabian Schäfer on 16.04.2017.
 */

public class ConnectedBluetoothThread extends Thread {

    private final OutputStream mOutstream;        // Output-Stream für die Übersendung eines Datensatzes.
    private String dataToBeSend;                  // Datensatz der gesendet werden soll.
    private ScheduledExecutorService scheduler;   // Scheduler zur Wiederholung der Datenübertragung in einem Intervall.
    private OptionsModel optionsModel = OptionsModel.getInstance(); //Objekt der Profildaten.

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
        scheduler = Executors.newScheduledThreadPool(1);
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

    public void run()
    {
        if(isInterrupted())
            return;
        else
            scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    setDataToBeSend(MainModel.dataToStringForTransfer());
                    write(dataToBeSend);
                }
            },0, optionsModel.getTransmissionTime(), TimeUnit.MILLISECONDS);
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
            try {
                mOutstream.close();
                return;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
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

    private void setDataToBeSend(String dataToBeSend) {
        this.dataToBeSend = dataToBeSend;
    }

    public void cancel()
    {
        this.interrupt();
        scheduler.shutdown();
        try {
            mOutstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
