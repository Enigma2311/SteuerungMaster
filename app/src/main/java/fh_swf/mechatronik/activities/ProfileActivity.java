package fh_swf.mechatronik.activities;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import android.view.View;
import fh_swf.mechatronik.model.MainModel;
import fh_swf.mechatronik.model.ProfileDataStorage;
import fh_swf.mechatronik.R;
import fh_swf.mechatronik.model.OptionsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Datenklasse für die Daten der Einstellungen (Optionen).
 * Es handelt sich um eine Singleton-Modell Klasse, damit nur eine Instanz der Daten vorhanden ist und alle
 * zugreifenden Klassen den selben Datenstand haben.
 *
 * Created by Fabian Schäfer on 03.04.2017.
 *
 */

public class ProfileActivity extends ListActivity {

    private OptionsModel optionsData;                   //Objekt für die Profildaten
    private ProfileDataStorage storage;                 //Objekt zur Speicherung der Daten in den Shared-Preferences
    private ArrayAdapter<String> mProfileArrayAdapter;  //ArrayAdapter zur Anzeige der Profilnamen in einer Liste
    private Button buttonNew;                           //Button für die Neuanlage von Profilen
    private Button buttonEdit;                          //Button für die Editierung von Profilen
    private Button buttonDelete;                        //Button zum Löschen von Profilen
    private Button buttonUse;                           //Button um ein gewähltes Profil anzuwenden
    private boolean isItemChecked;                      //Hilfsvariable zur Prüfung ob ein Profil ausgewählt wurde
    private int pos;                                    //Hilfsvariable für die Position des Profileintrags in der Liste
    private MainModel main = MainModel.getInstance();   //Objekt für die Hauptdaten.


    /**
     *
     * Methode zur Initialisierung der Profilanzeige bei Aufruf.
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
        setContentView(R.layout.activity_profile);

        storage = new ProfileDataStorage();

        optionsData = OptionsModel.getInstance();
        buttonNew = findViewById(R.id.profileNewButton);
        buttonEdit = findViewById(R.id.profileEditButton);
        buttonDelete = findViewById(R.id.profileDeleteButton);
        buttonUse = findViewById(R.id.profileUseButton);

        List<String> tmpList = new ArrayList<>((storage.loadProfileNamesArray(ProfileActivity.this)));

        optionsData.setProfileNamesList(tmpList);
        mProfileArrayAdapter = new ArrayAdapter<>(this, R.layout.listrow_layout, R.id.list_row_view, optionsData.getProfileNamesList());

        setListAdapter(mProfileArrayAdapter);

        isItemChecked = false;
        setNewBtnAction();
        setEditBtnAction();
        setDeleteBtnAction();
        setUseButtonAction();

        buttonEdit.setVisibility(View.INVISIBLE);
        buttonDelete.setVisibility(View.INVISIBLE);

    }

    /**
     * Beim erneuten Aufruf der Activity werden der edit/Delete-Button wieder ausgeblendet
     */

    @Override
    public void onResume()
    {
        super.onResume();

        buttonEdit.setVisibility(View.INVISIBLE);
        buttonDelete.setVisibility(View.INVISIBLE);

        mProfileArrayAdapter.notifyDataSetChanged();
    }

    /**
     *
     * Methode welche beim Antippen eines Listeneintrags diesen Eintrag als angewählt setzt und
     * den Editieren/Löschen-Button einblendet.
     *
     * @param l
     * Die Liste der Einträge
     * @param v
     * Die View in der die Liste angezeigt wird
     * @param position
     * Die Position des Eintrags in der Liste
     * @param id
     * Die inter ID des gewählten Eintrags
     */

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        String item = (String) getListAdapter().getItem(position);

        l.setItemChecked(position,true);
        v.getFocusables(position);
        v.setSelected(true);
        optionsData.setCurrentProfile(item);
        isItemChecked = true;
        pos = position;
        buttonEdit.setVisibility(View.VISIBLE);
        buttonDelete.setVisibility(View.VISIBLE);
    }

    /**
     * Aktion beim Druck des Buttons "Neues Profil".
     * Es wird das Fenster für die Profileinstellungen geöffnet.
     */

    private void setNewBtnAction()
    {
        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  Intent i = new Intent(ProfileActivity.this, OptionsActivity.class);
                    i.putExtra("ActionID", 1);
                    startActivity(i);
            }
        });
    }

    /**
     * Aktion beim Druck des Buttons "Profil editieren".
     * Es wird das Fenster für die Profileinstellungen mit den voreingestellten Werten des Profils geöffnet.
     * Fehlermeldung falls kein Profil gewählt wurde.
     */

    private void setEditBtnAction()
    {
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isItemChecked) {
                    Intent i = new Intent(ProfileActivity.this, OptionsActivity.class);
                    i.putExtra("ActionID", 2);
                    i.putExtra("Index", pos);
                    storage.getBytes(ProfileActivity.this);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(ProfileActivity.this, "Bitte ein Profil zum bearbeiten auswählen!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Aktion beim Druck des Buttons "Profil löschen".
     * Es wird ein Abfrage-Dialog angezeigt, ob das gewählte Profil wirklich gelöscht werden soll.
     * Falls ja wird das Profil entfernt und kann nicht mehr ausgewählt werden.
     */


    private void setDeleteBtnAction()
    {
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ProfileActivity.this);

                alertDialogBuilder.setTitle("Eintrag Löschen");

                alertDialogBuilder
                        .setMessage("Möchten Sie das Profil " + optionsData.getCurrentProfile() + " wirklich löschen?")
                        .setCancelable(false)
                        .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                optionsData.getProfileNamesList().remove(pos);
                                mProfileArrayAdapter.notifyDataSetChanged();
                                storage.deleteProfileEntryFromList(ProfileActivity.this, optionsData.getCurrentProfile(), pos);
                                storage.saveProfileNamesArray(ProfileActivity.this, optionsData.getProfileNamesList());
                            }
                        })
                        .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
            }
        });
    }

    /**
     * Aktion beim druck des Buttons "Profil anwenden"
     * Es wird das ausgewählte Profil als Grundlage genommen und die dafür
     * gewählte Benutzeroberfläche geladen.
     */

    private void setUseButtonAction()
    {
        buttonUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isItemChecked) {
                    storage.getBytes(ProfileActivity.this);

                    if (optionsData.getProfileType() == 1 || optionsData.getProfileType() == 3) {
                        main.resetData();
                        Intent HexapodActivityIntent = new Intent(ProfileActivity.this, MainActivityHexapod.class);
                        startActivity(HexapodActivityIntent);
                    } else if (optionsData.getProfileType() == 2) {
                        main.resetData();
                        Intent SegwayActivityIntent = new Intent(ProfileActivity.this, MainActivitySegway.class);
                        startActivity(SegwayActivityIntent);
                    }
                }
                else
                {
                    Toast.makeText(ProfileActivity.this, "Bitte ein Profil auswählen.", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    /**
     * Deaktivierung der zurück-Taste um Fehler oder versehentliches Betätigen zu vermeiden.
     */

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
