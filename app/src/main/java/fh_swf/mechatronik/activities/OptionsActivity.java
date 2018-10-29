package fh_swf.mechatronik.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import fh_swf.mechatronik.R;
import fh_swf.mechatronik.model.OptionsModel;
import fh_swf.mechatronik.model.ProfileDataStorage;

import java.util.Objects;


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
    private ProfileDataStorage stor;        // Storage-Objekt zum Speichern der Optionsdaten in den SharedPreferences des Geräts.
    private EditText profileName;           // Texteingabe für den Profilnamen (max. 15 Zeichen)
    private boolean isEdit;                 // Hilfswert um Einstellungen für die Editierung eines Profils zu tätigen.
    private int mListIndex;                 // Hilfsvariable um die Position eines Eintrags in der Liste zu speichern.
    private boolean connectionStatus = false;  // Hilfsvariable um sicherzustellen das Verbindungsdaten angegeben wurden.



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
     * Einstellung der vorhandenen Daten wenn ein Profil editiert wird.
     */

    @Override
    protected void onResume()
    {
        super.onResume();

        if(isEdit)
        setButtons();


    }

    /**
     *  Methode die alle zu Initialisierenden Elemente enthält.
     *  Der Aufruf erfolgt in der onCreate Methode.
     */

    private void initializeOptions()
    {
        profileGrp = findViewById(R.id.profil_RadioGroup);
        nullLageGrp = findViewById(R.id.nulllage_RadioGroup);
        transmissionText = findViewById(R.id.transmissionInput);
        transmissionType = findViewById(R.id.transmissionGrp);
        deviceListBtn = findViewById(R.id.deviceListBtn);
        saveBtn = findViewById(R.id.saveBtn);
        profileName = findViewById(R.id.profilNameInput);

        stor = new ProfileDataStorage();
        isEdit = false;

        radioGroupProfile();
        radioGroupNullage();
        transmissionTextSetting();
        transmissionTypeSetting();
        setDeviceListBtnAction();
        submitDataToMainActivity();

        if(Objects.requireNonNull(getIntent().getExtras()).getInt("ActionID") == 2)
        {
            isEdit = true;
            mListIndex = getIntent().getExtras().getInt("Index");
            setButtons();
        }


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

        profileName.setText(optionsData.getCurrentProfile());

    }

    /**
     * Listener der Radio-Button-Gruppe für die Auswahl des Profils.
     * Bei der Auswahl eines Profils wird ein Profilwert (1,2 oder 3) gesetzt, welcher intern
     * weiter verwendet wird um profilbedingte Einstellungen zu ermöglichen.
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
                         connectionStatus = true;
                         startActivity(i);
                    break;
                    case R.id.radioBtn_WLAN:
                        Intent j = new Intent(OptionsActivity.this, WifiConnectionActivity.class);
                        connectionStatus = true;
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

    private void submitDataToMainActivity() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            saveData();

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

    private boolean setProfileName () {
        String tempName = profileName.getText().toString().trim();
        boolean checkIfEmpty = tempName.length() > 0;
        boolean checkIfEditEntryExists = optionsData.getProfileNamesList().indexOf(profileName.getText().toString()) == mListIndex;
        boolean checkIfEntryExists = checkIfEntryExists();


            if (checkIfEmpty) {
                if (!checkIfEntryExists && !isEdit)
                {
                    optionsData.addProfileName(profileName.getText().toString());
                    optionsData.setCurrentProfile(profileName.getText().toString());
                    return true;
                }
                else if (checkIfEntryExists && isEdit && checkIfEditEntryExists || !checkIfEntryExists)
                {
                    optionsData.getProfileNamesList().remove(mListIndex);
                    optionsData.getProfileNamesList().add(mListIndex, profileName.getText().toString());
                    optionsData.setCurrentProfile(profileName.getText().toString());
                    return true;
                }
                else
                {
                    Toast.makeText(this, "Profilname existiert bereits, bitte anderen Namen wählen!", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
            else
            {
                Toast.makeText(this, "Profilname darf nicht leer sein, bitte geben Sie einen Namen an!", Toast.LENGTH_LONG).show();
                return false;
            }
    }

    /**
     * Prüfung ob ein Profilname bereits existiert.
     *
     * @return true, falls der Eintrag existiert, false falls nicht.
     */

    private boolean checkIfEntryExists()
    {
        for(String entry : optionsData.getProfileNamesList())
        {
            if(entry.equals(profileName.getText().toString()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Speichern aller Profilnamen in den Shared Preferences des Android-Geräts, um ein Laden der Daten
     * beim erneuten Nutzen der Applikation zu ermöglichen.
     */

    private void saveData() {

        if(checkValues() && setProfileName()) {

            stor.saveProfileNamesArray(OptionsActivity.this, optionsData.getProfileNamesList());
            stor.setBytes(OptionsActivity.this, optionsData.getOptionsDataSet());
            connectionStatus = false;
            finish();
        }

    }

    /**
     * Eine check-Methode, um zu prüfen ob alle Angaben für die Erstellung eines Profils angegeben wurden.
     *
     * @return true, falls alle Angaben vorhanden sind, false mit Fehlermeldung falls Datensätze unvollständig sind.
     */


    private boolean checkValues() {
        if (optionsData.getTransmissionTime() == 1 || (optionsData.getTransmissionTime() <= 1000 && optionsData.getTransmissionTime() >= 50)) {
            if (nullLageGrp.getCheckedRadioButtonId() != -1 && profileGrp.getCheckedRadioButtonId() != -1 && connectionStatus) {
                return true;
            } else {
                Toast.makeText(OptionsActivity.this, "Bitte alle Werte, inklusive Verbindungsdaten, angeben!", Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(OptionsActivity.this, "Unzulässiger Übertragungswert!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}
