package fh_swf.mechatronik.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import fh_swf.mechatronik.model.MainModel;
import fh_swf.mechatronik.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SliderFragment extends Fragment {

    private SeekBar glider_as;
    private SeekBar glider_az;
    private MainModel data = MainModel.getInstance();

    public SliderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_slider, container, false);

        glider_as =  view.findViewById(R.id.glider_AS);
        glider_az =  view.findViewById(R.id.glider_AZ);

        seekBar_Glider_AS();
        seekBar_Glider_Az();

        return view;
    }

    /**
     * Funktionalität des Schiebereglers "Glider-AS".
     * Beim Verändern des Werts durch Änderung am Regler wird der aktuelle Wert gesetzt und
     * im Anzeigetext in der Hauptaktivität angezeigt
     * Der Wertebereich beträgt von -100 bis +100.
     */

    private void seekBar_Glider_AS() {
        glider_as.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int prog = 0;
            int id = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                prog = progress - 100;
                id = data.getRb_g1();

                setGliderASValue();
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                id = data.getRb_g1();

                setGliderASValue();

            }

            private void setGliderASValue() {
                switch (id) {
                    case 1:
                        data.setRb_g1_m1_glider_AS((byte) prog);
                        //m1InfoText.setText(showMButtonValues(1));
                        break;
                    case 2:
                        data.setRb_g1_m2_glider_AS((byte) prog);
                        //m2InfoText.setText(showMButtonValues(2));
                        break;
                    case 3:
                        data.setRb_g1_m3_glider_AS((byte) prog);
                        //m3InfoText.setText(showMButtonValues(3));
                }
            }
        });
    }

    /**
     * Funktionalität des Schiebereglers "Glider-AZ".
     * Beim Verändern des Werts durch Änderung am Regler wird der aktuelle Wert gesetzt und
     * im Anzeigetext in der Hauptaktivität angezeigt.
     * Nach entfernen des Fingers vom Display wird der Regler wieder auf den Wert null gesetzt (Auto-Zero-Funktionalität).
     * Der Wertebereich beträgt von -100 bis +100.
     */

    private void seekBar_Glider_Az() {
        glider_az.setProgress(100);

        glider_az.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int prog = 0;
            int id = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                prog = progress - 100;
                id = data.getRb_g1();

                setGliderAZValue();
            }

            private void setGliderAZValue() {
                switch (id) {
                    case 1:
                        data.setRb_g1_m1_glider_AZ((byte) prog);
                       // m1InfoText.setText(showMButtonValues(1));
                        break;
                    case 2:
                        data.setRb_g1_m2_glider_AZ((byte) prog);
                      //  m2InfoText.setText(showMButtonValues(2));
                        break;
                    case 3:
                        data.setRb_g1_m3_glider_AZ((byte) prog);
                      //  m3InfoText.setText(showMButtonValues(3));

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                id = data.getRb_g1();

                setGliderAZValue();
                glider_az.setProgress(100);
            }
        });

    }

}
