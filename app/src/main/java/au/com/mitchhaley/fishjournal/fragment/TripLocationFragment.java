package au.com.mitchhaley.fishjournal.fragment;

import android.app.Dialog;
import android.database.Cursor;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.activity.TripEntryActivity;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.contentprovider.TripEntryContentProvider;
import au.com.mitchhaley.fishjournal.db.FishEntryTable;
import au.com.mitchhaley.fishjournal.db.LocationEntryTable;
import au.com.mitchhaley.fishjournal.db.TripEntryTable;
import au.com.mitchhaley.fishjournal.helper.DateTimeHelper;
import au.com.mitchhaley.fishjournal.picker.DateTimePicker;

/**
 * Created by mitch on 18/10/13.
 */
public class TripLocationFragment  extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>, LocationDialogFragment.SuperListener {
    
    private EditText mTripLocation;

    private double longitude;

    private double latitude;

    private SimpleCursorAdapter adapter;

    private int locationId = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TripEntryActivity) getActivity()).setTripLocationFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tripentry_location, container, false);

        mTripLocation = (EditText) view.findViewById(R.id.tripLocationEdit);

        if (getArguments() != null && getArguments().containsKey(TripEntryContentProvider.TRIP_CONTENT_ITEM_TYPE)) {
            fillData((Uri) getArguments().get(TripEntryContentProvider.TRIP_CONTENT_ITEM_TYPE));
        }

        ImageButton button = (ImageButton) view.findViewById(R.id.tripLocationButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationDialogFragment dialog = LocationDialogFragment.newInstance(TripLocationFragment.this, latitude, longitude);
                dialog.show(getFragmentManager(), "location_dialog_fragment");
            }
        });

        return view;
    }


    private void fillData(Uri uri) {

        Cursor cursor = getActivity().getContentResolver().query(uri, new String[] { TripEntryTable.COLUMN_LOCATION}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            this.locationId = (cursor.getInt(cursor.getColumnIndexOrThrow(TripEntryTable.COLUMN_LOCATION)));
            cursor.close();

            fillLocationData();
        }
    }

    private void fillLocationData() {

            Uri locationUri = Uri.parse(TripEntryContentProvider.LOCATIONS_URI + "/" + locationId);
            Cursor cursor = getActivity().getContentResolver().query(locationUri, new String[] { LocationEntryTable.COLUMN_LOCATION_TEXT, LocationEntryTable.COLUMN_LATITUDE, LocationEntryTable.COLUMN_LONGITUDE}, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                mTripLocation.setText(cursor.getString(cursor.getColumnIndexOrThrow(LocationEntryTable.COLUMN_LOCATION_TEXT)));

                this.longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationEntryTable.COLUMN_LONGITUDE));
                this.latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationEntryTable.COLUMN_LATITUDE));
                cursor.close();
            }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] from = new String[] { LocationEntryTable.COLUMN_LOCATION_TEXT };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.trip_location_text };

        adapter = new SimpleCursorAdapter(getActivity(), R.layout.trip_location_entry_row, null, from, to, 0);
        setListAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        String selectedValue =  ((TextView)v.findViewById(R.id.trip_location_text)).getText().toString();

        this.locationId = (int) id;
        fillLocationData();
//        mTripLocation.setText(selectedValue);
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { LocationEntryTable.PRIMARY_KEY,
                LocationEntryTable.COLUMN_LOCATION_TEXT };
        CursorLoader cursorLoader = new CursorLoader(getActivity(), TripEntryContentProvider.LOCATIONS_URI, projection, null, null, null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

    @Override
    public void onSomethingHappened(List<Address> addresses) {
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);

            mTripLocation.setText(new StringBuilder().append(address.getLocality()).append(", ").append(address.getAdminArea()));

            this.latitude = address.getLatitude();
            this.longitude = address.getLongitude();
        }
    }

    public String getTripLocation() {
        return mTripLocation.getText().toString();
    }

    public int getLocationId() {
        return this.locationId;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}

