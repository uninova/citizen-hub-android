package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.SmartBearDailyReportDao;
import pt.uninova.s4h.citizenhub.persistence.entity.SmartBearDailyReportRecord;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class SmartBearDailyReportRepository {

    private final SmartBearDailyReportDao dao;

    public SmartBearDailyReportRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        dao = citizenHubDatabase.smartBearUploadDateDao();
    }

    public void create(SmartBearDailyReportRecord record) {
        CitizenHubDatabase.executorService().execute(() -> dao.insert(record));
    }

    public void readDaysWithData(Observer<List<LocalDate>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(dao.selectDaysWithValues()));
    }
}