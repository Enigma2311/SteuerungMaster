package fh_swf.mechatronik;

/**
 * Hauptdatenklasse, welche alle Daten enthält die über die Hauptaktivität geändert werden können.
 * Es handelt sich um eine Singleton-Modell Klasse, damit nur eine Instanz der Daten vorhanden ist und alle
 * zugreifenden Klassen den selben Datenstand haben.
 *
 * Created by Fabian Schäfer 15.03.2017.
 */

class MainModel {

    private static MainModel data = new MainModel();   // Initialisierung des Singleton-Objekts, welches von allen Klassen genutzt wird.
    private byte rb_g1_m1_glider_AS;                   // Wert des oberen Schiebereglers, abhängig von Radio-Button M1.
    private byte rb_g1_m2_glider_AS;                   // Wert des oberen Schiebereglers, abhängig von Radio-Button M2.
    private byte rb_g1_m3_glider_AS;                   // Wert des oberen Schiebereglers, abhängig von Radio-Button M3.
    private byte rb_g1_m1_glider_AZ;                   // Wert des unteren Schiebereglers, abhängig von Radio-Button M1.
    private byte rb_g1_m2_glider_AZ;                   // Wert des unteren Schiebereglers, abhängig von Radio-Button M2.
    private byte rb_g1_m3_glider_AZ;                   // Wert des unteren Schiebereglers, abhängig von Radio-Button M3.
    private byte buttons;                              // Byte für die Werte der Buttons ValUp, ValDown, Play, Stop, und dem Schalter off/on.
    private byte rb_g1_m1_cross_left_right;            // Byte für die Werte des Steuerkreuzes für die Bewegung nach links und rechts, abhängig von Radio-Button M1.
    private byte rb_g1_m1_cross_up_down;               // Byte für die Werte des Steuerkreuzes für die Bewegung nach oben und unten, abhängig von Radio-Button M1.
    private byte rb_g1_m2_cross_up_down;               // // Byte für die Werte des Steuerkreuzes für die Bewegung nach oben und unten, abhängig von Radio-Button M2.
    private byte rb_g1_m2_cross_left_right;            // Byte für die Werte des Steuerkreuzes für die Bewegung nach links und rechts, abhängig von Radio-Button M2.
    private byte rb_g1_m3_cross_up_down;               // Byte für die Werte des Steuerkreuzes für die Bewegung nach oben und unten, abhängig von Radio-Button M3.
    private byte rb_g1_m3_cross_left_right;            // Byte für die Werte des Steuerkreuzes für die Bewegung nach links und rechts, abhängig von Radio-Button M3.
    private byte accel_x;                              // Byte für die Werte der Neigung des Geräts nach links / rechts.
    private byte accel_y;                              // Byte für die Werte der Neigung des Geräts nach vorne / hinten.
    private byte rb_g1;                                // Byte für die Werte der Radio-Button-Gruppe M1-M3.
    private byte rb_g2;                                // Byte für die Werte der Radio-Button-Gruppe F1-F4.

    /**
     * Konstruktor welche die Daten initialisiert.
     */

private MainModel()
{
    buttons = 0;
    rb_g1_m1_cross_left_right = 0;
    rb_g1_m1_cross_up_down = 0;
    rb_g1_m2_cross_left_right = 0;
    rb_g1_m2_cross_up_down = 0;
    rb_g1_m3_cross_left_right = 0;
    rb_g1_m3_cross_up_down = 0;
    rb_g1_m1_glider_AS = 0;
    rb_g1_m2_glider_AS = 0;
    rb_g1_m3_glider_AS = 0;
    rb_g1_m1_glider_AZ = 0;
    rb_g1_m2_glider_AZ = 0;
    rb_g1_m3_glider_AZ = 0;
    accel_x = 0;
    accel_y = 0;
    rb_g1 = 0;
}

    static MainModel getInstance()
    {
        return data;
    }

    byte getButtons() {
        return buttons;
    }

    void setButtons(byte buttons) {
        this.buttons = buttons;
    }

    byte getAccel_x() {
        return accel_x;
    }

