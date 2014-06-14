package au.com.mitchhaley.fishjournal.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
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
import au.com.mitchhaley.fishjournal.db.SpeciesEntryTable;
import au.com.mitchhaley.fishjournal.db.TripEntryTable;

/**
 * Created by mitch on 22/10/13.
 */
public class FishEntryContentProvider extends ContentProvider {

        // database
        private FishJournalDatabaseHelper database;

        // used for the UriMacher
        private static final int FISHES = 10;
        private static final int FISH_ID = 20;
//        private static final int FISHES_UNIQUE = 30;
        
        private static final String AUTHORITY = "au.com.mitchhaley.fishentry.contentprovider";

        private static final String BASE_PATH = "fish";
//        private static final String UNIQUE = "fish_unique";
//        public static final Uri FISHES_UNIQUE_URI = Uri.parse("content://" + AUTHORITY + "/" + UNIQUE);
        public static final Uri FISHES_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

//        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/fish";

        private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		public static final String CONTENT_ITEM_TYPE = "fish";
        static {
            sURIMatcher.addURI(AUTHORITY, BASE_PATH, FISHES);
//            sURIMatcher.addURI(AUTHORITY, UNIQUE, FISHES_UNIQUE);
            sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", FISH_ID);
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

            // Set the table
            queryBuilder.setTables(FishEntryTable.TABLE_FISH_ENTRY + " LEFT OUTER JOIN " + TripEntryTable.TABLE_TRIP_ENTRY + " ON " +
                    FishEntryTable.FULL_COLUMN_TRIP_KEY + " = " + TripEntryTable.FULL_PRIMARY_KEY + " INNER JOIN " + SpeciesEntryTable.TABLE_SPECIES_ENTRY + " ON " + FishEntryTable.COLUMN_SPECIES + " = " + SpeciesEntryTable.FULL_PRIMARY_KEY);

            queryBuilder.setProjectionMap(FishEntryTable.getProjectionMap());

            String groupBy = null;
            
            int uriType = sURIMatcher.match(uri);
            switch (uriType) {
                case FISHES:
                    break;
//                case FISHES_UNIQUE:
//                	groupBy = FishEntryTable.COLUMN_SPECIES;
//                	break;
                case FISH_ID:
                    // adding the ID to the original query
                    queryBuilder.appendWhere(FishEntryTable.FULL_PRIMARY_KEY + "="
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
                case FISHES:
                    id = sqlDB.insert(FishEntryTable.TABLE_FISH_ENTRY, null, values);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI: " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return Uri.parse(FISHES_URI + "/" + id);
        }

        @Override
        public int delete(Uri uri, String selection, String[] selectionArgs) {
            int uriType = sURIMatcher.match(uri);
            SQLiteDatabase sqlDB = database.getWritableDatabase();
            int rowsDeleted = 0;
            switch (uriType) {
                case FISHES:
                    rowsDeleted = sqlDB.delete(FishEntryTable.TABLE_FISH_ENTRY, selection, selectionArgs);
                    break;
                case FISH_ID:
                    String id = uri.getLastPathSegment();
                    if (TextUtils.isEmpty(selection)) {
                        rowsDeleted = sqlDB.delete(FishEntryTable.TABLE_FISH_ENTRY, FishEntryTable.PRIMARY_KEY + "=" + id, null);
                    } else {
                        rowsDeleted = sqlDB.delete(FishEntryTable.TABLE_FISH_ENTRY, FishEntryTable.PRIMARY_KEY + "=" + id + " and " + selection, selectionArgs);
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
                case FISHES:
                    rowsUpdated = sqlDB.update(FishEntryTable.TABLE_FISH_ENTRY,
                            values,
                            selection,
                            selectionArgs);
                    break;
                case FISH_ID:
                    String id = uri.getLastPathSegment();
                    if (TextUtils.isEmpty(selection)) {
                        rowsUpdated = sqlDB.update(FishEntryTable.TABLE_FISH_ENTRY,
                                values,
                                FishEntryTable.PRIMARY_KEY + "=" + id,
                                null);
                    } else {
                        rowsUpdated = sqlDB.update(FishEntryTable.TABLE_FISH_ENTRY,
                                values,
                                FishEntryTable.PRIMARY_KEY + "=" + id
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
    }