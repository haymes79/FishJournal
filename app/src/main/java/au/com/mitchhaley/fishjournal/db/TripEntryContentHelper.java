package au.com.mitchhaley.fishjournal.db;

import android.content.ContentValues;
import android.net.Uri;

import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.activity.TripEntryActivity;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.contentprovider.TripEntryContentProvider;
import au.com.mitchhaley.fishjournal.fragment.FishConditionsFragment;
import au.com.mitchhaley.fishjournal.fragment.FishDetailsFragment;
import au.com.mitchhaley.fishjournal.fragment.FishTypeListFragment;
import au.com.mitchhaley.fishjournal.fragment.TripDetailsFragment;

public class TripEntryContentHelper {

	public static Uri create(TripEntryActivity activity) {

        TripDetailsFragment tripDetails = activity.getTripDetailsFragment();

        String title = tripDetails.getTitle();
        String location = "";

        long startDateTime = tripDetails.getSelectedStartDateTime();
        long endDateTime = tripDetails.getSelectedEndDateTime();

        ContentValues values = new ContentValues();
        values.put(TripEntryTable.COLUMN_TITLE, title);
        values.put(TripEntryTable.COLUMN_LOCATION, location);
        values.put(TripEntryTable.COLUMN_START_DATETIME, startDateTime);
        values.put(TripEntryTable.COLUMN_END_DATETIME, endDateTime);

        return activity.getContentResolver().insert(TripEntryContentProvider.TRIPS_URI, values);
	}
	
}
