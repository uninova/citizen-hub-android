package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ReportMasterViewModel extends AndroidViewModel {

    private final ReportMasterRepository reportMasterRepository;

    public ReportMasterViewModel(Application application, int year, int month) {
        super(application);

        reportMasterRepository = new ReportMasterRepository(application, year, month);
    }

    public LiveData<List<Integer>> getReportMasterSummary() {
        return reportMasterRepository.getReportMasterSummary();
    }
}
