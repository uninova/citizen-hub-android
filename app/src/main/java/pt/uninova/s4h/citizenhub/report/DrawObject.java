package pt.uninova.s4h.citizenhub.report;

import android.graphics.Paint;

public class DrawObject {
    float xCoordinate;
    float yCoordinate;
    String text;
    Paint paint;

    public DrawObject(String text, float xCoordinate, float yCoordinate, Paint paint) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.text = text;
        this.paint = paint;
    }

    public DrawObject(String text, Paint paint) {
        this.xCoordinate = 0;
        this.yCoordinate = 0;
        this.text = text;
        this.paint = paint;
    }

    public DrawObject() {
        float xCoordinate = 0;
        float yCoordinate = 0;
        String text = null;
        Paint paint = null;
    }

    public DrawObject(DrawObject drawObject) {
        this.xCoordinate = drawObject.xCoordinate;
        this.yCoordinate = drawObject.yCoordinate;
        this.text = drawObject.text;
        this.paint = drawObject.paint;
    }

    public void clearObject() {
        this.xCoordinate = 0;
        this.yCoordinate = 0;
        this.text = null;
        this.paint = null;
    }

    public float getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(float xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public float getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(float yCoordinate) {
        this.yCoordinate = yCoordinate;
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
}
