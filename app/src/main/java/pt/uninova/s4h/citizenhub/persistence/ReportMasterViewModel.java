package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ReportMasterViewModel extends AndroidViewModel {

    private final LiveData<List<Integer>> reportMasterSummary;

    public ReportMasterViewModel(Application application, int year, int month) {
        super(application);
        final ReportMasterRepository reportMasterRepository = new ReportMasterRepository(application);
        reportMasterSummary = reportMasterRepository.getReportMasterSummary(year,month);
    }

    public LiveData<List<Integer>> getReportMasterSummary() {
        return reportMasterSummary;
    }
}
