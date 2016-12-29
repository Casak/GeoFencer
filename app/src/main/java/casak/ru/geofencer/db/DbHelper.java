package casak.ru.geofencer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import casak.ru.geofencer.db.Contract.*;
/**
 * Created by User on 29.12.2016.
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
                CoordEntry._ID + " INTEGER PRIMARY KEY, " +
                CoordEntry.COLUMN_LAT + " TEXT, " +
                CoordEntry.COLUMN_LNG + " TEXT, " +
                CoordEntry.COLUMN_ALT + " TEXT, " +
                CoordEntry.COLUMN_SPD + " TEXT, " +
                CoordEntry.COLUMN_ACCURACY + " TEXT, " +
                CoordEntry.COLUMN_HEADING + " TEXT, "  + ");";

        db.beginTransaction();
        db.execSQL(CREATE_COORD_TABLE);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //TODO Implement onUpgrage
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
