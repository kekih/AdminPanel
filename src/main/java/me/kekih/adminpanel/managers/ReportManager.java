package me.kekih.adminpanel.managers;

import me.kekih.adminpanel.models.Report;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class ReportManager {

    private final List<Report> reports = new CopyOnWriteArrayList<>();

    public void addReport(Report report) {
        reports.add(report);
    }

    public List<Report> getReports() {
        return new ArrayList<>(reports);
    }

    public Optional<Report> getReport(UUID id) {
        return reports.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    public boolean removeReport(UUID id) {
        return reports.removeIf(r -> r.getId().equals(id));
    }

    public int getCount() {
        return reports.size();
    }
}
