package pt.uninova.s4h.citizenhub.report;

import android.graphics.Canvas;
import android.graphics.Paint;

public class CanvasDrawHelper {

    double xCoordinate = 0;
    double yCoordinate = 0;
    String text;
    Paint paint;

    public CanvasDrawHelper(Canvas canvas) {
        this.text = text;
        this.paint = paint;
    }


    public CanvasDrawHelper(Canvas canvas, float x, float y) {
        this.xCoordinate = x;
        this.yCoordinate = y;
        this.text = null;
        this.paint = null;
    }

    public void drawLine() {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public double getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public double getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }


}
