package pt.uninova.s4h.citizenhub;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import pt.uninova.s4h.citizenhub.persistence.DailySummaryRepository;
import pt.uninova.util.Pair;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReportMasterViewModel extends AndroidViewModel {

    final private DailySummaryRepository repository;

    final private MutableLiveData<Set<LocalDate>> availableReportsLive;
    final private MediatorLiveData<Pair<LocalDate, LocalDate>> availableReportDateBoundariesLive;

    final private Set<Pair<Integer, Integer>> peekedMonths;


    public ReportMasterViewModel(Application application) {
        super(application);

        repository = new DailySummaryRepository(application);

        availableReportsLive = new MutableLiveData<>();
        availableReportDateBoundariesLive = new MediatorLiveData<>();

        availableReportDateBoundariesLive.addSource(repository.getLatestAvailableReportDateLive(), this::onLatestAvailableReportDate);
        repository.obtainEarliestAvailableReportDate(this::onEarliestAvailableReportDate);

        peekedMonths = new HashSet<>();

        peek();
    }

    public LiveData<Pair<LocalDate, LocalDate>> getAvailableReportDateBoundaries() {
        return availableReportDateBoundariesLive;
    }

    public LiveData<Set<LocalDate>> getAvailableReportDates() {
        return availableReportsLive;
    }

    private void onAvailableReportDates(List<LocalDate> dates) {
        System.out.println("onResult:" + dates.toString());

        if (dates.size() > 0) {
            Set<LocalDate> localDates = availableReportsLive.getValue();

            if (localDates == null) {
                localDates = new HashSet<>(dates.size());
            }

            localDates.addAll(dates);

            availableReportsLive.postValue(localDates);
        }
    }

    private void onEarliestAvailableReportDate(LocalDate localDate) {
        final Pair<LocalDate, LocalDate> old = availableReportDateBoundariesLive.getValue();

        availableReportDateBoundariesLive.postValue(new Pair<>(localDate, old == null ? LocalDate.now() : old.getSecond()));
    }

    private void onLatestAvailableReportDate(LocalDate localDate) {
        Pair<LocalDate, LocalDate> value = availableReportDateBoundariesLive.getValue();

        if (value == null) {
            availableReportDateBoundariesLive.postValue(new Pair<>(LocalDate.now(), localDate));
        } else if (value.getSecond().toEpochDay() < localDate.toEpochDay()) {
            availableReportDateBoundariesLive.postValue(new Pair<>(value.getFirst(), localDate));
        }
    }

    public void peek() {
        final LocalDate now = LocalDate.now();

        peek(now.getYear(), now.getMonthValue());
    }

    public void peek(int year, int month) {
        System.out.println("peek:" + year + ":" + month);
        Pair<Integer, Integer> peek = new Pair<>(year, month);

        if (!peekedMonths.contains(peek)) {
            peekedMonths.add(peek);
            repository.obtainAvailableReportDates(peek, this::onAvailableReportDates);
        }
    }
}
