package au.com.mitchhaley.fishjournal.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by mitch on 22/10/13.
 */
public class SpeciesEntryTable {


    public static final String TABLE_SPECIES_ENTRY = "speciesEntry";
    public static final String PRIMARY_KEY = "_id";
    public static final String FULL_PRIMARY_KEY = TABLE_SPECIES_ENTRY + "." + "_id";

    public static final String COLUMN_SPECIES_COMMON_TEXT = "species_common";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_SPECIES_SCIENTIFIC_TEXT = "species_scientific";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_SPECIES_ENTRY
            + " ("
            + PRIMARY_KEY + " integer primary key autoincrement, "
            + COLUMN_SPECIES_COMMON_TEXT + " text not null, "
            + COLUMN_TYPE  + " text, "
            + COLUMN_SPECIES_SCIENTIFIC_TEXT  + " text"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);

    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(SpeciesEntryTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SPECIES_ENTRY);

        onCreate(database);
    }

}
