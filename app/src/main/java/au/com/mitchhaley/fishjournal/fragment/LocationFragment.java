package au.com.mitchhaley.fishjournal.fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import au.com.mitchhaley.fishjournal.R;

/**
 * Created by mitch on 17/03/14.
 */
public class LocationFragment extends Fragment implements GoogleMap.OnMapClickListener {

    private SupportMapFragment fragment;

    private LatLng displayCoords = null;

    public interface LocationSelectListener {
        void onLocationSelected(double latitude, double longitude);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (latLng != null) {
            fragment.getMap().clear();
            Marker marker = fragment.getMap().addMarker(new MarkerOptions().position(latLng).draggable(true));
            displayCoords = latLng;

            Fragment parentFragment = getTargetFragment();
            ((LocationSelectListener) parentFragment).onLocationSelected(displayCoords.latitude, displayCoords.longitude);
        }

    }

    public static LocationFragment newInstance(LocationSelectListener listener, double latitude, double longitude){
        LocationFragment f = new LocationFragment();
        f.setTargetFragment((Fragment) listener, 0);

        if (longitude != 0.0d && latitude != 0.0d) {
            f.displayCoords = new LatLng(latitude, longitude);
        }

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.map, container, false);

        fragment = new SupportMapFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.mapView, fragment).commit();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        fragment.getMap().setOnMapClickListener(this);

        if (displayCoords != null) {
            Marker marker = fragment.getMap().addMarker(new MarkerOptions().position(displayCoords).draggable(true));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(displayCoords, 15);
            fragment.getMap().animateCamera(cameraUpdate);
        }
    }
}