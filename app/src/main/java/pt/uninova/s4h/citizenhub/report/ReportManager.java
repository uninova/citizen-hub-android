package pt.uninova.s4h.citizenhub.report;

import java.util.LinkedList;
import java.util.List;

//public class ReportManager <T extends PdfWriter> {
public class ReportManager<T extends PdfWriter> {

    private List<Report> reports; //queue
    private String basePath;

    public ReportManager(String pathName) {
        reports = new LinkedList<Report>();
        basePath = pathName;
    }

    public ReportManager() {
        reports = new LinkedList<Report>();
        basePath = "";
    }


    public void writeReport(Report report, String pathName) {
        // T writer = (T) T.createInstance(pathName);
        PdfWriter pdfWriter = new PdfWriter(pathName);
        pdfWriter.write(report);
    }

    public void writeAllReports() {
        final PdfWriter pdfWriter = new PdfWriter(basePath);

        for (Report rep : reports) {
            while (!pdfWriter.write(rep)) {
            }
        }
        reports.clear();
    }
}
