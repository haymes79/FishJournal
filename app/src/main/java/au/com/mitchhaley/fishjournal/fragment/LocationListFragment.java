package au.com.mitchhaley.fishjournal.fragment;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

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
import au.com.mitchhaley.fishjournal.contentprovider.TripEntryContentProvider;
import au.com.mitchhaley.fishjournal.db.LocationEntryTable;

/**
 * Created by mitch on 17/03/14.
 */
public class LocationListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>  {


    private SimpleCursorAdapter adapter;

    public interface TextLocationSelectListener {
        void onLocationSelected(int locationId);
    }

    public static LocationListFragment newInstance(TextLocationSelectListener listener){
        LocationListFragment f = new LocationListFragment();
        f.setTargetFragment((Fragment) listener, 0);

        return f;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Fragment parentFragment = getTargetFragment();
        ((TextLocationSelectListener) parentFragment).onLocationSelected((int) id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.location_list, container, false);

        String[] from = new String[] { LocationEntryTable.COLUMN_LOCATION_TEXT };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.trip_location_text };

        adapter = new SimpleCursorAdapter(getActivity(), R.layout.trip_location_entry_row, null, from, to, 0);
        setListAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);

        return view;
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
}