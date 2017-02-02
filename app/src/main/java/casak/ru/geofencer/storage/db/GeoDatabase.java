package casak.ru.geofencer.storage.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created on 01.02.2017.
 */

@Database(name = GeoDatabase.NAME, version = GeoDatabase.VERSION)
public class GeoDatabase {
    public static final String NAME = "GeoDatabase";

    public static final int VERSION = 1;
}
