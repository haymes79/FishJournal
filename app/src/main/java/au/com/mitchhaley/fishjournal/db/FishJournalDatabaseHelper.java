package au.com.mitchhaley.fishjournal.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mitch on 22/10/13.
 */
public class FishJournalDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fishjournalDB";
    private static final int DATABASE_VERSION = 1;

    public FishJournalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        FishEntryTable.onCreate(database);
        TripEntryTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        FishEntryTable.onUpgrade(database, oldVersion, newVersion);
        TripEntryTable.onUpgrade(database, oldVersion, newVersion);
    }

}
