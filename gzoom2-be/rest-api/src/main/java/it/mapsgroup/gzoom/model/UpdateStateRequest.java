package it.mapsgroup.gzoom.model;

/**
 * @author Andrea Fossi.
 */
public class UpdateStateRequest {
    private EntityState oldState;
    private EntityState newState;
    private String note;

    public EntityState getNewState() {
        return newState;
    }

    public void setNewState(EntityState newState) {
        this.newState = newState;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public EntityState getOldState() {
        return oldState;
    }

    public void setOldState(EntityState oldState) {
        this.oldState = oldState;
    }
}
