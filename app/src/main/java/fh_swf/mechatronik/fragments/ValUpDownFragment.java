package fh_swf.mechatronik.fragments;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import fh_swf.mechatronik.model.MainModel;
import fh_swf.mechatronik.R;

import static fh_swf.mechatronik.classes.BitManipulation.clearBit;
import static fh_swf.mechatronik.classes.BitManipulation.setBit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ValUpDownFragment extends Fragment {

    private ImageButton valUp;
    private ImageButton valDown;
    private MainModel data = MainModel.getInstance();

    public ValUpDownFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_val_up_down, container, false);

        valUp = view.findViewById(R.id.buttonValUp);
        valDown = view.findViewById(R.id.buttonValDown);

        imageButtonValUp();
        imageButtonValDown();

        return view;
    }

    /**
     * Funktionalität des ValUp-Buttons (Pfeil oben). Beim aktivieren des Buttons wird ein Bit des Bytes "ValUp_down_stop_start_on_off" gesetzt.
     * sowie der Hintergrund des Buttons weis eingefärbt.
     * Beim erneuten aktivieren des Buttons wird das vorher gesetzte Bit wieder zurückgesetzt und der Hintergrund wieder transparent.
     */

    private void imageButtonValUp() {
        valUp.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = false;

            @Override
            public void onClick(View v) {
                if (!isClicked) {
                    byte valUpChecked = data.getButtons();
                    valUpChecked = setBit(valUpChecked, 0);
                    data.setButtons(valUpChecked);
                    String s1 = String.format("%8s", Integer.toBinaryString(valUpChecked % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                    isClicked = true;
                    //valUp.setBackgroundColor(Color.WHITE);
                    valUp.getDrawable().setColorFilter(Color.parseColor("#00CCCC"), PorterDuff.Mode.MULTIPLY);

                } else {
                    byte valUpUnChecked = data.getButtons();
                    valUpUnChecked = clearBit(valUpUnChecked, 0);
                    data.setButtons(valUpUnChecked);
                    String s1 = String.format("%8s", Integer.toBinaryString(valUpUnChecked % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                    isClicked = false;
                    valUp.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

                }
            }
        });
    }

    /**
     * Funktionalität des ValDown-Buttons (Pfeil unten). Beim aktivieren des Buttons wird ein Bit des Bytes "ValUp_down_stop_start_on_off" gesetzt,
     * sowie der Hintergrund des Buttons weis eingefärbt.
     * Beim erneuten aktivieren des Buttons wird das vorher gesetzte Bit wieder zurückgesetzt und der Hintergrund wieder transparent.
     */

    private void imageButtonValDown() {
        valDown.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = false;

            @Override
            public void onClick(View v) {
                if (!isClicked) {
                    byte test = data.getButtons();
                    test = setBit(test, 1);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                    isClicked = true;
                    valDown.getDrawable().setColorFilter(Color.parseColor("#00CCCC"), PorterDuff.Mode.MULTIPLY);
                } else {
                    byte test = data.getButtons();
                    test = clearBit(test, 1);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                    isClicked = false;
                    valDown.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                }
            }
        });
    }

}
