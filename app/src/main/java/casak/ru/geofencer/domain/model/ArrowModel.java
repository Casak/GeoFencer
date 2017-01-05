package casak.ru.geofencer.domain.model;

import java.util.List;

public class ArrowModel {

    private List<Point> mArrowPoints;

    public ArrowModel(List<Point> arrowPoints) {
        mArrowPoints = arrowPoints;
    }

    public List<Point> getArrowPoints() {
        return mArrowPoints;
    }

    public void setArrowPoints(List<Point> mArrowPoints) {
        this.mArrowPoints = mArrowPoints;
    }
}
