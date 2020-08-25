package pt.uninova.s4h.citizenhub.persistence;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ReportDetailViewModel extends AndroidViewModel {

    private final LiveData<DailySummary> reportDetailSummary;

    public ReportDetailViewModel(Application application, int year, int month, int day) {
        super(application);
        final ReportDetailRepository reportDetailRepository = new ReportDetailRepository(application,year,month,day);
        reportDetailSummary = reportDetailRepository.getDailySummary(year,month,day);
    }

    public LiveData<DailySummary> getReportDetailSummary() {
        return reportDetailSummary;
    }
}
