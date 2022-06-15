package pt.uninova.s4h.citizenhub.ui.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import pt.uninova.s4h.citizenhub.CurrentDateDecorator;
import pt.uninova.s4h.citizenhub.DotDecorator;
import pt.uninova.s4h.citizenhub.R;

public class ReportMasterFragment extends Fragment {

    private ReportViewModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_report_master, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final MaterialCalendarView calendarView = view.findViewById(R.id.fragment_report_calendar_view);
        final DotDecorator reportDecorator = new DotDecorator(ContextCompat.getColor(requireActivity(), R.color.colorS4HDarkBlue));

        calendarView.addDecorator(reportDecorator);
        calendarView.addDecorator(new CurrentDateDecorator(getContext()));

        calendarView.setTitleFormatter(day -> {
            final LocalDate localDate = day.getDate();
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault());

            return localDate.format(formatter);
        });

        model.getDateSet().observe(getViewLifecycleOwner(), localDates -> {
            reportDecorator.setLocalDates(localDates);
            calendarView.invalidateDecorators();
        });

        calendarView.setOnMonthChangedListener((materialCalendarView, calendarDay) -> {
            model.setMonthView(calendarDay.getYear(), calendarDay.getMonth());
        });

        calendarView.setOnDateChangedListener((materialCalendarView, calendarDay, b) -> {
            model.setCurrentDate(LocalDate.of(calendarDay.getYear(), calendarDay.getMonth(), calendarDay.getDay()));

            Navigation.findNavController(requireView()).navigate(pt.uninova.s4h.citizenhub.ui.report.ReportMasterFragmentDirections.actionReportMasterFragmentToReportDetailFragment());
        });
    }
}
