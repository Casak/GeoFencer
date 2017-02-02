package casak.ru.geofencer.storage.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

import casak.ru.geofencer.storage.db.GeoDatabase;

/**
 * Created on 02.02.2017.
 */

@Table(database = GeoDatabase.class)
public class Route extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    int id;

    @Column
    @Unique
    int fieldId;

    @ForeignKey(tableClass = RouteType.class)
    @Column
    int type;

    @Column
    String points;
}
