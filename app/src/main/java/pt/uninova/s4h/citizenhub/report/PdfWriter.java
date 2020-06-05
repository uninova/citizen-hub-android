package pt.uninova.s4h.citizenhub.report;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.graphics.Bitmap.createScaledBitmap;
import static pt.uninova.s4h.citizenhub.ui.Home.homecontext;
import static pt.uninova.s4h.citizenhub.ui.R.drawable.logo2_s4h;

public class PdfWriter {

    public Bitmap bmp;
    public Bitmap scaledbitmap;
    private Report report;
    private final String pathName;

    public PdfWriter(String pathName) {
        this.pathName = pathName;
    }

    /*
        public static PdfWriter createInstance(String pathName){
            return new PdfWriter(pathName);
        }
    */
    public boolean write(Report report) {
        Log.w("ExternalStorage", "trying to create PdfWriter");
        bmp = BitmapFactory.decodeResource(homecontext.getResources(), logo2_s4h);
        scaledbitmap = createScaledBitmap(bmp, 700, 300, false);
        PdfDocument document = new PdfDocument();
        PageInfo pageInfo = new PageInfo.Builder(1200, 1000, 1).create();
        Paint myPaint = new Paint();
        Paint titlePaint = new Paint();
        Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(scaledbitmap, 0, 0, myPaint);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(30);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");

        String filename = "";// filename;
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

    /*    document.finishPage(page);
        File folder = new File("");
        folder.mkdirs();
        String extStorageDirectory = folder.toString();
        File file = new File(extStorageDirectory, "/" + filename + ".pdf");
        */
        createFolder();

        try {
            document.writeTo(new FileOutputStream(createFile().getName()));

        } catch (IOException e) {
            throw new RuntimeException("Error generating file", e);
        }

        document.close();
        return true;
    }

    private String createFolder() {
        File folder = new File(pathName);
        folder.mkdirs();
        String extStorageDirectory;
        return extStorageDirectory = folder.toString();
    }

    private File createFile() {
        File file = new File(pathName);
        return file;
//report.getPathName(), "/" + report.getFileName() + ".pdf"
    }


}