    void setAccel_x(byte accel_x) {
        this.accel_x = accel_x;
    }

    byte getAccel_y() {
        return accel_y;
    }

    void setAccel_y(byte accel_y) {
        this.accel_y = accel_y;
    }

    private byte getRb_g1() {
        return rb_g1;
    }

    void setRb_g1(byte rb_g1) {
        this.rb_g1 = rb_g1;
    }

    byte getRb_g1_m1_cross_left_right() {
        return rb_g1_m1_cross_left_right;
    }

    void setRb_g1_m1_cross_left_right(byte rb_g1_m1_cross_left_right) {

        if(rb_g1_m1_cross_left_right > 100)
        {
            rb_g1_m1_cross_left_right = 100;
        }
        else if(rb_g1_m1_cross_left_right < -100)
        {
            rb_g1_m1_cross_left_right = -100;
        }
        this.rb_g1_m1_cross_left_right = rb_g1_m1_cross_left_right;
    }

    byte getRb_g1_m2_cross_up_down() {
        return rb_g1_m2_cross_up_down;
    }

    void setRb_g1_m2_cross_up_down(byte rb_g1_m2_cross_up_down) {
        if(rb_g1_m2_cross_up_down > 100)
        {
            rb_g1_m2_cross_up_down = 100;
        }
        else if(rb_g1_m2_cross_up_down < -100)
        {
            rb_g1_m2_cross_up_down = -100;
        }
    this.rb_g1_m2_cross_up_down = rb_g1_m2_cross_up_down;
    }

    byte getRb_g1_m2_cross_left_right() {
        return rb_g1_m2_cross_left_right;
    }

    void setRb_g1_m2_cross_left_right(byte rb_g1_m2_cross_left_right) {
        if(rb_g1_m2_cross_left_right > 100)
        {
            rb_g1_m2_cross_left_right = 100;
        }
        else if(rb_g1_m2_cross_left_right < -100)
        {
            rb_g1_m2_cross_left_right = -100;
        }
    this.rb_g1_m2_cross_left_right = rb_g1_m2_cross_left_right;
    }

    byte getRb_g1_m3_cross_up_down() {
        return rb_g1_m3_cross_up_down;
    }

    void setRb_g1_m3_cross_up_down(byte rb_g1_m3_cross_up_down) {
        if(rb_g1_m3_cross_up_down > 100)
        {
            rb_g1_m3_cross_up_down = 100;
        }
        else if(rb_g1_m3_cross_up_down < -100)
        {
            rb_g1_m3_cross_up_down = -100;
        }
        this.rb_g1_m3_cross_up_down = rb_g1_m3_cross_up_down;
    }

    byte getRb_g1_m3_cross_left_right() {
        return rb_g1_m3_cross_left_right;
    }

    void setRb_g1_m3_cross_left_right(byte rb_g1_m3_cross_left_right) {
        if(rb_g1_m3_cross_left_right > 100)
        {
            rb_g1_m3_cross_left_right = 100;
        }
        else if(rb_g1_m3_cross_left_right < -100)
        {
            rb_g1_m3_cross_left_right = -100;
        }
        this.rb_g1_m3_cross_left_right = rb_g1_m3_cross_left_right;
    }

    byte getRb_g1_m1_cross_up_down() {
        return rb_g1_m1_cross_up_down;
    }

    void setRb_g1_m1_cross_up_down(byte rb_g1_m1_cross_up_down) {
        if(rb_g1_m1_cross_up_down > 100)
        {
            rb_g1_m1_cross_up_down = 100;
        }
        else if(rb_g1_m1_cross_up_down < -100)
        {
            rb_g1_m1_cross_up_down = -100;
        }
        this.rb_g1_m1_cross_up_down = rb_g1_m1_cross_up_down;
    }

    private byte getRb_g2() {
        return rb_g2;
    }

    void setRb_g2(byte rb_g2) {
        this.rb_g2 = rb_g2;
    }

    byte getRb_g1_m1_glider_AS() {
        return rb_g1_m1_glider_AS;
    }

