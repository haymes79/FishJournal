package au.com.mitchhaley.fishjournal.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by mitch on 22/10/13.
 */
public class MediaEntryTable {


    public static final String TABLE_MEDIA_ENTRY = "mediaEntry";
    public static final String PRIMARY_KEY = "_id";
    public static final String FULL_PRIMARY_KEY = TABLE_MEDIA_ENTRY + "." + "_id";

    public static final String COLUMN_MEDIA_TEXT = "media_text";
    public static final String COLUMN_MEDIA_FK = "media_fk";
    public static final String COLUMN_MEDIA_TYPE = "media_type";
    public static final String COLUMN_MEDIA_URI = "media_uri";
    public static final String COLUMN_RELATION_TYPE = "relation_type";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_MEDIA_ENTRY
            + " ("
            + PRIMARY_KEY + " integer primary key autoincrement, "
            + COLUMN_MEDIA_URI + " text not null, "
            + COLUMN_MEDIA_TEXT  + " text, "
            + COLUMN_MEDIA_TYPE  + " text, "
            + COLUMN_RELATION_TYPE  + " text, "
            + COLUMN_MEDIA_FK  + " integer not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);

    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(MediaEntryTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDIA_ENTRY);

        onCreate(database);
    }

}
