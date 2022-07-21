package pt.uninova.s4h.citizenhub.report;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;
import pt.uninova.s4h.citizenhub.persistence.repository.ReportRepository;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class DailyReportGeneratorPDF {

    private final Context context;

    private final Paint logoBackGroundPaint;
    private final Paint titlePaint;
    private final Paint darkTextPaint;
    private final Paint darkItalicTextPaint;
    private final Paint whiteTextPaint;
    private final Paint whiteItalicTextPaint;
    private final Paint boldTextPaint;
    private final Paint backgroundPaint;
    private final Paint rectPaint;
    private final Paint rectFillPaint;
    private final Path path;
    private final float[] corners;

    public DailyReportGeneratorPDF (Context context){
        this.context = context;

        this.logoBackGroundPaint = new Paint();
        logoBackGroundPaint.setStyle(Paint.Style.FILL);
        logoBackGroundPaint.setColor(Color.parseColor("#f0f0f0"));
        logoBackGroundPaint.setAntiAlias(true);

        this.titlePaint = new Paint();
        titlePaint.setColor(Color.parseColor("#FFFFFF"));
        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        titlePaint.setTextSize(18);

        this.darkTextPaint = new Paint();
        darkTextPaint.setColor(Color.parseColor("#000000"));
        darkTextPaint.setTextAlign(Paint.Align.LEFT);
        darkTextPaint.setTypeface(Typeface.DEFAULT);
        darkTextPaint.setTextSize(12);

        this.darkItalicTextPaint = new Paint();
        darkItalicTextPaint.setColor(Color.parseColor("#000000"));
        darkItalicTextPaint.setTextAlign(Paint.Align.LEFT);
        darkItalicTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        darkItalicTextPaint.setTextSize(12);

        this.whiteTextPaint = new Paint();
        whiteTextPaint.setColor(Color.parseColor("#ffffff"));
        whiteTextPaint.setTextAlign(Paint.Align.LEFT);
        whiteTextPaint.setTypeface(Typeface.DEFAULT);
        whiteTextPaint.setTextSize(12);

        this.whiteItalicTextPaint = new Paint();
        whiteItalicTextPaint.setColor(Color.parseColor("#ffffff"));
        whiteItalicTextPaint.setTextAlign(Paint.Align.LEFT);
        whiteItalicTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        whiteItalicTextPaint.setTextSize(12);

        this.boldTextPaint = new Paint();
        boldTextPaint.setColor(Color.parseColor("#000000"));
        boldTextPaint.setTextAlign(Paint.Align.LEFT);
        boldTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        boldTextPaint.setTextSize(12);

        this.backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(Color.parseColor("#2789C2"));
        backgroundPaint.setAntiAlias(true);

        this.rectPaint = new Paint();
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(3);
        rectPaint.setColor(Color.parseColor("#06344F"));

        this.rectFillPaint = new Paint();
        rectFillPaint.setStyle(Paint.Style.FILL);
        rectFillPaint.setStrokeWidth(2);
        rectFillPaint.setColor(Color.parseColor("#06344F"));

        this.corners = new float[]{
                12, 12,        // Top left radius in px
                12, 12,        // Top right radius in px
                0, 0,          // Bottom right radius in px
                0, 0           // Bottom left radius in px
        };

        this.path = new Path();
    }

    public void generateWorkTimeReportPDF(Observer observerReportPDF, Resources res, ReportRepository reportRepository, LocalDate date, MeasurementKindLocalization measurementKindLocalization) {

        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(600, 1100, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        canvas.setDensity(72);

        CanvasWriter canvasWriter = new CanvasWriter(canvas);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inDensity = 72;

        Observer<Report> observerReport = reportData -> {

            drawCitizenHub(canvas, res);

            int x = 50; //30
            int y = 80;
            canvas.drawRoundRect(x, y, 550, y + 70, 10, 10, backgroundPaint); //570
            canvasWriter.addText("Work Time Report", x + 165, y + 28, titlePaint);
            canvasWriter.addText("Results of: " + date.toString(), x + 165, y + 53, titlePaint);

            y += 110; //130

            List<Group> groupsReportData = reportData.getGroups();
            for (Group group : groupsReportData) {

                int rectHeight = y - 20;
                int groupLabel = ((MeasurementTypeLocalizedResource)group.getLabel()).getMeasurementType();

                path.addRoundRect(new RectF(x, rectHeight, 550, rectHeight + 25), corners, Path.Direction.CW);
                canvas.drawPath(path, rectFillPaint);
                canvasWriter.addText(measurementKindLocalization.localize(groupLabel), x + 20, y - 4, whiteTextPaint);

                y += 20;
                if (groupLabel == Measurement.TYPE_BLOOD_PRESSURE || groupLabel == Measurement.TYPE_LUMBAR_EXTENSION_TRAINING) {
                    for (Group groupDailyMeasurements : group.getGroupList()) {
                        System.out.println(groupDailyMeasurements.getLabel().getLocalizedString());
                        //canvasWriter.addText(groupDailyMeasurements.getLabel().getLocalizedString(), x + 50, y, darkTextPaint);
                        String timestamp = groupDailyMeasurements.getLabel().getLocalizedString();
                        canvasWriter.addText(timestamp.substring(timestamp.indexOf("T") + 1, timestamp.indexOf("Z")), x + 25, y, darkTextPaint);
                        canvasWriter.addTextInFront(" - MyTime", darkItalicTextPaint);
                        y += 20;
                        for (Item item : groupDailyMeasurements.getItemList()) {
                            canvasWriter.addText(item.getLabel().getLocalizedString(), x + 40, y, darkTextPaint);
                            canvasWriter.addText(" " + decimalFormat.format(Double.valueOf(item.getValue().getLocalizedString())), x + 360, y, darkTextPaint);
                            if(!item.getUnits().getLocalizedString().equals("-")){
                                canvasWriter.addText(" " + item.getUnits().getLocalizedString(), x + 440, y, darkTextPaint);
                            }
                            y += 20;
                        }
                    }
                } else {
                    for (Item item : group.getItemList()) {
                        canvasWriter.addText(item.getLabel().getLocalizedString(), x + 40, y, darkTextPaint);
                        canvasWriter.addText(" " + decimalFormat.format(Double.valueOf(item.getValue().getLocalizedString())), x + 360, y, darkTextPaint);
                        if(!item.getUnits().getLocalizedString().equals("-")){
                            canvasWriter.addText(" " + item.getUnits().getLocalizedString(), x + 440, y, darkTextPaint);
                        }
                        y += 20;
                    }
                }
                y = y + 40;
                RectF rectAround = new RectF(x, rectHeight, 550, y - 50);
                canvas.drawRoundRect(rectAround, 12, 12, rectPaint);
            }

            canvasWriter.draw();

            document.finishPage(page);

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try {
                document.writeTo(out);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] outByteArray = out.toByteArray();

            document.close();

            observerReportPDF.observe(outByteArray);

        };

        DailyReportGenerator dailyReportGenerator = new DailyReportGenerator(context);
        dailyReportGenerator.generateWorkTimeReport(reportRepository, date, observerReport);
    }

    public void generateNotWorkTimeReportPDF(Observer observerReportPDF, Resources res, ReportRepository reportRepository, LocalDate date, MeasurementKindLocalization measurementKindLocalization) {

        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(600, 1100, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        canvas.setDensity(72);

        CanvasWriter canvasWriter = new CanvasWriter(canvas);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inDensity = 72;

        Observer<Report> observerReport = reportData -> {

            drawCitizenHub(canvas, res);

            int x = 50; //30
            int y = 80;
            canvas.drawRoundRect(x, y, 550, y + 70, 10, 10, backgroundPaint); //570
            canvasWriter.addText("Not Work Time Report", x + 165, y + 28, titlePaint);
            canvasWriter.addText("Results of: " + date.toString(), x + 165, y + 53, titlePaint);

            y += 110; //130

            List<Group> groupsReportData = reportData.getGroups();
            for (Group group : groupsReportData) {

                int rectHeight = y - 20;
                int groupLabel = ((MeasurementTypeLocalizedResource)group.getLabel()).getMeasurementType();

                path.addRoundRect(new RectF(x, rectHeight, 550, rectHeight + 25), corners, Path.Direction.CW);
                canvas.drawPath(path, rectFillPaint);
                canvasWriter.addText(measurementKindLocalization.localize(groupLabel), x + 20, y - 4, whiteTextPaint);

                y += 20;
                if (groupLabel == Measurement.TYPE_BLOOD_PRESSURE || groupLabel == Measurement.TYPE_LUMBAR_EXTENSION_TRAINING) {
                    for (Group groupDailyMeasurements : group.getGroupList()) {
                        System.out.println(groupDailyMeasurements.getLabel().getLocalizedString());
                        //canvasWriter.addText(groupDailyMeasurements.getLabel().getLocalizedString(), x + 50, y, darkTextPaint);
                        String timestamp = groupDailyMeasurements.getLabel().getLocalizedString();
                        canvasWriter.addText(timestamp.substring(timestamp.indexOf("T") + 1, timestamp.indexOf("Z")), x + 25, y, darkTextPaint);
                        canvasWriter.addTextInFront(" - MyTime", darkItalicTextPaint);
                        y += 20;
                        for (Item item : groupDailyMeasurements.getItemList()) {
                            canvasWriter.addText(item.getLabel().getLocalizedString(), x + 40, y, darkTextPaint);
                            canvasWriter.addText(" " + decimalFormat.format(Double.valueOf(item.getValue().getLocalizedString())), x + 360, y, darkTextPaint);
                            if(!item.getUnits().getLocalizedString().equals("-")){
                                canvasWriter.addText(" " + item.getUnits().getLocalizedString(), x + 440, y, darkTextPaint);
                            }
                            y += 20;
                        }
                    }
                } else {
                    for (Item item : group.getItemList()) {
                        canvasWriter.addText(item.getLabel().getLocalizedString(), x + 40, y, darkTextPaint);
                        canvasWriter.addText(" " + decimalFormat.format(Double.valueOf(item.getValue().getLocalizedString())), x + 360, y, darkTextPaint);
                        if(!item.getUnits().getLocalizedString().equals("-")){
                            canvasWriter.addText(" " + item.getUnits().getLocalizedString(), x + 440, y, darkTextPaint);
                        }
                        y += 20;
                    }
                }
                y = y + 40;
                RectF rectAround = new RectF(x, rectHeight, 550, y - 50);
                canvas.drawRoundRect(rectAround, 12, 12, rectPaint);
            }

            canvasWriter.draw();

            document.finishPage(page);

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            try {
                document.writeTo(out);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] outByteArray = out.toByteArray();

            document.close();

            observerReportPDF.observe(outByteArray);

        };

        DailyReportGenerator dailyReportGenerator = new DailyReportGenerator(context);
        dailyReportGenerator.generateNotWorkTimeReport(reportRepository, date, observerReport);
    }

    public void generateCompleteReport(Observer observerReportPDF, Resources res, ReportRepository reportRepository, LocalDate date, MeasurementKindLocalization measurementKindLocalization) {

        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        canvas.setDensity(72);

        CanvasWriter canvasWriter = new CanvasWriter(canvas);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inDensity = 72;

        Observer<Report> observerWorkTimeReport = workTimeData -> {

            Observer<Report> observerNotWorkTimeReport = notWorkTimeData -> {

                drawCitizenHub(canvas, res);

                int x = 50; //30
                int y = 80;
                canvas.drawRoundRect(x, y, 550, y + 70, 10, 10, backgroundPaint); //570
                canvasWriter.addText("Complete Daily Report", x + 165, y + 28, titlePaint);
                canvasWriter.addText("Results of: " + date.toString(), x + 165, y + 53, titlePaint);

                //x += 20;

                /*canvas.drawRect(x, y + 60, 550, y + 90, rectFillPaint);
                canvasWriter.addText("Results of: " + date.toString(), 200, y + 80, titlePaint);*/

                y += 110; // 120

                List<Group> groupsWorkTimeData = workTimeData.getGroups();
                List<Group> groupsNotWorkTimeData = notWorkTimeData.getGroups();

                for (Group groupNotWorkTime : groupsNotWorkTimeData) {

                    int rectHeight = y - 20;
                    int notWorkTimeLabel = ((MeasurementTypeLocalizedResource)groupNotWorkTime.getLabel()).getMeasurementType();

                    path.addRoundRect(new RectF(x, rectHeight, 550, rectHeight + 25), corners, Path.Direction.CW);
                    canvas.drawPath(path, rectFillPaint);
                    canvasWriter.addText(measurementKindLocalization.localize(notWorkTimeLabel), x + 20, y - 4, whiteTextPaint);

                    y += 20;

                    if (notWorkTimeLabel == Measurement.TYPE_BLOOD_PRESSURE || notWorkTimeLabel == Measurement.TYPE_LUMBAR_EXTENSION_TRAINING) {
                        for (Group group : groupNotWorkTime.getGroupList()) {
                            String timestamp = group.getLabel().getLocalizedString();
                            canvasWriter.addText(timestamp.substring(timestamp.indexOf("T") + 1, timestamp.indexOf("Z")), x + 25, y, darkTextPaint);
                            canvasWriter.addTextInFront(" - MyTime", darkItalicTextPaint);
                            y += 20;
                            for (Item item : group.getItemList()) {
                                canvasWriter.addText(item.getLabel().getLocalizedString(), x + 40, y, darkTextPaint);
                                canvasWriter.addText(" " + decimalFormat.format(Double.valueOf(item.getValue().getLocalizedString())), x + 360, y, darkTextPaint);
                                if(!item.getUnits().getLocalizedString().equals("-")){
                                    canvasWriter.addText(" " + item.getUnits().getLocalizedString(), x + 440, y, darkTextPaint);
                                }
                                y += 20;
                            }
                        }
                        for (Group groupWorkTime : groupsWorkTimeData) {
                            int workTimeLabel = ((MeasurementTypeLocalizedResource)groupNotWorkTime.getLabel()).getMeasurementType();
                            if (notWorkTimeLabel == workTimeLabel) {
                                for (Group group : groupWorkTime.getGroupList()) {
                                    String timestamp = group.getLabel().getLocalizedString();
                                    canvasWriter.addText(timestamp.substring(timestamp.indexOf("T") + 1, timestamp.indexOf("Z")), x + 25, y, darkTextPaint);
                                    canvasWriter.addTextInFront(" - MyWork", darkItalicTextPaint);
                                    y += 20;
                                    for (Item item : group.getItemList()) {
                                        canvasWriter.addText(item.getLabel().getLocalizedString(), x + 40, y, darkTextPaint);
                                        canvasWriter.addText(" " + decimalFormat.format(Double.valueOf(item.getValue().getLocalizedString())), x + 360, y, darkTextPaint);
                                        if(!item.getUnits().getLocalizedString().equals("-")){
                                            canvasWriter.addText(" " + item.getUnits().getLocalizedString(), x + 440, y, darkTextPaint);
                                        }
                                        y += 20;
                                    }
                                    y += 20;
                                }
                            }
                        }
                    } else {
                        canvasWriter.addText("MyTime", x + 240, y - 24, whiteItalicTextPaint);
                        canvasWriter.addText("MyWork", x + 360, y - 24, whiteItalicTextPaint);
                        boolean hasItem = false;
                        for (Group groupWorkTime : groupsWorkTimeData) {
                            if (((MeasurementTypeLocalizedResource)groupNotWorkTime.getLabel()).getMeasurementType() == ((MeasurementTypeLocalizedResource)groupWorkTime.getLabel()).getMeasurementType()) {
                                hasItem = true;
                                for (Item itemNotWorkTime : groupNotWorkTime.getItemList()) {
                                    for (Item itemWorkTime : groupWorkTime.getItemList()) {
                                        if (itemNotWorkTime.getLabel().getLocalizedString().equals(itemWorkTime.getLabel().getLocalizedString())) {
                                            canvasWriter.addText(itemNotWorkTime.getLabel().getLocalizedString(), x + 40, y, darkTextPaint);
                                            canvasWriter.addText(" " + decimalFormat.format(Double.valueOf(itemNotWorkTime.getValue().getLocalizedString())), x + 240, y, darkTextPaint);
                                            canvasWriter.addText(" " + decimalFormat.format(Double.valueOf(itemWorkTime.getValue().getLocalizedString())), x + 360, y, darkTextPaint);
                                            if(!itemNotWorkTime.getUnits().getLocalizedString().equals("-")){
                                                canvasWriter.addText(" " + itemNotWorkTime.getUnits().getLocalizedString(), x + 440, y, darkTextPaint);
                                            }
                                            y += 20;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        if (!hasItem) {
                            for (Item item : groupNotWorkTime.getItemList()) {
                                canvasWriter.addText(item.getLabel().getLocalizedString(), x + 40, y, darkTextPaint);
                                canvasWriter.addText(" " + decimalFormat.format(Double.valueOf(item.getValue().getLocalizedString())), x + 240, y, darkTextPaint);
                                canvasWriter.addText(" 0", x + 360, y, darkTextPaint);
                                if(!item.getUnits().getLocalizedString().equals("-")){
                                    canvasWriter.addText(" " + item.getUnits().getLocalizedString(), x + 440, y, darkTextPaint);
                                }
                                y += 20;
                            }

                        }
                    }
                    y += 40;
                    RectF rectAround = new RectF(x, rectHeight, 550, y - 50);
                    canvas.drawRoundRect(rectAround, 12, 12, rectPaint);
                }

                for (Group groupWorkTime : groupsWorkTimeData) {
                    int rectHeight = y - 10;
                    boolean hasGroup = false;
                    int workTimeLabel = ((MeasurementTypeLocalizedResource)groupWorkTime.getLabel()).getMeasurementType();

                    for (Group groupNotWorkTime : groupsNotWorkTimeData) {
                        if (workTimeLabel == ((MeasurementTypeLocalizedResource) groupNotWorkTime.getLabel()).getMeasurementType()) {
                            hasGroup = true;
                            break;
                        }
                    }

                    if (!hasGroup) {
                        path.addRoundRect(new RectF(x, rectHeight, 550, rectHeight + 25), corners, Path.Direction.CW);
                        canvas.drawPath(path, rectFillPaint);
                        canvasWriter.addText(measurementKindLocalization.localize(workTimeLabel), x + 20, y, darkTextPaint);
                        y += 20;
                        if (workTimeLabel == Measurement.TYPE_BLOOD_PRESSURE ||
                                workTimeLabel == Measurement.TYPE_LUMBAR_EXTENSION_TRAINING) {
                            for (Group group : groupWorkTime.getGroupList()) {
                                canvasWriter.addText(group.getLabel().getLocalizedString(), x + 25, y, darkTextPaint);
                                canvasWriter.addTextInFront(" - MyWork", darkItalicTextPaint);
                                y += 20;
                                for (Item item : group.getItemList()) {
                                    canvasWriter.addText(item.getLabel().getLocalizedString() + ":", x + 40, y, darkTextPaint);
                                    canvasWriter.addText(" " + decimalFormat.format(Double.valueOf(item.getValue().getLocalizedString())), x + 360, y, darkTextPaint);
                                    if(!item.getUnits().getLocalizedString().equals("-")){
                                        canvasWriter.addText(" " + item.getUnits().getLocalizedString(), x + 440, y, darkTextPaint);
                                    }
                                    y += 20;
                                }
                                y += 20;
                            }
                        }
                        else {
                            canvasWriter.addText("MyTime", x + 240, y - 24, whiteItalicTextPaint);
                            canvasWriter.addText("MyWork", x + 360, y - 24, whiteItalicTextPaint);
                            for (Item item : groupWorkTime.getItemList()) {
                                canvasWriter.addText(item.getLabel().getLocalizedString(), x + 40, y, darkTextPaint);
                                canvasWriter.addText(" 0", x + 240, y, darkTextPaint);
                                canvasWriter.addText(" " + decimalFormat.format(Double.valueOf(item.getValue().getLocalizedString())), x + 360, y, darkTextPaint);
                                if(!item.getUnits().getLocalizedString().equals("-")){
                                    canvasWriter.addText(" " + item.getUnits().getLocalizedString(), x + 440, y, darkTextPaint);
                                }
                                y += 20;
                            }
                        }
                        y += 40;
                        RectF rectAround = new RectF(x, rectHeight - 10, 550, y);
                        canvas.drawRoundRect(rectAround, 12, 12, rectPaint);
                    }
                }

                canvasWriter.draw();

                document.finishPage(page);

                ByteArrayOutputStream out = new ByteArrayOutputStream();

                try {
                    document.writeTo(out);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                byte[] outByteArray = out.toByteArray();

                document.close();

                observerReportPDF.observe(outByteArray);

            };

            DailyReportGenerator dailyReportGenerator = new DailyReportGenerator(context);
            dailyReportGenerator.generateNotWorkTimeReport(reportRepository, date, observerNotWorkTimeReport);
        };

        DailyReportGenerator dailyReportGenerator = new DailyReportGenerator(context);
        dailyReportGenerator.generateWorkTimeReport(reportRepository, date, observerWorkTimeReport);

    }

    private void drawCitizenHub (Canvas canvas, Resources res){
        canvas.drawRect(0, 0, 595, 70, logoBackGroundPaint);

        Drawable citizenHubLogo = res.getDrawable(R.drawable.ic_citizen_hub_logo, null);
        citizenHubLogo.setBounds(5, 5, citizenHubLogo.getIntrinsicWidth(), citizenHubLogo.getIntrinsicHeight());
        canvas.save();
        canvas.translate(50, 5);
        canvas.scale(1.5f, 1.5f);
        citizenHubLogo.draw(canvas);
        canvas.restore();

        Drawable citizenHub = res.getDrawable(R.drawable.logo_citizen_hub_text_only, null);
        citizenHub.setBounds(5, 5, citizenHub.getIntrinsicWidth(), citizenHub.getIntrinsicHeight());
        canvas.save();
        canvas.translate(80, 3);
        canvas.scale(3f, 4.5f);
        citizenHub.draw(canvas);
        canvas.restore();

    }

}