package it.mapsgroup.gzoom.model;

import java.util.Date;

/**
 * @author Andrea Fossi.
 */
public class NotificationEvent {
    private String id;
    private String description;
    private String code;
    private Boolean frequencyAllowed;
    private Boolean notificationPeriodAllowed;
    private Boolean enabled;
    private Date startDate;
    private Integer executionInterval;
    private Integer notificationInterval;
    private Integer ordinal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getFrequencyAllowed() {
        return frequencyAllowed;
    }

    public void setFrequencyAllowed(Boolean frequencyAllowed) {
        this.frequencyAllowed = frequencyAllowed;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getExecutionInterval() {
        return executionInterval;
    }

    public void setExecutionInterval(Integer executionInterval) {
        this.executionInterval = executionInterval;
    }

    public Integer getNotificationInterval() {
        return notificationInterval;
    }

    public void setNotificationInterval(Integer notificationInterval) {
        this.notificationInterval = notificationInterval;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public Boolean getNotificationPeriodAllowed() {
        return notificationPeriodAllowed;
    }

    public void setNotificationPeriodAllowed(Boolean notificationPeriodAllowed) {
        this.notificationPeriodAllowed = notificationPeriodAllowed;
    }
}
