package au.com.mitchhaley.fishjournal.fragment;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import au.com.mitchhaley.fishjournal.db.FishEntryTable;

/**
 * Created by mitch on 18/10/13.
 */
public class FishTypeListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	private SimpleCursorAdapter adapter;
	
	private EditText mFishSpecies;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        ((FishEntryActivity) getActivity()).setFishTypeListFragment(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fishtype_list, container, false);

		mFishSpecies = (EditText) view.findViewById(R.id.fishTypeEditText);
		
		mFishSpecies.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mFishSpecies.setText("");
			}
		});

        if (getArguments() != null && getArguments().containsKey(FishEntryContentProvider.CONTENT_ITEM_TYPE)) {
            fillData((Uri) getArguments().get(FishEntryContentProvider.CONTENT_ITEM_TYPE));
        }

		return view;
	}

    private void fillData(Uri uri) {
        String[] projection = new String[] { FishEntryTable.COLUMN_SPECIES};

        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();

            mFishSpecies.setText(cursor.getString(cursor.getColumnIndexOrThrow(FishEntryTable.COLUMN_SPECIES)));

            // always close the cursor
            cursor.close();
        }
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	    String[] from = new String[] { FishEntryTable.COLUMN_SPECIES };
	    // Fields on the UI to which we map
	    int[] to = new int[] { R.id.fish_type_text };
		
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.fish_type_entry_row, null, from, to, 0);
		setListAdapter(adapter);

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String selectedValue =  ((TextView)v.findViewById(R.id.fish_type_text)).getText().toString();
		mFishSpecies.setText(selectedValue);
	}

	// creates a new loader after the initLoader () call
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { FishEntryTable.PRIMARY_KEY,
				FishEntryTable.COLUMN_SPECIES };
		CursorLoader cursorLoader = new CursorLoader(getActivity(), FishEntryContentProvider.FISHES_UNIQUE_URI, projection, null, null, null);
		
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
	
	public String getFishSpecies() {
		return mFishSpecies.getText().toString();
	}

}
