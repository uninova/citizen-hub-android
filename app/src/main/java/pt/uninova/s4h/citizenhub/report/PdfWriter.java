package pt.uninova.s4h.citizenhub.report;

import android.content.res.Resources;
import android.graphics.*;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.util.Log;
import pt.uninova.s4h.citizenhub.persistence.MeasurementAggregate;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import static android.graphics.Bitmap.createScaledBitmap;

public class PdfWriter {

    private final String pathName;
    public Bitmap bmp;
    public Bitmap scaledbitmap;
    private Report report;

    public PdfWriter(String pathName) {
        this.pathName = pathName;
    }

    public boolean writeAll(Resources res, int logo) {
        return write(null, res, logo);
    }

    /*
        public static PdfWriter createInstance(String pathName){
            return new PdfWriter(pathName);
        }
    */



    public boolean write(Report report, Resources res, int logo) {
        Log.w("ExternalStorage", "trying to create PdfWriter");
        bmp = BitmapFactory.decodeResource(res, logo);
        //bmp = BitmapFactory.decodeResource(homecontext.getResources(), logo2_s4h);
        scaledbitmap = createScaledBitmap(bmp, 700, 300, false);
        PdfDocument document = new PdfDocument();
        PageInfo pageInfo = new PageInfo.Builder(1200, 1000, 1).create();
        Paint myPaint = new Paint();
        Paint titlePaint = new Paint();
        Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        //canvas.drawBitmap(scaledbitmap, 0, 0, myPaint);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(30);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");

        canvas.drawText("Username: " + report.getUsername(), 200, 250, titlePaint);
        canvas.drawText(report.getAge() + " years old", 200, 300, titlePaint);
        canvas.drawText("Today's results:  " + dateOnly.format(cal.getTime()), 200, 350, titlePaint);
        canvas.drawText("Steps: " + report.getSteps() + ".", 200, 400, titlePaint);
        canvas.drawText("Steps goal: " + report.getStepsGoal() + ".", 200, 450, titlePaint);
        canvas.drawText("Estimated calories burned: " + report.getCalories() + " calories.", 200, 500, titlePaint);
        canvas.drawText("Total distance walked: " + report.getCalories() + " km", 200, 550, titlePaint);
        canvas.drawText("Spent " + report.getHoursSitting() + "h" + report.getMinutesSitting() + "m sitting.", 200, 600, titlePaint);
        canvas.drawText("Spent " + report.getHoursGoodPosture() + "h" + report.getMinutesGoodPosture() + "m seated with good posture.", 200, 650, titlePaint);
        canvas.drawText("Average heart rate: " + report.getHeartrate() + " bpm.", 200, 700, titlePaint);
        canvas.drawText("Minimum heart rate " + report.getMinHeartRate() + " bpm, at " + report.getMinHeartRateTime(), 200, 750, titlePaint);
        canvas.drawText("Maximum heart rate " + report.getMaxHeartrate() + " bpm, at " + report.getMaxHeartRateTime(), 200, 800, titlePaint);

        createFolder(pathName);

        try {
            document.writeTo(new FileOutputStream(createFile()));

        } catch (IOException e) {
            throw new RuntimeException("Error generating file", e);
        }
        document.close();
        return true;
    }

    private void createFolder(String path) {
        File fol = new File(path, "Smart4Health");
        File folder = new File(fol, "Smart4Health");
        if (!folder.exists()) {
            boolean bool = folder.mkdir();
        }
    }

    private File createFile(String filename) {
        File file = new File(pathName, filename);
        return file;
    }

    private File createFile() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
        File file = new File(pathName, dateOnly.format(cal.getTime()));
        return file;
    }


}
