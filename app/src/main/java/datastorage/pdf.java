package datastorage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.graphics.Bitmap.createScaledBitmap;
import static pt.uninova.s4h.citizenhub.ui.Home.homecontext;
import static pt.uninova.s4h.citizenhub.ui.R.drawable.logo2_s4h;

public class pdf extends Activity implements Runnable {


    public Bitmap bmp;
    public Bitmap scaledbitmap;
    public String HeartRate, Steps, Distance, Calories, HoursSitting, MinutesSitting, HoursGoodPosture, MinutesGoodPosture;
    public int pageWidth = 1200;

    public pdf(int heartrate, int steps, int distance, int calories, int hoursSitting, int minutesSitting, int hoursGoodPosture, int minutesGoodPosture) {
        this.HeartRate = String.valueOf(heartrate);
        this.Steps = String.valueOf(steps);
        this.Distance = String.valueOf(distance);
        this.Calories = String.valueOf(calories);
        this.HoursSitting = String.valueOf(hoursSitting);
        this.MinutesSitting = String.valueOf(minutesSitting);
        this.HoursGoodPosture = String.valueOf(hoursGoodPosture);
        this.MinutesGoodPosture = String.valueOf(minutesGoodPosture);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_main);
        Log.w("ExternalStorage", "pdf class");

        createPDF();
    }


    /**
     * PDF Gen should run in own thread to not slow the GUI
     */
    public void createPDF() {
        Log.w("ExternalStorage", "createpdf");

        new Thread(this).start();
    }

    public void run() {
        Log.w("ExternalStorage", "trying to create pdf");
        bmp = BitmapFactory.decodeResource(homecontext.getResources(), logo2_s4h);
        scaledbitmap = createScaledBitmap(bmp, 700, 300, false);
        // Create a shiny new (but blank) PDF document in memory
        // We want it to optionally be printable, so add PrintAttributes
        // and use a PrintedPdfDocument. Simpler: new PdfDocument().
      /*  PrintAttributes printAttrs = new PrintAttributes.Builder().
                setColorMode(PrintAttributes.COLOR_MODE_COLOR).
                setMediaSize(PrintAttributes.MediaSize.NA_LETTER).
                setResolution(new Resolution("smart4health", PRINT_SERVICE, 300, 300)).
                setMinMargins(Margins.NO_MARGINS).
                build();

       */
        PdfDocument document = new PdfDocument();
        PageInfo pageInfo = new PageInfo.Builder(1200, 1000, 1).create();

        Paint myPaint = new Paint();
        Paint titlePaint = new Paint();
        // crate a page description

        // create a new page from the PageInfo
        Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        canvas.drawBitmap(scaledbitmap, 0, 0, myPaint);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(30);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");


        canvas.drawText("Today's results:  " + dateOnly.format(cal.getTime()), 600, 270, titlePaint);
        canvas.drawText("Your average heart rate was " + HeartRate + " bpm.", 600, 350, titlePaint);
        canvas.drawText("Your total steps today were " + Steps + ".", 600, 400, titlePaint);
        canvas.drawText("and burned " + Calories + " calories today!", 600, 500, titlePaint);
        canvas.drawText("You've walked " + Distance + " km!", 600, 450, titlePaint);
        canvas.drawText("Spent " + HoursSitting + "h"+ MinutesSitting + "m and "+ HoursGoodPosture + "h"+ MinutesGoodPosture+"m with good posture.", 600, 550, titlePaint);
        /*
        // repaint the user's text into the page
        View content = findViewById(R.id.sunday);
        content.draw(page.getCanvas());
*/
        // do final processing of the page
        document.finishPage(page);
        //  File file = new File(Environment.getExternalStorageDirectory(), "/smart4health.pdf");
        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/smart4health/pdfs");
        folder.mkdirs();
        String extStorageDirectory = folder.toString();
        File file = new File(extStorageDirectory, "/smart4health.pdf");
        Log.w("ExternalStorage", Environment.getExternalStorageDirectory().toString());

        try {
            document.writeTo(new FileOutputStream(file));

        } catch (IOException e) {
            Log.w("ExternalStorage", "error");
            throw new RuntimeException("Error generating file", e);
        }
        Log.w("ExternalStorage", "success");

        document.close();
    }

}
