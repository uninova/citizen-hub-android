package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

public class ReportDetailViewModel extends AndroidViewModel {

    private final ReportDetailRepository reportDetailRepository;
    private final DailySummary reportDetailSummary;

    public ReportDetailViewModel(Application application, int year, int month, int day) {
        super(application);
        reportDetailRepository = new ReportDetailRepository(application, year, month, day);
        reportDetailSummary = reportDetailRepository.getDailySummary();
    }

    public DailySummary getReportDetailSummary() {
        return reportDetailSummary;
    }
}
