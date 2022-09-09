package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.SettingDao;
import pt.uninova.s4h.citizenhub.persistence.entity.SettingRecord;
import pt.uninova.s4h.citizenhub.util.messaging.Observer;

public class SettingRepository {

    private final SettingDao settingDao;

    public SettingRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        settingDao = citizenHubDatabase.settingDao();
    }

    public void insertOrReplace(String address, String key, String value) {
        CitizenHubDatabase.executorService().execute(() -> settingDao.insert(address, key, value));
    }

    public void read(String address, Observer<List<SettingRecord>> observer) {
        CitizenHubDatabase.executorService().execute(() -> observer.observe(settingDao.select(address)));
    }

    public void read(String address, String name, Observer<String> observer) {
        CitizenHubDatabase.executorService().execute(() ->  {
            String val = settingDao.selectValue(address, name);
            observer.observe(val);
        });
    }
}