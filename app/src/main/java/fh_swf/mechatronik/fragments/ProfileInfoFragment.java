package fh_swf.mechatronik.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import fh_swf.mechatronik.model.OptionsModel;
import fh_swf.mechatronik.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileInfoFragment extends Fragment {

    private TextView profileText;
    private TextView addressText;
    private TextView profileTextStatic;
    private TextView addressTextStatic;
    private OptionsModel optionsData = OptionsModel.getInstance();


    public ProfileInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_info, container, false);

        profileText = view.findViewById(R.id.textViewProfile);
        addressText = view.findViewById(R.id.textViewIP_ID);
        addressTextStatic = view.findViewById(R.id.textViewIP_IDStatic);
        profileTextStatic = view.findViewById(R.id.textViewProfileStatic);

        setTextViewContent();
        autoSizeForText();

        return view;
    }


    private void setTextViewContent() {
        profileText.setText(optionsData.getCurrentProfile());

        if (optionsData.getTransmissionType() == 1)
            addressText.setText(optionsData.getBluetoothAddress());
        else
            addressText.setText(optionsData.getIpAddress());
    }

    private void autoSizeForText()
    {
        TextViewCompat.setAutoSizeTextTypeWithDefaults(profileText, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(addressText, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(addressTextStatic, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(profileTextStatic, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
    }
}
