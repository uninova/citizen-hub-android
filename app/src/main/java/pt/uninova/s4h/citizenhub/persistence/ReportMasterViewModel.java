package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ReportMasterViewModel extends AndroidViewModel {

    private final ReportMasterRepository reportMasterRepository;

    public ReportMasterViewModel(Application application) {
        super(application);

        reportMasterRepository = new ReportMasterRepository(application);
    }

    public LiveData<List<Integer>> getReportMasterSummary(int year, int month) {
        return reportMasterRepository.getReportMasterSummary(year, month);
    }
}
