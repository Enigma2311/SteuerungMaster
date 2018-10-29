package fh_swf.mechatronik.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import fh_swf.mechatronik.classes.MovingCircleView;
import fh_swf.mechatronik.R;
import fh_swf.mechatronik.classes.SensorViewModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MovingCircleFragment extends Fragment {

    private MovingCircleView movingCircle;
    private float[] value;
    SensorViewModel model;
    Observer<float[]> obs;

    public MovingCircleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        model = ViewModelProviders.of(getActivity()).get(SensorViewModel.class);

        model.getAccelerationSensorListener().observe(getActivity(), obs = new Observer<float[]>() {
            @Override
            public void onChanged(@Nullable float[] floats) {
                value = floats;
                updateCircle();

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moving_circle, container, false);
        movingCircle = view.findViewById(R.id.drawingFrame);
        return view;

    }

    private void updateCircle() {

        movingCircle.updatePoint(value[1], value[0]);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        model.getAccelerationSensorListener().removeObserver(obs);
    }
}
