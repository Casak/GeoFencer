package casak.ru.geofencer.storage.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

import casak.ru.geofencer.storage.db.GeoDatabase;

/**
 * Created on 02.02.2017.
 */

@Table(name = "session", database = GeoDatabase.class)
public class Point extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    public long id;

    @Column
    public double lat;

    @Column
    public double lng;

    @Column
    public double alt;

    @Column
    public double speed;

    @Column
    public double bearing;


    @Column
    public double accuracy;

    @Column
    public Date date;
}
