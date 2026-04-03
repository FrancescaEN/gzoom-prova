package it.mapsgroup.gzoom.quartz.scheduler.dto;

import it.mapsgroup.gzoom.mybatis.dto.QrtzJobDetails;

import java.time.Instant;
import java.util.Map;

public class InfoBase {
    private QrtzJobDetails jobDetails;
    private Map<String, Object> callbackObject;
    private Instant startDate;
    private Instant endDate;
    private String frequency;
    private String cronExpression;

    public QrtzJobDetails getJobDetails() {
        return jobDetails;
    }

    public void setJobDetails(QrtzJobDetails jobDetails) {
        this.jobDetails = jobDetails;
    }

    public Map<String, Object> getCallbackObject() {
        return callbackObject;
    }

    public void setCallbackObject(Map<String, Object> callbackObject) {
        this.callbackObject = callbackObject;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}
