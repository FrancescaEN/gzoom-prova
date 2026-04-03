package it.mapsgroup.gzoom.model;

/**
 * @author Andrea Fossi.
 */
public class WorkLogSummaryData extends Company{
    private Integer personEvents;
    private Integer equipmentEvents;

    public Integer getPersonEvents() {
        return personEvents;
    }

    public void setPersonEvents(Integer personEvents) {
        this.personEvents = personEvents;
    }

    public Integer getEquipmentEvents() {
        return equipmentEvents;
    }

    public void setEquipmentEvents(Integer equipmentEvents) {
        this.equipmentEvents = equipmentEvents;
    }
}
