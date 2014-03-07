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

/**
 * Created by mitch on 22/10/13.
 */
public class FishEntryContentProvider extends ContentProvider {

        // database
        private FishJournalDatabaseHelper database;

        // used for the UriMacher
        private static final int FISHES = 10;
        private static final int FISH_ID = 20;
        private static final int FISHES_UNIQUE = 30;
        
        private static final String AUTHORITY = "au.com.mitchhaley.fishentry.contentprovider";

        private static final String BASE_PATH = "fish";
        private static final String UNIQUE = "fish_unique";
        public static final Uri FISHES_UNIQUE_URI = Uri.parse("content://" + AUTHORITY + "/" + UNIQUE);
        public static final Uri FISHES_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

//        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/fish";

        private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		public static final String CONTENT_ITEM_TYPE = "fish";
        static {
            sURIMatcher.addURI(AUTHORITY, BASE_PATH, FISHES);
            sURIMatcher.addURI(AUTHORITY, UNIQUE, FISHES_UNIQUE);
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

            // check if the caller has requested a column which does not exists
            checkColumns(projection);

            // Set the table
            queryBuilder.setTables(FishEntryTable.TABLE_FISH_ENTRY);

            String groupBy = null;
            
            int uriType = sURIMatcher.match(uri);
            switch (uriType) {
                case FISHES:
                    break;
                case FISHES_UNIQUE:
                	groupBy = FishEntryTable.COLUMN_SPECIES;
                	break;
                case FISH_ID:
                    // adding the ID to the original query
                    queryBuilder.appendWhere(FishEntryTable.PRIMARY_KEY + "="
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
            return Uri.parse(BASE_PATH + "/" + id);
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

        private void checkColumns(String[] projection) {
            String[] available = { FishEntryTable.PRIMARY_KEY, FishEntryTable.COLUMN_SIZE, FishEntryTable.COLUMN_CONDITIONS,
                    FishEntryTable.COLUMN_NOTES, FishEntryTable.COLUMN_SPECIES, FishEntryTable.COLUMN_TEMPERATURE,
                    FishEntryTable.COLUMN_WEIGHT, FishEntryTable.COLUMN_DATETIME};
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