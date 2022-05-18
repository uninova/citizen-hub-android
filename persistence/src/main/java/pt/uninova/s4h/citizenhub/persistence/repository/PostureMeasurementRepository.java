package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.PostureMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.entity.PostureMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.PostureClassificationSum;

public class PostureMeasurementRepository {

    private final PostureMeasurementDao postureMeasurementDao;

    public PostureMeasurementRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        postureMeasurementDao = citizenHubDatabase.postureMeasurementDao();
    }

    public void create(PostureMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> postureMeasurementDao.insert(record));
    }

    public LiveData<List<PostureMeasurementRecord>> get(LocalDate localDate) {
        return postureMeasurementDao.get(localDate, localDate.plusDays(1));
    }

    public LiveData<List<PostureClassificationSum>> getClassificationSum(LocalDate localDate) {
        return postureMeasurementDao.getSum(localDate, localDate.plusDays(1));
    }
}
