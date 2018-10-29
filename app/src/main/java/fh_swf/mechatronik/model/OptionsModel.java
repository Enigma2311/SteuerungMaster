package fh_swf.mechatronik.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Klasse für die Datenhaltung des Optionsmenüs.
 * Es handelt sich um ein Singleton-Modell, damit alle Klassen beim Zugriff auf die Optionsdaten den gleichen
 * Datenstand zur Verfügung haben.
 *
 * Created by Fabian Schäfer on 06.04.2017.
 */
public class OptionsModel {


    private static OptionsModel optionsData = new OptionsModel();  // Zum Programmstart erstellte Singleton-Instanz der OptionsModel-Klasse
    private int zero;                                              // Nulllage-Einstellung (0, 15 oder 30 Grad)
    private int transmissionTime;                                  // Übertragungsintervall der Daten (50 - 1000 Millisekunden).
    private int transmissionType;                                  // Übertragungstyp ( 1 = WLAN, 0 = Bluetooth)
    private int profileType;                                       // Variable für den Profiltyp
    private String bluetoothAddress;                               // MAC-Adresse des zu verbindenden Zielgeräts
    private String ipAddress;                                      // IP-Adresse des zu verbindenden Zielgeräts (UDP-Verbindung)
    private int port;                                              // Port-Nummer des zu verbindenen Zielgeräts (UDP-Verbindung)
    private String[] optionsDataSet;                               // Array für einen Datensatz der Optiosndaten (für Speichern/Laden)
    private List<String> profileNamesList;
    private String currentProfile;


    /**
     * Konstruktor der OptionsModel-Klasse welcher zum Programmstart die benötigten
     * Variablen/Objekte initialisiert.
     */

    private OptionsModel() {
        zero = 0;
        transmissionTime = 0;
        transmissionType = 0;
        profileType = 0;
        bluetoothAddress = "unknown";
        ipAddress = "unknown";
        port = 0;
        optionsDataSet = new String[7];
        profileNamesList = new ArrayList<>();
    }

    public static OptionsModel getInstance() {
        return optionsData;
    }

    public int getZero() {
        return zero;
    }

    public void setZero(int zero) {
        this.zero = zero;
    }

    public int getTransmissionTime() {
        return transmissionTime;
    }

    public void setTransmissionTime(int transmissionTime) {
        this.transmissionTime = transmissionTime;
    }

    public int getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(int transmissionType) {
        this.transmissionType = transmissionType;
    }

    public int getProfileType() {
        return profileType;
    }

    public void setProfileType(int profileType) {
        this.profileType = profileType;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) {
        this.bluetoothAddress = bluetoothAddress;
    }

    public String[] getOptionsDataSet() {
        optionsDataSet = setOptionsArrayForSaving();
        return optionsDataSet;
    }

    public void setOptionsDataSet(String[] optionsDataSet) {
        this.optionsDataSet = optionsDataSet;
        setOptionsArrayForLoading(optionsDataSet);
    }

    /**
     *
     * Es wird ein Array erzeugt, welches die aktuellen Werte der OptionsModel-Daten enthält.
     * Dieses Array wird benötigt um die Daten in den SharedPreferences zu speichern.
     *
     * @return
     *
     * String-Array, welches alle Optionsdaten enthält.
     *
     */

    private String[] setOptionsArrayForSaving() {

        String[] tmpArray = new String[10];

        tmpArray[0] = String.valueOf(getZero());
        tmpArray[1] = String.valueOf(getTransmissionTime());
        tmpArray[2] = String.valueOf(getTransmissionType());
        tmpArray[3] = String.valueOf(getProfileType());
        tmpArray[4] = String.valueOf(getPort());
        tmpArray[5] = getBluetoothAddress();
        tmpArray[6] = getIpAddress();

        return tmpArray;

    }

    /**
     *
     * Es werden die aus den SharedPreferences geladenen Daten übergeben
     * und den korrekten Variablen/Objekten zugewiesen.
     *
     * @param dataToLoad
     *
     * String-Array, welches die geladenen Daten enthält.
     *
     */

    private void setOptionsArrayForLoading(String[] dataToLoad) {

        setZero(Integer.parseInt(dataToLoad[0]));
        setTransmissionTime(Integer.parseInt(dataToLoad[1]));
        setTransmissionType(Integer.parseInt(dataToLoad[2]));
        setProfileType(Integer.parseInt(dataToLoad[3]));
        setPort(Integer.parseInt(dataToLoad[4]));
        setBluetoothAddress(dataToLoad[5]);
        setIpAddress(dataToLoad[6]);

    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void addProfileName (String profileNameToAdd)
    {

        profileNamesList.add(profileNameToAdd);

//        int profileArrayLength = profileNamesList.length;
//
//        if(profileArrayLength == 0)
//        {
//            profileNamesList[profileArrayLength] = profileNameToAdd;
//        }
//        else
//        {
//            profileNamesList[profileArrayLength-1] = profileNameToAdd;
//        }

    }

    public List<String> getProfileNamesList() {
        return profileNamesList;
    }

    public String getCurrentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(String currentProfile) {
        this.currentProfile = currentProfile;
    }

    public void setProfileNamesList(List<String> profileNamesList) {
        this.profileNamesList = profileNamesList;
    }

}
