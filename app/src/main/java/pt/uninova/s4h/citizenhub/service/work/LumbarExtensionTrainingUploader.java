package pt.uninova.s4h.citizenhub.service.work;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import care.data4life.fhir.r4.model.Attachment;
import care.data4life.fhir.r4.model.CodeSystemDocumentReferenceStatus;
import care.data4life.fhir.r4.model.CodeableConcept;
import care.data4life.fhir.r4.model.Coding;
import care.data4life.fhir.r4.model.DocumentReference;
import care.data4life.fhir.r4.model.FhirDate;
import care.data4life.fhir.r4.model.FhirDateTime;
import care.data4life.fhir.r4.model.FhirInstant;
import care.data4life.fhir.r4.model.FhirTime;
import care.data4life.fhir.r4.model.Organization;
import care.data4life.sdk.Data4LifeClient;
import care.data4life.sdk.SdkContract.Fhir4RecordClient;
import care.data4life.sdk.call.Callback;
import care.data4life.sdk.call.Fhir4Record;
import care.data4life.sdk.helpers.lang.DataRestrictionException;
import care.data4life.sdk.helpers.r4.AttachmentBuilder;
import care.data4life.sdk.helpers.r4.DocumentReferenceBuilder;
import care.data4life.sdk.helpers.r4.OrganizationBuilder;
import care.data4life.sdk.lang.D4LException;
import pt.uninova.s4h.citizenhub.persistence.repository.LumbarExtensionTrainingRepository;
import pt.uninova.s4h.citizenhub.report.IndividualPdfReport;
import pt.uninova.s4h.citizenhub.report.Report;
import pt.uninova.s4h.citizenhub.report.Smart4HealthReportGenerator;
import pt.uninova.s4h.citizenhub.report.TimestampLocalizedResource;

public class LumbarExtensionTrainingUploader extends ListenableWorker {

    private final long sampleId;

    public LumbarExtensionTrainingUploader(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        sampleId = workerParams.getInputData().getLong("sampleId", -1);
    }

    @NonNull
    @Override
    public ListenableFuture<Result> startWork() {
        final SettableFuture<Result> future = SettableFuture.create();

        final LumbarExtensionTrainingRepository repository = new LumbarExtensionTrainingRepository(getApplicationContext());

        repository.read(sampleId, sample -> {
            if (sample == null) {
                future.set(Result.success());
                return;
            }

            final Smart4HealthReportGenerator generator = new Smart4HealthReportGenerator(getApplicationContext());
            final Report report = generator.createLumbarExtensionTrainingReport(sample);
            final IndividualPdfReport pdfReport = new IndividualPdfReport(getApplicationContext(), report);

            final Fhir4RecordClient client = Data4LifeClient.getInstance().getFhir4();
            final List<Attachment> attachments = new ArrayList<>(1);

            final LocalDateTime now = LocalDateTime.now();

            final FhirDate date = new FhirDate(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
            final FhirTime time = new FhirTime(now.getHour(), now.getMinute(), now.getSecond(), null, null);
            final FhirDateTime dateTime = new FhirDateTime(date, time, TimeZone.getDefault());

            try {
                final Attachment attachment = AttachmentBuilder.buildWith(now.toString(), dateTime, "application/pdf", pdfReport.getBytes());
                attachments.add(attachment);

                final DocumentReference documentReference = DocumentReferenceBuilder.buildWith(
                        report.getTitle().getLocalizedString(),
                        CodeSystemDocumentReferenceStatus.CURRENT,
                        attachments,
                        getGeneralReportCodeableConcept(),
                        getAuthor(),
                        null);

                final Instant instant = ((TimestampLocalizedResource) report.getDate()).getTimestamp();
                final LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

                final FhirDate dt = new FhirDate(localDateTime.getYear(), localDateTime.getMonthValue(), localDateTime.getDayOfMonth());
                final FhirTime tm = new FhirTime(localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond(), null, null);
                final FhirDateTime dttm = new FhirDateTime(dt, tm, TimeZone.getDefault());

                documentReference.date = new FhirInstant(dttm);

                client.create(documentReference, new ArrayList<>(), new Callback<Fhir4Record<DocumentReference>>() {
                    @Override
                    public void onSuccess(Fhir4Record<DocumentReference> fhir4Record) {
                        future.set(Result.success());
                    }

                    @Override
                    public void onError(@NonNull D4LException e) {
                        e.printStackTrace();
                        future.set(Result.failure());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                future.set(Result.failure());
            }
        });

        return future;
    }

    private Organization getAuthor() {
        final Organization organization = OrganizationBuilder.buildWith(
                getOrganizationType(),
                "UNINOVA - Instituto de Desenvolvimento de Novas Tecnologias",
                "Faculdade de Ciências e Tecnologia",
                "2829-516",
                "Caparica",
                "(+351) 21 29 48 527",
                "https://www.uninova.pt/");

        return organization;
    }

    private CodeableConcept getOrganizationType() {
        final Coding organizationType = new Coding();

        organizationType.code = "edu";

        final CodeableConcept codeableConcept = new CodeableConcept();

        codeableConcept.coding = Collections.singletonList(organizationType);

        return codeableConcept;
    }

    private CodeableConcept getGeneralReportCodeableConcept() {
        final Coding coding = new Coding();

        coding.code = "general-report";
        coding.display = "General report";
        coding.system = "http://fhir.smart4health.eu/CodeSystem/s4h-user-doc-type";

        final CodeableConcept concept = new CodeableConcept();

        concept.coding = Collections.singletonList(coding);

        return concept;
    }


}
