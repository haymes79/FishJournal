package au.com.mitchhaley.fishjournal.db;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import java.util.List;

import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.contentprovider.TripEntryContentProvider;
import au.com.mitchhaley.fishjournal.fragment.ContactsFragment;

public class SpeciesContentHelper {

	public static Uri create(Context context, String commonSpecies) {

            ContentValues values = new ContentValues();
            values.put(SpeciesEntryTable.COLUMN_SPECIES_COMMON_TEXT, commonSpecies);

            return context.getContentResolver().insert(TripEntryContentProvider.SPECIES_URI, values);
	}
	
}

