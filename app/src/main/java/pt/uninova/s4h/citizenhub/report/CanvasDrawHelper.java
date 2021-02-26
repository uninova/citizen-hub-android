package pt.uninova.s4h.citizenhub.report;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public class CanvasDrawHelper {
    Canvas canvas;
    List<DrawObject> drawingList;


    public CanvasDrawHelper(Canvas canvas) {
        this.canvas = canvas;
        drawingList = new ArrayList<DrawObject>();
    }


    public void addText(String text, float x, float y, Paint paint) {
        DrawObject drawObject = new DrawObject(text, x, y, paint);
        drawingList.add(drawObject);
    }


    public void addTextInFront(String text, Paint paint) {

        DrawObject lastDrawObject = drawingList.get(drawingList.size() - 1);

        drawingList.add(new DrawObject(text, lastDrawObject.paint.measureText(lastDrawObject.text), lastDrawObject.yCoordinate, lastDrawObject.paint));

    }

    public void addNewLine(String text, float x, float spacing, Paint paint) {
        DrawObject drawObject = new DrawObject(text, x, spacing, paint);
        drawingList.add(drawObject);
    }


    public void addNewLine(String text, float spacing) {
        DrawObject lastDrawObject = drawingList.get(drawingList.size() - 1);
        drawingList.add(new DrawObject(text, lastDrawObject.xCoordinate, spacing, lastDrawObject.paint));
    }

    public void draw() {
        DrawObject drawObject = new DrawObject();
        if (drawingList != null) {
            for (int i = 0; i < drawingList.size(); i++) {
                drawObject = drawingList.get(i);
                canvas.drawText(drawObject.text, drawObject.xCoordinate, drawObject.yCoordinate, drawObject.paint);
                drawObject.clearObject();
            }
        }
    }


}
