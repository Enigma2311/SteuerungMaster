package fh_swf.mechatronik.fragments;


import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import fh_swf.mechatronik.model.MainModel;
import fh_swf.mechatronik.model.OptionsModel;
import fh_swf.mechatronik.R;
import fh_swf.mechatronik.classes.BitManipulation;
import fh_swf.mechatronik.classes.BlueToothConnection;
import fh_swf.mechatronik.classes.WiFiConnection;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SwitchFragment extends Fragment {

    private SwitchCompat powerSwitch;
    private SwitchCompat connectionSwitch;
    private SwitchCompat cameraSwitch;
    private OptionsModel optionsData;
    private MainModel data;
    private BlueToothConnection blueTooth;
    private WiFiConnection wifi;
    private FragmentManager fragmentTransaction;

    public SwitchFragment() {
        // Required empty public constructor
    }

    public BroadcastReceiver bluetooth_connection_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_ACL_DISCONNECTED))
            {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Verbindung unterbrochen!");
                alertDialog.setMessage("Die Verbindung mit Gerät " + optionsData.getBluetoothAddress() + " wurde unterbrochen!");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                alertDialog.show();
                connectionSwitch.setChecked(false);
            }

            if(action.equals(BluetoothDevice.ACTION_ACL_CONNECTED))
            {
                Toast.makeText(getActivity(), "Verbindung mit " + optionsData.getBluetoothAddress() + " erfolgreich hergestellt.", Toast.LENGTH_LONG).show();
                connectionSwitch.setChecked(true);
            }
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_switch, container, false);

        wifi = null;
        blueTooth = null;
        fragmentTransaction = getActivity().getSupportFragmentManager();

        initSwitch(view);

        if(optionsData.getProfileType() == 2)
        {
            cameraSwitch.setVisibility(View.INVISIBLE);
        }

        return view;

    }

    @Override
    public void onResume()
    {
        super.onResume();

        IntentFilter blueFilter = new IntentFilter();
        blueFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        blueFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);

        getActivity().registerReceiver(this.bluetooth_connection_receiver,blueFilter);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try {
            getActivity().unregisterReceiver(this.bluetooth_connection_receiver);
        }
        catch (IllegalArgumentException ex)
        {}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(this.bluetooth_connection_receiver);
        }
        catch (IllegalArgumentException ex)
        {}
    }

    private void initSwitch(View view) {

        optionsData = OptionsModel.getInstance();
        data = MainModel.getInstance();

        powerSwitch = view.findViewById(R.id.switchPower);
        connectionSwitch = view.findViewById(R.id.switchConnection);
        cameraSwitch = view.findViewById(R.id.switchCamera);

        setPowerSwitchAction();
        setConnectionSwitchAction();
        setCameraSwitchAction();
        System.out.println("Type: " + optionsData.getTransmissionType());

    }

    private void setPowerSwitchAction() {
        powerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                byte test = data.getButtons();
                if (isChecked) {
                    test = BitManipulation.setBit(test, 4);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                } else {
                    test = BitManipulation.clearBit(test, 4);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                }

            }
        });
    }

    private void setConnectionSwitchAction() {
        connectionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && (optionsData.getTransmissionType() == 1)){
                    //TODO Verbindungen Testen!!!!
                    blueTooth = new BlueToothConnection(getActivity());
                    blueTooth.start();
                    if(blueTooth.getBlueSocket() != null) {
                        if (!blueTooth.getBlueSocket().isConnected()) {
                            blueTooth = null;
                        }
                    }
                    if(blueTooth!=null)
                    if(!blueTooth.getSuccess())
                        connectionSwitch.setChecked(false);
                }


                else if (isChecked && (optionsData.getTransmissionType() == 2))
                {
                    wifi = new WiFiConnection();
                }
                else if(!isChecked)
                {
                    if(wifi != null) {
                        wifi.stop();
                        wifi = null;
                    }
                    if(blueTooth != null)
                        blueTooth.btDisconnect();
                        blueTooth = null;

                }
            }
        });

    }

    private void setCameraSwitchAction()
    {
        cameraSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    CameraStreamFragment camera = CameraStreamFragment.newInstance();
                    fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                    fragmentTransaction.beginTransaction().replace(R.id.replacementFrameInfoCamera, camera).commit();

                }
                else
                {
                    InfoTextFragment info = new InfoTextFragment();
                    fragmentTransaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
                    fragmentTransaction.beginTransaction().replace(R.id.replacementFrameInfoCamera,info).commit();
                }
            }
        });
    }
}
