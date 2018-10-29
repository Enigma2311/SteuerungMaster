package fh_swf.mechatronik.classes;

/**
 *
 * Klasse zur Filterung der Sensorwerte.
 * Created by zero_ on 26.06.2018.
 */
public class LowPassFilter {

    private static final float timeConstant = 0.297f; // Zeitkonstante
    private float[] output = new float[]{0,0,0};      // Array für die Sensordaten.
    private float timestampOld = System.nanoTime();   // Zeitstempel bei Initialisierung der Klasse
    private int count = 0;                            // Zählvariable
    private float alpha = 0.0f;                       // alpha-wert für die Filterung

    /**
     * Methode welche den Tiefpassfilter implementiert und somit die Sensordaten "glättet".
     * @param input
     * rohe Eingangswerte des Beschleunigungssensors.
     * @return
     * gefilterte Sensordaten.
     */

    public float[] filterValues(float[] input)
    {

        float timestamp = System.nanoTime();

        float dt = 1 / (count / ((timestamp - timestampOld) / 1000000000.0f));

        count++;

         alpha = timeConstant / (timeConstant + dt);
//         alpha = dt/(timeConstant+dt);

        output[0] = alpha * output[0] + (1 - alpha) * input[0];
        output[1] = alpha * output[1] + (1 - alpha) * input[1];
        output[2] = alpha * output[2] + (1 - alpha) * input[2];


//        output[0] = output[0] + alpha * (input[0] - output[0]);
//        output[1] = output[1] + alpha * (input[1] - output[1]);
//        output[2] = output[2] + alpha * (input[2] - output[2]);


        return output;

    }
}
