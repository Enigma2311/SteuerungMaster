package fh_swf.mechatronik.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.common.net.InetAddresses;
import fh_swf.mechatronik.R;
import fh_swf.mechatronik.classes.WiFiConnection;
import fh_swf.mechatronik.model.OptionsModel;

import java.net.InetAddress;

/**
 * Aktivität zur Eingabe einer IP-Adresse und eine Portnummer, sowie der Möglichkeit über einen Button
 * eine drahtlose Verbindung zu einem Zielgerät mit den angegeben Daten herzustellen.
 *
 * Created by Fabian Schäfer on 23.04.2017.
 *
 */

public class WifiConnectionActivity extends AppCompatActivity {

    private EditText ipAddress;                 // IP-Adresse des Zielgeräts zur Herstellung einer drahtlosen-UDP-Verbindung.
    private EditText port;                      // Portnummer des Zielgeräts zur Herstellung einer drahtlosen-UDP-Verbindung.
    private Button connectBtn;                  // Button um die Verbindung mit den eingegeben Daten herzustellen.
    private OptionsModel optionsData;           // Objekt für den Zugriff auf die Optionsdaten, notwendig für den Übertragungsintervall.
    private InetAddress ipTest;                 // Testobjekt für die Überprüfung der IP-Adresse.



    /**
     *
     * Methode zur Initialisierung der Daten/Elemente beim Aufruf der Aktivität.
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
        setContentView(R.layout.activity_wifi_connection);

        optionsData = OptionsModel.getInstance();

        ipAddress = findViewById(R.id.ipAdressText);
        port = findViewById(R.id.portText);
        connectBtn = findViewById(R.id.ConnectIPBtn);

        setLoadedData();
        setConnectIpBtn();

    }

    /**
     * Funktionalität des Connect-Buttons.
     * Es werden die angebene IP-Adresse und der angebene Port gesetzt
     * und mithilfe dieser Daten über die Klasse "WifiConnection" eine Verbindung aufgebaut.
     */

    private void setConnectIpBtn()
    {
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("UnstableApiUsage")
            @Override
            public void onClick(View v) {

                try
                {
                    ipTest = InetAddresses.forString(ipAddress.getText().toString()); //Prüfung ob die eingebene IP-Adresse verwertbar ist.
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    Toast.makeText(WifiConnectionActivity.this, "Fehler in der IP-Adresse, bitte Eingabe prüfen!", Toast.LENGTH_LONG).show();
                    return;
                }

                optionsData.setIpAddress(ipAddress.getText().toString());  //Setzen der Ip-Adresse
                optionsData.setPort(Integer.parseInt(port.getText().toString()));       //Setzen der Portnummer
                finish();
            }
        });
    }

    /**
     *
     * Methode die die momentan in den Optionsdaten gesetzten Werte für IP-Adresse
     * und Port anzeigt.
     *
     */

    private void setLoadedData()
    {
        ipAddress.setText(optionsData.getIpAddress());
        port.setText(String.valueOf(optionsData.getPort()));
    }

    /**
     * Deaktivierung der zurück-Taste um Fehler oder versehentliches Betätigen zu vermeiden.
     */

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }


}
