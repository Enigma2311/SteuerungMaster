package fh_swf.mechatronik;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * Klasse für die korrekte Datenspeicherung der drei verschiedenen Profile.
 *
 * Created by Fabian Schäfer on 18.04.2017.
 */
class ProfileDataStorage {

    private final static String PREF_HEXAPOD_OPTIONS = "Profil_Hexapod_options";   // Statisches Namensattribut für Speichern/Laden des Hexapod-Profils.
    private final static String PREF_SEGWAY_OPTIONS = "Profil_Segway_options";     // Statisches Namensattribut für Speichern/Laden des Mini-Segway-Profils.
    private final static String PREF_BB8_OPTIONS = "Profil_BB8_options";           // Statisches Namensattribut für Speichern/Laden des BB8-Profils.
    private final static String PROFILE_DATA_NAME ="optionsData";                  // Statisches Namensattribut, welches die zu speichernden Daten beschreibt.
    private OptionsModel optionsData;                                              // Optionsdaten-Objekt, welches die zu speichernden Daten enthält.

    /**
     * Konstruktor zur Initialisierung des benötigten OptionsDaten-Objekts.
     */

    ProfileDataStorage()
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
     * @param id
     *
     * ID des Profils welches gespeichert werden soll.
     *
     */

    void setBytes(Context ctx, String[] optionData, int id) {
        switch (id) {
            case 1:
                saveOptionsData(ctx, optionData, PREF_HEXAPOD_OPTIONS, PROFILE_DATA_NAME, optionsData.getIpAddress());
                break;
            case 2:
                saveOptionsData(ctx, optionData, PREF_SEGWAY_OPTIONS, PROFILE_DATA_NAME, optionsData.getIpAddress());
                break;
            case 3:
                saveOptionsData(ctx, optionData, PREF_BB8_OPTIONS, PROFILE_DATA_NAME, optionsData.getIpAddress());
                break;
        }
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
     * @param id
     *
     * ID des Profils welches geladen werden soll.
     *
     */

    void getBytes(Context ctx, int id)
    {
        switch (id){

            case 1:
                optionsData.setOptionsDataSet(loadOptionsData(ctx, PREF_HEXAPOD_OPTIONS, PROFILE_DATA_NAME));
                break;
            case 2:
                optionsData.setOptionsDataSet(loadOptionsData(ctx, PREF_SEGWAY_OPTIONS, PROFILE_DATA_NAME));
                break;
            case 3:
                optionsData.setOptionsDataSet(loadOptionsData(ctx, PREF_BB8_OPTIONS, PROFILE_DATA_NAME));
                break;
        }
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
            for (int i = 0; i < count; i++) {
                tempArray[i] = prefs.getString("OptionsData" + i, String.valueOf(i));
            }
            tempArray[5] = prefs.getString("ipAddress", "DEFAULT");
            return tempArray;
        }
        else
        {
            String[] poop = new String[10];
            System.out.println("SharedPreferences für "+profileToLoad+" existiert nicht!");
            return poop;
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
     * @param ip
     *
     * IP-Adresse die gespeichert werden soll. (Sie wird dediziert angegeben, da es mit dem gewählten Datenmodell nicht
     * ohne massive Änderungen möglich gewesen wäre es dem Array der zu speichernden Daten hinzuzufügen).
     *
     */

    private void saveOptionsData(Context ctx, String[] optionsArray, String profileToSave, String dataToSave, String ip)
    {
        SharedPreferences prefs = ctx.getSharedPreferences(profileToSave, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = prefs.edit();

            e.putString("ipAddress", ip);
            e.putInt(dataToSave, optionsArray.length);
            int count = 0;
            for(String i : optionsArray)
            {
                e.putString("OptionsData" + count++, i);
            }
            e.commit();
    }


}
