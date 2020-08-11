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
import static pt.uninova.s4h.citizenhub.ui.R.drawable.logo_s4h_2;

public class pdf extends Activity implements Runnable {


    public Bitmap bmp;
    public Bitmap scaledbitmap;
    public String HeartRate, Steps, Distance, Calories, HoursSitting, MinutesSitting, HoursGoodPosture, MinutesGoodPosture;
    public String Username, Age, StepsGoal, StandingTimeHours, StandingTimeMinutes, MinHeartRate, MaxHeartRate;
    public String MinHeartRateTime, MaxHeartRateTime, Filename;
    public int pageWidth = 1200;

    public pdf(int heartrate, int steps, int distance, int calories, int hoursSitting, int minutesSitting, int hoursGoodPosture, int minutesGoodPosture,
               String username, int age, int stepsgoal, int standingtimehours, int standingtimeminutes, int minheartrate, int maxheartrate, String minheartratetime,
               String maxheartratetime) {
        this.HeartRate = String.valueOf(heartrate);
        this.Steps = String.valueOf(steps);
        this.Distance = String.valueOf(distance);
        this.Calories = String.valueOf(calories);
        this.HoursSitting = String.valueOf(hoursSitting);
        this.MinutesSitting = String.valueOf(minutesSitting);
        this.HoursGoodPosture = String.valueOf(hoursGoodPosture);
        this.MinutesGoodPosture = String.valueOf(minutesGoodPosture);
        this.Username = username;
        this.Age = String.valueOf(age);
        this.StepsGoal = String.valueOf(stepsgoal);
        this.StandingTimeHours = String.valueOf(standingtimehours);
        this.StandingTimeMinutes = String.valueOf(standingtimeminutes);
        this.MaxHeartRate = String.valueOf(maxheartrate);
        this.MinHeartRate = String.valueOf(minheartrate);
        this.MinHeartRateTime = minheartratetime;
        this.MaxHeartRateTime = maxheartratetime;
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
        bmp = BitmapFactory.decodeResource(homecontext.getResources(), logo_s4h_2);
        scaledbitmap = createScaledBitmap(bmp, 400, 150, false);
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
        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(30);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat tempfilename = new SimpleDateFormat("MM-dd-yyyy");

        Filename = tempfilename.format(cal.getTime());
        canvas.drawText("Username: " + Username, 200, 250, titlePaint);
        canvas.drawText(Age + " years old", 200, 300, titlePaint);
        canvas.drawText("Today's results:  " + dateOnly.format(cal.getTime()), 200, 350, titlePaint);
        canvas.drawText("Steps: " + Steps, 200, 400, titlePaint);
        canvas.drawText("Steps goal: " + StepsGoal, 200, 450, titlePaint);
        canvas.drawText("Estimated calories burned: " + Calories + " calories", 200, 500, titlePaint);
        canvas.drawText("Total distance walked: " + Distance + " km", 200, 550, titlePaint);
        canvas.drawText("Spent " + HoursSitting + "h"+ MinutesSitting + "m sitting", 200, 600, titlePaint);
        canvas.drawText("Spent " + HoursGoodPosture + "h"+ MinutesGoodPosture+"m seated with good posture", 200, 650, titlePaint);
        canvas.drawText("Average heart rate: " + HeartRate + " bpm", 200, 700, titlePaint);
        canvas.drawText("Minimum heart rate " + MinHeartRate +" bpm, at " + MinHeartRateTime, 200, 750, titlePaint);
        canvas.drawText("Maximum heart rate " + MaxHeartRate +" bpm, at " + MaxHeartRateTime, 200, 800, titlePaint);
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
        File file = new File(extStorageDirectory, "/" + Filename + ".pdf");
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
