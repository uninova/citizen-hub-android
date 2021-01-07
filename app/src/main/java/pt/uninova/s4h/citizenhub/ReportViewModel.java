package pt.uninova.s4h.citizenhub;

import android.app.Application;
import android.graphics.*;
import android.graphics.pdf.PdfDocument;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import care.data4life.fhir.stu3.model.*;
import care.data4life.sdk.Data4LifeClient;
import care.data4life.sdk.config.DataRestrictionException;
import care.data4life.sdk.helpers.AttachmentBuilder;
import care.data4life.sdk.helpers.DocumentReferenceBuilder;
import care.data4life.sdk.helpers.PractitionerBuilder;
import care.data4life.sdk.listener.ResultListener;
import care.data4life.sdk.model.Record;
import pt.uninova.s4h.citizenhub.persistence.MeasurementAggregate;
import pt.uninova.s4h.citizenhub.persistence.MeasurementKind;
import pt.uninova.s4h.citizenhub.persistence.MeasurementRepository;
import pt.uninova.util.Pair;
import pt.uninova.util.messaging.Observer;
import pt.uninova.util.time.LocalDateInterval;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class ReportViewModel extends AndroidViewModel {

    final private MeasurementRepository repository;

    final private MutableLiveData<Set<LocalDate>> availableReportsLive;
    final private MediatorLiveData<LocalDateInterval> dateBoundsLive;

    final private Set<Pair<Integer, Integer>> peekedMonths;

    private LocalDate detailDate;
    private Map<MeasurementKind, MeasurementAggregate> detailAggregates;

    public ReportViewModel(Application application) {
        super(application);

        repository = new MeasurementRepository(application);

        availableReportsLive = new MutableLiveData<>(new HashSet<>());
        dateBoundsLive = new MediatorLiveData<>();

        dateBoundsLive.addSource(repository.getDateBounds(), this::onDateBoundsChanged);

        peekedMonths = new HashSet<>();

        detailDate = LocalDate.now();

        peek();
    }

    static Practitioner getFakePractitioner() {
        return PractitionerBuilder.buildWith(
                "Bruce",
                "Banner",
                "Dr.",
                "MD",
                "Walvisbaai 3",
                "2333ZA",
                "Den helder",
                "+31715269111",
                "www.webpage.com");
    }

    static CodeableConcept getFakePracticeSpeciality() {
        Coding coding = new Coding();
        coding.code = "General Medicine";
        coding.display = "General Medicine";
        coding.system = "http://www.ihe.net/xds/connectathon/practiceSettingCodes";

        CodeableConcept concept = new CodeableConcept();
        concept.coding = Arrays.asList(coding);
        return concept;
    }

    static CodeableConcept getFakeDocumentReferenceType() {
        Coding coding = new Coding();
        coding.code = "34108-1";
        coding.display = "Outpatient Note";
        coding.system = "http://loinc.org";

        CodeableConcept concept = new CodeableConcept();
        concept.coding = Arrays.asList(coding);
        return concept;
    }

    public Bitmap resize(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    public byte[] createPdf() {
        PdfDocument document = new PdfDocument();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.setDensity(72);

        DecimalFormat decimalFormat = new DecimalFormat("#.#");

        Paint titlePaint = new Paint();

        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        titlePaint.setTextSize(24);

        Paint textPaint = new Paint();

        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(Typeface.DEFAULT);
        textPaint.setTextSize(12);

        int y = 50;
        int x = 50;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inDensity = 72;

        Bitmap logo = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.img_s4h_logo_report, options);

        canvas.drawBitmap(logo, x, y, new Paint(Paint.FILTER_BITMAP_FLAG));

        y += 80;

        canvas.drawText(R.string.fragment_report_pdf_title + detailDate.toString(), x, y, titlePaint);
        y += 40;
        x += 20;

        MeasurementAggregate measurementAggregate = detailAggregates.get(MeasurementKind.HEART_RATE);

        if (measurementAggregate != null) {
            canvas.drawText(R.string.fragment_report_pdf_hr_avg + decimalFormat.format(measurementAggregate.getAverage()), x, y, textPaint);
            y += 20;
            canvas.drawText(String.valueOf(R.string.fragment_report_pdf_hr_min + measurementAggregate.getMin()), x, y, textPaint);
            y += 20;
            canvas.drawText(String.valueOf(R.string.fragment_report_pdf_hr_max + measurementAggregate.getMax()), x, y, textPaint);
            y += 20;
        }

        measurementAggregate = detailAggregates.get(MeasurementKind.STEPS);

        if (measurementAggregate != null) {
            canvas.drawText(String.valueOf(R.string.fragment_report_pdf_steps_taken + measurementAggregate.getSum()), x, y, textPaint);
            y += 20;
        }

        measurementAggregate = detailAggregates.get(MeasurementKind.DISTANCE);

        if (measurementAggregate != null) {
            canvas.drawText(String.valueOf(R.string.fragment_report_pdf_distance + measurementAggregate.getSum()), x, y, textPaint);
            y += 20;
        }

        measurementAggregate = detailAggregates.get(MeasurementKind.CALORIES);

        if (measurementAggregate != null) {
            canvas.drawText(String.valueOf(R.string.fragment_report_pdf_calories + measurementAggregate.getSum()), x, y, textPaint);
            y += 20;
        }

        measurementAggregate = detailAggregates.get(MeasurementKind.GOOD_POSTURE);

        if (measurementAggregate != null) {
            canvas.drawText(String.valueOf(R.string.fragment_report_pdf_good_posture + measurementAggregate.getSum()), x, y, textPaint);
        }

        document.finishPage(page);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            document.writeTo(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        document.close();

        return out.toByteArray();
    }

    private void onDateBoundsChanged(LocalDateInterval dateBounds) {
        if (dateBoundsLive.getValue() == null || !dateBoundsLive.getValue().equals(dateBounds)) {
            dateBoundsLive.postValue(dateBounds);
        }
    }

    public LiveData<LocalDateInterval> getAvailableReportDateBoundaries() {
        return dateBoundsLive;
    }

    public LiveData<Set<LocalDate>> getAvailableReportDates() {
        return availableReportsLive;
    }

    public LocalDate getDetailDate() {
        return detailDate;
    }

    public void setDetailDate(LocalDate detailDate) {
        this.detailDate = detailDate;
    }

    public void obtainSummary(Observer<Map<MeasurementKind, MeasurementAggregate>> observer) {
        repository.obtainDailyAggregate(detailDate, value -> {
            detailAggregates = value;

            observer.onChanged(value);
        });
    }

    private void onDatesChanged(List<LocalDate> dates) {
        if (dates.size() > 0) {
            Set<LocalDate> localDates = availableReportsLive.getValue();

            localDates.addAll(dates);

            availableReportsLive.postValue(localDates);
        }
    }

    public void peek() {
        final LocalDate now = LocalDate.now();

        peek(now.getYear(), now.getMonthValue());
    }

    public void peek(int year, int month) {
        Pair<Integer, Integer> peek = new Pair<>(year, month);

        if (!peekedMonths.contains(peek)) {
            peekedMonths.add(peek);
            repository.obtainDates(peek, this::onDatesChanged);
        }
    }

    public void sendDetail(ResultListener<Record<DocumentReference>> resultListener) {
        Data4LifeClient client = Data4LifeClient.getInstance();
        LocalDateTime now = LocalDateTime.now();
        List<Attachment> attachments = new ArrayList<>(1);

        byte[] data = createPdf();
        Attachment attach = null;

        try {
            attach = AttachmentBuilder.buildWith(now.toString(),
                    new FhirDateTime(new FhirDate(now.getYear(), now.getMonthValue(), now.getDayOfMonth()),
                            new FhirTime(now.getHour(), now.getMinute(), now.getSecond(), null, null),
                            TimeZone.getDefault()),
                    "application/pdf",
                    data);
        } catch (DataRestrictionException.UnsupportedFileType | DataRestrictionException.MaxDataSizeViolation unsupportedFileType) {
            unsupportedFileType.printStackTrace();
        }

        attachments.add(attach);

        DocumentReference documentReference = DocumentReferenceBuilder.buildWith(
                R.string.fragment_report_pdf_file_title + detailDate.toString(),
                new FhirInstant(
                        new FhirDateTime(
                                new FhirDate(now.getYear(), now.getMonthValue(), now.getDayOfMonth()),
                                new FhirTime(now.getHour(), now.getMinute(), now.getSecond(), null, null),
                                TimeZone.getDefault())),
                CodeSystems.DocumentReferenceStatus.CURRENT,
                attachments,
                getFakeDocumentReferenceType(),
                getFakePractitioner(),
                getFakePracticeSpeciality()
        );

        client.createRecord(documentReference, resultListener);
    }
}
