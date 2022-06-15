package pt.uninova.s4h.citizenhub;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class DotDecorator implements DayViewDecorator {

    private final int color;
    private Set<LocalDate> localDates;

    public DotDecorator(int color) {
        this(color, new HashSet<>());
    }

    public DotDecorator(int color, Set<LocalDate> localDates) {
        this.color = color;
        this.localDates = localDates;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(10, color));
    }

    public Set<LocalDate> getLocalDates() {
        return localDates;
    }

    public void setLocalDates(Set<LocalDate> localDates) {
        this.localDates = localDates;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return localDates.contains(LocalDate.of(day.getYear(), day.getMonth(), day.getDay()));
    }

}
