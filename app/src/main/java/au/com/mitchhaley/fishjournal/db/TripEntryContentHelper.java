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
import au.com.mitchhaley.fishjournal.fragment.TripLocationFragment;

public class TripEntryContentHelper {

	public static Uri create(TripEntryActivity activity) {

        TripDetailsFragment tripDetails = activity.getTripDetailsFragment();

        TripLocationFragment tripLocation = activity.getTripLocationFragment();

        String title = tripDetails.getTitle();


        long startDateTime = tripDetails.getSelectedStartDateTime();
        long endDateTime = tripDetails.getSelectedEndDateTime();




        ContentValues values = new ContentValues();
        values.put(TripEntryTable.COLUMN_TITLE, title);

        if (tripLocation.getLocationId() <= 0) {
            Uri uri = createTripLocation(activity);
            values.put(TripEntryTable.COLUMN_LOCATION, Integer.parseInt(uri.getLastPathSegment()));
        } else {
            values.put(TripEntryTable.COLUMN_LOCATION, tripLocation.getLocationId());
        }


        values.put(TripEntryTable.COLUMN_START_DATETIME, startDateTime);
        values.put(TripEntryTable.COLUMN_END_DATETIME, endDateTime);


        return activity.getContentResolver().insert(TripEntryContentProvider.TRIPS_URI, values);
	}

    private static Uri createTripLocation(TripEntryActivity activity) {
        ContentValues values = new ContentValues();

        TripLocationFragment tripLocation = activity.getTripLocationFragment();

        values.put(LocationEntryTable.COLUMN_LOCATION_TEXT, tripLocation.getTripLocation());
        values.put(LocationEntryTable.COLUMN_LATITUDE, tripLocation.getLatitude());
        values.put(LocationEntryTable.COLUMN_LONGITUDE, tripLocation.getLongitude());

        return activity.getContentResolver().insert(TripEntryContentProvider.LOCATIONS_URI, values);

    }
	
}
