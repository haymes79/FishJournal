package au.com.mitchhaley.fishjournal.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by mitch on 22/10/13.
 */
public class FishEntryTable {


    public static final String TABLE_FISH_ENTRY = "fishEntry";
    public static final String PRIMARY_KEY = "_id";
    public static final String COLUMN_SPECIES = "species";
    public static final String COLUMN_CONDITIONS = "conditions";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_DATETIME = "date_time";

    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_NOTES = "notes";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_FISH_ENTRY
            + " ("
            + PRIMARY_KEY + " integer primary key autoincrement, "
            + COLUMN_SPECIES + " text not null, "
            + COLUMN_CONDITIONS + " text, "
            + COLUMN_TEMPERATURE  + " text, "
            + COLUMN_SIZE  + " text, "
            + COLUMN_WEIGHT  + " text, "
            + COLUMN_NOTES  + " text, "
            + COLUMN_DATETIME  + " integer"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(FishEntryTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_FISH_ENTRY);

        onCreate(database);
    }

}
