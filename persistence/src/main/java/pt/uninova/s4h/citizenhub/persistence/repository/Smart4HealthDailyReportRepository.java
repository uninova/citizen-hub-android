package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.Smart4HealthDailyReportDao;
import pt.uninova.s4h.citizenhub.persistence.entity.Smart4HealthDailyReportRecord;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class Smart4HealthDailyReportRepository {

    private final Smart4HealthDailyReportDao dao;

    public Smart4HealthDailyReportRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        dao = citizenHubDatabase.smart4HealthDailyReportDao();
    }

    public void create(Smart4HealthDailyReportRecord record) {
        CitizenHubDatabase.executorService().execute(() -> dao.insert(record));
    }

    public void create(Smart4HealthDailyReportRecord record, Observer<Long> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(dao.insert(record)));
    }

    public void createOrUpdateFhir(LocalDate date, Boolean value) {
        CitizenHubDatabase.executorService().execute(() -> dao.insertOrReplaceFhir(date, value));
    }

    public void createOrUpdatePdf(LocalDate date, Boolean value) {
        CitizenHubDatabase.executorService().execute(() -> dao.insertOrReplacePdf(date, value));
    }

    public void createOrUpdatePdfUTC(LocalDate date, Boolean value) {
        CitizenHubDatabase.executorService().execute(() -> dao.insertOrReplacePdf(date.toEpochDay() * 86400000, value));
    }

    public void readDaysWithData(Observer<List<LocalDate>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(dao.selectDaysWithValues()));
    }
}
