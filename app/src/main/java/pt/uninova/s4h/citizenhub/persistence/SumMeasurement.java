package pt.uninova.s4h.citizenhub.persistence;

import androidx.room.DatabaseView;

import java.util.Date;


@DatabaseView("SELECT SUM(value) AS AverageValue , date(timestamp) AS Date, name as CharacteristicName FROM measurement GROUP BY Date, CharacteristicName")
public class SumMeasurement {
    public float SumValue;
    public Date date;
    public String characteristicName;
}

