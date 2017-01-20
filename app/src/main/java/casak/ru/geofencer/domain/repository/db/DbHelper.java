package casak.ru.geofencer.domain.repository.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

import casak.ru.geofencer.domain.repository.db.Contract.*;

/**
 * Created on 29.12.2016.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final String TAG = DbHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "coord.db";
    public static final Integer DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_COORD_TABLE = "CREATE TABLE " + CoordEntry.TABLE_NAME + " (" +
                CoordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CoordEntry.COLUMN_LAT + " TEXT, " +
                CoordEntry.COLUMN_LNG + " TEXT, " +
                CoordEntry.COLUMN_ALT + " TEXT, " +
                CoordEntry.COLUMN_SPD + " TEXT, " +
                CoordEntry.COLUMN_ACCURACY + " TEXT, " +
                CoordEntry.COLUMN_HEADING + " TEXT " + ");";

        final String CREATE_FILTER_COORD_TABLE = "CREATE TABLE " + FilteredCoordEntry.TABLE_NAME + " (" +
                FilteredCoordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FilteredCoordEntry.COLUMN_LAT + " TEXT, " +
                FilteredCoordEntry.COLUMN_LNG + " TEXT, " +
                FilteredCoordEntry.COLUMN_ALT + " TEXT, " +
                FilteredCoordEntry.COLUMN_SPD + " TEXT, " +
                FilteredCoordEntry.COLUMN_ACCURACY + " TEXT, " +
                FilteredCoordEntry.COLUMN_HEADING + " TEXT " + ");";

        final String CREATE_ARROW_TABLE = "CREATE TABLE " + ArrowEntry.TABLE_NAME + " (" +
//                ArrowEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                ArrowEntry.COLUMN_IS_LEFT_ARROW + " INTEGER, " +
//                ArrowEntry.COLUMN_LEFT_POINT + " TEXT, " +
//                ArrowEntry.COLUMN_FIELD_ID + " TEXT " + "," +
//                "FOREIGN KEY (" + UpcomingEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
//                MovieEntry.TABLE_NAME + "(" + MovieEntry._ID + ")  " +
                ");";



        db.beginTransaction();
        db.execSQL(CREATE_COORD_TABLE);
        db.execSQL(CREATE_FILTER_COORD_TABLE);
        db.execSQL(CREATE_ARROW_TABLE);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //TODO Implement onUpgrage, onDowngrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            if (SQLiteDatabase.deleteDatabase(new File(db.getPath() + DATABASE_NAME)))
                onCreate(db);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            if (SQLiteDatabase.deleteDatabase(new File(db.getPath() + DATABASE_NAME)))
                onCreate(db);
        }
    }
}
