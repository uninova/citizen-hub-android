package pt.uninova.s4h.citizenhub.datastorage;

import androidx.room.DatabaseView;

import java.util.Date;

@DatabaseView("SELECT AVG(value) AS averageValue FROM measurements WHERE timestamp BETWEEN date() AND date()")
public class AvgMeasurementView {
    public String averageValue;
    public Date from;
    public Date to;
}
