package it.mapsgroup.gzoom.quartz.scheduler.info;


import it.mapsgroup.gzoom.mybatis.dto.UserLogin;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;

/**
 * This class represents the jobData object used for passing data to scheduler jobs.
 * @author Leonardo Minaudo
 */
public class JobData implements Serializable {
    private String cronExpression;
    private Map<String, Object> callbackObject;
    private Instant startDate;
    private Instant endDate;
    private String frequency;

   public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
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

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}