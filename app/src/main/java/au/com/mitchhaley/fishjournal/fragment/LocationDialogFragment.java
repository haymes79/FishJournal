package au.com.mitchhaley.fishjournal.fragment;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.contentprovider.TripEntryContentProvider;
import au.com.mitchhaley.fishjournal.db.TripEntryTable;

/**
 * Created by mitch on 17/03/14.
 */
public class LocationDialogFragment extends DialogFragment implements GoogleMap.OnMapClickListener {

    private SupportMapFragment fragment;

    private LatLng displayCoords;

    public interface SuperListener {
        void onSomethingHappened(List<Address> addresses);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Fragment parentFragment = getTargetFragment();

        Geocoder gc = new Geocoder(getActivity());
        try {
            List<Address> addresses = gc.getFromLocation(latLng.latitude, latLng.longitude, 1);

            ((SuperListener) parentFragment).onSomethingHappened(addresses);
        } catch (IOException e) {
            return;
        }

        getDialog().dismiss();
    }



    public static LocationDialogFragment newInstance(SuperListener listener, double latitude, double longitude){
        LocationDialogFragment f = new LocationDialogFragment();
        f.setTargetFragment((Fragment) listener, 0);

        f.displayCoords = new LatLng(latitude, longitude);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.map, container);

        getDialog().setTitle("Location Select");

        fragment = new SupportMapFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.mapView, fragment).commit();



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        fragment.getMap().setOnMapClickListener(this);

        Marker melbourne = fragment.getMap().addMarker(new MarkerOptions().position(displayCoords).draggable(true));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(displayCoords, 15);
        fragment.getMap().animateCamera(cameraUpdate);
    }
}