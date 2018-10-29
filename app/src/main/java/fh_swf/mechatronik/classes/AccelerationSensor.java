package fh_swf.mechatronik.classes;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * LiveData-Klasse, welche die Möglichkeit bietet Sensordaten für andere Klassen zur
 * Verfügung zu stellen.
 *
 * Created by Fabian Schaefer on 10.07.2018.
 */
public class AccelerationSensor extends LiveData<float[]> {

    private LowPassFilter lowPass; //Objekt zur Filterung der SensorDaten.

    /**
     * Konstruktor für die Klasse.
     * Der SensorManager für die Daten des Beschleunigungssensors wird initialisiert.
     *
     * @param context
     */

  public AccelerationSensor(Context context)
    {

        lowPass = new LowPassFilter();

        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        assert mSensorManager != null;
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SimpleSensorListener listener = new SimpleSensorListener();

        mSensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);

    }

    /**
     * Interne Klasse, welche einen SensorEventListener implementiert mit dessen Hilfe die
     * Daten des Beschleunigungssensors abgerufen werden.
     */

    private class SimpleSensorListener implements SensorEventListener{

        @Override
        public void onSensorChanged(SensorEvent event) {

            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                float[] acc = new float[3];
                System.arraycopy(event.values, 0, acc, 0, event.values.length);
                acc = lowPass.filterValues(acc);
                setValue(acc);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }


}


