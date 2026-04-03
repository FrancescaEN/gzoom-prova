package it.mapsgroup.gzoom.quartz.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class ServiceJobInfo {
    private String key;
    private String name;
    private String className;
    private String description;
    private String cronExpression;
    private boolean required = false;
    private List<ParameterJob> parameters;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<ParameterJob> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterJob> parameters) {
        this.parameters = parameters;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
