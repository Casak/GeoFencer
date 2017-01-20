package casak.ru.geofencer.util;

import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.SphericalUtil;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import casak.ru.geofencer.domain.Constants;

public class MapsUtils {

    static final double FIELD_HEIGHT_METERS = 100;
    static final double EARTH_RADIUS_METERS = 6371.008 * 1000;


    public static PolylineOptions createPolylineOptions(LatLng... latLngs) {
        return new PolylineOptions().add(latLngs);
    }

    public static PolylineOptions createArrow(LatLng routeCenter, double routeDistance, double routeHeading, boolean toLeft) {
        List<LatLng> result = new LinkedList<>();

        result.add(routeCenter);

        double headingArrowToTop;

        if (toLeft) {
            headingArrowToTop = routeHeading + Constants.HEADING_TO_LEFT;
        } else {
            headingArrowToTop = routeHeading + Constants.HEADING_TO_RIGHT;
        }

        double headingArrowToLeft = headingArrowToTop + 180 + 30;
        double headingArrowToRight = headingArrowToTop + 180 - 30;

        LatLng leftArrowTop = SphericalUtil.computeOffset(routeCenter, routeDistance / 2, headingArrowToTop);
        LatLng leftArrowFromTopToLeft = SphericalUtil.computeOffset(leftArrowTop, routeDistance / 4, headingArrowToLeft);
        LatLng leftArrowFromTopToRight = SphericalUtil.computeOffset(leftArrowTop, routeDistance / 4, headingArrowToRight);

        result.add(leftArrowTop);
        result.add(leftArrowFromTopToLeft);
        result.add(leftArrowTop);
        result.add(leftArrowFromTopToRight);
        result.add(leftArrowTop);

        return new PolylineOptions()
                .addAll(result)
                .width(20f)
                .color(Constants.ARROW_COLOR)
                .geodesic(true)
                .clickable(true);
    }

    public static PolylineOptions createPolylineOptions(List<LatLng> latLngs) {
        return new PolylineOptions()
                .add(latLngs.toArray(new LatLng[latLngs.size()]))
                .geodesic(true);
    }

    //TODO rename signature
    public static PolygonOptions createFieldPolygonOptions(LatLng start, LatLng end, double width, boolean toLeft) {
        double offset = width == 0 ? 0d : width / 2;
        return new PolygonOptions()
                .add(computeCorners(start, end, offset, toLeft).toArray(new LatLng[4]))
                .fillColor(Constants.FIELD_FILL_COLOR)
                .strokeColor(Constants.FIELD_STROKE_COLOR)
                .geodesic(true);
    }

    public static List<LatLng> computeCorners(LatLng start, LatLng end, double offset, boolean toLeft) {
        double heading = SphericalUtil.computeHeading(start, end);

        List<LatLng> result = new ArrayList<>(4);

        double heading1, heading2;
        if (toLeft) {
            heading1 = 90;
            heading2 = -90;
        } else {
            heading1 = -90;
            heading2 = 90;
        }

        LatLng cornerSouthWest = SphericalUtil.computeOffset(start, offset, heading + heading1);
        LatLng cornerNorthWest = SphericalUtil.computeOffset(cornerSouthWest, FIELD_HEIGHT_METERS, heading + heading2);
        LatLng cornerSouthEast = SphericalUtil.computeOffset(end, offset, heading + heading1);
        LatLng cornerNorthEast = SphericalUtil.computeOffset(cornerSouthEast, FIELD_HEIGHT_METERS, heading + heading2);

        result.add(cornerNorthWest);
        result.add(cornerSouthWest);
        result.add(cornerSouthEast);
        result.add(cornerNorthEast);
        return result;
    }


    //TODO rename
    public static List<LatLng> computeNewPath(Polyline polyline, double width, double heading) {
        List<LatLng> points = polyline.getPoints();
        List<LatLng> result = new ArrayList<>(points.size());

        for (LatLng point : points) {
            LatLng newPoint = SphericalUtil.computeOffset(point, width, heading);
            result.add(newPoint);
        }
        return result;
    }

    //TODO implement
    public static CameraUpdate harvestedPolygonToCameraUpdate(List<LatLng> points) {
        if (points == null || points.size() < 1)
            return null;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(points.get(0));
        boundsBuilder.include(points.get(points.size() - 1));
        LatLngBounds bounds = boundsBuilder.build();

        return CameraUpdateFactory.newLatLngBounds(bounds, 1);
    }

