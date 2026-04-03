package it.mapsgroup.gzoom.model;

import java.util.ArrayList;
import java.util.List;

public class WeeklyWorkLogCompanyChange extends Identity {

    private String businessName;
    private String vatNumber;
    private List<WeeklyWorkLogPersonChange> persons;
    private List<WeeklyWorkLogEquipmentChange> equipments;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public List<WeeklyWorkLogPersonChange> getPersons() {
        if (persons == null) {
            persons = new ArrayList<>();
        }
        return persons;
    }

    public void setPersons(List<WeeklyWorkLogPersonChange> persons) {
        this.persons = persons;
    }

    public List<WeeklyWorkLogEquipmentChange> getEquipments() {
        if (equipments == null) {
            equipments = new ArrayList<>();
        }
        return equipments;
    }

    public void setEquipments(List<WeeklyWorkLogEquipmentChange> equipments) {
        this.equipments = equipments;
    }
}
