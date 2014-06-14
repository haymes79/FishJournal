package au.com.mitchhaley.fishjournal.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mitch on 22/10/13.
 */
public class FishEntryTable {


    public static final String TABLE_FISH_ENTRY = "fishEntry";
    public static final String PRIMARY_KEY = "_id";
    public static final String FULL_PRIMARY_KEY = TABLE_FISH_ENTRY + "." + PRIMARY_KEY;

    public static final String COLUMN_SPECIES = "species_id";
    public static final String FULL_COLUMN_SPECIES = TABLE_FISH_ENTRY + "." + COLUMN_SPECIES;

    public static final String COLUMN_CONDITIONS = "conditions";
    public static final String FULL_COLUMN_CONDITIONS = TABLE_FISH_ENTRY + "." + COLUMN_CONDITIONS;

    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String FULL_COLUMN_TEMPERATURE = TABLE_FISH_ENTRY + "." + COLUMN_TEMPERATURE;

    public static final String COLUMN_WATER_TEMPERATURE = "water_temperature";
    public static final String FULL_COLUMN_WATER_TEMPERATURE = TABLE_FISH_ENTRY + "." + COLUMN_WATER_TEMPERATURE;

    public static final String COLUMN_WATER_DEPTH = "water_depth";
    public static final String FULL_COLUMN_WATER_DEPTH = TABLE_FISH_ENTRY + "." + COLUMN_WATER_DEPTH;

    public static final String COLUMN_TRIP_KEY = "trip_id";
    public static final String FULL_COLUMN_TRIP_KEY = TABLE_FISH_ENTRY + "." + COLUMN_TRIP_KEY;

    public static final String COLUMN_DATETIME = "date_time";
    public static final String FULL_COLUMN_DATETIME = TABLE_FISH_ENTRY + "." + COLUMN_DATETIME;

    public static final String COLUMN_SIZE = "size";
    public static final String FULL_COLUMN_SIZE = TABLE_FISH_ENTRY + "." + COLUMN_SIZE;

    public static final String COLUMN_WEIGHT = "weight";
    public static final String FULL_COLUMN_WEIGHT = TABLE_FISH_ENTRY + "." + COLUMN_WEIGHT;

    public static final String COLUMN_NOTES = "notes";
    public static final String FULL_COLUMN_NOTES = TABLE_FISH_ENTRY + "." + COLUMN_NOTES;

    public static final String COLUMN_MOON = "moon";
    public static final String FULL_COLUMN_MOON = TABLE_FISH_ENTRY + "." + COLUMN_MOON;

    public static final String COLUMN_TIDE = "tide";
    public static final String FULL_COLUMN_TIDE = TABLE_FISH_ENTRY + "." + COLUMN_TIDE;

    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String FULL_COLUMN_LONGITUDE = TABLE_FISH_ENTRY + "." + COLUMN_LONGITUDE;

    public static final String COLUMN_LATITUDE = "latitude";
    public static final String FULL_COLUMN_LATITUDE = TABLE_FISH_ENTRY + "." + COLUMN_LATITUDE;

    public static final String COLUMN_ANGLER_KEY = "angler";
    public static final String FULL_COLUMN_ANGLER_KEY = TABLE_FISH_ENTRY + "." + COLUMN_ANGLER_KEY;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_FISH_ENTRY
            + " ("
            + PRIMARY_KEY + " integer primary key autoincrement, "
            + COLUMN_SPECIES + " text not null, "
            + COLUMN_CONDITIONS + " text, "
            + COLUMN_TEMPERATURE  + " text, "
            + COLUMN_WATER_TEMPERATURE  + " text, "
            + COLUMN_WATER_DEPTH  + " text, "
            + COLUMN_SIZE  + " text, "
            + COLUMN_WEIGHT  + " text, "
            + COLUMN_NOTES  + " text, "
            + COLUMN_MOON  + " text, "
            + COLUMN_TIDE  + " text, "
            + COLUMN_DATETIME  + " integer, "
            + COLUMN_LATITUDE  + " double, "
            + COLUMN_LONGITUDE  + " double, "
            + COLUMN_TRIP_KEY  + " integer, "
            + COLUMN_ANGLER_KEY  + " integer"
            + ");";


    public static Map<String, String> getProjectionMap() {


        Map<String, String> projectionMap = new HashMap<String, String>();
        projectionMap.put(PRIMARY_KEY, FULL_PRIMARY_KEY);
        projectionMap.put(COLUMN_SPECIES, FULL_COLUMN_SPECIES);
        projectionMap.put(COLUMN_CONDITIONS, FULL_COLUMN_CONDITIONS);
        projectionMap.put(COLUMN_TEMPERATURE, FULL_COLUMN_TEMPERATURE);
        projectionMap.put(COLUMN_WATER_TEMPERATURE, FULL_COLUMN_WATER_TEMPERATURE);
        projectionMap.put(COLUMN_WATER_DEPTH, FULL_COLUMN_WATER_DEPTH);
        projectionMap.put(COLUMN_TRIP_KEY, FULL_COLUMN_TRIP_KEY);
        projectionMap.put(COLUMN_DATETIME, FULL_COLUMN_DATETIME);
        projectionMap.put(COLUMN_SIZE, FULL_COLUMN_SIZE);
        projectionMap.put(COLUMN_WEIGHT, FULL_COLUMN_WEIGHT);
        projectionMap.put(COLUMN_NOTES, FULL_COLUMN_NOTES);
        projectionMap.put(COLUMN_MOON, FULL_COLUMN_MOON);
        projectionMap.put(COLUMN_TIDE, FULL_COLUMN_TIDE);
        projectionMap.put(COLUMN_LONGITUDE, FULL_COLUMN_LONGITUDE);
        projectionMap.put(COLUMN_LATITUDE, FULL_COLUMN_LATITUDE);
        projectionMap.put(COLUMN_ANGLER_KEY, FULL_COLUMN_ANGLER_KEY);
        projectionMap.put(TripEntryTable.COLUMN_TITLE, TripEntryTable.TABLE_TRIP_ENTRY + "." + TripEntryTable.COLUMN_TITLE);
        projectionMap.put(SpeciesEntryTable.COLUMN_SPECIES_COMMON_TEXT, SpeciesEntryTable.TABLE_SPECIES_ENTRY + "." + SpeciesEntryTable.COLUMN_SPECIES_COMMON_TEXT);
//        projectionMap.put("trip_id", TripEntryTable.FULL_PRIMARY_KEY + " AS " + "trip_id");

        return projectionMap;
    }




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
