package casak.ru.geofencer.db;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by User on 29.12.2016.
 */

public class Contract {
    private static final String TAG = Contract.class.getSimpleName();
    //TODO create a normal authority
    public static final String CONTENT_AUTHORITY  = "ru.casak.geofencer";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_COORD = "coord";
    public static final String PATH_COORDS = "coords";
    public static final String PATH_FILTERED_COORD = "filter_coord";
    public static final String PATH_FILTERED_COORDS = "filter_coords";


    public static final class CoordEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COORDS).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COORDS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COORD;


        public static final String TABLE_NAME = "coord";

        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LNG = "lng";
        public static final String COLUMN_ALT = "alt";
        public static final String COLUMN_SPD = "spd";
        public static final String COLUMN_HEADING = "heading";
        public static final String COLUMN_ACCURACY = "accuracy";
    }

    public static final class FilteredCoordEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FILTERED_COORDS).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILTERED_COORDS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILTERED_COORD;


        public static final String TABLE_NAME = "filter_coord";

        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LNG = "lng";
        public static final String COLUMN_ALT = "alt";
        public static final String COLUMN_SPD = "spd";
        public static final String COLUMN_HEADING = "heading";
        public static final String COLUMN_ACCURACY = "accuracy";
    }

}
