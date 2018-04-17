package fh_swf.mechatronik;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.webkit.WebView;
import android.widget.*;

/**
 *
 * Controller-Klasse für das Hauptfenster. Die meisten Funktionalitäten der Applikation werden in dieser Klasse realisiert.
 *
 * Created by Fabian Schäfer on 03.03.2017.
 */

public class MainActivity extends AppCompatActivity{

    private MainModel data;                                         // Objekt für das Datenmodell der zu übertragenden Daten.
    private ImageButton valUp;                                      // Button Pfeil oben
    private ImageButton valDown;                                    // Button Pfeil unten
    private TextView accelXText;                                    // Textanzeige für den Y-Beschleunigungswert
    private TextView accelYText;                                    // Textanzeige für den X-Beschleunigungswert
    private RadioGroup rb_g1;                                       // Gruppe der Radio-Buttons M1-M3
    private TextView m1InfoText;                                    // Textanzeige für von Radio-Button M1-Abhängige Werte.
    private SeekBar glider_as;                                      // Schieberegler AS
    private RadioGroup rb_g2_12;                                    // Gruppe für Radio-Buttons F1-F2
    private RadioGroup rb_g2_34;                                    // Gruppe für Radio-Buttons F3-F4
    private ImageButton play;                                       // Button Play
    private ImageButton stop;                                       // Button Stop
    private Switch on_off;                                          // An / Aus-Schalter
    private SeekBar glider_az;                                      // Schieberegler AZ
    private TextView m2InfoText;                                    // Textanzeige für von Radio-Button M2-Abhängige Werte.
    private TextView m3InfoText;                                    // Textanzeige für von Radio-Button M3-Abhängige Werte.
    private TextView profileText;                                   // Textanzeige für das ausgewählte Profil
    private TextView addressText;                                   // Textanzeige für die angegebene IP / Mac - Adresse
    private OptionsModel optionsData;                               // Objekt für das Datenmodell der gewählten Einstellungen.
    private float[] mGravity;                                       // Feld für Beschleunigungswerte zur Berechnung der Neigung.
    private float[] mGeomagnetic;                                   // Fels für Magnetfeldwerte zur Berechnung der Neigung.
    private LinearLayout relate;                                    // Layout für das Touchfeld ( Werte X und Y )
    private WebView video;                                          // VideoView für die Anzeige des Kamerabilds.


    /**
     *
     * Methode zur Initialisierung des Hauptfensters beim Programmstart.
     *
     * @param savedInstanceState
     *
     * savedInstanceState enthält den Zustand der Aktivität.
     * So können Daten wieder angezeigt werden wenn die Aktivität neu aufgebaut werden muss
     * (etwa bei Änderungen der Ausrichtung).
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    data = MainModel.getInstance();
    optionsData = OptionsModel.getInstance();
    InitializeApp();

    
}

    /**
     *
     * Methode für die Erstellung des Menüs am oberen Bildschirmrand (Optionen)
     *
     * @param menu
     *
     * menu stellt die Menüleiste am oberen Bildschirmrand dar.
     *
     * @return
     *
     * Methode der Super-Klasse mit dem Menü als Übergabeparameter.
     * (Wird zusätzlich zum eigenen Code in der Methode ausgeführt).
     *
     */

@Override
public boolean onCreateOptionsMenu(Menu menu)
{
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.optionsmenu, menu);
    return super.onCreateOptionsMenu(menu);
}

    /**
     *
     * Methode zum Öffnen der Applikations-Optionen bei Auswahl des Menüpunkts Optionen.
     *
     * @param item
     *
     * Menüpunkt Optionen in der Menüleiste.
     *
     * @return
     *
     * true
     */


