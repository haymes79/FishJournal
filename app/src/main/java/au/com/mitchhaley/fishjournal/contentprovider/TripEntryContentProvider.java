package au.com.mitchhaley.fishjournal.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

import au.com.mitchhaley.fishjournal.db.FishEntryTable;
import au.com.mitchhaley.fishjournal.db.FishJournalDatabaseHelper;
import au.com.mitchhaley.fishjournal.db.LocationEntryTable;
import au.com.mitchhaley.fishjournal.db.MediaEntryTable;
import au.com.mitchhaley.fishjournal.db.TripEntryTable;

/**
 * Created by mitch on 22/10/13.
 */
public class TripEntryContentProvider extends ContentProvider {

        // database
        private FishJournalDatabaseHelper database;

        // used for the UriMacher
        private static final int TRIPS = 10;
        private static final int TRIP_ID = 20;

        private static final int LOCATIONS = 30;
        private static final int LOCATION_ID = 40;

        private static final int MEDIAS = 50;
        private static final int MEDIA_ID = 60;


        public static final String TRIP_CONTENT_ITEM_TYPE = "trip";
        public static final String LOCATION_CONTENT_ITEM_TYPE = "location";

        private static final String AUTHORITY = "au.com.mitchhaley.tripentry.contentprovider";

        private static final String TRIP_BASE_PATH = "trip";
        private static final String MEDIA_BASE_PATH = "media";
        private static final String LOCATION_BASE_PATH = "location";

        public static final Uri TRIPS_URI = Uri.parse("content://" + AUTHORITY + "/" + TRIP_BASE_PATH);
        public static final Uri MEDIAS_URI = Uri.parse("content://" + AUTHORITY + "/" + MEDIA_BASE_PATH);
        public static final Uri LOCATIONS_URI = Uri.parse("content://" + AUTHORITY + "/" + LOCATION_BASE_PATH);

        private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        static {
            sURIMatcher.addURI(AUTHORITY, TRIP_BASE_PATH, TRIPS);
            sURIMatcher.addURI(AUTHORITY, TRIP_BASE_PATH + "/#", TRIP_ID);

            sURIMatcher.addURI(AUTHORITY, LOCATION_BASE_PATH, LOCATIONS);
            sURIMatcher.addURI(AUTHORITY, LOCATION_BASE_PATH + "/#", LOCATION_ID);

            sURIMatcher.addURI(AUTHORITY, MEDIA_BASE_PATH, MEDIAS);
            sURIMatcher.addURI(AUTHORITY, MEDIA_BASE_PATH + "/#", MEDIA_ID);


        }

        @Override
        public boolean onCreate() {
            database = new FishJournalDatabaseHelper(getContext());
            return false;
        }

        @Override
        public Cursor query(Uri uri, String[] projection, String selection,
                            String[] selectionArgs, String sortOrder) {

            // Uisng SQLiteQueryBuilder instead of query() method
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

            String groupBy = null;
            
            int uriType = sURIMatcher.match(uri);
            switch (uriType) {
                case TRIPS:
                    queryBuilder.setTables(TripEntryTable.TABLE_TRIP_ENTRY + " LEFT OUTER JOIN " + LocationEntryTable.TABLE_LOCATION_ENTRY + " ON " +
                            TripEntryTable.FULL_COLUMN_LOCATION + " = " + LocationEntryTable.FULL_PRIMARY_KEY);
                    queryBuilder.setProjectionMap(TripEntryTable.getProjectionMap());
                    break;
                case TRIP_ID:
                    queryBuilder.setTables(TripEntryTable.TABLE_TRIP_ENTRY);
                    // adding the ID to the original query
                    queryBuilder.appendWhere(TripEntryTable.PRIMARY_KEY + "="
                            + uri.getLastPathSegment());
                    break;
                case LOCATIONS:
                    queryBuilder.setTables(LocationEntryTable.TABLE_LOCATION_ENTRY);
                    break;
                case LOCATION_ID:
                    queryBuilder.setTables(LocationEntryTable.TABLE_LOCATION_ENTRY);
                    // adding the ID to the original query
                    queryBuilder.appendWhere(LocationEntryTable.PRIMARY_KEY + "="
                            + uri.getLastPathSegment());
                    break;
                case MEDIAS:
                    queryBuilder.setTables(MediaEntryTable.TABLE_MEDIA_ENTRY);
                    break;
                case MEDIA_ID:
                    queryBuilder.setTables(MediaEntryTable.TABLE_MEDIA_ENTRY);
                    // adding the ID to the original query
                    queryBuilder.appendWhere(MediaEntryTable.PRIMARY_KEY + "="
                            + uri.getLastPathSegment());
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }

            SQLiteDatabase db = database.getWritableDatabase();
            Cursor cursor = queryBuilder.query(db, projection, selection,
                    selectionArgs, groupBy, null, sortOrder);

            // make sure that potential listeners are getting notified
            cursor.setNotificationUri(getContext().getContentResolver(), uri);

            return cursor;
        }

