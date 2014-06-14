package au.com.mitchhaley.fishjournal.db;

import java.util.Calendar;

import android.content.ContentValues;
import android.net.Uri;

import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.fragment.FishConditionsFragment;
import au.com.mitchhaley.fishjournal.fragment.FishDetailsFragment;
import au.com.mitchhaley.fishjournal.fragment.FishLocationFragment;
import au.com.mitchhaley.fishjournal.fragment.FishSpeciesFragment;
import au.com.mitchhaley.fishjournal.fragment.FishTypeListFragment;

public class FishEntryContentHelper {

	public static int update(FishEntryActivity activity) {

        ContentValues values = populateContentValues(activity);

        return activity.getContentResolver().update(activity.getFishEntry(), values, null, null);

	}

    public static Uri create(FishEntryActivity activity) {

        ContentValues values = populateContentValues(activity);

        return activity.getContentResolver().insert(FishEntryContentProvider.FISHES_URI, values);
    }

    private static ContentValues populateContentValues(FishEntryActivity activity) {
        FishDetailsFragment fishDetailsFragment = activity.getFishDetailsFragment();

        FishConditionsFragment fishConditionsFragment = activity.getFishConditionsFragment();

        FishLocationFragment fishLocationFragment = activity.getFishLocationFragment();

        FishSpeciesFragment fishTypeFragment = activity.getFishSpeciesFragment();

        ContentValues values = new ContentValues();

        if (fishDetailsFragment != null) {
            long tripId = fishDetailsFragment.getSelectedTripId();
            values.put(FishEntryTable.COLUMN_TRIP_KEY, tripId);

            String weight = fishDetailsFragment.getWeight();
            values.put(FishEntryTable.COLUMN_WEIGHT, weight);

            String size = fishDetailsFragment.getSize();
            values.put(FishEntryTable.COLUMN_SIZE, size);

            long dateTime = fishDetailsFragment.getSelectedDateTime();
            values.put(FishEntryTable.COLUMN_DATETIME, dateTime);

            long angler = fishDetailsFragment.getSelectedAngler();
            values.put(FishEntryTable.COLUMN_ANGLER_KEY, angler);
        }

        if (fishConditionsFragment != null) {
            String temperature = fishConditionsFragment.getTemperature();
            values.put(FishEntryTable.COLUMN_TEMPERATURE, temperature);

            String condition = fishConditionsFragment.getCondition();
            values.put(FishEntryTable.COLUMN_CONDITIONS, condition);

            String tide = fishConditionsFragment.geTide();
            values.put(FishEntryTable.COLUMN_TIDE, tide);

            String moon = fishConditionsFragment.getMoon();
            values.put(FishEntryTable.COLUMN_MOON, moon);

            String waterTemp = fishConditionsFragment.getWaterTemperature();
            values.put(FishEntryTable.COLUMN_WATER_TEMPERATURE, waterTemp);

            String waterDepth = fishConditionsFragment.getWaterDepth();
            values.put(FishEntryTable.COLUMN_WATER_DEPTH, waterDepth);
        }

        if (fishTypeFragment != null) {
            int species = fishTypeFragment.getSpeciesId();
            values.put(FishEntryTable.COLUMN_SPECIES, species);
        }

        if (fishLocationFragment != null && fishLocationFragment.hasLocation()) {
            values.put(FishEntryTable.COLUMN_LATITUDE, fishLocationFragment.getLatitude());
            values.put(FishEntryTable.COLUMN_LONGITUDE, fishLocationFragment.getLongitude());
        }

        return  values;
    }

}