@Override
public boolean onOptionsItemSelected(MenuItem item){
    Intent i = new Intent(MainActivity.this, OptionsActivity.class);
    startActivityForResult(i,2);
    return true;
}

    /**
     *
     * Kann bei Änderung der Konfiguration der Aktivität aufgerufen werden (Etwa Änderung der Bildschmirm-Orientation)
     * Wird in dieser Applikation nicht genutzt!
     *
     * @param newConfig
     *
     * Die neue Konfiguration.
     */

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Selbsterstellte Methode für die Initialisierung der UI-Elemente (Buttons, TextViews etc.) sowie der Sensorik
     * und weiteren Funktionalitäten.
     */

    private void InitializeApp() {

        // Hier werden die Elemente aus den XML-Dateien (GUI) den privaten Variablen / Objekten zugewiesen.

    valUp = (ImageButton) findViewById(R.id.buttonValUp);
    valDown = (ImageButton) findViewById(R.id.buttonValDown);
    accelXText = (TextView) findViewById(R.id.textViewAccel_X);
    accelYText = (TextView) findViewById(R.id.textViewAccel_Y);
    rb_g1 = (RadioGroup) findViewById(R.id.radioGroupM1_M3);
    m1InfoText = (TextView) findViewById(R.id.textViewM1);
    m2InfoText = (TextView) findViewById(R.id.textViewM2);
    m3InfoText = (TextView) findViewById(R.id.textViewM3);
    glider_as = (SeekBar) findViewById(R.id.glider_AS);
    rb_g2_12 = (RadioGroup) findViewById(R.id.radioGroupF1_2);
    rb_g2_34 = (RadioGroup) findViewById(R.id.radioGroup2);
    play = (ImageButton) findViewById(R.id.buttonPlay);
    stop = (ImageButton) findViewById(R.id.buttonStop);
    on_off = (Switch) findViewById(R.id.switchOff_On);
    glider_az = (SeekBar) findViewById(R.id.glider_AZ);
    profileText = (TextView) findViewById(R.id.textViewProfile);
    addressText = (TextView) findViewById(R.id.textViewIP_ID);
    relate = (LinearLayout) findViewById(R.id.CrossLayout);
    video = (WebView) findViewById(R.id.videoView);

        SensorManager mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);            // SensorManager für den Zugriff auf die Sensoren des Geräts.
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);          // Initialisierung des Zugriffs auf den Beschleunigungssensor.
        Sensor magnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);              // Initialisierung des Zugriffs auf den Magnetfeldsensor.
        mSensorManager.registerListener(sel, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);     // Registrierung des Listeners für die Auswertung des Beschleunigungssensors.
        mSensorManager.registerListener(sel, magnetic, SensorManager.SENSOR_DELAY_NORMAL);          // Registrierung des Listeners für die Auswertung des Magnetfeldsensors.


    imageButtonValUp();     // Funktionalität des Buttons "Val up" (Pfeil oben)
    imageButtonValDown();   // Funktionalität des Buttons "Val down" (Pfeil unten)
    radioGroup_RB_G1();     // Funktionalität der Radio-Button-Gruppe M1-M3
    seekBar_Glider_AS();    // Funktionalität des Schiebereglers Glider-AS.
    radioGroup_Rb_G2();     // Funktionalität der Radio-Button-Gruppe F1-F4.
    imageButtonPlay();      // Funktionalität des Buttons Play.
    imageButtonStop();      // Funktionalität des Buttons Stop.
    switch_on_off();        // Funktionalität des An/Aus-Schalters.
    seekBar_Glider_Az();    // Funtkionalität des Schiebereglers Glider-AZ.
    crossControl();         // Funktionalität des Steuerkreuzes (Touch-Pad Funktionalität)
   // videoView();            // Test für den VideoStream

    }

    /**
     *  der SensorEventListener für die Auswertung der Werte der genutzten Sensoren (Beschleunigung und Magnetfeld).
     *  Die SenosrWerte der beiden Sensoren werden in einer RotationsMatrix verarbeitet.
     *  Das Ergebnis sind die Werte für die aktuelle Neigung des Geräts.
     *  Diese Ergebniswerte werden dann in der Methode "calculateSensors" in den benötigten Wertebereich umgewandelt.
     */

    private SensorEventListener sel = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {

                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                    mGravity = event.values;
//                if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
//                    mGeomagnetic = event.values;
                if (mGravity != null) {
                    float R[] = new float[16];
                    float I[] = new float[16];
                   // boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);


                        float[] out = new float[16];
                        SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_MINUS_Y, SensorManager.AXIS_X, out);

                        float orientation[] = new float[3];
                        SensorManager.getOrientation(out, orientation);

                        calculateSensors(orientation);



                }
            }

                      @Override
                      public void onAccuracyChanged(Sensor sensor, int accuracy) {
                }
            };


    /**
     *
     * Umwandlung der Rohwerte der Neigung des Geräts in Grad.
     * Diese umgewandelten Grad-Zahlen werden dann auf den benötigten Wertebereich (-100 bis +100) umgerechnet.
     *
     * @param input
     *
     * Feld mit Rohwerten für die Neigung des Telefons
     *
     */

    private void calculateSensors(float[] input)
        {

            float valY = input[1];
            float valX = input[2];

            valY = (float) Math.toDegrees(valY);
            valX = (float) Math.toDegrees(valX);

            valY = (valY/30)*100;
            valX = (valX/30)*100;

            int accelX = (int) valX;
            int accelY = (int) valY*-1;

            switch(optionsData.getZero()) {

                case 0:

                    if(accelY > 100)
                    {
                        accelY = 100;
                    }
                    else if(accelY < -100)
                    {
                        accelY = -100;
                    }
                    if(accelX > 100)
                    {
                        accelX = 100;
                    }
                    else if(accelX < -100)
                    {
                        accelX = -100;
                    }


                    data.setAccel_x((byte) accelX);
                    data.setAccel_y((byte) accelY);

                    accelYText.setText("Y: "+ data.getAccel_y());
                    accelXText.setText("X: "+ data.getAccel_x());


                    break;

                case 15:

                    accelY = accelY+50;

                    if(accelY > 100)
                    {
                        accelY = 100;
                    }
                    else if(accelY < -100)
                    {
                        accelY = -100;
                    }
                    if(accelX > 100)
                    {
                        accelX = 100;
                    }
                    else if(accelX < -100)
                    {
                        accelX = -100;
                    }

                    data.setAccel_x((byte) accelX);
                    data.setAccel_y((byte) accelY);

                    accelYText.setText("Y: "+ data.getAccel_y());
                    accelXText.setText("X: "+ data.getAccel_x());

                    break;

                case 30:

                    accelY = accelY+100;


                    if(accelY > 100)
                    {
                        accelY = 100;
                    }
                    else if(accelY < -100)
                    {
                        accelY = -100;
                    }
                    if(accelX > 100)
                    {
                        accelX = 100;
                    }
                    else if(accelX < -100)
                    {
                        accelX = -100;
                    }

                    data.setAccel_x((byte) accelX);
                    data.setAccel_y((byte) accelY);

                    accelYText.setText("Y: "+ data.getAccel_y());
                    accelXText.setText("X: "+ data.getAccel_x());

                    break;

            }
        }


    /**
     *
     *  Methode die Eingabewerte aus dem Optionsmenü übernimmt und das Interface entsprechend der
     *  übergebenen Werte anpasst (z.B Profil und Adresse)
     *
     * @param requestCode
     *
     * Interne Codenummer für die Anfrage
     *
     * @param resultCode
     *
     * Interne Codenummer für das Ergebnis
     *
     * @param data
     *
     * zu übergebende Daten für die Hauptaktivität.
     *
     */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == 2)
        {
            if(data != null)
            {
                int profile = data.getIntExtra("profile", 1);
                setProfile(profile);

            }
        }
    }

    /**
     * Methode für die Steuerkreuzfunktionalität.
     * Es wird ein Layout eingerichtet von welchem die aktuellen Positionswerte bei Berührung und Bewegung des Fingers
     * übernommen und in den benötigten Wertebereich (-100 bis +100) im X und Y Bereich umgewandelt werden.
     * Die Funktionsweise ähnelt dem eines Touchpads an einem Notebook.
     */

    private void crossControl()
    {
        relate.setOnTouchListener(new View.OnTouchListener() {

            byte touchX;
            byte touchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int id = rb_g1.getCheckedRadioButtonId();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:

                        float x_1 = ((event.getX()/(relate.getWidth()/2))*100)-100;
                        float y_1 = ((event.getY()/(relate.getHeight()/2))*100)-100;

                        if(x_1 > 100)
                        {
                            x_1 = 100;
                        }
                        if(x_1 < -100)
                        {
                            x_1 = -100;
                        }
                        if(y_1 > 100)
                        {
                            y_1 = 100;
                        }
                        if(y_1 < -100)
                        {
                            y_1 = -100;
                        }

                        touchX = (byte) x_1;
                        touchY = (byte) (y_1*-1);

                        setCrossValues(id, touchX, touchY);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        float x_2 = ((event.getX()/(relate.getWidth()/2))*100)-100;
                        float y_2 = ((event.getY()/(relate.getHeight()/2))*100)-100;

                        if(x_2 > 100)
                        {
                            x_2 = 100;
                        }
                        if(x_2 < -100)
                        {
                            x_2 = -100;
                        }
                        if(y_2 > 100)
                        {
                            y_2 = 100;
                        }
                        if(y_2 < -100)
                        {
                            y_2 = -100;
                        }

                        touchX = (byte) x_2;
                        touchY = (byte) (y_2*-1);

                        setCrossValues(id, touchX, touchY);
                        break;

                        case MotionEvent.ACTION_UP:

                            float x_3 = ((event.getX()/(relate.getWidth()/2))*100)-100;
                            float y_3 = ((event.getY()/(relate.getHeight()/2))*100)-100;

                            if(x_3 > 100)
                            {
                                x_3 = 100;
                            }
                            if(x_3 < -100)
                            {
                                x_3 = -100;
                            }
                            if(y_3 > 100)
                            {
                                y_3 = 100;
                            }
                            if(y_3 < -100)
                            {
                                y_3 = -100;
                            }

                            touchX = (byte) x_3;
                            touchY = (byte) (y_3*-1);

                            setCrossValues(id, touchX, touchY);
                            break;
                }
                return true;
            }
        });
    }

    /**
     * Hilfs-Methode, die zum Setzen und Anzeigen der Werte des Steuerkreuzes dient.
     * @param id
     *
     * id des aktiven M-Buttons.
     *
     * @param touchX
     *
     * aktueller X-Wert des Steuerkreuzes.
     *
     * @param touchY
     *
     * aktueller Y-Wert des Steuerkreuzes.
     */

    private void setCrossValues(int id, byte touchX, byte touchY)
    {
        switch (id) {
            case R.id.M1:

                data.setRb_g1_m1_cross_left_right(touchX);
                data.setRb_g1_m1_cross_up_down(touchY);
                m1InfoText.setText(showMButtonValues(1));
                break;

            case R.id.M2:

                data.setRb_g1_m2_cross_up_down(touchX);
                data.setRb_g1_m2_cross_left_right(touchY);
                m2InfoText.setText(showMButtonValues(2));
                break;

            case R.id.M3:

                data.setRb_g1_m3_cross_up_down(touchY);
                data.setRb_g1_m3_cross_left_right(touchX);
                m3InfoText.setText(showMButtonValues(3));
                break;
        }
    }


    /**
     *
     * Das Profil wird je nach gewähltem Profil in den Optionen gesetzt.
     * Die Methode wird in der "onActivityResult" aufgerufen.
     *
     * @param id
     *
     * Das ausgewählte Profil.
     *
     */

    private void setProfile(int id)
    {
        System.out.println(id);

        switch(id)
        {
            case R.id.radio_Hexapod: setHexapodProfile();
            break;
            case R.id.radio_Segway: setSegwayProfile();
            break;
            case R.id.radio_bb8: setBB8Profile();
        }
    }

    /**
     * Einstellungen für das Profil des "Hexapod"
     * Benötigte Elemente werden eingeblendet, nicht benötigte Elemente ausgeblendet.
     */

    private void setHexapodProfile()
    {
        profileText.setText("HEXAPOD");
        if(optionsData.getTransmissionType() == 1)
        {
            addressText.setText(optionsData.getBluetoothAddress());
        }
        else if (optionsData.getTransmissionType() == 2)
        {
            addressText.setText(optionsData.getIpAddress());
        }

        // Einblenden der Elemente

        valUp.setVisibility(View.VISIBLE);
        valDown.setVisibility(View.VISIBLE);
        rb_g1.setVisibility(View.VISIBLE);
        rb_g2_12.setVisibility(View.VISIBLE);
        rb_g2_34.setVisibility(View.VISIBLE);
        m1InfoText.setVisibility(View.VISIBLE);
        m2InfoText.setVisibility(View.VISIBLE);
        m3InfoText.setVisibility(View.VISIBLE);
        glider_as.setVisibility(View.VISIBLE);
        glider_az.setVisibility(View.VISIBLE);
        play.setVisibility(View.VISIBLE);
        stop.setVisibility(View.VISIBLE);
    }

    /**
     * Einstellungen für das Profil "Mini-Segway"
     * Benötigte Elemente werden eingeblendet, nicht benötigte Elemente ausgeblendet.
     */

    private void setSegwayProfile()
    {
        profileText.setText("SEGWAY");
        addressText.setText(optionsData.getBluetoothAddress());

        //Ausblenden der Elemente

        valUp.setVisibility(View.INVISIBLE);
        valDown.setVisibility(View.INVISIBLE);
        rb_g1.setVisibility(View.INVISIBLE);
        rb_g2_12.setVisibility(View.INVISIBLE);
        rb_g2_34.setVisibility(View.INVISIBLE);
        m1InfoText.setVisibility(View.INVISIBLE);
        m2InfoText.setVisibility(View.INVISIBLE);
        m3InfoText.setVisibility(View.INVISIBLE);
        glider_az.setVisibility(View.INVISIBLE);
        glider_as.setVisibility(View.INVISIBLE);
        play.setVisibility(View.INVISIBLE);
        stop.setVisibility(View.INVISIBLE);

    }

    /**
     * Einstellungen für das Profil "BB8"
     * Benötigte Elemente werden eingeblendet, nicht benötigte Elemente ausgeblendet.
     */

    private void setBB8Profile()
    {
        profileText.setText("BB8");
        addressText.setText(optionsData.getBluetoothAddress());

        // Einblenden der Elemente

        valUp.setVisibility(View.VISIBLE);
        valDown.setVisibility(View.VISIBLE);
        rb_g1.setVisibility(View.VISIBLE);
        rb_g2_12.setVisibility(View.VISIBLE);
        rb_g2_34.setVisibility(View.VISIBLE);
        m1InfoText.setVisibility(View.VISIBLE);
        m2InfoText.setVisibility(View.VISIBLE);
        m3InfoText.setVisibility(View.VISIBLE);
        glider_as.setVisibility(View.VISIBLE);
        glider_az.setVisibility(View.VISIBLE);
        play.setVisibility(View.VISIBLE);
        stop.setVisibility(View.VISIBLE);
    }


    /**
     * Funktionalität des ValUp-Buttons (Pfeil oben). Beim aktivieren des Buttons wird ein Bit des Bytes "ValUp_down_stop_start_on_off" gesetzt.
     * sowie der Hintergrund des Buttons weis eingefärbt.
     * Beim erneuten aktivieren des Buttons wird das vorher gesetzte Bit wieder zurückgesetzt und der Hintergrund wieder transparent.
     */

    private void imageButtonValUp()
    {
        valUp.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = false;
            @Override
            public void onClick(View v) {
                if(!isClicked) {
                    byte valUpChecked = data.getButtons();
                    valUpChecked = setBit(valUpChecked, 0);
                    data.setButtons(valUpChecked);
                    String s1 = String.format("%8s", Integer.toBinaryString(valUpChecked % 0xFF)).replace(' ','0');
                    System.out.println(s1);
                    isClicked = true;
                    valUp.setBackgroundColor(Color.WHITE);

                }
                else
                {
                    byte valUpUnChecked = data.getButtons();
                    valUpUnChecked = clearBit(valUpUnChecked, 0);
                    data.setButtons(valUpUnChecked);
                    String s1 = String.format("%8s", Integer.toBinaryString(valUpUnChecked % 0xFF)).replace(' ','0');
                    System.out.println(s1);
                    isClicked = false;
                    valUp.setBackgroundColor(Color.TRANSPARENT);

                }
            }
        });
    }

    /**
     * Funktionalität des ValDown-Buttons (Pfeil unten). Beim aktivieren des Buttons wird ein Bit des Bytes "ValUp_down_stop_start_on_off" gesetzt,
     * sowie der Hintergrund des Buttons weis eingefärbt.
     * Beim erneuten aktivieren des Buttons wird das vorher gesetzte Bit wieder zurückgesetzt und der Hintergrund wieder transparent.
     */

    private void imageButtonValDown()
    {
        valDown.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = false;
            @Override
            public void onClick(View v) {
                if(!isClicked) {
                    byte test = data.getButtons();
                    test = setBit(test, 1);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ','0');
                    System.out.println(s1);
                    isClicked = true;
                    valDown.setBackgroundColor(Color.WHITE);
                }
                else
                {
                    byte test = data.getButtons();
                    test = clearBit(test, 1);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ','0');
                    System.out.println(s1);
                    isClicked = false;
                    valDown.setBackgroundColor(Color.TRANSPARENT);
                }
            }

        });
    }

    /**
     * Funktionalität des Play-Buttons. Beim aktivieren des Buttons wird ein Bit des Bytes "ValUp_down_stop_start_on_off" gesetzt.
     * sowie der Hintergrund des Buttons weis eingefärbt.
     * Beim erneuten aktivieren des Buttons wird das vorher gesetzte Bit wieder zurückgesetzt und der Hintergrund wieder transparent.
     */

    private void imageButtonPlay()
    {
        play.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = false;
            @Override
            public void onClick(View v) {
                if (!isClicked) {
                    byte test = data.getButtons();
                    test = setBit(test, 2);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                    isClicked = true;
                    play.setBackgroundColor(Color.WHITE);
                } else {
                    byte test = data.getButtons();
                    test = clearBit(test, 2);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                    isClicked = false;
                    play.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
    }

    /**
     * Funktionalität des Stop-Buttons. Beim aktivieren des Buttons wird ein Bit des Bytes "ValUp_down_stop_start_on_off" gesetzt.
     * sowie der Hintergrund des Buttons weis eingefärbt.
     * Beim erneuten aktivieren des Buttons wird das vorher gesetzte Bit wieder zurückgesetzt und der Hintergrund wieder transparent.
     */

    private void imageButtonStop()
    {
        stop.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = false;
            @Override
            public void onClick(View v) {
                if (!isClicked) {
                    byte test = data.getButtons();
                    test = setBit(test, 3);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                    isClicked = true;
                    stop.setBackgroundColor(Color.WHITE);
                } else {
                    byte test = data.getButtons();
                    test = clearBit(test, 3);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                    isClicked = false;
                    stop.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
    }

    /**
     * Funktionalität des An/Aus-Schalters. Beim Ein-Status des Schalters wird ein Bit des Bytes "ValUp_down_stop_start_on_off" gesetzt.
     * sowie der Hintergrund des Schalters weis eingefärbt.
     * Beim Aus-Status des Schalters wird das vorher gesetzte Bit wieder ungesetzt und der Hintergrund wieder transparent.
     */

    private void switch_on_off()
    {
        on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                byte test = data.getButtons();
                if(isChecked)
                {
                    test = setBit(test, 4);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                    on_off.setBackgroundColor(Color.WHITE);
                }
                else
                {
                    test = clearBit(test, 4);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                    on_off.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
    }


    /**
     * Funktionalität der Radio-Button-Gruppe für die Radio-Buttons M1-M3.
     * Bei Aktivierung eines Buttons wird dessen Wert im Datenmodell gesetzt und der
     * entsprechende Anzeigetext in der Hauptaktivität gesetzt.
     */

    private void radioGroup_RB_G1()
    {
        rb_g1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.M1)
                {
                    m1InfoText.setText(showMButtonValues(1));
                    data.setRb_g1((byte) 1);
                }
                if (checkedId == R.id.M2)
                {
                    m2InfoText.setText(showMButtonValues(2));
                    data.setRb_g1((byte) 2);
                }
                if (checkedId == R.id.M3)
                {
                    m3InfoText.setText(showMButtonValues(3));
                    data.setRb_g1((byte) 3);
                }
            }
        });
    }

    /**
     *  Funktionalität des Schiebereglers "Glider-AS".
     *  Beim Verändern des Werts durch Änderung am Regler wird der aktuelle Wert gesetzt und
     *  im Anzeigetext in der Hauptaktivität angezeigt
     *  Der Wertebereich beträgt von -100 bis +100.
     */

    private void seekBar_Glider_AS()
    {
        glider_as.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int prog = 0;
            int id = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                prog = progress-100;
                id = rb_g1.getCheckedRadioButtonId();

                switch(id)
             {
                case R.id.M1: data.setRb_g1_m1_glider_AS((byte)prog);
                    m1InfoText.setText(showMButtonValues(1));
                    break;
                case R.id.M2: data.setRb_g1_m2_glider_AS((byte)prog);
                    m2InfoText.setText(showMButtonValues(2));
                    break;
                case R.id.M3: data.setRb_g1_m3_glider_AS((byte)prog);
                    m3InfoText.setText(showMButtonValues(3));
             }
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                id = rb_g1.getCheckedRadioButtonId();

                switch(id)
                {
                    case R.id.M1: data.setRb_g1_m1_glider_AS((byte)prog);
                        m1InfoText.setText(showMButtonValues(1));
                    break;
                    case R.id.M2: data.setRb_g1_m2_glider_AS((byte)prog);
                        m2InfoText.setText(showMButtonValues(2));
                    break;
                    case R.id.M3: data.setRb_g1_m3_glider_AS((byte)prog);
                        m3InfoText.setText(showMButtonValues(3));
                }

            }
        });
    }

    /**
     *  Funktionalität des Schiebereglers "Glider-AZ".
     *  Beim Verändern des Werts durch Änderung am Regler wird der aktuelle Wert gesetzt und
     *  im Anzeigetext in der Hauptaktivität angezeigt.
     *  Nach entfernen des Fingers vom Display wird der Regler wieder auf den Wert null gesetzt (Auto-Zero-Funktionalität).
     *  Der Wertebereich beträgt von -100 bis +100.
     */

    private void seekBar_Glider_Az()
    {
        glider_az.setProgress(100);

        glider_az.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int prog = 0;
            int id = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                prog = progress-100;
                id = rb_g1.getCheckedRadioButtonId();

                switch (id) {
                    case R.id.M1:
                        data.setRb_g1_m1_glider_AZ((byte) prog);
                        m1InfoText.setText(showMButtonValues(1));
                        break;
                    case R.id.M2:
                        data.setRb_g1_m2_glider_AZ((byte) prog);
                        m2InfoText.setText(showMButtonValues(2));
                        break;
                    case R.id.M3:
                        data.setRb_g1_m3_glider_AZ((byte) prog);
                        m3InfoText.setText(showMButtonValues(3));

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                id = rb_g1.getCheckedRadioButtonId();

                switch (id) {
                    case R.id.M1:
                        data.setRb_g1_m1_glider_AZ((byte) prog);
                        m1InfoText.setText(showMButtonValues(1));
                        break;
                    case R.id.M2:
                        data.setRb_g1_m2_glider_AZ((byte) prog);
                        m2InfoText.setText(showMButtonValues(2));
                        break;
                    case R.id.M3:
                        data.setRb_g1_m3_glider_AZ((byte) prog);
                        m3InfoText.setText(showMButtonValues(3));
                }
                glider_az.setProgress(100);
            }
        });

    }

    /**
     * Funktionalität für die Radio-Button-Gruppe der Radio-Buttons F1-F4.
     * Da es in Android nicht möglich ist Radio-Buttons in einer Gruppe direkt nebenbeinander und Untereinander
     * zu positionieren kommen hier zwei Radio-Gruppen, die kombiniert werden, zum Einsatz.
     */

    private void radioGroup_Rb_G2()
    {
        rb_g2_12.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.F1)
                {
                    data.setRb_g2((byte) 1);
                    rb_g2_34.clearCheck();
                    rb_g2_12.check(R.id.F1);

                }
                if(checkedId == R.id.F2)
                {
                    data.setRb_g2((byte) 2);
                    rb_g2_34.clearCheck();
                    rb_g2_12.check(R.id.F2);

                }
            }
        });

        rb_g2_34.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.F3)
                {
                    data.setRb_g2((byte) 3);
                    rb_g2_12.clearCheck();
                    rb_g2_34.check(R.id.F3);

                }
                if(checkedId == R.id.F4)
                {
                    data.setRb_g2((byte) 4);
                    rb_g2_12.clearCheck();
                    rb_g2_34.check(R.id.F4);

                }
            }
        });
    }

    /**
     * Methode die es ermöglicht ein bestimmtes Bit in einem übergebenem Byte zu setzen.
     *
     * @param n
     *
     * Das Byte in welchem ein Bit gesetzt werden soll.
     *
     * @param pos
     *
     * Die Position des Bits welches gesetzt werden soll.
     *
     * @return
     *
     * Das Byte mit dem gesetzten Bit.
     *
     */

    private static byte setBit(byte n, int pos)
    {
        return (byte) (n | (1 << pos));
    }

    /**
     *
     * Methode die es ermöglicht ein gestztes Bit in einem übergeben Byte wieder zu löschen.
     *
     * @param n
     *
     * Das Byte in welchem ein gesetztes Bit gelöscht werden soll.
     *
     * @param pos
     *
     * Die Position des Bits, welches gelöscht werden soll.
     *
     * @return
     *
     * Das Byte mit dem zurückgesetzten Bit.
     *
     */

    private static byte clearBit(byte n, int pos)
    {
        return (byte) (n & ~(1 << pos));
    }


    /**
     * Methode zur Anzeige der Informationen der momentanen Werte welche von den Radio-Buttons M1-M3 abhängen.
     *
     * @param activeMButton
     *
     * Der zur Zeit aktive Button der Radio-Gruppe "RB-G1" (M1,M2 oder M3)
     *
     * @return
     *
     * Ein String der die aktuellen Werte in Abhängigkeit des jeweilig aktiven Radio-Buttons anzeigt.
     *
     */

        private String showMButtonValues(int activeMButton)
        {
            switch(activeMButton)
            {
                case 1: return "M1X= "+data.getRb_g1_m1_cross_left_right()+", M1Y= "+data.getRb_g1_m1_cross_up_down()+", AS= "+data.getRb_g1_m1_glider_AS()+", AZ= "+data.getRb_g1_m1_glider_AZ();

                case 2: return "M2X= "+data.getRb_g1_m2_cross_left_right()+", M2Y= "+data.getRb_g1_m2_cross_up_down()+", AS= "+data.getRb_g1_m2_glider_AS()+", AZ= "+data.getRb_g1_m2_glider_AZ();

                case 3: return "M3X= "+data.getRb_g1_m3_cross_left_right()+", M3Y= "+data.getRb_g1_m3_cross_up_down()+", AS= "+data.getRb_g1_m3_glider_AS()+", AZ= "+data.getRb_g1_m3_glider_AZ();

                default: return "InfoText für CROSS_AX, CROSS_AY, GLIDER_AS und GLIDER AZ";
            }
        }

        private void videoView()
        {
            video.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    System.out.println("drinne!");
                    streamingTest();
                    return false;

                }
            });

        }

        private void streamingTest()
        {

//                String uriTest = optionsData.getIpAddress() + ":" + optionsData.getPort();
//
//                Uri vidUri = Uri.parse("http://192.168.178.32:8080/stream/video.mjpeg");
//                video.setVideoURI(vidUri);
//                MediaController mediacontroller = new MediaController(this);
//                video.setMediaController(mediacontroller);
//                video.start();
//
//                Toast.makeText(MainActivity.this,
//                        "Connect: " + vidUri,
//                        Toast.LENGTH_LONG).show();

            video.loadUrl("http://192.168.178.32:8080/stream/video.mjpeg");
            video.getSettings().setLoadWithOverviewMode(true);
            video.getSettings().setUseWideViewPort(true);




        }

    }

