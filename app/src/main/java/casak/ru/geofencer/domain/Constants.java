package casak.ru.geofencer.domain;

public class Constants {
    private Constants() {
    }

    public static final double FIELD_HEIGHT_METERS = 100;

    public static final Integer WIDTH_METERS = 10;
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


    //Bluetooth
    public static final int BLUETOOTH_REQUEST_CODE = 1478;
    public static final String BLUETOOTH_GPS_ANTENNA_HARDWARE_ADDRESS = "98:D3:31:80:3A:26";
    public static final String ANTENNA_UUID = "00001101-0000-1000-8000-00805f9b34fb";

    //Filter
    public static final float FILTER_HEADING_DIFFERENCE = 3f;


}
