package fh_swf.mechatronik.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import fh_swf.mechatronik.model.OptionsModel;

import java.util.*;

/**
 *
 * Klasse für die korrekte Datenspeicherung der drei verschiedenen Profile.
 *
 * Created by Fabian Schäfer on 18.04.2017.
 */
public class ProfileDataStorage {

    private final static String PROFILE_DATA_NAME ="optionsData";                  // Statisches Namensattribut, welches die zu speichernden Daten beschreibt.
    private OptionsModel optionsData;                                              // Optionsdaten-Objekt, welches die zu speichernden Daten enthält.

    /**
     * Konstruktor zur Initialisierung des benötigten OptionsDaten-Objekts.
     */

    public ProfileDataStorage()
    {
        optionsData = OptionsModel.getInstance();
    }

    /**
     *
     * Methode zur Unterscheidung, für welches Profil die aktuellen Daten gespeichert werden sollen.
     *
     * @param ctx
     *
     * Kontext der für die Speicherung genutzt wird (in diesem Fall Applikations-Kontext, da nur eine Instanz für die
     * Speicherung / Laden der Daten benötigt wird).
     *
     * @param optionData
     *
     * Die zu speichernden OptionsDaten in einem String-Array.
     *
//     * @param id
     *
     * ID des Profils welches gespeichert werden soll.
     *
     */

//    void setBytes(Context ctx, String[] optionData, int id) {
//        switch (id) {
//            case 1:
//                saveOptionsData(ctx, optionData, PREF_HEXAPOD_OPTIONS, PROFILE_DATA_NAME, optionsData.getIpAddress());
//                break;
//            case 2:
//                saveOptionsData(ctx, optionData, PREF_SEGWAY_OPTIONS, PROFILE_DATA_NAME, optionsData.getIpAddress());
//                break;
//            case 3:
//                saveOptionsData(ctx, optionData, PREF_BB8_OPTIONS, PROFILE_DATA_NAME, optionsData.getIpAddress());
//                break;
//        }
//    }

    public void setBytes(Context ctx, String[] optionData) {

                saveOptionsData(ctx, optionData, optionsData.getCurrentProfile(), PROFILE_DATA_NAME);
    }

    /**
     *
     * Methode zur Unterscheidung, für welches Profil die aktuellen Daten geladen werden sollen.
     *
     * @param ctx
     *
     * Kontext der für das wiederherstellen der Daten genutzt wird (in diesem Fall Applikations-Kontext, da nur eine Instanz für die
     * Speicherung / Laden der Daten benötigt wird).
     *
//     * @param id
     *
     * ID des Profils welches geladen werden soll.
     *
     */

//    void getBytes(Context ctx, int id)
//    {
//        switch (id){
//
//            case 1:
//                optionsData.setOptionsDataSet(loadOptionsData(ctx, PREF_HEXAPOD_OPTIONS, PROFILE_DATA_NAME));
//                break;
//            case 2:
//                optionsData.setOptionsDataSet(loadOptionsData(ctx, PREF_SEGWAY_OPTIONS, PROFILE_DATA_NAME));
//                break;
//            case 3:
//                optionsData.setOptionsDataSet(loadOptionsData(ctx, PREF_BB8_OPTIONS, PROFILE_DATA_NAME));
//                break;
//        }
//    }

    public void getBytes(Context ctx)
    {
                optionsData.setOptionsDataSet(loadOptionsData(ctx, optionsData.getCurrentProfile(), PROFILE_DATA_NAME));
    }

    /**
     *
     * Methode zum Laden der in den SharedPreferences gespeicherten Einstellungs-Daten für ein bestimmtes Profil.
     * Die Daten werden aus den SharedPreferences nacheinander in ein String-Array eingelesen.
     * Das String Array wird genutzt um die Objekte/Variablen der OptionsModel-Klasse zu setzen.
     *
     * @param ctx
     *
     * Kontext der für das wiederherstellen der Daten genutzt wird (in diesem Fall Applikations-Kontext, da nur eine Instanz für die
     * Speicherung / Laden der Daten benötigt wird).
     *
     * @param profileToLoad
     *
     * Name des Profils, dessen Daten geladen werden sollen.
     *
     * @param dataToLoad
     *
     * Name der Daten die geladen werden sollen.
     *
     * @return
     *
     * String-Array, welches die aus den SharedPreferences geladenen Daten enthält.
     *
     */

