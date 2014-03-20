package au.com.mitchhaley.fishjournal.db;

import java.util.Calendar;

import android.content.ContentValues;
import android.net.Uri;
import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.fragment.FishConditionsFragment;
import au.com.mitchhaley.fishjournal.fragment.FishDetailsFragment;
import au.com.mitchhaley.fishjournal.fragment.FishTypeListFragment;

public class FishEntryContentHelper {

	public static Uri create(FishEntryActivity activity) {

		FishDetailsFragment fishDetailsFragment = activity.getFishDetailsFragment();
		
		FishConditionsFragment fishConditionsFragment = activity.getFishConditionsFragment();
		
		FishTypeListFragment fishTypeFragment = activity.getFishTypeListFragment();
		String temperature = fishConditionsFragment.getTemperature();
		String condition = fishConditionsFragment.getCondition();

		String weight = fishDetailsFragment.getWeight();
		String size = fishDetailsFragment.getSize();

        String tide = fishConditionsFragment.geTide();
        String moon = fishConditionsFragment.getMoon();

		String species = fishTypeFragment.getFishSpecies();
		
		long dateTime = fishDetailsFragment.getSelectedDateTime();
        long tripId = fishDetailsFragment.getSelectedTripId();
		
        ContentValues values = new ContentValues();
        values.put(FishEntryTable.COLUMN_TRIP_KEY, tripId);
        values.put(FishEntryTable.COLUMN_CONDITIONS, condition);
        values.put(FishEntryTable.COLUMN_TEMPERATURE, temperature);
        
        values.put(FishEntryTable.COLUMN_SPECIES, species);
        
        values.put(FishEntryTable.COLUMN_SIZE, size);
        values.put(FishEntryTable.COLUMN_WEIGHT, weight);
        values.put(FishEntryTable.COLUMN_DATETIME, dateTime);
        
        return activity.getContentResolver().insert(FishEntryContentProvider.FISHES_URI, values);
	}
	
}
