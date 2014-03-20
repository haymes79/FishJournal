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

        private static final String AUTHORITY = "au.com.mitchhaley.tripentry.contentprovider";

        private static final String BASE_PATH = "trip";
        private static final String UNIQUE = "trip_unique";
        public static final Uri TRIPS_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);


        private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		public static final String CONTENT_ITEM_TYPE = "trip";

        static {
            sURIMatcher.addURI(AUTHORITY, BASE_PATH, TRIPS);
            sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TRIP_ID);
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

            // check if the caller has requested a column which does not exists
            checkColumns(projection);

            // Set the table
            queryBuilder.setTables(TripEntryTable.TABLE_TRIP_ENTRY);

            String groupBy = null;
            
            int uriType = sURIMatcher.match(uri);
            switch (uriType) {
                case TRIPS:
                    break;
                case TRIP_ID:
                    // adding the ID to the original query
                    queryBuilder.appendWhere(TripEntryTable.PRIMARY_KEY + "="
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
            switch (uriType) {
                case TRIPS:
                    id = sqlDB.insert(TripEntryTable.TABLE_TRIP_ENTRY, null, values);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return Uri.parse(BASE_PATH + "/" + id);
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