    private String[] loadOptionsData(Context ctx, String profileToLoad, String dataToLoad)
    {
        String[] tempArray = new String[10];
        SharedPreferences prefs = ctx.getSharedPreferences(profileToLoad, Context.MODE_PRIVATE);
        if(prefs.contains(dataToLoad)) {
            int count = prefs.getInt(dataToLoad, 0);
            System.out.println("loadOptionsData - Count : " + count);
            for (int i = 0; i < count; i++) {
                tempArray[i] = prefs.getString("optionsData" + i, String.valueOf(i));
            }
            return tempArray;
        }
        else
        {
            Toast.makeText(ctx, "SharedPreferences für "+profileToLoad+" existiert nicht!", Toast.LENGTH_LONG).show();
            System.out.println("SharedPreferences für "+profileToLoad+" existiert nicht!");
            return tempArray;
        }
    }


    /**
     *
     * Methode die zum Speichern der OptionsDaten eines gewählten Profils in den SharedPreferences.
     * Die Daten werden aus einem String-Array áusgelesen und dann nacheinander in einer Schleife in den SharedPreferences abgelegt.
     *
     * @param ctx
     *
     * Kontext der für das wiederherstellen der Daten genutzt wird (in diesem Fall Applikations-Kontext, da nur eine Instanz für die
     * Speicherung / Laden der Daten benötigt wird).
     *
     * @param optionsArray
     *
     * Array das die zu Speichernden Daten enthält.
     *
     * @param profileToSave
     *
     * Profilname, dessen Daten gespeichert werden sollen.
     *
     * @param dataToSave
     *
     * Name der Daten die gespeichert werden sollen.
     *
//     * @param ip
     *
     * IP-Adresse die gespeichert werden soll. (Sie wird dediziert angegeben, da es mit dem gewählten Datenmodell nicht
     * ohne massive Änderungen möglich gewesen wäre es dem Array der zu speichernden Daten hinzuzufügen).
     *
     */

    private void saveOptionsData(Context ctx, String[] optionsArray, String profileToSave, String dataToSave)
    {
        SharedPreferences prefs = ctx.getSharedPreferences(profileToSave, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = prefs.edit();

            e.putInt(dataToSave, optionsArray.length);
            int count = 0;
            for(String i : optionsArray)
            {
                e.putString("optionsData" + count++, i);
            }
            e.commit();
    }

//    void saveProfileNamesArray(Context ctx, List<String> profiles)
//    {
//        SharedPreferences profs = ctx.getSharedPreferences("profile_name_list", Context.MODE_PRIVATE);
//        SharedPreferences.Editor profNameEditor = profs.edit();
//
//        int profileCounter = 0;
//
//        for (String profNames : profiles)
//        {
//            profNameEditor.putString("ProfileName" + profileCounter++, profNames);
//        }
//        profNameEditor.commit();
//
//    }

    public void saveProfileNamesArray(Context ctx, List<String> profiles) {
        SharedPreferences profs = ctx.getSharedPreferences("profile_name_list", Context.MODE_PRIVATE);
        SharedPreferences.Editor profNameEditor = profs.edit();

        Set<String> profileNamesSet = new TreeSet<>();
        profileNamesSet.addAll(optionsData.getProfileNamesList());
        profNameEditor.putStringSet("ProfileNames", profileNamesSet);
        profNameEditor.commit();

    }

    public void deleteProfileEntryFromList(Context ctx, String profileToDelete, int position)
    {
        SharedPreferences profs = ctx.getSharedPreferences("profile_name_list", Context.MODE_PRIVATE);
        SharedPreferences.Editor profNameEditor = profs.edit();
        profNameEditor.clear();
        profNameEditor.commit();

    }

//    List<String> loadProfileNamesArray(Context ctx)
//    {
//        SharedPreferences prefs = ctx.getSharedPreferences("profile_name_list", Context.MODE_PRIVATE);
//        List<String> profiles = new ArrayList<>();
//        int count = prefs.getAll().size();
//        for (int i = 0; i < count; i++ )
//        {
//            profiles.add(prefs.getString("ProfileName" + i, "ERROR"));
//        }
//
//        return profiles;
//    }

   public List<String> loadProfileNamesArray(Context ctx) {
        SharedPreferences prefs = ctx.getSharedPreferences("profile_name_list", Context.MODE_PRIVATE);
        List<String> profiles = new ArrayList<>();

        if(prefs.contains("ProfileNames")) {
            Set<String> tmpSet = prefs.getStringSet("ProfileNames", null);
            profiles.addAll(tmpSet);
            Collections.sort(profiles);
        }

        return profiles;
    }
}
