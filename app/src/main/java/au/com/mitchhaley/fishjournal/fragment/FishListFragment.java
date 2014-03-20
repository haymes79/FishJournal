    package au.com.mitchhaley.fishjournal.fragment;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import au.com.mitchhaley.fishjournal.R;
import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.adapter.SectionCursorAdapter;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.db.FishEntryTable;
import au.com.mitchhaley.fishjournal.db.TripEntryTable;
import au.com.mitchhaley.fishjournal.helper.DateTimeHelper;

    public class FishListFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PLATFORM = 1;

	private static final int DELETE_ID = Menu.FIRST + 1;
	// private Cursor cursor;
	private ItemSectionAdapter adapter;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.generallist, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		this.getListView().setDividerHeight(2);
        getLoaderManager().initLoader(0, null, this);

        adapter =  new ItemSectionAdapter(getActivity(), null);
        setListAdapter(adapter);

        registerForContextMenu(getListView());
	}
	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			Uri uri = Uri.parse(FishEntryContentProvider.FISHES_URI + "/" + info.id);
			getActivity().getContentResolver().delete(uri, null, null);

			//fillData();

			return true;
		}
		return super.onContextItemSelected(item);
	}

	// Opens the second activity if an entry is clicked
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(getActivity(), FishEntryActivity.class);
		Uri uri = Uri.parse(FishEntryContentProvider.FISHES_URI + "/" + id);
		i.putExtra(FishEntryContentProvider.CONTENT_ITEM_TYPE, uri);

		startActivity(i);
	}

	// creates a new loader after the initLoader () call
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { FishEntryTable.PRIMARY_KEY,
				FishEntryTable.COLUMN_SPECIES, FishEntryTable.COLUMN_SIZE,
				FishEntryTable.COLUMN_WEIGHT, FishEntryTable.COLUMN_DATETIME, FishEntryTable.COLUMN_CONDITIONS, FishEntryTable.COLUMN_TEMPERATURE, TripEntryTable.COLUMN_TITLE};
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
				FishEntryContentProvider.FISHES_URI, projection, null, null,FishEntryTable.COLUMN_SPECIES + " desc");
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

    private class ItemSectionAdapter extends SectionCursorAdapter {

        public ItemSectionAdapter(Context context, Cursor c) {
            super(context, c, android.R.layout.preference_category, PLATFORM);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            TextView speciesView =  (TextView) view.findViewById(R.id.label);
            speciesView.setText(cursor.getString(cursor.getColumnIndex(FishEntryTable.COLUMN_SPECIES)));

            TextView sizeView =  (TextView) view.findViewById(R.id.sizeValue);
            sizeView.setText(cursor.getString(cursor.getColumnIndex(FishEntryTable.COLUMN_SIZE)));

            TextView weightView =  (TextView) view.findViewById(R.id.weightValue);
            weightView.setText(cursor.getString(cursor.getColumnIndex(FishEntryTable.COLUMN_WEIGHT)));

            TextView dateTimeView =  (TextView) view.findViewById(R.id.dateValue);
            long dateTimeValue = cursor.getLong(cursor.getColumnIndex(FishEntryTable.COLUMN_DATETIME));
            dateTimeView.setText(DateTimeHelper.formatDate(dateTimeValue, DateTimeHelper.dateTimeDisplayFormat));

            TextView temperatureView = (TextView) view.findViewById(R.id.temperatureValue);
            temperatureView.setText(cursor.getString(cursor.getColumnIndex(FishEntryTable.COLUMN_TEMPERATURE)));

            ImageView imageView = (ImageView) view.findViewById(R.id.conditionsValue);
            String conditionValue = cursor.getString(cursor.getColumnIndex(FishEntryTable.COLUMN_CONDITIONS));

            TextView tripView = (TextView) view.findViewById(R.id.labelTrip);
            String tripTitle = cursor.getString(cursor.getColumnIndex(TripEntryTable.COLUMN_TITLE));

            if (tripTitle != null && tripTitle.length() > 0) {
                tripView.setText(" @ " + tripTitle);
            }

            for (int i=0; i < FishConditionsFragment.conditionValues.length; i++) {
                if (FishConditionsFragment.conditionValues[i].equals(conditionValue)) {
                    imageView.setImageResource(FishConditionsFragment.conditionImages[i]);
                }
            }

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return getActivity().getLayoutInflater().inflate(R.layout.fishlist_entry_row, null);
        }
    }

}