    void setRb_g1_m1_glider_AS(byte rb_g1_m1_glider_AS) {
        this.rb_g1_m1_glider_AS = rb_g1_m1_glider_AS;
    }

    byte getRb_g1_m2_glider_AS() {
        return rb_g1_m2_glider_AS;
    }

    void setRb_g1_m2_glider_AS(byte rb_g1_m2_glider_AS) {
        this.rb_g1_m2_glider_AS = rb_g1_m2_glider_AS;
    }

    byte getRb_g1_m3_glider_AS() {
        return rb_g1_m3_glider_AS;
    }

    void setRb_g1_m3_glider_AS(byte rb_g1_m3_glider_AS) {
        this.rb_g1_m3_glider_AS = rb_g1_m3_glider_AS;
    }

    byte getRb_g1_m1_glider_AZ() {
        return rb_g1_m1_glider_AZ;
    }

    void setRb_g1_m1_glider_AZ(byte rb_g1_m1_glider_AZ) {
        this.rb_g1_m1_glider_AZ = rb_g1_m1_glider_AZ;
    }

    byte getRb_g1_m2_glider_AZ() {
        return rb_g1_m2_glider_AZ;
    }

    void setRb_g1_m2_glider_AZ(byte rb_g1_m2_glider_AZ) {
        this.rb_g1_m2_glider_AZ = rb_g1_m2_glider_AZ;
    }

    byte getRb_g1_m3_glider_AZ() {
        return rb_g1_m3_glider_AZ;
    }

    void setRb_g1_m3_glider_AZ(byte rb_g1_m3_glider_AZ) {
        this.rb_g1_m3_glider_AZ = rb_g1_m3_glider_AZ;
    }


    /**
     * Die zu übertragenden Daten werden zusammengestellt (aus den Hauptdaten) und dann in einen String umgewandelt.
     * Die so aufbereiteten Daten können vom Empfangsgerät gelesen und ausgewertet werden.
     *
     * @return
     *
     * Ein String der einen kompletten Datensatz für die Datenübertragung enthält.
     *
     */

     static String dataToStringForTransfer () {

        String RB_G1 = Byte.toString(data.getRb_g1());
        String RB_G2 = Byte.toString(data.getRb_g2());
        String BUTTONS = Byte.toString(data.getButtons());
        String ACCEL_X = Byte.toString(data.getAccel_x());
        String ACCEL_Y = Byte.toString(data.getAccel_y());
        String CROSS_AS_X="0";
        String CROSS_AS_Y="0";
        String GLIDER_AS="0";
        String GLIDER_AZ="0";

        switch(data.getRb_g1())
        {
            case (byte) 1:
                CROSS_AS_X = Byte.toString(data.getRb_g1_m1_cross_up_down());
                CROSS_AS_Y = Byte.toString(data.getRb_g1_m1_cross_left_right());
                GLIDER_AS = Byte.toString(data.getRb_g1_m1_glider_AS());
                GLIDER_AZ = Byte.toString(data.getRb_g1_m1_glider_AZ());
                break;
            case (byte) 2:
                CROSS_AS_X = Byte.toString(data.getRb_g1_m2_cross_up_down());
                CROSS_AS_Y = Byte.toString(data.getRb_g1_m2_cross_left_right());
                GLIDER_AS = Byte.toString(data.getRb_g1_m2_glider_AS());
                GLIDER_AZ = Byte.toString(data.getRb_g1_m2_glider_AZ());
                break;
            case (byte) 3:
                CROSS_AS_X = Byte.toString(data.getRb_g1_m3_cross_up_down());
                CROSS_AS_Y = Byte.toString(data.getRb_g1_m3_cross_left_right());
                GLIDER_AS = Byte.toString(data.getRb_g1_m3_glider_AS());
                GLIDER_AZ = Byte.toString(data.getRb_g1_m3_glider_AZ());
                break;
        }


         return ACCEL_X+","+ACCEL_Y+","+CROSS_AS_X+","+CROSS_AS_Y+","+GLIDER_AS+","+GLIDER_AZ+","+RB_G1+","+RB_G2+","+BUTTONS+"\n";
    }


}


