package pt.uninova.s4h.citizenhub.datastorage;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import pt.uninova.s4h.citizenhub.datastorage.DatabaseViews.HeartRateView;
import pt.uninova.s4h.citizenhub.datastorage.DatabaseViews.StepsView;

@Database(entities = {Device.class, Source.class, Measurement.class}, views = {HeartRateView.class, StepsView.class}, version = 2)
@TypeConverters({Converters.class})

public abstract class CitizenDatabase extends RoomDatabase {

    public abstract DeviceDAO deviceDao();

    public abstract SourceDAO sourceDAO();

    public abstract MeasurementDAO measurementDAO();

}
