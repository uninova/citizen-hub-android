package pt.uninova.s4h.citizenhub.datastorage;

import androidx.room.DatabaseView;

import java.util.Date;

@DatabaseView("SELECT value, timestamp FROM measurements WHERE name = 'HeartRate' ORDER BY timestamp ASC")
public class AvgMeasurementView {
    public float value;
    public Date timestamp;
}
