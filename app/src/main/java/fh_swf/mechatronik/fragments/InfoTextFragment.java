package fh_swf.mechatronik.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fh_swf.mechatronik.model.MainModel;
import fh_swf.mechatronik.R;
import fh_swf.mechatronik.classes.SensorViewModel;
import fh_swf.mechatronik.model.OptionsModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoTextFragment extends Fragment {

    private TextView infoTextTop;
    private TextView infoTextMiddle;
    private TextView infoTextBottom;
    private TextView infoTextTargetDevice;
    private ScheduledExecutorService refreshTimer;
    private MainModel data = MainModel.getInstance();
    private OptionsModel optionsData = OptionsModel.getInstance();
    SensorViewModel model;
    Observer<float[]> obs;

    public InfoTextFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        refreshTimer = Executors.newScheduledThreadPool(3);

        initInfoText();

        model = ViewModelProviders.of(getActivity()).get(SensorViewModel.class);

        obs = new Observer<float[]>() {
            @Override
            public void onChanged(@Nullable float[] floats) {
                sensorValueSettings(floats);
            }
        };

        model.getAccelerationSensorListener().observe(getActivity(),obs);

        System.out.println("ONACTIVITYCREATED");

        if(optionsData.getProfileType() == 2)
        {
            infoTextBottom.setVisibility(View.INVISIBLE);
        }

        refreshReceivedMessage();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_info_text, container, false);
        System.out.println("ONCREATEVIEW");
        return v;
    }

    @Override
    public void onPause()
    {
        super.onPause();
       // refreshTimer.shutdown();
        System.out.println("ONPAUSE");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        System.out.println("ONRESUME");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        refreshTimer.shutdown();
        refreshTimer = null;
        model.getAccelerationSensorListener().removeObserver(obs);
        model = null;
        System.out.println("ONDESTROY");
    }

    private void initInfoText()
    {
        infoTextTop = getView().findViewById(R.id.infoTextTop);
        infoTextBottom = getView().findViewById(R.id.infoTextBottom);
        infoTextMiddle = getView().findViewById(R.id.infoTextMiddle);
        infoTextTargetDevice = getView().findViewById(R.id.infoTextTarget);
        scaleTextSize();

        infoTextTop.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        infoTextMiddle.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        infoTextBottom.setText("");
        infoTextTargetDevice.setText("");

    }


    private void scaleTextSize()
    {
        TextViewCompat.setAutoSizeTextTypeWithDefaults(infoTextTop, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(infoTextBottom, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(infoTextMiddle, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(infoTextTargetDevice, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
    }

    private void sensorValueSettings(float[] floats)
    {

        if(data.isBlocked())
            return;

        float valX = ((float) (floats[0] / 9.81)) *333;
        float valY = ((float) (floats[1] / 9.81)) *333;
//
//        int accelX = (int)(xValue*1.5f);
//        int accelY = (int)(yValue*1.5f);

//        float valX = floats[0] / 9.81f * 100;
//        float valY = floats[1] / 9.81f * 100;

//        float valX = floats[0];
//        float valY = floats[1];
//
//        valY = (float) Math.toDegrees(valY);
//        valX = (float) Math.toDegrees(valX);

//        valY = valY/3.33f;
//        valX = valX/3.33f;

//        valY = (valY / 9.81f);
//        valX = (valX / 9.81f);

        int accelX = (int)valX*-1;
        int accelY = (int)valY;


//        if(accelX > 100)
//            accelX = 100;
//        if(accelX < -100)
//            accelX = -100;
//        if(accelY > 100)
//            accelY = 100;
//        if(accelY < -100)
//            accelY = -100;

//        data.setAccel_x((byte)accelX);
//        data.setAccel_y((byte)accelY);

        sensorZero(accelX,accelY);
//
//        infoTextTop.setText(String.valueOf(accelX));
//        infoTextMiddle.setText(String.valueOf(accelY));
        infoTextBottom.setText(showMButtonValues(data.getRb_g1()));

    }

    private void refreshMessage()
    {
        infoTextTargetDevice.setText(data.getReceivedMessage());
    }

    private void refreshReceivedMessage()
    {
                refreshTimer.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshMessage();
                                System.out.println("REFRESH");
                            }
                        });
                    }
                },0,2, TimeUnit.SECONDS);
    }


    private String showMButtonValues(int activeMButton) {
        switch (activeMButton) {
            case 1:
                return "M1X= " + data.getRb_g1_m1_cross_left_right() + ", M1Y= " + data.getRb_g1_m1_cross_up_down() + ", AS= " + data.getRb_g1_m1_glider_AS() + ", AZ= " + data.getRb_g1_m1_glider_AZ();

            case 2:
                return "M2X= " + data.getRb_g1_m2_cross_left_right() + ", M2Y= " + data.getRb_g1_m2_cross_up_down() + ", AS= " + data.getRb_g1_m2_glider_AS() + ", AZ= " + data.getRb_g1_m2_glider_AZ();

            case 3:
                return "M3X= " + data.getRb_g1_m3_cross_left_right() + ", M3Y= " + data.getRb_g1_m3_cross_up_down() + ", AS= " + data.getRb_g1_m3_glider_AS() + ", AZ= " + data.getRb_g1_m3_glider_AZ();

            default:
                return "InfoText für CROSS_AX, CROSS_AY, GLIDER_AS und GLIDER AZ";
        }
    }

    private void sensorZero(int accelX, int accelY)
    {

        switch (optionsData.getZero()) {

            case 0:

                if (accelY > 100) {
                    accelY = 100;
                } else if (accelY < -100) {
                    accelY = -100;
                }
                if (accelX > 100) {
                    accelX = 100;
                } else if (accelX < -100) {
                    accelX = -100;
                }


                data.setAccel_x((byte) accelX);
                data.setAccel_y((byte) accelY);

                infoTextTop.setText(String.valueOf(accelX));
                infoTextMiddle.setText(String.valueOf(accelY));

//                accelYText.setText("Y: " + data.getAccel_y());
//                accelXText.setText("X: " + data.getAccel_x());


                break;

            case 15:

                accelX = accelX + 50;

                if (accelY > 100) {
                    accelY = 100;
                } else if (accelY < -100) {
                    accelY = -100;
                }
                if (accelX > 100) {
                    accelX = 100;
                } else if (accelX < -100) {
                    accelX = -100;
                }

                data.setAccel_x((byte) accelX);
                data.setAccel_y((byte) accelY);

                infoTextTop.setText(String.valueOf(accelX));
                infoTextMiddle.setText(String.valueOf(accelY));

//                accelYText.setText("Y: " + data.getAccel_y());
//                accelXText.setText("X: " + data.getAccel_x());

                break;

            case 30:

                accelX = accelX + 100;


                if (accelY > 100) {
                    accelY = 100;
                } else if (accelY < -100) {
                    accelY = -100;
                }
                if (accelX > 100) {
                    accelX = 100;
                } else if (accelX < -100) {
                    accelX = -100;
                }

                data.setAccel_x((byte) accelX);
                data.setAccel_y((byte) accelY);

                infoTextTop.setText(String.valueOf(accelX));
                infoTextMiddle.setText(String.valueOf(accelY));

//                accelYText.setText("Y: " + data.getAccel_y());
//                accelXText.setText("X: " + data.getAccel_x());

                break;

                default:

                    if (accelY > 100) {
                        accelY = 100;
                    } else if (accelY < -100) {
                        accelY = -100;
                    }
                    if (accelX > 100) {
                        accelX = 100;
                    } else if (accelX < -100) {
                        accelX = -100;
                    }

                    data.setAccel_x((byte) accelX);
                    data.setAccel_y((byte) accelY);

                    infoTextTop.setText(String.valueOf(accelX));
                    infoTextMiddle.setText(String.valueOf(accelY));

                    break;

        }
    }

}