    public static CameraUpdate fieldPolygonToCameraUpdate(Polygon polygon) {
        List<LatLng> points = polygon.getPoints();
        if (points == null || points.size() < 4)
            return null;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(points.get(Constants.NORTH_EAST));
        boundsBuilder.include(points.get(Constants.SOUTH_WEST));
        LatLngBounds bounds = boundsBuilder.build();

        return CameraUpdateFactory.newLatLngBounds(bounds, 100);
    }

    public static CameraUpdate latLngToCameraUpdate(LatLng latLng) {
        return CameraUpdateFactory.newLatLng(latLng);
    }

    public static PolygonOptions harvestedPolygonOptions(Polyline route) {
        List<LatLng> points = route.getPoints();
        List<LatLng> upperBound = new LinkedList<>();
        List<LatLng> bottomBound = new LinkedList<>();
        List<LatLng> fullArea = new LinkedList<>();

        if (points.size() > 1) {
            LatLng start = points.get(0);
            LatLng end = points.get(points.size() - 1);
            double heading = SphericalUtil.computeHeading(start, end);

            for (LatLng point : points) {
                upperBound.add(SphericalUtil.computeOffset(point,
                        Constants.WIDTH_METERS / 2,
                        heading + 90));
                bottomBound.add(SphericalUtil.computeOffset(point,
                        Constants.WIDTH_METERS / 2,
                        heading - 90));
            }
            fullArea.addAll(upperBound);
            for (int i = bottomBound.size() - 1; i >= 0; i--)
                fullArea.add(bottomBound.get(i));
            return new PolygonOptions()
                    .add(fullArea.toArray(new LatLng[fullArea.size()]))
                    .fillColor(Constants.HARVESTED_FILL_COLOR)
                    .strokeColor(Constants.HARVESTED_STROKE_COLOR)
                    .zIndex(Constants.HARVESTED_INDEX)
                    .geodesic(true);
        }
        return null;
    }

    public LatLng computeCentralPoint(LatLng start, LatLng end) {
        double distanceToCenter = SphericalUtil.computeDistanceBetween(start, end) / 2;
        double heading = SphericalUtil.computeHeading(start, end);
        return SphericalUtil.computeOffset(start, distanceToCenter, heading);
    }


    public static void mockLocations(LocationListener listener, @Nullable LatLng... locations) {
        new MockLocationAsyncTask(listener).execute(locations);
    }

    private static class MockLocationAsyncTask extends AsyncTask<LatLng, Location, String> {
        List<Location> locations = new LinkedList<>();
        LocationListener listener;

        public MockLocationAsyncTask(LocationListener locationListener) {
            listener = locationListener;
        }

