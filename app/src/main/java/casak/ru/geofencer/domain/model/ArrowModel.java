package casak.ru.geofencer.domain.model;

import java.util.List;

public class ArrowModel {
    public enum Type {
        LEFT,
        RIGHT
    }

    private List<Point> mArrowPoints;
    private Type type;
    private boolean isChosen;

    public ArrowModel(List<Point> arrowPoints, Type type) {
        mArrowPoints = arrowPoints;
        this.type = type;
        isChosen = false;
    }

    public List<Point> getArrowPoints() {
        return mArrowPoints;
    }

    public void setArrowPoints(List<Point> mArrowPoints) {
        this.mArrowPoints = mArrowPoints;
    }

    public Type getType() {
        return type;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

}
