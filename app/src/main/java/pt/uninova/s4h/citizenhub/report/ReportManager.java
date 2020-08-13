package pt.uninova.s4h.citizenhub.report;

import android.content.res.Resources;

import java.util.LinkedList;
import java.util.List;

public class ReportManager implements AutoCloseable {

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

    @Override
    public void close() {
        reports.clear();
    }

    public void writeReport(Report report, Resources res, int logo) {
        // T writer = (T) T.createInstance(pathName);
        PdfWriter pdfWriter = new PdfWriter(basePath);
        pdfWriter.write(report, res, logo);
    }

    public void writeAllReports() {
        final PdfWriter pdfWriter = new PdfWriter(basePath);

        for (Report rep : reports) {
            while (!pdfWriter.writeAll(rep)) {
            }
        }
        reports.clear();
    }
}
