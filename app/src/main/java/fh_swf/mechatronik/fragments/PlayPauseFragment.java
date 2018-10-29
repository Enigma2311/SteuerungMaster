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
public class PlayPauseFragment extends Fragment {

    private ImageButton play;
    private ImageButton stop;
    private MainModel data = MainModel.getInstance();

    public PlayPauseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_pause, container, false);

        play = view.findViewById(R.id.imageButtonPlay);
        stop = view.findViewById(R.id.imageButtonPause);

        imageButtonPlay();
        imageButtonStop();

        return view;
    }

    /**
     * Funktionalität des Play-Buttons. Beim aktivieren des Buttons wird ein Bit des Bytes "ValUp_down_stop_start_on_off" gesetzt.
     * sowie der Hintergrund des Buttons weis eingefärbt.
     * Beim erneuten aktivieren des Buttons wird das vorher gesetzte Bit wieder zurückgesetzt und der Hintergrund wieder transparent.
     */

    private void imageButtonPlay() {
        play.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = false;

            @Override
            public void onClick(View v) {
                if (!isClicked) {
                    byte test = data.getButtons();
                    test = setBit(test, 2);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                    isClicked = true;
                    //play.setBackgroundColor(Color.WHITE);
                    play.getDrawable().setColorFilter(Color.parseColor("#00CCCC"), PorterDuff.Mode.MULTIPLY);
                } else {
                    byte test = data.getButtons();
                    test = clearBit(test, 2);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                    isClicked = false;
                    //play.setBackgroundColor(Color.TRANSPARENT);
                    play.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                }
            }
        });
    }

    /**
     * Funktionalität des Stop-Buttons. Beim aktivieren des Buttons wird ein Bit des Bytes "ValUp_down_stop_start_on_off" gesetzt.
     * sowie der Hintergrund des Buttons weis eingefärbt.
     * Beim erneuten aktivieren des Buttons wird das vorher gesetzte Bit wieder zurückgesetzt und der Hintergrund wieder transparent.
     */

    private void imageButtonStop() {
        stop.setOnClickListener(new View.OnClickListener() {
            boolean isClicked = false;

            @Override
            public void onClick(View v) {
                if (!isClicked) {
                    byte test = data.getButtons();
                    test = setBit(test, 3);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                    isClicked = true;
                    stop.getDrawable().setColorFilter(Color.parseColor("#00CCCC"), PorterDuff.Mode.MULTIPLY);
                } else {
                    byte test = data.getButtons();
                    test = clearBit(test, 3);
                    data.setButtons(test);
                    String s1 = String.format("%8s", Integer.toBinaryString(test % 0xFF)).replace(' ', '0');
                    System.out.println(s1);
                    isClicked = false;
                    stop.getDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                }
            }
        });
    }

}
