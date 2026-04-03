package it.mapsgroup.gzoom.model;

import java.util.ArrayList;
import java.util.List;

public class WorkLogEmployment {

    // PersonEmployment.id
    private List<Long> persons;
    // EquipmentEmployment.id
    private List<Long> equipments;

    public List<Long> getPersons() {
        if (persons == null) {
            persons = new ArrayList<>();
        }
        return persons;
    }

    public void setPersons(List<Long> persons) {
        this.persons = persons;
    }

    public List<Long> getEquipments() {
        if (equipments == null) {
            equipments = new ArrayList<>();
        }
        return equipments;
    }

    public void setEquipments(List<Long> equipments) {
        this.equipments = equipments;
    }
}
