package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class ReportMasterViewModel extends AndroidViewModel {

    private final ReportMasterRepository reportMasterRepository;
    private final List<Integer> reportMasterSummary;

    public ReportMasterViewModel(Application application, int year, int month) {
        super(application);

        reportMasterRepository = new ReportMasterRepository(application, year, month);
        reportMasterSummary = reportMasterRepository.getReportMasterSummary();
    }

    public List<Integer> getReportMasterSummary() {
        return reportMasterSummary;
    }
}
