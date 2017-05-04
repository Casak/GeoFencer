package com.smartagrodriver.core.storage.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import com.smartagrodriver.core.storage.db.GeoDatabase;

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
