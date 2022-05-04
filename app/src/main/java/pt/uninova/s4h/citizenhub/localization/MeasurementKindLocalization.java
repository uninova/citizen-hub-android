package pt.uninova.s4h.citizenhub.localization;

import android.content.Context;
import android.content.res.Resources;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pt.uninova.s4h.citizenhub.CitizenHubApplication;
import pt.uninova.s4h.citizenhub.R;
import pt.uninova.s4h.citizenhub.persistence.Measurement;

public class MeasurementKindLocalization {

    private final Context context;

    public MeasurementKindLocalization(Context context) {
        this.context = context;
    }

    public String localize(int measurementKind) {
        Resources resources = context.getResources();
        System.out.println(String.format(Locale.getDefault(), "measurement_type.%d", measurementKind));

        final int resourceId = resources.getIdentifier(String.format(Locale.getDefault(), "measurement_type.%d", measurementKind), "string", context.getPackageName());

        return resourceId != 0 ? resources.getString(resourceId) : "";
    }
}
