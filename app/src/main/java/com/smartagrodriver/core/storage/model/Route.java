package com.smartagrodriver.core.storage.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import com.smartagrodriver.core.storage.db.GeoDatabase;

/**
 * Created on 02.02.2017.
 */

@Table(database = GeoDatabase.class)
public class Route extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    public long id;

    @Column
    public long fieldId;

    @ForeignKey(tableClass = RouteType.class)
    @Column
    public int type;

    @Column
    public String points;
}
