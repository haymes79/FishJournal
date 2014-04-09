package au.com.mitchhaley.fishjournal.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by mitch on 22/10/13.
 */
public class LocationEntryTable {


    public static final String TABLE_LOCATION_ENTRY = "locationEntry";
    public static final String PRIMARY_KEY = "_id";
    public static final String FULL_PRIMARY_KEY = TABLE_LOCATION_ENTRY + "." + "_id";

    public static final String COLUMN_LOCATION_TEXT = "location_desc";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_LOCATION_ENTRY
            + " ("
            + PRIMARY_KEY + " integer primary key autoincrement, "
            + COLUMN_LOCATION_TEXT + " text not null, "
            + COLUMN_LATITUDE  + " double, "
            + COLUMN_LONGITUDE  + " double"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);

    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(LocationEntryTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION_ENTRY);

        onCreate(database);
    }

}
