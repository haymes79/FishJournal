package au.com.mitchhaley.fishjournal.fragment;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;

import java.io.IOException;
import java.util.List;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.activity.TripEntryActivity;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.contentprovider.TripEntryContentProvider;
import au.com.mitchhaley.fishjournal.db.LocationEntryTable;
import au.com.mitchhaley.fishjournal.db.SpeciesEntryTable;
import au.com.mitchhaley.fishjournal.db.TripEntryTable;
import au.com.mitchhaley.fishjournal.helper.GPSTracker;

/**
 * Created by mitch on 18/10/13.
 */
public class FishSpeciesFragment extends Fragment implements FishTypeListFragment.TextSpeciesSelectListener, SpeciesEntryFragment.SpeciesAddedListener {

    private SimpleCursorAdapter adapter;

    private EditText mFishSpecies;

    private int speciesId = -1;

    private static final int ENTRY_MODE = 0;
    private static final int LIST_MODE = 1;

    private int fragmentDisplayMode = LIST_MODE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((FishEntryActivity) getActivity()).setFishSpeciesFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fish_species, container, false);

        mFishSpecies = (EditText) view.findViewById(R.id.fishSpeciesEdit);

        if (getArguments() != null && getArguments().containsKey(FishEntryContentProvider.CONTENT_ITEM_TYPE)) {
            fillData((Uri) getArguments().get(FishEntryContentProvider.CONTENT_ITEM_TYPE));
        }


        FishTypeListFragment fishTypeListFragment = FishTypeListFragment.newInstance(FishSpeciesFragment.this);
        android.support.v4.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.fishSpeciesFrameLayout, fishTypeListFragment);
        ft.commit();

        ImageButton button = (ImageButton) view.findViewById(R.id.fishSpeciesAddButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fragmentDisplayMode == LIST_MODE) {
                    SpeciesEntryFragment speciesEntryFragment;

                    speciesEntryFragment = SpeciesEntryFragment.newInstance(FishSpeciesFragment.this, speciesId);

                    android.support.v4.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    ft.replace(R.id.fishSpeciesFrameLayout, speciesEntryFragment);
                    ft.commit();
                    fragmentDisplayMode = ENTRY_MODE;

                } else {
                    closeSpeciesEntryFragment();
                }
            }
        });

        return view;
    }


    private void fillData(Uri uri) {
        if (uri == null || uri.getLastPathSegment() == null) {
            return;
        }

        String[] projection = new String[] { SpeciesEntryTable.COLUMN_SPECIES_COMMON_TEXT};

        String fishSpeciesId = uri.getLastPathSegment();

        Uri speciesUri = Uri.parse(TripEntryContentProvider.SPECIES_URI + "/" + fishSpeciesId);
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            mFishSpecies.setText(cursor.getString(cursor.getColumnIndexOrThrow(SpeciesEntryTable.COLUMN_SPECIES_COMMON_TEXT)));

            // always close the cursor
            cursor.close();
        }
    }

    private void fillSpeciesData(Uri uri) {
            Cursor cursor = getActivity().getContentResolver().query(uri, new String[] { SpeciesEntryTable.COLUMN_SPECIES_COMMON_TEXT}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                mFishSpecies.setText(cursor.getString(cursor.getColumnIndexOrThrow(SpeciesEntryTable.COLUMN_SPECIES_COMMON_TEXT)));

                cursor.close();
            }
    }



    @Override
    public void onSpeciesSelected(int speciesId) {
        this.speciesId = speciesId;
        Uri speciesUri = Uri.parse(TripEntryContentProvider.SPECIES_URI + "/" + speciesId);
        fillSpeciesData(speciesUri);

    }

    public int getSpeciesId() {
        return this.speciesId;
    }


    @Override
    public void onSpeciesAdded(Uri speciesUri) {
        this.speciesId = Integer.parseInt(speciesUri.getLastPathSegment());
        fillSpeciesData(speciesUri);

    }

    @Override
    public void closeSpeciesEntryFragment() {
        FishTypeListFragment fishTypeListFragment = FishTypeListFragment.newInstance(FishSpeciesFragment.this);
        android.support.v4.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fishSpeciesFrameLayout, fishTypeListFragment);
        ft.commit();
        fragmentDisplayMode = LIST_MODE;
    }
}