        @Override
        public String getType(Uri uri) {
            return null;
        }

        @Override
        public Uri insert(Uri uri, ContentValues values) {
            int uriType = sURIMatcher.match(uri);
            SQLiteDatabase sqlDB = database.getWritableDatabase();

            long id = 0;
            Uri returnUri;
            switch (uriType) {
                case TRIPS:
                    id = sqlDB.insert(TripEntryTable.TABLE_TRIP_ENTRY, null, values);
                    returnUri = Uri.parse(TRIP_BASE_PATH + "/" + id);
                    break;
                case LOCATIONS:
                    id = sqlDB.insert(LocationEntryTable.TABLE_LOCATION_ENTRY, null, values);
                    returnUri = Uri.parse(LOCATION_BASE_PATH + "/" + id);
                    break;
                case MEDIAS:
                    id = sqlDB.insert(MediaEntryTable.TABLE_MEDIA_ENTRY, null, values);
                    returnUri = Uri.parse(MEDIA_BASE_PATH + "/" + id);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }

            getContext().getContentResolver().notifyChange(uri, null);
            return returnUri;
        }

        @Override
        public int delete(Uri uri, String selection, String[] selectionArgs) {
            int uriType = sURIMatcher.match(uri);
            SQLiteDatabase sqlDB = database.getWritableDatabase();
            int rowsDeleted = 0;
            switch (uriType) {
                case TRIPS:
                    rowsDeleted = sqlDB.delete(TripEntryTable.TABLE_TRIP_ENTRY, selection, selectionArgs);
                    break;
                case TRIP_ID:
                    String id = uri.getLastPathSegment();
                    if (TextUtils.isEmpty(selection)) {
                        rowsDeleted = sqlDB.delete(TripEntryTable.TABLE_TRIP_ENTRY, TripEntryTable.PRIMARY_KEY + "=" + id, null);
                    } else {
                        rowsDeleted = sqlDB.delete(TripEntryTable.TABLE_TRIP_ENTRY, TripEntryTable.PRIMARY_KEY + "=" + id + " and " + selection, selectionArgs);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return rowsDeleted;
        }

        @Override
        public int update(Uri uri, ContentValues values, String selection,
                          String[] selectionArgs) {

            int uriType = sURIMatcher.match(uri);
            SQLiteDatabase sqlDB = database.getWritableDatabase();
            int rowsUpdated = 0;
            switch (uriType) {
                case TRIPS:
                    rowsUpdated = sqlDB.update(TripEntryTable.TABLE_TRIP_ENTRY,
                            values,
                            selection,
                            selectionArgs);
                    break;
                case TRIP_ID:
                    String id = uri.getLastPathSegment();
                    if (TextUtils.isEmpty(selection)) {
                        rowsUpdated = sqlDB.update(TripEntryTable.TABLE_TRIP_ENTRY,
                                values,
                                TripEntryTable.PRIMARY_KEY + "=" + id,
                                null);
                    } else {
                        rowsUpdated = sqlDB.update(TripEntryTable.TABLE_TRIP_ENTRY,
                                values,
                                TripEntryTable.PRIMARY_KEY + "=" + id
                                        + " and "
                                        + selection,
                                selectionArgs);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return rowsUpdated;
        }

        private void checkColumns(String[] projection) {
            String[] available = { TripEntryTable.PRIMARY_KEY, TripEntryTable.COLUMN_TITLE, TripEntryTable.COLUMN_LOCATION, TripEntryTable.COLUMN_START_DATETIME, TripEntryTable.COLUMN_END_DATETIME};
            if (projection != null) {
                HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
                HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
                // check if all columns which are requested are available
                if (!availableColumns.containsAll(requestedColumns)) {
                    throw new IllegalArgumentException("Unknown columns in projection");
                }
            }
        }

    }