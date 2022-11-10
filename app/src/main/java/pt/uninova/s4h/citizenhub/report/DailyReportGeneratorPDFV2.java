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
import android.os.Looper;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import androidx.core.content.res.ResourcesCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.localization.MeasurementKindLocalization;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class DailyReportGeneratorPDFV2 {

    private final Context context;

    private final Paint logoBackgroundPaint;
    private final TextPaint footerPaint;
    private final Paint titlePaint;
    private final Paint darkTextPaintAlignLeft;
    private final Paint darkTextPaintAlignRight;
    private final Paint darkItalicTextPaint;
    private final Paint whiteTextPaint;
    private final Paint whiteItalicTextPaint;
    private final Paint backgroundPaint;
    private final Paint rectPaint;
    private final Paint rectFillPaint;
    private final float[] corners;

    public DailyReportGeneratorPDFV2(Context context) {
        this.context = context;

        this.logoBackgroundPaint = new Paint();
        logoBackgroundPaint.setStyle(Paint.Style.FILL);
        logoBackgroundPaint.setColor(Color.parseColor("#f0f0f0"));
        logoBackgroundPaint.setAntiAlias(true);

        this.footerPaint = new TextPaint();
        footerPaint.setStyle(Paint.Style.FILL);
        footerPaint.setTextSize(10);
        footerPaint.setColor(Color.parseColor("#000000"));
        footerPaint.setAntiAlias(true);

        this.titlePaint = new Paint();
        titlePaint.setColor(Color.parseColor("#FFFFFF"));
        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        titlePaint.setTextSize(18);

        this.darkTextPaintAlignLeft = new Paint();
        darkTextPaintAlignLeft.setColor(Color.parseColor("#000000"));
        darkTextPaintAlignLeft.setTextAlign(Paint.Align.LEFT);
        darkTextPaintAlignLeft.setTypeface(Typeface.DEFAULT);
        darkTextPaintAlignLeft.setTextSize(12);

        this.darkTextPaintAlignRight = new Paint();
        darkTextPaintAlignRight.setColor(Color.parseColor("#000000"));
        darkTextPaintAlignRight.setTextAlign(Paint.Align.RIGHT);
        darkTextPaintAlignRight.setTypeface(Typeface.DEFAULT);
        darkTextPaintAlignRight.setTextSize(12);

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
        whiteItalicTextPaint.setTextAlign(Paint.Align.RIGHT);
        whiteItalicTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        whiteItalicTextPaint.setTextSize(12);

        Paint boldTextPaint = new Paint();
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
    }

    /*public void generateWorkTimeReportPDF(Observer observerReportPDF, Resources res, ReportRepository reportRepository, LocalDate date, MeasurementKindLocalization measurementKindLocalization) {

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
                int groupLabel = ((StringMeasurementId)group.getLabel()).getMeasurementId();

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
                int groupLabel = ((StringMeasurementId)group.getLabel()).getMeasurementId();

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
    }*/

    public void generateCompleteReport(Report workTime, Report notWorkTime, Resources res, LocalDate date, MeasurementKindLocalization measurementKindLocalization, Observer<byte[]> observerReportPDF) {
        Looper.prepare();

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        final PdfDocument.Page[] page = {document.startPage(pageInfo)}; // Não percebo porque é que se tem que alterar para isto

        final Canvas[] canvas = {page[0].getCanvas()};
        canvas[0].setDensity(72);

        final CanvasWriter[] canvasWriter = {new CanvasWriter(canvas[0])};

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inDensity = 72;

        List<Group> groupsWorkTime = workTime.getGroups();
        List<Group> groupsNotWorkTime = notWorkTime.getGroups();

        drawHeaderAndFooter(canvas[0], canvasWriter[0], res, date);
        int y = 200;

        for (Group groupNotWorkTime : groupsNotWorkTime) {
            if(!verifyGroupSize(groupNotWorkTime, y, false)){
                writePage(document, page[0], canvasWriter[0]);
                y = createNewPage(document, page, pageInfo, canvas, canvasWriter, res, date);
            }
            int rectHeight = y - 20;
            int notWorkTimeLabel = ((MeasurementTypeLocalizedResource)groupNotWorkTime.getLabel()).getMeasurementType();
            drawGroupHeader(canvas[0], canvasWriter[0], measurementKindLocalization, notWorkTimeLabel, y, rectHeight);
            y += 25;

            if (notWorkTimeLabel == Measurement.TYPE_BLOOD_PRESSURE || notWorkTimeLabel == Measurement.TYPE_LUMBAR_EXTENSION_TRAINING) {
                for (Group group : groupNotWorkTime.getGroupList()) {
                    if (!verifyGroupSize(group, y, true)) {
                        drawRect(canvas[0], y + 38, rectHeight);
                        writePage(document, page[0], canvasWriter[0]);
                        y = createNewPage(document, page, pageInfo, canvas, canvasWriter, res, date);
                        rectHeight = y - 20;
                        drawGroupHeader(canvas[0], canvasWriter[0], measurementKindLocalization, notWorkTimeLabel, y, rectHeight);
                        y += 25;
                    }
                    y = drawComplexGroups(canvasWriter[0], group, 0, y);
                }
                for (Group groupWorkTime : groupsWorkTime) {
                    int workTimeLabel = ((MeasurementTypeLocalizedResource) groupNotWorkTime.getLabel()).getMeasurementType();
                    if (notWorkTimeLabel == workTimeLabel) {
                        for (Group group : groupWorkTime.getGroupList()) {
                            if(!verifyGroupSize(groupWorkTime, y, true)){
                                drawRect(canvas[0], y + 38, rectHeight);
                                writePage(document, page[0], canvasWriter[0]);
                                y = createNewPage(document, page, pageInfo, canvas, canvasWriter, res, date);
                                rectHeight = y - 20;
                                drawGroupHeader(canvas[0], canvasWriter[0], measurementKindLocalization, notWorkTimeLabel, y, rectHeight);
                                y += 25;
                            }
                            y = drawComplexGroups(canvasWriter[0], group, 120, y);
                        }
                    }
                }
                y += 38;
            } else {
                boolean hasItem = false;
                for (Group groupWorkTime : groupsWorkTime) {
                    if (notWorkTimeLabel == ((MeasurementTypeLocalizedResource)groupWorkTime.getLabel()).getMeasurementType()) {
                        hasItem = true;
                        y = drawSimpleGroups(canvasWriter[0], groupNotWorkTime, groupWorkTime, y);
                    }
                }
                if (!hasItem) {
                    y = drawSimpleGroups(canvasWriter[0], groupNotWorkTime, null, y);
                }
            }
            drawRect(canvas[0], y, rectHeight);
        }
        for (Group groupWorkTime : groupsWorkTime) {
            boolean hasGroup = false;
            int label = ((MeasurementTypeLocalizedResource)groupWorkTime.getLabel()).getMeasurementType();
            for (Group groupNotWorkTime : groupsNotWorkTime) {
                if (label == ((MeasurementTypeLocalizedResource) groupNotWorkTime.getLabel()).getMeasurementType()) {
                    hasGroup = true;
                    break;
                }
            }
            if (!hasGroup) {
                if(!verifyGroupSize(groupWorkTime, y, false)) {
                    writePage(document, page[0], canvasWriter[0]);
                    y = createNewPage(document, page, pageInfo, canvas, canvasWriter, res, date);
                }
                int rectHeight = y - 20;
                drawGroupHeader(canvas[0], canvasWriter[0], measurementKindLocalization, label, y, rectHeight);
                y += 25;
                if (label == Measurement.TYPE_BLOOD_PRESSURE || label == Measurement.TYPE_LUMBAR_EXTENSION_TRAINING) {
                    for (Group group : groupWorkTime.getGroupList()) {
                        if (!verifyGroupSize(group, y, true)) {
                            drawRect(canvas[0], y, rectHeight);
                            writePage(document, page[0], canvasWriter[0]);
                            y = createNewPage(document, page, pageInfo, canvas, canvasWriter, res, date);
                            rectHeight = y - 20;
                            drawGroupHeader(canvas[0], canvasWriter[0], measurementKindLocalization, label, y, rectHeight);
                            y += 25;
                        }
                        y = drawComplexGroups(canvasWriter[0], group, 120, y);
                    }
                    y += 38;
                }
                else {
                    y = drawSimpleGroups(canvasWriter[0], null, groupWorkTime, y);
                }
                drawRect(canvas[0], y, rectHeight);
            }
        }

        writePage(document, page[0], canvasWriter[0]);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            document.writeTo(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        byte[] outByteArray = out.toByteArray();
        document.close();
        observerReportPDF.observe(outByteArray);

    }

    private void drawHeaderAndFooter(Canvas canvas, CanvasWriter canvasWriter, Resources res, LocalDate date){
        /* CitizenHub Logo */
        final Drawable citizenHubLogo = ResourcesCompat.getDrawable(res, R.drawable.ic_citizen_hub_logo, null);

        citizenHubLogo.setBounds(0, 0, citizenHubLogo.getIntrinsicWidth(), citizenHubLogo.getIntrinsicHeight());
        canvas.save();
        canvas.translate(60, 40);
        canvas.scale(1.0f, 1.0f);
        citizenHubLogo.draw(canvas);
        canvas.restore();

        final Drawable citizenHub = ResourcesCompat.getDrawable(res, R.drawable.logo_citizen_hub_text_only, null);

        citizenHub.setBounds(0, 0, citizenHub.getIntrinsicWidth(), citizenHub.getIntrinsicHeight());
        canvas.save();
        canvas.translate(100, 50);
        canvas.scale(2f, 2f);
        citizenHub.draw(canvas);
        canvas.restore();

        /* Header */
        canvas.drawRoundRect(50, 110, 550, 155, 10, 10, backgroundPaint); // 80
        canvasWriter.addText(res.getString(R.string.report_complete_report), 60, 138, titlePaint);
        canvasWriter.addText(date.toString(), 445, 138, titlePaint);

        /* Footer */
        StaticLayout textLayout = new StaticLayout(res.getString(R.string.report_footer), footerPaint, 480, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.translate(60, 805);
        textLayout.draw(canvas);
        canvas.translate(-60, -805);
    }

    /*private void drawCitizenHub (Canvas canvas, Resources res){
        canvas.drawRect(0, 0, 595, 70, logoBackgroundPaint);

        citizenHubLogo.setBounds(0, 0, citizenHubLogo.getIntrinsicWidth(), citizenHubLogo.getIntrinsicHeight());
        canvas.save();
        canvas.translate(60, 40);
        canvas.scale(1.0f, 1.0f);
        citizenHubLogo.draw(canvas);
        canvas.restore();

        final Drawable citizenHub = ResourcesCompat.getDrawable(resources, R.drawable.logo_citizen_hub_text_only, null);

        citizenHub.setBounds(0, 0, citizenHub.getIntrinsicWidth(), citizenHub.getIntrinsicHeight());
        canvas.save();
        canvas.translate(100, 50);
        canvas.scale(2f, 2f);
        citizenHub.draw(canvas);
        canvas.restore();
    }

    private void drawHeader(Canvas canvas, CanvasWriter canvasWriter, Resources res, LocalDate date){
        canvas.drawRoundRect(50, 80, 550, 125, 10, 10, backgroundPaint);
        canvasWriter.addText(res.getString(R.string.report_complete_report), 60, 108, titlePaint);
        canvasWriter.addText(date.toString(), 445, 108, titlePaint);
    }

    private void drawFooter(Canvas canvas, CanvasWriter canvasWriter, Resources res){
        canvas.drawRect(0, 820,595, 842, logoBackgroundPaint);
        canvasWriter.addText("Escreve aqui qualquer coisa", 60, 835, whiteTextPaint);
    }*/

    private void drawGroupHeader(Canvas canvas, CanvasWriter canvasWriter, MeasurementKindLocalization measurementKindLocalization, int label, int y, int rectHeight){
        Path path = new Path();
        path.addRoundRect(new RectF(50, rectHeight, 550, rectHeight + 25), corners, Path.Direction.CW);
        canvas.drawPath(path, rectFillPaint);
        canvasWriter.addText(measurementKindLocalization.localize(label), 70, y - 4, whiteTextPaint);
        canvasWriter.addText("MyTime", 380, y - 4, whiteItalicTextPaint);
        canvasWriter.addText("MyWork", 500, y - 4, whiteItalicTextPaint);
    }

    private void drawRect(Canvas canvas, int y, int rectHeight){
        RectF rectAround = new RectF(50, rectHeight, 550, y - 50);
        canvas.drawRoundRect(rectAround, 12, 12, rectPaint);
    }

    private boolean verifyGroupSize(Group group, int y, boolean complex){
        y += 25;
        if(group.getGroupList().size() == 0) {
            if (complex){
                y += 20 + 20 * group.getItemList().size() + 5 +  38;
                return y < 842;
            }
            y += 20 * group.getItemList().size() + 5;
            return y < 842;
        }
        y += 20 + 20 * group.getItemList().size() + 5 + 38;
        return y < 842;
    }

    private int drawSimpleGroups(CanvasWriter canvasWriter, Group firstGroup, Group secondGroup, int y){
        if(firstGroup != null & secondGroup != null){
            for (Item itemNotWorkTime : firstGroup.getItemList()) {
                for (Item itemWorkTime : secondGroup.getItemList()) {
                    if (itemNotWorkTime.getLabel().getLocalizedString().equals(itemWorkTime.getLabel().getLocalizedString())) {
                        canvasWriter.addText(itemNotWorkTime.getLabel().getLocalizedString(), 90, y, darkTextPaintAlignLeft);
                        canvasWriter.addText(itemNotWorkTime.getValue().getLocalizedString(), 350, y, darkTextPaintAlignRight);
                        if(!itemNotWorkTime.getUnits().getLocalizedString().equals("-")){
                            canvasWriter.addText(itemNotWorkTime.getUnits().getLocalizedString(), 360, y, darkItalicTextPaint);
                        }
                        canvasWriter.addText(itemWorkTime.getValue().getLocalizedString(), 470, y, darkTextPaintAlignRight);
                        if(!itemWorkTime.getUnits().getLocalizedString().equals("-")){
                            canvasWriter.addText(itemNotWorkTime.getUnits().getLocalizedString(), 480, y, darkItalicTextPaint);
                        }
                        y += 20;
                        break;
                    }
                }
            }
        } else {
            if (firstGroup != null) {
                for (Item item : firstGroup.getItemList()) {
                    canvasWriter.addText(item.getLabel().getLocalizedString(), 90, y, darkTextPaintAlignLeft);
                    canvasWriter.addText(item.getValue().getLocalizedString(), 350, y, darkTextPaintAlignRight);
                    if (!item.getUnits().getLocalizedString().equals("-")) {
                        canvasWriter.addText(" " + item.getUnits().getLocalizedString(), 360, y, darkItalicTextPaint);
                    }
                    canvasWriter.addText("-", 470, y, darkTextPaintAlignRight);
                    y += 20;
                }
            } else {
                if (secondGroup != null) {
                    for (Item item : secondGroup.getItemList()) {
                        canvasWriter.addText(item.getLabel().getLocalizedString(), 90, y, darkTextPaintAlignLeft);
                        canvasWriter.addText("-", 350, y, darkTextPaintAlignRight);
                        canvasWriter.addText(item.getValue().getLocalizedString(), 470, y, darkTextPaintAlignRight);
                        if (!item.getUnits().getLocalizedString().equals("-")) {
                            canvasWriter.addText(" " + item.getUnits().getLocalizedString(), 480, y, darkItalicTextPaint);
                        }
                        y += 20;
                    }
                }
            }
        }
        y += 43;
        return y;
    }

    private int drawComplexGroups(CanvasWriter canvasWriter, Group group, int x, int y){
        String timestamp = group.getLabel().getLocalizedString();
        canvasWriter.addText(timestamp.substring(timestamp.indexOf("T") + 1, timestamp.indexOf("Z")), 75, y, darkTextPaintAlignLeft);
        y += 20;
        for (Item item : group.getItemList()) {
            canvasWriter.addText(item.getLabel().getLocalizedString(), 90, y, darkTextPaintAlignLeft);
            canvasWriter.addText(item.getValue().getLocalizedString(), x + 350, y, darkTextPaintAlignRight);
            if(!item.getUnits().getLocalizedString().equals("-"))
                canvasWriter.addText(item.getUnits().getLocalizedString(), x + 360, y, darkItalicTextPaint);
            canvasWriter.addText("-", 470 - x, y, darkTextPaintAlignRight);
            y += 20;
        }
        y += 5;
        return y;
    }

    private int createNewPage(PdfDocument document, PdfDocument.Page[] page, PdfDocument.PageInfo pageInfo, Canvas[] canvas, CanvasWriter[] canvasWriter, Resources res, LocalDate date){
        page[0] = document.startPage(pageInfo);
        canvas[0] = page[0].getCanvas();
        canvasWriter[0] = new CanvasWriter(canvas[0]);
        drawHeaderAndFooter(canvas[0], canvasWriter[0], res, date);
        return 200;
    }

    private void writePage(PdfDocument document, PdfDocument.Page page, CanvasWriter canvasWriter){
        canvasWriter.draw();
        document.finishPage(page);
    }
}