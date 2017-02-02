package casak.ru.geofencer.storage.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import casak.ru.geofencer.storage.db.GeoDatabase;

/**
 * Created on 02.02.2017.
 */

@Table(database = GeoDatabase.class)
public class RouteType extends BaseModel {
    @Column
    @PrimaryKey
    public int id;

    @Column
    public String type;
}
