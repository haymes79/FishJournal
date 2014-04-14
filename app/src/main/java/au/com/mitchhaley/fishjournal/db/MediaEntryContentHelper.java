package au.com.mitchhaley.fishjournal.db;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import java.util.List;

import au.com.mitchhaley.fishjournal.activity.FishEntryActivity;
import au.com.mitchhaley.fishjournal.contentprovider.FishEntryContentProvider;
import au.com.mitchhaley.fishjournal.contentprovider.TripEntryContentProvider;
import au.com.mitchhaley.fishjournal.fragment.FishConditionsFragment;
import au.com.mitchhaley.fishjournal.fragment.FishDetailsFragment;
import au.com.mitchhaley.fishjournal.fragment.FishLocationFragment;
import au.com.mitchhaley.fishjournal.fragment.FishTypeListFragment;

public class MediaEntryContentHelper {

	public static void create(Context context, List<String> filesToAdd, String type, int foreignKey) {

        for (String file : filesToAdd) {

            ContentValues values = new ContentValues();
            values.put(MediaEntryTable.COLUMN_MEDIA_URI, file);
            values.put(MediaEntryTable.COLUMN_MEDIA_TYPE, "IMAGE");
            values.put(MediaEntryTable.COLUMN_MEDIA_TEXT, "");
            values.put(MediaEntryTable.COLUMN_RELATION_TYPE, type);
            values.put(MediaEntryTable.COLUMN_MEDIA_FK, foreignKey);

            context.getContentResolver().insert(TripEntryContentProvider.MEDIAS_URI, values);
        }
	}
	
}

