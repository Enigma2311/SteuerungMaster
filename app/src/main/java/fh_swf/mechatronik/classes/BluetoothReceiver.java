package fh_swf.mechatronik.classes;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import fh_swf.mechatronik.model.MainModel;
import fh_swf.mechatronik.model.OptionsModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Empfängerklasse für die Daten die das Zielgerät bereitstellt.
 */

public class BluetoothReceiver extends Thread {

    private InputStream mmInStream;             // Eingangsstream für die Daten des Zielgeräts
    private Handler msgHandle;                  // Handler-Objekt für die Verarbeitung der Daten.
    private ScheduledExecutorService scheduler; // Scheduler für die wiederholte Ausführung einer Methode.
    // Todo receiver testen

    /**
     * Konstruktor welche die Bluetooth-Verbindung übernimmt und Initialisierung vornimmt.
     *
     * @param bluetoothSocket
     * Socket mit der Bluetooth-Verbindung zum Endgerät.
     */

    BluetoothReceiver(BluetoothSocket bluetoothSocket)
    {

        scheduler = Executors.newScheduledThreadPool(1);
        InputStream tmpIn = null;
        msgHandle = new MsgHandler();


     if(bluetoothSocket != null) {

         try {
             tmpIn = bluetoothSocket.getInputStream();
         }
         catch (IOException e)
         {
             e.printStackTrace();
         }
         mmInStream = tmpIn;
     }
    }

    /**
     * run-Methode des Threads.
     */

    @Override
    public void run() { receiveData(); }

    /**
     * Methode welche die Daten empfängt und an den Message-Handle weitergibt.
     */

    public void receiveData()
    {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                byte[] buffer = new byte[256];
                int bytes;

                while (true)
                {
                    try {
                        bytes = mmInStream.read(buffer);
                        msgHandle.obtainMessage(1,bytes,-1,buffer);
                    }
                    catch (IOException e)
                    {
                        break;
                    }

                }
            }
        },0,1, TimeUnit.SECONDS);
    }

    /**
     * Handler-Klasse welche die ankommenden Daten verarbeitet.
     */

    static class MsgHandler extends Handler {

        StringBuilder sb = new StringBuilder();
        MainModel data = MainModel.getInstance();

            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                        byte[] readBuf = (byte[]) msg.obj;
                        String strIncom = new String(readBuf, 0, msg.arg1);
                        sb.append(strIncom);
                        int endOfLineIndex = sb.indexOf("\r\n");
                        if (endOfLineIndex > 0) {
                            String sbprint = sb.substring(0, endOfLineIndex);
                            sb.delete(0, sb.length());
                            data.setReceivedMessage(sbprint);
                        }
                        break;
                }
            }
    }

    /**
     * Methode zum sauberen Beenden der Empfangsklasse.
     */

    public void cancel()
    {
        try {
            mmInStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        scheduler.shutdown();
    }


}
