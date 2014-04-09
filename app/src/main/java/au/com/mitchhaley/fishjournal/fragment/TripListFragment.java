package au.com.mitchhaley.fishjournal.fragment;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Date;

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

public class TripListFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DELETE_ID = Menu.FIRST + 1;
    // private Cursor cursor;
    private SimpleCursorAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.generallist, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.getListView().setDividerHeight(2);
        fillData();
        registerForContextMenu(getListView());
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                        .getMenuInfo();
                Uri uri = Uri.parse(TripEntryContentProvider.TRIPS_URI + "/" + info.id);
                getActivity().getContentResolver().delete(uri, null, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    // Opens the second activity if an entry is clicked
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(getActivity(), TripEntryActivity.class);
        Uri uri = Uri.parse(TripEntryContentProvider.TRIPS_URI + "/" + id);
        i.putExtra(TripEntryContentProvider.TRIP_CONTENT_ITEM_TYPE, uri);

        startActivity(i);
    }

    private void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { TripEntryTable.COLUMN_TITLE, TripEntryTable.COLUMN_START_DATETIME, TripEntryTable.COLUMN_END_DATETIME, LocationEntryTable.COLUMN_LOCATION_TEXT};
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.label, R.id.startDateValue, R.id.endDateValue, R.id.locationValue};

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.triplist_entry_row,
                null, from, to, 0);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            public boolean setViewValue(View aView, Cursor aCursor, int aColumnIndex) {

                if (aColumnIndex == 2 || aColumnIndex == 3) {
                    long dateTimeLong = aCursor.getLong(aColumnIndex);
                    TextView textView = (TextView) aView;
                    Date date = new Date();
                    date.setTime(dateTimeLong);

                    textView.setText(DateTimeHelper.convertDate(date, "dd/MM/yyyy"));
                    return true;
                }

                return false;
            }
        });

        setListAdapter(adapter);
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { TripEntryTable.PRIMARY_KEY,
                TripEntryTable.COLUMN_TITLE, TripEntryTable.COLUMN_START_DATETIME, TripEntryTable.COLUMN_END_DATETIME, LocationEntryTable.COLUMN_LOCATION_TEXT  };
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                TripEntryContentProvider.TRIPS_URI, projection, null, null,null);
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