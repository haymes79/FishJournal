package au.com.mitchhaley.fishjournal.fragment;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Arrays;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.contentprovider.TripEntryContentProvider;
import au.com.mitchhaley.fishjournal.db.FishEntryTable;
import au.com.mitchhaley.fishjournal.db.SpeciesEntryTable;

/**
 * Created by mitch on 18/10/13.
 */
public class FishTypeListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private SimpleCursorAdapter adapter;

    public interface TextSpeciesSelectListener {
        void onSpeciesSelected(int speciesId);
    }

    public static FishTypeListFragment newInstance(TextSpeciesSelectListener listener){
        FishTypeListFragment f = new FishTypeListFragment();
        f.setTargetFragment((Fragment) listener, 0);

        return f;
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fishtype_list, container, false);

        if (getArguments() != null && getArguments().containsKey(FishEntryContentProvider.CONTENT_ITEM_TYPE)) {
            fillData((Uri) getArguments().get(FishEntryContentProvider.CONTENT_ITEM_TYPE));
        }

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

//TODO: Select current value on list.
//            mFishSpecies.setText(cursor.getString(cursor.getColumnIndexOrThrow(SpeciesEntryTable.COLUMN_SPECIES_COMMON_TEXT)));

            // always close the cursor
            cursor.close();
        }
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	    String[] from = new String[] { SpeciesEntryTable.COLUMN_SPECIES_COMMON_TEXT };
	    // Fields on the UI to which we map
	    int[] to = new int[] { R.id.fish_type_text };
		
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.fish_type_entry_row, null, from, to, 0);
		setListAdapter(adapter);

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String selectedValue =  ((TextView)v.findViewById(R.id.fish_type_text)).getText().toString();

        Fragment parentFragment = getTargetFragment();
        ((TextSpeciesSelectListener) parentFragment).onSpeciesSelected((int) id);

	}

	// creates a new loader after the initLoader () call
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { SpeciesEntryTable.PRIMARY_KEY,
				SpeciesEntryTable.COLUMN_SPECIES_COMMON_TEXT };
		CursorLoader cursorLoader = new CursorLoader(getActivity(), TripEntryContentProvider.SPECIES_URI, projection, null, null, null);
		
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
