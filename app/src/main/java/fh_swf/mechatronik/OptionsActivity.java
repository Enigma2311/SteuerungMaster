package fh_swf.mechatronik;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;


/**
 * Datenklasse für die Daten der Einstellungen (Optionen).
 * Es handelt sich um eine Singleton-Modell Klasse, damit nur eine Instanz der Daten vorhanden ist und alle
 * zugreifenden Klassen den selben Datenstand haben.
 *
 * Created by Fabian Schäfer on 03.04.2017.
 *
 */

public class OptionsActivity extends AppCompatActivity {

    private OptionsModel optionsData;       // Objekt für das Optionsdatenmodell.
    private RadioGroup profileGrp;          // Radio-Button-Gruppe für die Auswahl eines der drei Profile.
    private RadioGroup nullLageGrp;         // Radio-Button-Gruppe für die Auswahl der Nulllage (0, 15 oder 30 Grad).
    private EditText transmissionText;      // Texteingabe des Werts für den Intervall der Datenübertragung (50 - 1000 Millisekunden).
    private RadioGroup transmissionType;    // Radio-Button-Gruppe für die Auswahl des Übetragungstyps (WLAN oder Bluetooth).
    private Button deviceListBtn;           // Button zum Aufruf der Verbindungsaktivität zur Herstellung einer Verbindung.
    private Button saveBtn;                 // Button zum Speichern der Einstellungen eines Profils.
    private Button loadBtn;                 // Button für das Laden des gewählten Profils.
    private ProfileDataStorage stor;        // Storage-Objekt zum Speichern der Optionsdaten in den SharedPreferences des Geräts.


    /**
     *
     * Initialisierung der Optionen-Aktivität beim Aufruf.
     *
     * @param savedInstanceState
     *
     * savedInstanceState enthält den Zustand der Aktivität.
     * So können Daten wieder angezeigt werden wenn die Aktivität neu aufgebaut werden muss
     *(etwa bei Änderungen der Ausrichtung).
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        optionsData = OptionsModel.getInstance();
        initializeOptions();
    }

    /**
     *  Methode die alle zu Initialisierenden Elemente enthält.
     *  Der Aufruf erfolgt in der onCreate Methode.
     */

    private void initializeOptions()
    {
        profileGrp = (RadioGroup) findViewById(R.id.profil_RadioGroup);
        nullLageGrp = (RadioGroup) findViewById(R.id.nulllage_RadioGroup);
        transmissionText = (EditText) findViewById(R.id.transmissionInput);
        transmissionType = (RadioGroup) findViewById(R.id.transmissionGrp);
        deviceListBtn = (Button) findViewById(R.id.deviceListBtn);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        loadBtn = (Button) findViewById(R.id.loadBtn);

        stor = new ProfileDataStorage();

        radioGroupProfile();
        radioGroupNullage();
        transmissionTextSetting();
        transmissionTypeSetting();
        setDeviceListBtnAction();
        submitDataToMainActivity();
        setButtons();
        loadProfileDataOnClick();


    }

    /**
     * Methode die beim erneuten Aufruf der Optionen die bisher getätigten Einstellungen setzt und anzeigt.
     */

    private void setButtons()
    {
        switch(optionsData.getProfileType()){

            case 1: profileGrp.check(R.id.radio_Hexapod);
            break;
            case 2: profileGrp.check(R.id.radio_Segway);
            break;
            case 3: profileGrp.check(R.id.radio_bb8);
            break;
            default: profileGrp.clearCheck();

        }

        switch (optionsData.getZero()){
            case 0: nullLageGrp.check(R.id.radioBtn_lage_0);
                break;
            case 15: nullLageGrp.check(R.id.radioBtn_lage_15);
                break;
            case 30: nullLageGrp.check(R.id.radioBtn_lage_30);
                break;
            default: nullLageGrp.clearCheck();
        }


        transmissionText.setText(Integer.toString(optionsData.getTransmissionTime()));

        switch (optionsData.getTransmissionType())
        {
            case 1: transmissionType.check(R.id.radioBtn_BlueTooth);
            break;
            case 2: transmissionType.check(R.id.radioBtn_WLAN);
            break;
            default: transmissionType.clearCheck();
        }

    }

    /**
     * Listener der Radio-Button-Gruppe für die Auswahl des Profils.
     * Bei der Auswahl eines Profils wird ein Profilwert (1,2 oder 3) gesetzt welcher intern
     * weiter verwendet wird um Profilbedingte Einstellungen zu ermöglichen.
     */

