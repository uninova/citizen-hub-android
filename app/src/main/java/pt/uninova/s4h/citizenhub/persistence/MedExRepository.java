package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MedExRepository {

    private final MedExDao medExDao;

    private final Map<LocalDate, LiveData<Map<MeasurementKind, MeasurementAggregate>>> dailyAggregateMap;

    public MedExRepository(Application application) {
        final CitizenHubDatabase citizenHubDatabase = CitizenHubDatabase.getInstance(application);

        medExDao = citizenHubDatabase.medExDao();
        dailyAggregateMap = new HashMap<>();
    }

}