package au.com.mitchhaley.fishjournal.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.db.SpeciesContentHelper;

/**
 * Created by mitch on 17/03/14.
 */
public class SpeciesEntryFragment extends Fragment {

    EditText mCommonName;

    public interface SpeciesAddedListener {
        void onSpeciesAdded(Uri species);

        void closeSpeciesEntryFragment();
    }

    public static SpeciesEntryFragment newInstance(SpeciesAddedListener listener, int speciesId){
        SpeciesEntryFragment f = new SpeciesEntryFragment();
        f.setTargetFragment((Fragment) listener, 0);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fish_entry_details, container, false);

        mCommonName = (EditText) view.findViewById(R.id.commonNameEditText);

        Button cancelButton = (Button) view.findViewById(R.id.cancelButton);
        Button saveButton = (Button) view.findViewById(R.id.saveButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment parentFragment = getTargetFragment();
                ((SpeciesAddedListener) parentFragment).closeSpeciesEntryFragment();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = SpeciesContentHelper.create(getActivity(), mCommonName.getText().toString());
                Fragment parentFragment = getTargetFragment();

                ((SpeciesAddedListener) parentFragment).onSpeciesAdded(uri);
                ((SpeciesAddedListener) parentFragment).closeSpeciesEntryFragment();
            }
        });

        return view;
    }

}