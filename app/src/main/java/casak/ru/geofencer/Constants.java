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

    //Rectangle field corners
    public static final Integer NORTH_EAST = 0;
    public static final Integer SOUTH_EAST = 1;
    public static final Integer SOUTH_WEST = 2;
    public static final Integer NORTH_WEST = 3;


    //TODO delete this shit
    public static final double HEADING = SphericalUtil.computeHeading(
            new LatLng(50.097119d, 30.124142d), new LatLng(50.099563d, 30.127152d)) - 90;


    /**
     * Map for storing information about airports in the San Francisco bay area.
     */
    public static final HashMap<String, LatLng> BAY_AREA_LANDMARKS = new HashMap<String, LatLng>();
    static {
        // San Francisco International Airport.
        BAY_AREA_LANDMARKS.put("SFO", new LatLng(37.621313, -122.378955));

        // Googleplex.
        BAY_AREA_LANDMARKS.put("GOOGLE", new LatLng(37.422611,-122.0840577));

        BAY_AREA_LANDMARKS.put("HOME", new LatLng(50.430705,30.422127));
    }
}
