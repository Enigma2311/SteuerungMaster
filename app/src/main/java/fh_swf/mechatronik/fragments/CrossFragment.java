package fh_swf.mechatronik.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import fh_swf.mechatronik.model.MainModel;
import fh_swf.mechatronik.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrossFragment extends Fragment {

    private LinearLayout relate;
    private MainModel data;


    public CrossFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cross, container, false);


        data = MainModel.getInstance();
        relate = v.findViewById(R.id.CrossLayout);

        crossControl();

        return v;

    }

    @SuppressLint("ClickableViewAccessibility")
    private void crossControl() {
        relate.setOnTouchListener(new View.OnTouchListener() {

            byte touchX;
            byte touchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int id = data.getRb_g1();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:

                        float x_1 = ((event.getX() / (relate.getWidth() / 2)) * 100) - 100;
                        float y_1 = ((event.getY() / (relate.getHeight() / 2)) * 100) - 100;

                        if (x_1 > 100) {
                            x_1 = 100;
                        }
                        if (x_1 < -100) {
                            x_1 = -100;
                        }
                        if (y_1 > 100) {
                            y_1 = 100;
                        }
                        if (y_1 < -100) {
                            y_1 = -100;
                        }

                        touchX = (byte) x_1;
                        touchY = (byte) (y_1 * -1);

                        setCrossValues(id, touchX, touchY);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        float x_2 = ((event.getX() / (relate.getWidth() / 2)) * 100) - 100;
                        float y_2 = ((event.getY() / (relate.getHeight() / 2)) * 100) - 100;

                        if (x_2 > 100) {
                            x_2 = 100;
                        }
                        if (x_2 < -100) {
                            x_2 = -100;
                        }
                        if (y_2 > 100) {
                            y_2 = 100;
                        }
                        if (y_2 < -100) {
                            y_2 = -100;
                        }

                        touchX = (byte) x_2;
                        touchY = (byte) (y_2 * -1);

                        setCrossValues(id, touchX, touchY);
                        break;

                    case MotionEvent.ACTION_UP:

                        float x_3 = ((event.getX() / (relate.getWidth() / 2)) * 100) - 100;
                        float y_3 = ((event.getY() / (relate.getHeight() / 2)) * 100) - 100;

                        if (x_3 > 100) {
                            x_3 = 100;
                        }
                        if (x_3 < -100) {
                            x_3 = -100;
                        }
                        if (y_3 > 100) {
                            y_3 = 100;
                        }
                        if (y_3 < -100) {
                            y_3 = -100;
                        }

                        touchX = (byte) x_3;
                        touchY = (byte) (y_3 * -1);

                        setCrossValues(id, touchX, touchY);
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Hilfs-Methode, die zum Setzen und Anzeigen der Werte des Steuerkreuzes dient.
     *
     * @param id     id des aktiven M-Buttons.
     * @param touchX aktueller X-Wert des Steuerkreuzes.
     * @param touchY aktueller Y-Wert des Steuerkreuzes.
     */

    private void setCrossValues(int id, byte touchX, byte touchY) {
        switch (id) {
            case 1:

                data.setRb_g1_m1_cross_left_right(touchX);
                data.setRb_g1_m1_cross_up_down(touchY);
               // m1InfoText.setText(showMButtonValues(1));
                break;

            case 2:

                data.setRb_g1_m2_cross_up_down(touchX);
                data.setRb_g1_m2_cross_left_right(touchY);
               // m2InfoText.setText(showMButtonValues(2));
                break;

            case 3:

                data.setRb_g1_m3_cross_up_down(touchY);
                data.setRb_g1_m3_cross_left_right(touchX);
             //  m3InfoText.setText(showMButtonValues(3));
                break;
        }
    }

}
