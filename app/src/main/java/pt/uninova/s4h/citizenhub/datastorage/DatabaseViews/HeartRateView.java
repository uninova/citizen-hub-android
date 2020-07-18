package pt.uninova.s4h.citizenhub.datastorage.DatabaseViews;

import androidx.room.DatabaseView;

import java.util.Date;

@DatabaseView("SELECT value, timestamp FROM measurements WHERE name = 'HeartRate' ORDER BY timestamp ASC")
public class HeartRateView {
    public float value;
    public Date timestamp;
}
