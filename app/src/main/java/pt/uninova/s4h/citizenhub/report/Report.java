package pt.uninova.s4h.citizenhub.report;

import android.content.Context;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import pt.uninova.s4h.citizenhub.data.Measurement;
import pt.uninova.s4h.citizenhub.persistence.entity.util.ReportUtil;
import pt.uninova.s4h.citizenhub.persistence.repository.ReportRepository;

public class Report {

    private final String title;
    private final LocalDate date;
    private final List<Group> groups;

    public Report(String title, LocalDate date){
        this.title = title;
        this.date = date;
        this.groups = new LinkedList<>();
    }

    /***************************************
     * This section only has get functions *
     ***************************************/
    public String getTitle(){
        return title;
    }

    public LocalDate getDate(){
        return date;
    }

    public List<Group> getGroups() {
        return groups;
    }

}
