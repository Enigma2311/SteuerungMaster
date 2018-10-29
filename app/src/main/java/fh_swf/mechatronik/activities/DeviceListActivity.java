package fh_swf.mechatronik.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import fh_swf.mechatronik.R;
import fh_swf.mechatronik.model.OptionsModel;

import java.util.Set;

/**
 *  Aktivität die alle per Bluetooth gekoppelten Geräte in einer Liste anzeigt und die Möglichkeit bietet per Antippen eines
 *  Eintrags dessen Adresse zu speichern.
 *
 *  Created by Fabian Schäfer on 06.04.2017.
 *
 */

public class DeviceListActivity extends AppCompatActivity {

    private TextView textConnectionStatus;                      // Textanzeige wenn eine Verbindung hergestellt wird / eine Verbindung hergestellt wurde.
    private ListView pairedListView;                            // Liste für die Anzeige der gekoppelten Geräte.
    private BluetoothAdapter mBtAdapter;                        // Bluetooth-Adapter der benötigt wird um die gekoppelten Geräte anzuzeigen.
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;    // Array welches die gekoppelten Geräte aufnimmt.
    private String address;                                     // Bluetooth-MAC-Adresse des Zielgeräts.
    private OptionsModel optionsData;                           // Objekt für die Optionsdaten.


    /**
     *
     * Methode zur Initialisierung des Optionenfensters beim Aufruf.
     *
     * @param savedInstanceState
     *
     * savedInstanceState enthält den Zustand der Aktivität.
     * So können Daten wieder angezeigt werden wenn die Aktivität neu aufgebaut werden muss
     * (etwa bei Änderungen der Ausrichtung).
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        optionsData = OptionsModel.getInstance();

        textConnectionStatus = findViewById(R.id.connecting);
        textConnectionStatus.setTextSize(40);

        mPairedDevicesArrayAdapter = new ArrayAdapter<>(this, R.layout.device_name);

        pairedListView = findViewById(R.id.paired_devices);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);

        mDeviceClickListener();

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);

    }

    /**
     * Methode die Aufgerufen wird sobald die Aktivität wieder in den Vordergrund gerufen wird.
     * Es wird die Liste mit den gekoppelten Geräten angezeigt.
     */

    @Override
    public void onResume()
    {
        super.onResume();

        checkBTState();

        mPairedDevicesArrayAdapter.clear();

        textConnectionStatus.setText(" ");

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        if(pairedDevices.size() > 0)
        {
            findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
            for(BluetoothDevice device : pairedDevices)
            {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
            else
            {
                mPairedDevicesArrayAdapter.add("no devices paired");
            }
        }

    /**
     * Prüfung ob das genutzte Gerät Bluetooth unterstützt. Falls ja wird eine Abfrage gestartet ob Bluetooth aktiviert
     * werden soll (sollte es noch nicht aktiviert sein). Sollte das Gerät kein Bluetooth unterstützen wird ein Infotext
     * angezeigt und das Fenster geschlossen.
     */

    private void checkBTState()
    {

        mBtAdapter=BluetoothAdapter.getDefaultAdapter();
        if(mBtAdapter == null) {
            Toast.makeText(getBaseContext(), "Device does not support Bluetooth!", Toast.LENGTH_LONG).show();
            finish();
        }
        else
        {

            if(!mBtAdapter.isEnabled())
            {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }

        }

    }

    /**
     *  Listener der bei Antippen eines Eintrags in der Liste der gekoppelten Geräte die
     *  MAC-Adresse des gewählten Geräts in den Profildaten speichert und die Activity beendet.
     */

    private void mDeviceClickListener()
    {
        pairedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String info = ((TextView) view).getText().toString();
                address = info.substring(info.length() - 17);    //Extrahieren der Mac-Adresse
                optionsData.setBluetoothAddress(address);
                finish();
                }
        });
    }

    /**
     * Deaktivierung der zurück-Taste um Fehler oder versehentliches Betätigen zu vermeiden.
     */

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

}
