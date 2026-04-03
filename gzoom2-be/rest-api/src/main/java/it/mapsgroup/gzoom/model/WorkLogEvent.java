package it.mapsgroup.gzoom.model;

import java.util.*;

public class WorkLogEvent extends Identity {
    private Long id;
    private PersonEmployment personEmployment;
    private EquipmentEmployment equipmentEmployment;
    private Date timestamp;
    private String direction;
    private Wbs wbs;
    private String type;
    private EntityState state;
    private List<String> errors;
    private String origin;
    private Map<String, Object> receivedData;
    private WorkLog workLog;

    public WorkLogEvent() {
        this.errors = new ArrayList<>();
        receivedData = new HashMap<>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public PersonEmployment getPersonEmployment() {
        return personEmployment;
    }

    public void setPersonEmployment(PersonEmployment personEmployment) {
        this.personEmployment = personEmployment;
    }

    public EquipmentEmployment getEquipmentEmployment() {
        return equipmentEmployment;
    }

    public void setEquipmentEmployment(EquipmentEmployment equipmentEmployment) {
        this.equipmentEmployment = equipmentEmployment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Wbs getWbs() {
        return wbs;
    }

    public void setWbs(Wbs wbs) {
        this.wbs = wbs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EntityState getState() {
        return state;
    }

    public void setState(EntityState state) {
        this.state = state;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Map<String, Object> getReceivedData() {
        return receivedData;
    }

    public void setReceivedData(Map<String, Object> receivedData) {
        this.receivedData = receivedData;
    }

    public WorkLog getWorkLog() {
        return workLog;
    }

    public void setWorkLog(WorkLog workLog) {
        this.workLog = workLog;
    }
}
