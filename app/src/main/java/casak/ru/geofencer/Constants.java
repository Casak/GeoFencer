package casak.ru.geofencer;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.HashMap;

/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class Constants {
    private Constants() {
    }

    public static final Integer WIDTH_METERS = 20;
    public static final double HEADING_TO_LEFT = -90;
    public static final double HEADING_TO_RIGHT = 90;


    //Rectangle field corners
    public static final Integer NORTH_WEST = 0;
    public static final Integer SOUTH_WEST = 1;
    public static final Integer SOUTH_EAST = 2;
    public static final Integer NORTH_EAST = 3;

    //Z-Indexes for map
    public static final float FIELD_INDEX = 0.0f;
    public static final float HARVESTED_INDEX = 1.0f;
    public static final float ROUTE_INDEX = 2.0f;


    //UI elements colors
    public static final int HARVESTED_FILL_COLOR = 0x7f0061ff;
    public static final int HARVESTED_STROKE_COLOR = 0x8f0061ff;
    public static final int FIELD_FILL_COLOR = 0x7F00FF00;
    public static final int FIELD_STROKE_COLOR = 0x7F00F000;
    public static final int ARROW_COLOR = 0x9FF4F142;
    public static final int COMPUTED_ROUTE_COLOR = 0x9FFF00FF;

}
