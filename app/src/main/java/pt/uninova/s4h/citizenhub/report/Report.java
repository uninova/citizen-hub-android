package pt.uninova.s4h.citizenhub.report;

import java.time.LocalDateTime;
import java.util.List;

public class Report {

    private final String title;
    private final LocalDateTime date;
    private List<Group> dailyInfo;

    public Report(String title, LocalDateTime date){
        this.title = title;
        this.date = date;
    }

    public boolean fillInfo(){
        boolean infoFilled = false;
        // fazer a query par obter os dados do dia
        // ciclo para percorrer cada valor da query e adicionar aos grupos ou items
        Group info = new Group();
        infoFilled = info.fillInfo();
        dailyInfo.add(info);
        return infoFilled;
    }

    /***************************************
     * This section only has get functions *
     ***************************************/
    public String getTitle(){
        return title;
    }

    public LocalDateTime getDate(){
        return date;
    }

    public List<Group> getDailyInfo() {
        return dailyInfo;
    }

}
