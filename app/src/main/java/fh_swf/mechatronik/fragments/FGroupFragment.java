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
public class FGroupFragment extends Fragment {

    private RadioGroup rb_g2_12;
    private RadioGroup rb_g2_34;
    private MainModel data;

    public FGroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_fgroup, container, false);

        data = MainModel.getInstance();

        rb_g2_12 = v.findViewById(R.id.radioGroupF12);
        rb_g2_34 = v.findViewById(R.id.radioGroupF34);

        radioGroup_Rb_G2();

        return v;
    }

    /**
     * Funktionalität für die Radio-Button-Gruppe der Radio-Buttons F1-F4.
     * Da es in Android nicht möglich ist Radio-Buttons in einer Gruppe direkt nebenbeinander und Untereinander
     * zu positionieren kommen hier zwei Radio-Gruppen, die kombiniert werden, zum Einsatz.
     */

    private void radioGroup_Rb_G2() {
        rb_g2_12.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonF1) {
                    data.setRb_g2((byte) 1);
                    rb_g2_34.clearCheck();
                    rb_g2_12.check(R.id.radioButtonF1);
                    System.out.println("F1 Checked!");

                }
                if (checkedId == R.id.radioButtonF2) {
                    data.setRb_g2((byte) 2);
                    rb_g2_34.clearCheck();
                    rb_g2_12.check(R.id.radioButtonF2);
                    System.out.println("F2 Checked!");
                }
            }
        });

        rb_g2_34.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonF3) {
                    data.setRb_g2((byte) 3);
                    rb_g2_12.clearCheck();
                    rb_g2_34.check(R.id.radioButtonF3);
                    System.out.println("F3 Checked!");
                }
                if (checkedId == R.id.radioButtonF4) {
                    data.setRb_g2((byte) 4);
                    rb_g2_12.clearCheck();
                    rb_g2_34.check(R.id.radioButtonF4);
                    System.out.println("F4 Checked!");
                }
            }
        });
    }

}
