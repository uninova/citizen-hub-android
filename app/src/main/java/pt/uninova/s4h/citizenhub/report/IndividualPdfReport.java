package pt.uninova.s4h.citizenhub.report;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;

import androidx.core.content.res.ResourcesCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import pt.uninova.s4h.citizenhub.R;

public class IndividualPdfReport {

    private final byte[] bytes;

    public IndividualPdfReport(Context context, Report report) {
        final Resources resources = context.getResources();

        final PdfDocument document = new PdfDocument();

        final PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        final PdfDocument.Page page = document.startPage(pageInfo);

        final Canvas canvas = page.getCanvas();
        canvas.setDensity(72);

        final CanvasWriter canvasWriter = new CanvasWriter(canvas);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inDensity = 72;

        final Paint darkTextPaintAlignLeft = new Paint();
        darkTextPaintAlignLeft.setColor(Color.parseColor("#000000"));
        darkTextPaintAlignLeft.setTextAlign(Paint.Align.LEFT);
        darkTextPaintAlignLeft.setTypeface(Typeface.DEFAULT);
        darkTextPaintAlignLeft.setTextSize(12);

        final Paint darkTextPaintAlignRight = new Paint();
        darkTextPaintAlignRight.setColor(Color.parseColor("#000000"));
        darkTextPaintAlignRight.setTextAlign(Paint.Align.RIGHT);
        darkTextPaintAlignRight.setTypeface(Typeface.DEFAULT);
        darkTextPaintAlignRight.setTextSize(12);

        final Paint darkItalicTextPaint = new Paint();
        darkItalicTextPaint.setColor(Color.parseColor("#000000"));
        darkItalicTextPaint.setTextAlign(Paint.Align.LEFT);
        darkItalicTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        darkItalicTextPaint.setTextSize(12);


        final float[] corners = new float[]{
                12, 12,        // Top left radius in px
                12, 12,        // Top right radius in px
                0, 0,          // Bottom right radius in px
                0, 0           // Bottom left radius in px
        };

        drawLogo(canvas, resources);

        int x = 50;
        int y = 110;

        final Paint backgroundPaint = new Paint();
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(Color.parseColor("#2789C2"));
        backgroundPaint.setAntiAlias(true);

        canvas.drawRoundRect(x, y, 550, y + 60, 10, 10, backgroundPaint);

        final Paint titlePaint = new Paint();
        titlePaint.setColor(Color.parseColor("#FFFFFF"));
        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        titlePaint.setTextSize(18);

        final Paint subtitlePaint = new Paint();
        subtitlePaint.setColor(Color.parseColor("#FFFFFF"));
        subtitlePaint.setTextAlign(Paint.Align.LEFT);
        subtitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        subtitlePaint.setTextSize(12);

        final Paint rectFillPaint = new Paint();
        rectFillPaint.setStyle(Paint.Style.FILL);
        rectFillPaint.setStrokeWidth(2);
        rectFillPaint.setColor(Color.parseColor("#06344F"));

        final Paint whiteTextPaint = new Paint();
        whiteTextPaint.setColor(Color.parseColor("#ffffff"));
        whiteTextPaint.setTextAlign(Paint.Align.LEFT);
        whiteTextPaint.setTypeface(Typeface.DEFAULT);
        whiteTextPaint.setTextSize(12);
        final Paint whiteItalicTextPaint = new Paint();
        whiteItalicTextPaint.setColor(Color.parseColor("#ffffff"));
        whiteItalicTextPaint.setTextAlign(Paint.Align.RIGHT);
        whiteItalicTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
        whiteItalicTextPaint.setTextSize(12);

        canvasWriter.addText(report.getTitle().getLocalizedString(), x + 10, y = y + 28, titlePaint);
        canvasWriter.addText(report.getDate().getLocalizedString(), x + 10, y = y + 20, subtitlePaint);

        y += 40;

        final Group inner = report.getGroups().get(0);
        final Group timestamp = inner.getGroupList().get(0);

        for (Item item : timestamp.getItemList()) {
            canvasWriter.addText(item.getLabel().getLocalizedString(), x + 40, y, darkTextPaintAlignLeft);
            canvasWriter.addText(item.getValue().getLocalizedString(), x + 420, y, darkTextPaintAlignRight);

            if (!item.getUnits().getLocalizedString().equals("-"))
                canvasWriter.addText(item.getUnits().getLocalizedString(), x + 430, y, darkItalicTextPaint);

            y += 20;
        }

        y += 5;

        canvasWriter.draw();

        document.finishPage(page);

        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            document.writeTo(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        bytes = out.toByteArray();
    }

    private void drawLogo(Canvas canvas, Resources resources) {
        final Drawable citizenHubLogo = ResourcesCompat.getDrawable(resources, R.drawable.ic_citizen_hub_logo, null);

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

    public byte[] getBytes() {
        return bytes;
    }

}
