package pt.uninova.s4h.citizenhub.ui.summary;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class ChartValueFormatter extends ValueFormatter {

    private final DecimalFormat mFormat = new DecimalFormat("###,###,###");

    public String getFormattedValue(float value) {
         if(value <= -1){
            return "";
        }
        return mFormat.format(value);
    }

}
