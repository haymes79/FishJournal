package au.com.mitchhaley.fishjournal.fragment;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.IOException;
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
import au.com.mitchhaley.fishjournal.helper.GPSTracker;
import au.com.mitchhaley.fishjournal.picker.DateTimePicker;

/**
 * Created by mitch on 18/10/13.
 */
public class TripLocationFragment extends Fragment implements LocationFragment.LocationSelectListener, GPSTracker.LocationReceiver, LocationListFragment.TextLocationSelectListener {
    
    private EditText mTripLocation;

    private double longitude;

    private double latitude;

    private double currentLongitude = 0.0;

    private double currentLatitude = 0.0;

    private SimpleCursorAdapter adapter;

    private GPSTracker gpsTracker;

    private int locationId = -1;

    private static final int MAP_MODE = 0;
    private static final int LIST_MODE = 1;

    private int fragmentDisplayMode = LIST_MODE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((TripEntryActivity) getActivity()).setTripLocationFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tripentry_location, container, false);

        mTripLocation = (EditText) view.findViewById(R.id.tripLocationEdit);

        gpsTracker = new GPSTracker(getActivity(), this);

        if (getArguments() != null && getArguments().containsKey(TripEntryContentProvider.TRIP_CONTENT_ITEM_TYPE)) {
            fillData((Uri) getArguments().get(TripEntryContentProvider.TRIP_CONTENT_ITEM_TYPE));
        }

        LocationListFragment locationListFragment = LocationListFragment.newInstance(TripLocationFragment.this);
        android.support.v4.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.add(R.id.locationFrameLayout, locationListFragment);
        ft.commit();

        ImageButton button = (ImageButton) view.findViewById(R.id.tripLocationButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fragmentDisplayMode == LIST_MODE) {
                    LocationFragment locationFragment;
                    if (currentLatitude != 0.0d && currentLongitude != 0.0d) {
                        locationFragment = LocationFragment.newInstance(TripLocationFragment.this, currentLatitude, currentLongitude);
                    } else {
                        locationFragment = LocationFragment.newInstance(TripLocationFragment.this, latitude, longitude);
                        onLocationSelected(latitude, longitude);
                    }

                    android.support.v4.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    ft.replace(R.id.locationFrameLayout, locationFragment);
                    ft.commit();
                    fragmentDisplayMode = MAP_MODE;

                } else {
                    LocationListFragment locationListFragment = LocationListFragment.newInstance(TripLocationFragment.this);
                    android.support.v4.app.FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    ft.replace(R.id.locationFrameLayout, locationListFragment);
                    ft.commit();
                    fragmentDisplayMode = LIST_MODE;
                }
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

                this.currentLongitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationEntryTable.COLUMN_LONGITUDE));
                this.currentLatitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LocationEntryTable.COLUMN_LATITUDE));
                cursor.close();
            }
    }

    @Override
    public void receiveLocation(double longitude, double latitude) {
        Log.d("FishLocationFragment", "Received new Location");
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void onLocationSelected(double la, double lo) {
        this.currentLatitude = la;
        this.currentLongitude = lo;

        this.locationId = -1;

        Geocoder gc = new Geocoder(getActivity());
        try {
            List<Address> addresses = gc.getFromLocation(la,lo, 1);
            if (addresses != null && addresses.size() == 1) {
                mTripLocation.setText(new StringBuilder().append(addresses.get(0).getLocality()).append(", ").append(addresses.get(0).getAdminArea()));
            }
        } catch (IOException e) {
            return;
        }
    }

    @Override
    public void onLocationSelected(int locationId) {
        this.locationId = locationId;
        fillLocationData();

    }

    public String getTripLocation() {
        return mTripLocation.getText().toString();
    }

    public int getLocationId() {
        return this.locationId;
    }

    public double getLongitude() {
        return currentLongitude;
    }

    public double getLatitude() {
        return currentLatitude;
    }

}

