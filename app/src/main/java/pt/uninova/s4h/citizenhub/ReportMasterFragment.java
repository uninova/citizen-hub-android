package pt.uninova.s4h.citizenhub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import org.jetbrains.annotations.NotNull;
import pt.uninova.util.time.LocalDateInterval;

import java.time.LocalDate;
import java.util.Set;

public class ReportMasterFragment extends Fragment {

    private MaterialCalendarView calendarView;
    private ReportViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_master, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendarView = view.findViewById(R.id.fragment_report_calendar_view);
        calendarView.setOnMonthChangedListener(this::onMonthChanged);

        model = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);
        DotDecorator reportDecorator = new DotDecorator(ContextCompat.getColor(requireActivity(), R.color.colorS4HDarkBlue), model.getAvailableReportDates().getValue());

        calendarView.addDecorator(reportDecorator);

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            model.setDetailDate(LocalDate.of(date.getYear(), date.getMonth(), date.getDay()));
            Navigation.findNavController(getView()).navigate(ReportMasterFragmentDirections.actionReportMasterFragmentToReportDetailFragment());
        });

        model.getAvailableReportDates().observe(getViewLifecycleOwner(), this::onNewMonth);
        model.getAvailableReportDateBoundaries().observe(getViewLifecycleOwner(), this::onAvailableReportDateBoundaries);
    }

    private void onAvailableReportDateBoundaries(LocalDateInterval boundaries) {
        final LocalDate lower = boundaries.getLower();
        final LocalDate upper = boundaries.getUpper();

        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(lower.getYear(), lower.getMonthValue(),1))
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