        private void initLocations() {
            String locationsString =
                            "50.37922966,30.443585655;" +
                            "50.3792444766667,30.4435697983333;" +
                            "50.3792881133333,30.4435172633333;" +
                            "50.3793163366667,30.4434824683333;" +
                            "50.3793474066667,30.4434438066667;" +
                            "50.379823555,30.4428282666667;" +
                            "50.3798501566667,30.44278962;" +
                            "50.3798753083333,30.4427537483333;" +
                            "50.3799910433333,30.44256623;" +
                            "50.37998511,30.442531405;" +
                            "50.3799695266667,30.4425068833333;" +
                            "50.3799485166667,30.4424973016667;" +
                            "50.3799275916667,30.4425044633333;" +
                            "50.3798967833333,30.4425610633333;" +
                            "50.379880665,30.4426006966667;" +
                            "50.37985706,30.44263915;" +
                            "50.379829255,30.4426796716667;" +
                            "50.3797668966667,30.44276148;" +
                            "50.3797039033333,30.442842505;" +
                            "50.37967276,30.4428826283333;" +
                            "50.3790271783333,30.443683575;" +
                            "50.3789993266667,30.4437156683333;" +
                            "50.3789731716667,30.44374549;" +
                            "50.3789167083333,30.443845505;" +
                            "50.3789228216667,30.4438877916667;" +
                            "50.37894393,30.4439162666667;" +
                            "50.3789730116667,30.4439200933333;" +
                            "50.3790271983333,30.443871585;" +
                            "50.379053005,30.4438396416667;" +
                            "50.3797870383333,30.4429148133333;" +
                            "50.3798138,30.4428762333333;" +
                            "50.37984014,30.442838915;" +
                            "50.3799646333333,30.4426765316667;" +
                            "50.3799884283333,30.4426468083333;" +
                            "50.3800067533333,30.44260997;" +
                            "50.3800140433333,30.4425683533333;" +
                            "50.3800107583333,30.4425299483333;" +
                            "50.3799999283333,30.442497435;" +
                            "50.379962565,30.4424551;" +
                            "50.37994002,30.4424469166667;" +
                            "50.379917195,30.442450495;" +
                            "50.37989608,30.4424655466667;" +
                            "50.3798763483333,30.4424887;" +
                            "50.37985518,30.4425191633333;" +
                            "50.3793661683333,30.4432254683333;" +
                            "50.3793371383333,30.443262775;" +
                            "50.3793071733333,30.4433007433333;" +
                            "50.3791257483333,30.4435237683333;" +
                            "50.37909487,30.4435570083333;" +
                            "50.3790648766667,30.4435886983333;" +
                            "50.3789171533333,30.4437539516667;" +
                            "50.3788974,30.443784715;" +
                            "50.3788847166667,30.443824715;" +
                            "50.3788856666667,30.44387009;" +
                            "50.3788989366667,30.4439125233333;" +
                            "50.3789147766667,30.443940135;" +
                            "50.3789441433333,30.4439697016667;" +
                            "50.378957715,30.4439779883333;" +
                            "50.3789729066667,30.4439838083333;" +
                            "50.37899351,30.4439852766667;" +
                            "50.3790180466667,30.443977975;" +
                            "50.3790433433333,30.4439613983333;" +
                            "50.3791022633333,30.4439104866667;" +
                            "50.3791338316667,30.4438777816667;" +
                            "50.3791645516667,30.4438415283333;" +
                            "50.37922547,30.443761035;" +
                            "50.379256015,30.4437206733333;" +
                            "50.379286765,30.4436803483333;" +
                            "50.3798683416667,30.4429098683333;" +
                            "50.37991807,30.4428325583333;" +
                            "50.379942385,30.44279597;" +
                            "50.3800094433333,30.4426846933333;" +
                            "50.3800263583333,30.4426443766667;" +
                            "50.3800402933333,30.4425600233333;" +
                            "50.3800343283333,30.44251732;" +
                            "50.38002146,30.44248158;" +
                            "50.3800036233333,30.4424532766667;" +
                            "50.37998223,30.4424324733333;" +
                            "50.3799579016667,30.442419885;" +
                            "50.379904565,30.4424276983333;" +
                            "50.379876285,30.442448675;" +
                            "50.37984967,30.4424798366667;" +
                            "50.379821905,30.4425172316667;" +
                            "50.3796796966667,30.4427261633333;" +
                            "50.3796501816667,30.4427707683333;" +
                            "50.37961942,30.4428163066667;" +
                            "50.37936392,30.4431778983333;" +
                            "50.3793333633333,30.4432175583333;" +
                            "50.3792716066667,30.4432977566667;" +
                            "50.3791092133333,30.4434975533333;" +
                            "50.37904624,30.4435659966667;" +
                            "50.379017085,30.4435973433333;" +
                            "50.3788983516667,30.443739345;" +
                            "50.3788835466667,30.44376694;" +
                            "50.37887275,30.4437973366667;" +
                            "50.3788654816667,30.4438309283333;" +
                            "50.3788630183333,30.4438679066667;" +
                            "50.37886774,30.443911855;" +
                            "50.3788798333333,30.4439601816667;" +
                            "50.378918115,30.44405695;" +
                            "50.378940055,30.4441036633333;" +
                            "50.378962505,30.4441462633333;" +
                            "50.3789835666667,30.4441804316667;" +
                            "50.379004975,30.4442060716667;" +
                            "50.3790287816667,30.44422284;" +
                            "50.3790553666667,30.4442254016667;" +
                            "50.3791149916667,30.44417389;" +
                            "50.3791470133333,30.44412946;" +
                            "50.379180605,30.444080525;" +
                            "50.3792157516667,30.444030385;" +
                            "50.3794945633333,30.4436514983333;" +
                            "50.37952454,30.44362918;" +
                            "50.3795565816667,30.4436230583333";

            /*String locationsString =
                            "50.37922966,30.443585655;" +
                            "50.3792444766667,30.4435697983333;" +
                            "50.3792635783333,30.4435472283333;" +
                            "50.3792881133333,30.4435172633333;" +
                            "50.3793163366667,30.4434824683333;" +
                            "50.3793474066667,30.4434438066667;" +
                            "50.3794109366667,30.44336486;" +
                            "50.379445345,30.4433223733333;" +
                            "50.3794801516667,30.4432790033333;" +
                            "50.3795142266667,30.44323582;" +
                            "50.3795484266667,30.4431920583333;" +
                            "50.3795825283333,30.4431481683333;" +
                            "50.3796149433333,30.4431057316667;" +
                            "50.3796771516667,30.4430242783333;" +
                            "50.3797075383333,30.4429852183333;" +
                            "50.3797372916667,30.4429465283333;" +
                            "50.3797665616667,30.4429075783333;" +
                            "50.379823555,30.4428282666667;" +
                            "50.3798501566667,30.44278962;" +
                            "50.3798753083333,30.4427537483333;" +
                            "50.3798986816667,30.4427201916667;" +
                            "50.37992113,30.4426888783333;" +
                            "50.37994337,30.4426599016667;" +
                            "50.3799833966667,30.4426044183333;" +
                            "50.3799910433333,30.44256623;" +
                            "50.37998511,30.442531405;" +
                            "50.3799695266667,30.4425068833333;" +
                            "50.3799485166667,30.4424973016667;" +
                            "50.3799275916667,30.4425044633333;" +
                            "50.3798967833333,30.4425610633333;" +
                            "50.379880665,30.4426006966667;" +
                            "50.37985706,30.44263915;" +
                            "50.379829255,30.4426796716667;" +
                            "50.3797985,30.442720655;" +
                            "50.3797668966667,30.44276148;" +
                            "50.3797039033333,30.442842505;" +
                            "50.37967276,30.4428826283333;" +
                            "50.3796427466667,30.442921795;" +
                            "50.3796139416667,30.4429608166667;" +
                            "50.3795855183333,30.442999355;" +
                            "50.3795281733333,30.443075195;" +
                            "50.379498265,30.4431130433333;" +
                            "50.37946725,30.443151015;" +
                            "50.37943545,30.4431897116667;" +
                            "50.379402865,30.4432297183333;" +
                            "50.3793696833333,30.4432705083333;" +
                            "50.3793036316667,30.4433538316667;" +
                            "50.3792715883333,30.443393815;" +
                            "50.37924124,30.4434319;" +
                            "50.379210935,30.4434689916667;" +
                            "50.379179895,30.4435059133333;" +
                            "50.37911768,30.44357827;" +
                            "50.3790869933333,30.44361408;" +
                            "50.3790566733333,30.4436495983333;" +
                            "50.3790271783333,30.443683575;" +
                            "50.3789993266667,30.4437156683333;" +
                            "50.3789731716667,30.44374549;" +
                            "50.378948645,30.443773605;" +
                            "50.3789167083333,30.443845505;" +
                            "50.3789228216667,30.4438877916667;" +
                            "50.37894393,30.4439162666667;" +
                            "50.3789730116667,30.4439200933333;" +
                            "50.3790271983333,30.443871585;" +
                            "50.379053005,30.4438396416667;" +
                            "50.379079,30.44380692;" +
                            "50.3791054033333,30.4437742766667;" +
                            "50.3791321283333,30.4437418466667;" +
                            "50.3791591033333,30.4437097766667;" +
                            "50.3792123366667,30.4436458666667;" +
                            "50.3792391933333,30.4436130466667;" +
                            "50.3792666133333,30.443579055;" +
                            "50.3792935983333,30.4435451483333;" +
                            "50.379320125,30.4435127466667;" +
                            "50.3793469366667,30.4434797066667;" +
                            "50.37940334,30.4434109833333;" +
                            "50.3794312533333,30.443376405;" +
                            "50.379458995,30.443342215;" +
                            "50.3794869733333,30.4433072066667;" +
                            "50.37951529,30.4432715333333;" +
                            "50.3795716116667,30.443199075;" +
                            "50.37959938,30.443162695;" +
                            "50.379626715,30.44312673;" +
                            "50.379653565,30.4430914233333;" +
                            "50.3796802066667,30.443056355;" +
                            "50.3797340133333,30.4429864233333;" +
                            "50.3797605283333,30.4429516583333;" +
                            "50.3797870383333,30.4429148133333;" +
                            "50.3798138,30.4428762333333;" +
                            "50.37984014,30.442838915;" +
                            "50.3798660016667,30.4428032333333;" +
                            "50.37989179,30.4427686;" +
                            "50.3799646333333,30.4426765316667;" +
                            "50.3799884283333,30.4426468083333;" +
                            "50.3800067533333,30.44260997;" +
                            "50.3800140433333,30.4425683533333;" +
                            "50.3800107583333,30.4425299483333;" +
                            "50.3799999283333,30.442497435;" +
                            "50.379962565,30.4424551;" +
                            "50.37994002,30.4424469166667;" +
                            "50.379917195,30.442450495;" +
                            "50.37989608,30.4424655466667;" +
                            "50.3798763483333,30.4424887;" +
                            "50.37985518,30.4425191633333;" +
                            "50.379831555,30.4425557183333;" +
                            "50.3797797683333,30.44263528;" +
                            "50.379752095,30.4426758633333;" +
                            "50.3797245283333,30.4427171;" +
                            "50.3796967883333,30.4427582716667;" +
                            "50.379643425,30.4428348333333;" +
                            "50.37959643,30.4429017116667;" +
                            "50.3795738433333,30.4429342333333;" +
                            "50.3795499266667,30.4429701783333;" +
                            "50.379524795,30.4430087733333;" +
                            "50.3794992716667,30.4430469466667;" +
                            "50.3794729933333,30.4430847066667;" +
                            "50.3794206116667,30.443154975;" +
                            "50.3793940066667,30.44318966;" +
                            "50.3793661683333,30.4432254683333;" +
                            "50.3793371383333,30.443262775;" +
                            "50.3793071733333,30.4433007433333;" +
                            "50.3792769316667,30.44333925;" +
                            "50.3792469416667,30.4433782333333;" +
                            "50.3791868233333,30.44345379;" +
                            "50.3791564233333,30.4434897466667;" +
                            "50.3791257483333,30.4435237683333;" +
                            "50.37909487,30.4435570083333;" +
                            "50.3790648766667,30.4435886983333;" +
                            "50.3790366,30.4436183866667;" +
                            "50.3790096466667,30.4436466766667;" +
                            "50.378938555,30.4437273783333;" +
                            "50.3789171533333,30.4437539516667;" +
                            "50.3788974,30.443784715;" +
                            "50.3788847166667,30.443824715;" +
                            "50.3788856666667,30.44387009;" +
                            "50.3788989366667,30.4439125233333;" +
                            "50.3789147766667,30.443940135;" +
                            "50.3789441433333,30.4439697016667;" +
                            "50.378957715,30.4439779883333;" +
                            "50.3789729066667,30.4439838083333;" +
                            "50.37899351,30.4439852766667;" +
                            "50.3790180466667,30.443977975;" +
                            "50.3790433433333,30.4439613983333;" +
                            "50.3791022633333,30.4439104866667;" +
                            "50.3791338316667,30.4438777816667;" +
                            "50.3791645516667,30.4438415283333;" +
                            "50.3791951683333,30.4438017116667;" +
                            "50.37922547,30.443761035;" +
                            "50.379256015,30.4437206733333;" +
                            "50.379286765,30.4436803483333;" +
                            "50.379345555,30.4436036183333;" +
                            "50.3793750966667,30.443564895;" +
                            "50.3794050683333,30.4435249566667;" +
                            "50.37943583,30.44348419;" +
                            "50.3794669183333,30.4434426266667;" +
                            "50.3794974833333,30.443400865;" +
                            "50.37952778,30.4433595966667;" +
                            "50.3795863883333,30.4432805516667;" +
                            "50.3796146483333,30.4432419733333;" +
                            "50.3796431766667,30.443202855;" +
                            "50.3796720866667,30.443163285;" +
                            "50.3797019333333,30.4431246016667;" +
                            "50.3797608916667,30.4430521166667;" +
                            "50.3797894966667,30.4430170566667;" +
                            "50.3798169216667,30.4429820583333;" +
                            "50.3798430416667,30.4429468516667;" +
                            "50.3798683416667,30.4429098683333;" +
                            "50.37991807,30.4428325583333;" +
                            "50.379942385,30.44279597;" +
                            "50.37996642,30.4427595816667;" +
                            "50.3799890283333,30.4427231616667;" +
                            "50.3800094433333,30.4426846933333;" +
                            "50.3800263583333,30.4426443766667;" +
                            "50.3800402933333,30.4425600233333;" +
                            "50.3800343283333,30.44251732;" +
                            "50.38002146,30.44248158;" +
                            "50.3800036233333,30.4424532766667;" +
                            "50.37998223,30.4424324733333;" +
                            "50.3799579016667,30.442419885;" +
                            "50.379904565,30.4424276983333;" +
                            "50.379876285,30.442448675;" +
                            "50.37984967,30.4424798366667;" +
                            "50.379821905,30.4425172316667;" +
                            "50.3797927516667,30.4425582733333;" +
                            "50.3797641366667,30.4425999716667;" +
                            "50.3797081083333,30.44268305;" +
                            "50.3796796966667,30.4427261633333;" +
                            "50.3796501816667,30.4427707683333;" +
                            "50.37961942,30.4428163066667;" +
                            "50.3795875466667,30.4428631183333;" +
                            "50.3795548216667,30.442911245;" +
                            "50.37949055,30.4430066466667;" +
                            "50.3794590666667,30.4430508316667;" +
                            "50.379427425,30.44309388;" +
                            "50.3793953583333,30.4431367783333;" +
                            "50.37936392,30.4431778983333;" +
                            "50.3793333633333,30.4432175583333;" +
                            "50.3792716066667,30.4432977566667;" +
                            "50.3792404333333,30.4433376983333;" +
                            "50.3792083966667,30.4433785733333;" +
                            "50.3791753066667,30.443419295;" +
                            "50.379142145,30.443459475;" +
                            "50.3791092133333,30.4434975533333;" +
                            "50.37904624,30.4435659966667;" +
                            "50.379017085,30.4435973433333;" +
                            "50.3789895166667,30.4436271583333;" +
                            "50.37896348,30.44365636;" +
                            "50.3789390283333,30.4436850883333;" +
                            "50.3788983516667,30.443739345;" +
                            "50.3788835466667,30.44376694;" +
                            "50.37887275,30.4437973366667;" +
                            "50.3788654816667,30.4438309283333;" +
                            "50.3788630183333,30.4438679066667;" +
                            "50.37886774,30.443911855;" +
                            "50.3788798333333,30.4439601816667;" +
                            "50.378918115,30.44405695;" +
                            "50.378940055,30.4441036633333;" +
                            "50.378962505,30.4441462633333;" +
                            "50.3789835666667,30.4441804316667;" +
                            "50.379004975,30.4442060716667;" +
                            "50.3790287816667,30.44422284;" +
                            "50.3790553666667,30.4442254016667;" +
                            "50.3791149916667,30.44417389;" +
                            "50.3791470133333,30.44412946;" +
                            "50.379180605,30.444080525;" +
                            "50.3792157516667,30.444030385;" +
                            "50.3792497533333,30.4439831033333;" +
                            "50.379282805,30.4439376833333;" +
                            "50.3793155716667,30.4438926483333;" +
                            "50.3793797916667,30.4438032633333;" +
                            "50.37941011,30.4437605833333;" +
                            "50.37943924,30.4437199516667;" +
                            "50.379467275,30.44368319;" +
                            "50.3794945633333,30.4436514983333;" +
                            "50.37952454,30.44362918;" +
                            "50.3795565816667,30.4436230583333;";*/
            String[] locationArray = locationsString.split(";");
            for (String aLocationArray : locationArray) {
                String[] locationSting = aLocationArray.split(",");
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(Double.parseDouble(locationSting[0]));
                location.setLongitude(Double.parseDouble(locationSting[1]));
                locations.add(location);
            }
        }

        private void initLocations(LatLng... locationArray) {
            List<Location> reverse = new LinkedList<>();
            for (LatLng point : locationArray) {
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(point.latitude);
                location.setLongitude(point.longitude);
                reverse.add(location);
            }

            for (int i = reverse.size() - 1; i > 0; i--) {
                locations.add(reverse.get(i));
            }
        }

        @Override
        protected String doInBackground(LatLng... locationArray) {
            if (locationArray.length == 0)
                initLocations();
            else
                initLocations(locationArray);

            for (Location point : locations) {
                point.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                point.setTime(System.currentTimeMillis());
                point.setAccuracy(1);
                publishProgress(point);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Location... point) {
            listener.onLocationChanged(point[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("All mocked locations ", "are passed to the app");
        }
    }
}
