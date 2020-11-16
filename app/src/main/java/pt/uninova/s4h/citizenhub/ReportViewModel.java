package pt.uninova.s4h.citizenhub;

import android.app.Application;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
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

    public byte[] createPdf() {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1200, 1000, 1).create();
        Paint myPaint = new Paint();
        Paint titlePaint = new Paint();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(30);

        int y = 50;

        for (MeasurementKind i : detailAggregates.keySet()) {
            MeasurementAggregate agg = detailAggregates.get(i);

            canvas.drawText(i.name() + " Sum: " + agg.getSum() + "; Avg: " + agg.getAverage() + "; Min: " + agg.getMin() + "; Max: " + agg.getMax() + "; Count: " + agg.getCount(), 200, y, titlePaint);
            y += 50;
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
                "Daily Report " + now.toString(),
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
