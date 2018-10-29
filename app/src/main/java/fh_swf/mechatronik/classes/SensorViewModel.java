package fh_swf.mechatronik.classes;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

/**
 *
 * ViewModelKlasse um den SensorListener der AccelerationSensor-Klasse abrufen zu können.
 * Created by Fabian Schäfer on 11.09.2018.
 */
public class SensorViewModel extends AndroidViewModel {

    private AccelerationSensor sensorListener;  //SensorListener für die Beschleunigungswerte

    /**
     * Konstruktor in dem der SensorListenser zugewiesen wird.
     * @param application
     */

    public SensorViewModel(Application application){

        super(application);
        sensorListener = new AccelerationSensor(application);

    }

    /**
     * Rückgabe des Listeners, damit Klassen über einen Observer auf die Werte zugreifen können.
     * @return
     * der SensorListener für die Sensorwerte.
     */

   public AccelerationSensor getAccelerationSensorListener() {

        return sensorListener;

    }


}
