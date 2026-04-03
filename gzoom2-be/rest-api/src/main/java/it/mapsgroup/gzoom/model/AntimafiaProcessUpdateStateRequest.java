package it.mapsgroup.gzoom.model;

/**
 * @author Andrea Fossi.
 */
public class AntimafiaProcessUpdateStateRequest extends UpdateStateRequest{
    private EntityState oldCausal;
    private EntityState newCausal;

    public EntityState getNewCausal() {
        return newCausal;
    }

    public void setNewCausal(EntityState newCausal) {
        this.newCausal = newCausal;
    }

    public EntityState getOldCausal() {
        return oldCausal;
    }

    public void setOldCausal(EntityState oldCausal) {
        this.oldCausal = oldCausal;
    }
}
