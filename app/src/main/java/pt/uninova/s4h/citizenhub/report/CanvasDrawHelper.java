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

    public void addNewLine(String text, float x, float verticalSpacing, Paint paint) {
        DrawObject lastDrawObject = drawingList.get(drawingList.size() - 1);
        DrawObject drawObject = new DrawObject(text, x, verticalSpacing + lastDrawObject.getYCoordinate(), paint);
        drawingList.add(drawObject);
    }


    public void addNewLine(String text, float verticalSpacing) {
        DrawObject lastDrawObject = drawingList.get(drawingList.size() - 1);
        drawingList.add(new DrawObject(text, lastDrawObject.xCoordinate, verticalSpacing + lastDrawObject.getYCoordinate(), lastDrawObject.paint));
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

    public void clear() {
        drawingList.clear();
    }


}
