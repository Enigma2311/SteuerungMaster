package fh_swf.mechatronik.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import fh_swf.mechatronik.R;

/**
 *  Aktivität, welche die Hauptnutzeroberfläche für die Nutzung des Mini-Segway
 *  darstellt.
 *
 *  Created by Fabian Schäfer on 09.07.2018.
 *
 */

public class MainActivitySegway extends AppCompatActivity {


    /**
     *
     * Methode zur Initialisierung des der Nutzeroberfläche beim Aufruf.
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
        setContentView(R.layout.activity_main_segway);
    }

    /**
     * Methode für die Erstellung des Menüs am oberen Bildschirmrand (Optionen)
     *
     * @param menu menu stellt die Menüleiste am oberen Bildschirmrand dar.
     * @return Methode der Super-Klasse mit dem Menü als Übergabeparameter.
     * (Wird zusätzlich zum eigenen Code in der Methode ausgeführt).
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Methode zum Öffnen der Applikations-Profilauswahl bei Auswahl des Menüpunkts Optionen.
     *
     * @param item Menüpunkt Optionen in der Menüleiste.
     * @return true
     */


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent i = new Intent(MainActivitySegway.this, ProfileActivity.class);
        startActivity(i);
        finish();
        return true;
    }

    /**
     * Deaktivierung der zurück-Taste um Fehler oder versehentliches Betätigen zu vermeiden.
     */

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
