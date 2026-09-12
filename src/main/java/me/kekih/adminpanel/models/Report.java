package me.kekih.adminpanel.models;

import java.util.UUID;

public class Report {

    private final UUID id;
    private final String reporterName;
    private final UUID reporterUuid;
    private final String targetName;
    private final UUID targetUuid;
    private final String reason;
    private final long timestamp;

    public Report(String reporterName, UUID reporterUuid, String targetName, UUID targetUuid, String reason) {
        this.id = UUID.randomUUID();
        this.reporterName = reporterName;
        this.reporterUuid = reporterUuid;
        this.targetName = targetName;
        this.targetUuid = targetUuid;
        this.reason = reason;
        this.timestamp = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public String getReporterName() {
        return reporterName;
    }

    public UUID getReporterUuid() {
        return reporterUuid;
    }

    public String getTargetName() {
        return targetName;
    }

    public UUID getTargetUuid() {
        return targetUuid;
    }

    public String getReason() {
        return reason;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
