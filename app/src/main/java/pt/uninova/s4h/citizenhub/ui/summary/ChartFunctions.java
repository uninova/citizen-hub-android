package pt.uninova.s4h.citizenhub.ui.summary;

import java.util.ArrayList;
import java.util.List;

import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailBloodPressureUtil;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailHeartRateUtil;
import pt.uninova.s4h.citizenhub.persistence.entity.util.SummaryDetailUtil;

public class ChartFunctions {

    public static List<SummaryDetailUtil> parseBloodPressureUtil(List<SummaryDetailBloodPressureUtil> bloodPressureUtils){
        List<SummaryDetailUtil> utils = new ArrayList<>();
        for (SummaryDetailBloodPressureUtil data : bloodPressureUtils){
            SummaryDetailUtil util = new SummaryDetailUtil();
            util.setValue1(data.getSystolic());
            util.setValue2(data.getDiastolic());
            util.setValue3(data.getMean());
            util.setTime(data.getTime());
            utils.add(util);
        }
        return utils;
    }

    public static List<SummaryDetailUtil> parseHeartRateUtil(List<SummaryDetailHeartRateUtil> heartRateUtils){
        List<SummaryDetailUtil> utils = new ArrayList<>();
        for (SummaryDetailHeartRateUtil data : heartRateUtils){
            SummaryDetailUtil util = new SummaryDetailUtil();
            util.setValue1(data.getAverage());
            util.setValue2(data.getMaximum());
            util.setValue3(data.getMinimum());
            util.setTime(data.getTime());
            utils.add(util);
        }
        return utils;
    }

}
