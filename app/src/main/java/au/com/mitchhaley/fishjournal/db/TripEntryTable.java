package au.com.mitchhaley.fishjournal.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by mitch on 22/10/13.
 */
public class TripEntryTable {


    public static final String TABLE_TRIP_ENTRY = "tripEntry";
    public static final String PRIMARY_KEY = "_id";

    public static final String FULL_PRIMARY_KEY = TABLE_TRIP_ENTRY + "." + "_id";

    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_LOCATION = "location";

    public static final String COLUMN_START_DATETIME = "start_date_time";

    public static final String COLUMN_END_DATETIME = "end_date_time";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_TRIP_ENTRY
            + " ("
            + PRIMARY_KEY + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_START_DATETIME + " text not null, "
            + COLUMN_END_DATETIME + " text not null, "
            + COLUMN_LOCATION + " text"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
//        database.execSQL("ALTER TABLE " + FishEntryTable.TABLE_FISH_ENTRY + " ADD FOREIGN KEY TRIP_ID " + TABLE_TRIP_ENTRY + "(" + PRIMARY_KEY + ")");
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(TripEntryTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP_ENTRY);

        onCreate(database);
    }

}
