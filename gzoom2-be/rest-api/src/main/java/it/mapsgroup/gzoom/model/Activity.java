package it.mapsgroup.gzoom.model;

public class Activity extends Identifiable {
    private String timesheetId;
    private String workEffortId;
    private String attivitaLiv1;
    private String attivitaLiv2;
    private String attivitaLiv3;

    public String getTimesheetId() {
        return timesheetId;
    }

    public void setTimesheetId(String timesheetId) {
        this.timesheetId = timesheetId;
    }

    public String getWorkEffortId() {
        return workEffortId;
    }

    public void setWorkEffortId(String workEffortId) {
        this.workEffortId = workEffortId;
    }

    public String getAttivitaLiv1() {
        return attivitaLiv1;
    }

    public void setAttivitaLiv1(String attivitaLiv1) {
        this.attivitaLiv1 = attivitaLiv1;
    }

    public String getAttivitaLiv2() {
        return attivitaLiv2;
    }

    public void setAttivitaLiv2(String attivitaLiv2) {
        this.attivitaLiv2 = attivitaLiv2;
    }

    public String getAttivitaLiv3() {
        return attivitaLiv3;
    }

    public void setAttivitaLiv3(String attivitaLiv3) {
        this.attivitaLiv3 = attivitaLiv3;
    }
}
