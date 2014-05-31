package au.com.mitchhaley.fishjournal.fragment;

import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ZoomControls;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.List;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.db.FishEntryTable;
import au.com.mitchhaley.fishjournal.db.TripEntryTable;
import au.com.mitchhaley.fishjournal.helper.DateTimeHelper;
import au.com.mitchhaley.fishjournal.helper.GPSTracker;

/**
 * Created by mitch on 20/03/14.
 */
public class FishLocationFragment extends SupportMapFragment implements GPSTracker.LocationReceiver {

    private GoogleMap gMapView;

    private GPSTracker gpsTracker;

    private double longitude;

    private double latitude;

    private boolean hasLocation = false;

    private SupportMapFragment mapFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((FishEntryActivity) getActivity()).setFishLocationFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        gMapView = this.getMap();

        if (((FishEntryActivity) getActivity()).getFishEntry() != null) {
            fillData(((FishEntryActivity) getActivity()).getFishEntry());
            setLocationOnMap();
        } else {
            gpsTracker = new GPSTracker(getActivity(), this);
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (gpsTracker != null) {
            gpsTracker.stopUsingGPS();
        }

        if (mapFragment!= null)
            getFragmentManager().beginTransaction().remove(mapFragment).commit();
    }

    private void fillData(Uri uri) {
        String[] projection = new String[] { FishEntryTable.COLUMN_LATITUDE, FishEntryTable.COLUMN_LONGITUDE };

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            longitude = (cursor.getDouble(cursor.getColumnIndexOrThrow(FishEntryTable.COLUMN_LONGITUDE)));
            latitude = (cursor.getDouble(cursor.getColumnIndexOrThrow(FishEntryTable.COLUMN_LATITUDE)));

            hasLocation = true;

            // always close the cursor
            cursor.close();
        }
    }

    private void setLocationOnMap() {
        LatLng latLng = new LatLng(latitude, longitude);

        Marker melbourne = gMapView.addMarker(new MarkerOptions().position(latLng).draggable(true));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        gMapView.animateCamera(cameraUpdate);


    }

    @Override
    public void receiveLocation(double longitude, double latitude) {
        Log.d("FishLocationFragment", "Received new Location");
        this.latitude = latitude;
        this.longitude = longitude;

        this.hasLocation = true;

        setLocationOnMap();
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public boolean hasLocation() {
        return hasLocation;
    }
}
