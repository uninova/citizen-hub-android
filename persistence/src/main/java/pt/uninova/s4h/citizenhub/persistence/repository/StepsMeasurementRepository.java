package pt.uninova.s4h.citizenhub.persistence.repository;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;

import androidx.lifecycle.LiveData;
import pt.uninova.s4h.citizenhub.persistence.CitizenHubDatabase;
import pt.uninova.s4h.citizenhub.persistence.dao.StepsMeasurementDao;
import pt.uninova.s4h.citizenhub.persistence.entity.StepsMeasurementRecord;
import pt.uninova.s4h.citizenhub.persistence.entity.util.WalkingInformation;

public class StepsMeasurementRepository {

    private final StepsMeasurementDao stepsMeasurementDao;

    public StepsMeasurementRepository(Context context) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(context);

        stepsMeasurementDao = citizenHubDatabase.stepsMeasurementDao();
    }

    public void create(StepsMeasurementRecord record) {
        CitizenHubDatabase.executorService().execute(() -> stepsMeasurementDao.insert(record));
    }

    public LiveData<List<StepsMeasurementRecord>> read(LocalDate localDate) {
        return stepsMeasurementDao.selectLiveData(localDate, localDate.plusDays(1));
    }

    public LiveData<WalkingInformation> readLatestWalkingInformation(LocalDate localDate) {
        return stepsMeasurementDao.selectLatestWalkingInformationLiveData(localDate, localDate.plusDays(1));
    }

    public LiveData<Integer> getStepsSum (LocalDate localDate) {
        return stepsMeasurementDao.getStepsSum(localDate, localDate.plusDays(1));
    }
}