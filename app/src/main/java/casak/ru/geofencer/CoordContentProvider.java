package casak.ru.geofencer;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import casak.ru.geofencer.db.Contract;
import casak.ru.geofencer.db.DbHelper;

/**
 * Created by User on 30.12.2016.
 */

public class CoordContentProvider extends ContentProvider {
    private static final String TAG = CoordContentProvider.class.getSimpleName();
    static final int COORD = 1;
    static final int COORDS = 2;
    static final int FILTERED_COORD = 3;
    static final int FILTERED_COORDS = 4;

    private static final SQLiteQueryBuilder coordQuerybuilder;

    static {
        coordQuerybuilder = new SQLiteQueryBuilder();
        coordQuerybuilder.setTables(Contract.CoordEntry.TABLE_NAME);
    }

    private static UriMatcher uriMatcher = buildUriMatcher();

    private DbHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)){
            case COORD:
                return coordQuerybuilder.query(db,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case COORD:
                return Contract.CoordEntry.CONTENT_ITEM_TYPE;
            case COORDS:
                return Contract.CoordEntry.CONTENT_DIR_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri result;
        long _id;

        switch (match) {
            case COORDS:
                _id = db.insert(Contract.CoordEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    result = ContentUris.withAppendedId(uri, _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case FILTERED_COORDS:
                _id = db.insert(Contract.FilteredCoordEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    result = ContentUris.withAppendedId(uri, _id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int returnCount = 0;

        switch (match) {
            case COORDS:
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(Contract.CoordEntry.TABLE_NAME, null, value);
                        if (_id > 0)
                            returnCount++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    static UriMatcher buildUriMatcher() {
        UriMatcher result = new UriMatcher(UriMatcher.NO_MATCH);

        result.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_COORD, COORD);
        result.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_COORDS, COORDS);
        result.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_FILTERED_COORD, FILTERED_COORD);
        result.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_FILTERED_COORDS, FILTERED_COORDS);

        return result;
    }
}
