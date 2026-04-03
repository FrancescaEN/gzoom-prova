package it.mapsgroup.gzoom.quartz.scheduler.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class ServiceJob {
    private List<ServiceJobInfo> services;

    public ServiceJob() {}

    public List<ServiceJobInfo> getServices() {
        return services;
    }

    public void setServices(List<ServiceJobInfo> services) {
        this.services = services;
    }

    public List<ServiceJobInfo> getServicesRequired () {
        return  services.stream().filter(ServiceJobInfo::isRequired).collect(Collectors.toList());
    }
}
