package pt.uninova.s4h.citizenhub.datastorage.DatabaseViews;

import androidx.room.DatabaseView;

import java.util.Date;

@DatabaseView("SELECT value, timestamp FROM measurements WHERE name = 'Steps' ORDER BY timestamp ASC")
public class StepsView {
    public float value;
    public Date timestamp;
}


