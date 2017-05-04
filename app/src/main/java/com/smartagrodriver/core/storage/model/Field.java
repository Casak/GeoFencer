package com.smartagrodriver.core.storage.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

import com.smartagrodriver.core.storage.db.GeoDatabase;

/**
 * Created on 01.02.2017.
 */

@Table(database = GeoDatabase.class)
public class Field extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    public int id;

    @Column
    public String points;

    public List<Route> routes;

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "routes")
    public List<Route> getRoutes() {
        if (routes == null || routes.isEmpty()) {
            routes = SQLite.select()
                    .from(Route.class)
                    .where(Route_Table.fieldId.eq(id))
                    .queryList();
        }
        return routes;
    }
}
