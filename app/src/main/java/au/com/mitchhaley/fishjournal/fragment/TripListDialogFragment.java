package au.com.mitchhaley.fishjournal.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.contentprovider.TripEntryContentProvider;
import au.com.mitchhaley.fishjournal.db.TripEntryTable;

/**
 * Created by mitch on 17/03/14.
 */
public class TripListDialogFragment extends DialogFragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;

    public interface SuperListener {
        void onSomethingHappened(Uri uri);
    }

    public static TripListDialogFragment newInstance(SuperListener listener){
        TripListDialogFragment f = new TripListDialogFragment();
        f.setTargetFragment((Fragment) listener, 0);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.triplist_dialog, container);

        getDialog().setTitle("Trip Select");

        ListView listview = (ListView) view.findViewById(R.id.listView);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Fragment parentFragment = getTargetFragment();
                Uri uri = Uri.parse(TripEntryContentProvider.TRIPS_URI + "/" + id);

                ((SuperListener) parentFragment).onSomethingHappened(uri);

                TripListDialogFragment.this.dismiss();
            }
        });

        fillData();
        listview.setAdapter(adapter);

        return view;
    }

    private void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { TripEntryTable.COLUMN_TITLE };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.label};

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.triplist_entry_row,
                null, from, to, 0);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            public boolean setViewValue(View aView, Cursor aCursor, int aColumnIndex) {

//                if (aColumnIndex == 4) {
//                    long dateTimeLong = aCursor.getLong(aColumnIndex);
//                    TextView textView = (TextView) aView;
//                    Date date = new Date();
//                    date.setTime(dateTimeLong);
//
//                    textView.setText(DateTimePicker.convertDate(date, "dd/MM/yyyy hh:mm"));
//                    return true;
//                }

                return false;
            }
        });
    }

    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { TripEntryTable.PRIMARY_KEY,
                TripEntryTable.COLUMN_TITLE  };
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