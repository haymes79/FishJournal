package au.com.mitchhaley.fishjournal.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by mitch on 22/10/13.
 */
public class ContactEntryTable {


    public static final String TABLE_CONTACT_ENTRY = "contactEntry";
    public static final String PRIMARY_KEY = "_id";
    public static final String FULL_PRIMARY_KEY = TABLE_CONTACT_ENTRY + "." + "_id";

    public static final String COLUMN_NAME_TEXT = "name_text";
    public static final String COLUMN_TRIP_ID = "trip_id";
    public static final String COLUMN_CONTACT_FK = "contact_fk";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_CONTACT_ENTRY
            + " ("
            + PRIMARY_KEY + " integer primary key autoincrement, "
            + COLUMN_NAME_TEXT + " text not null, "
            + COLUMN_CONTACT_FK + " text not null, "
            + COLUMN_TRIP_ID + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);

    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(ContactEntryTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT_ENTRY);


        onCreate(database);
    }

}
