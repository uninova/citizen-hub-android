package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import pt.uninova.util.Pair;

import java.time.LocalDate;
import java.util.Set;

public class ReportMasterFragment extends Fragment {

    private MaterialCalendarView calendarView;
    private ReportMasterViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_report, container, false);

        calendarView = view.findViewById(R.id.fragment_report_calendar_view);
        calendarView.setOnMonthChangedListener(this::onMonthChanged);

        model = new ViewModelProvider(requireActivity()).get(ReportMasterViewModel.class);
        DotDecorator reportDecorator = new DotDecorator(ContextCompat.getColor(requireActivity(), R.color.colorS4HDarkBlue), model.getAvailableReportDates().getValue());

        calendarView.addDecorator(reportDecorator);

        model.getAvailableReportDates().observe(getViewLifecycleOwner(), this::onNewMonth);
        model.getAvailableReportDateBoundaries().observe(getViewLifecycleOwner(), this::onAvailableReportDateBoundaries);

        return view;
    }

    private void onAvailableReportDateBoundaries(Pair<LocalDate, LocalDate> boundaries) {
        final LocalDate lower = boundaries.getFirst();
        final LocalDate upper = boundaries.getSecond();

        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(lower.getYear(), lower.getMonthValue(), lower.getDayOfMonth()))
                .setMaximumDate(CalendarDay.from(upper.getYear(), upper.getMonthValue(), upper.getDayOfMonth()))
                .commit();

        // TODO: KEEP TRACK OF LAST PEEK calendarView.setCurrentDate(CalendarDay.from(2020,3, 10));
    }

    private void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
        model.peek(calendarDay.getYear(), calendarDay.getMonth());
    }

    private void onNewMonth(Set<LocalDate> localDates) {
        calendarView.invalidateDecorators();
    }

}