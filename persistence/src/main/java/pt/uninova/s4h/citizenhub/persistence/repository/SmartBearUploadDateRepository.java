package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.SmartBearUploadDateDao;
import pt.uninova.s4h.citizenhub.persistence.entity.SmartBearUploadDateRecord;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class SmartBearUploadDateRepository {

    private final SmartBearUploadDateDao dao;

    public SmartBearUploadDateRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        dao = citizenHubDatabase.smartBearUploadDateDao();
    }

    public void create(SmartBearUploadDateRecord record) {
        CitizenHubDatabase.executorService().execute(() -> dao.insert(record));
    }

    public void readDaysWithData(Observer<List<LocalDate>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(dao.selectDaysWithValues()));
    }
}