    private void radioGroupProfile()
    {

        profileGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_Hexapod)
                {
                    optionsData.setProfileType(1);
                }
                if(checkedId == R.id.radio_Segway)
                {
                    optionsData.setProfileType(2);
                }
                if(checkedId == R.id.radio_bb8)
                {
                    optionsData.setProfileType(3);
                }
            }
        });
    }

    /**
     * Listener der Radio-Button-Gruppe für die Auswahl der Nulllage.
     * Bei der Auswahl eines Werts (0, 15 oder 30 Grad) wird der Wert bei der
     * Neigung des Geräts mit eingerechnet und ermöglicht so verschiedene Neigungswerte als Grundlage.
     */

    private void radioGroupNullage()
    {

        nullLageGrp.check(R.id.radioBtn_lage_0);

        nullLageGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioBtn_lage_0)
                {
                    optionsData.setZero(0);
                }
                if(checkedId == R.id.radioBtn_lage_15)
                {
                    optionsData.setZero(15);
                }
                if(checkedId == R.id.radioBtn_lage_30)
                {
                    optionsData.setZero(30);
                }
            }
        });
    }


    /**
     * Texterfassung für die Einstellung der Übertragungsintervalls.
     * Es sind Werte von 50 - 1000 Millisekunden möglich.
     */

    private void transmissionTextSetting()
    {

        transmissionText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!transmissionText.getText().toString().isEmpty()) {
                    int trans = Integer.parseInt(transmissionText.getText().toString());
                    if(trans == 0)
                    {
                        optionsData.setTransmissionTime(1);
                    }
                    else {
                        optionsData.setTransmissionTime(trans);
                    }

                    }
                }

        });
    }


    /**
     * Auswahlmöglichkeit für den Übetragungstyp.
     * Radio-Button-Gruppe mit der Wahl für Bluetooth- oder WLAN-Übertragung.
     * Bluetooth = 1, WLAN = 2.
     */

    private void transmissionTypeSetting()
    {

        transmissionType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radioBtn_BlueTooth)
                {
                    optionsData.setTransmissionType(1);
                }
                if(checkedId == R.id.radioBtn_WLAN)
                {
                    optionsData.setTransmissionType(2);
                }

            }
        });
    }

    /**
     * Button für die Öffnung der Aktivität zur Herstellung einer Verbindung für die Datenübertragung.
     * Welche Aktivität geöffnet wird hängt mit der gewählten Übertragungsart (WLAN / Bluetooth) zusammen.
     */

    private void setDeviceListBtnAction()
    {
        deviceListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (transmissionType.getCheckedRadioButtonId())
                {
                    case R.id.radioBtn_BlueTooth:
                         Intent i = new Intent(OptionsActivity.this, DeviceListActivity.class);
                         startActivity(i);
                    break;
                    case R.id.radioBtn_WLAN:
                        Intent j = new Intent(OptionsActivity.this, WifiConnectionActivity.class);
                        startActivity(j);
                        break;
                        default: Toast.makeText(OptionsActivity.this, "Keine Verbindungsart ausgewählt!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    /**
     * Button-Listener der die angegeben Daten in den SharedPreferences speichert und die Optionen-Aktivität beendet. Etwaige relevante Daten
     * (Profil / IP/MAC-Adresse) werden an die Hauptaktivität übergeben.
     * Die Übergabe der Daten an die Hauptaktivität geschieht durch ein Intent.
     * Sollten nicht alle Werte angegeben werden oder der Wert des Übertragungsintervalls fehlerhaft sein, wird eine Meldung angezeigt.
     */

    private void submitDataToMainActivity()
    {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(optionsData.getTransmissionTime() == 1 || (optionsData.getTransmissionTime() <= 1000 && optionsData.getTransmissionTime() >= 50)) {
                    if(nullLageGrp.getCheckedRadioButtonId() != -1 && profileGrp.getCheckedRadioButtonId() != -1) {
                        Intent optionsActivityIntent = new Intent();

                        int profile = profileGrp.getCheckedRadioButtonId();

                        optionsActivityIntent.putExtra("profile", profile);

                        setResult(2, optionsActivityIntent);

                        switch (profileGrp.getCheckedRadioButtonId()) {
                            case R.id.radio_Hexapod:
                                stor.setBytes(OptionsActivity.this, optionsData.getOptionsDataSet(), 1);
                                break;
                            case R.id.radio_Segway:
                                stor.setBytes(OptionsActivity.this, optionsData.getOptionsDataSet(), 2);
                                break;
                            case R.id.radio_bb8:
                                stor.setBytes(OptionsActivity.this, optionsData.getOptionsDataSet(), 3);
                        }
                        finish();
                    }
                    else
                    {
                        Toast.makeText(OptionsActivity.this, "Bitte alle Werte angeben!", Toast.LENGTH_LONG).show();
                    }
                }

                else
                {
                    Toast.makeText(OptionsActivity.this, "Unzulässiger Übertragungswert!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Button-Listener, welcher beim Drücken des "LOAD DATA" Buttons die Daten des ausgewählten Profils
     * aus den SharedPreferences lädt (sofern vorhanden).
     */

    private void loadProfileDataOnClick(){

        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (profileGrp.getCheckedRadioButtonId()){

                    case R.id.radio_Hexapod: stor.getBytes(OptionsActivity.this, 1);
                    break;
                    case R.id.radio_Segway: stor.getBytes(OptionsActivity.this, 2);
                    break;
                    case R.id.radio_bb8: stor.getBytes(OptionsActivity.this, 3);

                }

                setButtons();

            }
        });

    }

    /**
     * Überschreiben des Zurück-Knopfs des Mobilgeräts, damit die Optionen nicht ohne Speichern der Daten
     * verlassen werden können. (Verlassen ohne Speicherung führte zu Problemen und Abstürzen).
     */

    @Override
    public void onBackPressed(){

        Toast.makeText(this, "Bitte SAVE DATA zum Verlassen drücken.", Toast.LENGTH_LONG).show();

    }

}
