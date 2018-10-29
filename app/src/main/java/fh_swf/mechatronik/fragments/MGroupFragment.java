package fh_swf.mechatronik.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import fh_swf.mechatronik.model.MainModel;
import fh_swf.mechatronik.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MGroupFragment extends Fragment {

   private RadioGroup rb_g1;
   private MainModel data = MainModel.getInstance();

    public MGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mgroup, container, false);

        rb_g1 = view.findViewById(R.id.radioGroupM1_M3);

        radioGroup_RB_G1();

        return view;

    }

    /**
     * Funktionalität der Radio-Button-Gruppe für die Radio-Buttons M1-M3.
     * Bei Aktivierung eines Buttons wird dessen Wert im Datenmodell gesetzt und der
     * entsprechende Anzeigetext in der Hauptaktivität gesetzt.
     */

    private void radioGroup_RB_G1() {
        rb_g1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.M1) {
                    //m1InfoText.setText(showMButtonValues(1));
                    data.setRb_g1((byte) 1);
                }
                if (checkedId == R.id.M2) {
                    //m2InfoText.setText(showMButtonValues(2));
                    data.setRb_g1((byte) 2);
                }
                if (checkedId == R.id.M3) {
                    //m3InfoText.setText(showMButtonValues(3));
                    data.setRb_g1((byte) 3);
                }
            }
        });
    }


}
