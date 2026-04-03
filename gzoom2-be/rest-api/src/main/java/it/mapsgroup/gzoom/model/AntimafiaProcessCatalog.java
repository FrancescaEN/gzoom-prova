package it.mapsgroup.gzoom.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Fossi.
 */
public class AntimafiaProcessCatalog {
    private List<Identifiable> prefectures;
    private Antimafia antimafia;

    public AntimafiaProcessCatalog() {
        prefectures = new ArrayList<>();
    }

    public List<Identifiable> getPrefectures() {
        return prefectures;
    }

    public void setPrefectures(List<Identifiable> prefectures) {
        this.prefectures = prefectures;
    }

    public Antimafia getAntimafia() {
        return antimafia;
    }

    public void setAntimafia(Antimafia antimafia) {
        this.antimafia = antimafia;
    }

    public static class Antimafia {
        List<EntityState> causals;
        List<EntityState> states;
        private Phase phase;

        public Antimafia() {
            causals = new ArrayList<>();
            states = new ArrayList<>();
        }

        public List<EntityState> getCausals() {
            return causals;
        }

        public void setCausals(List<EntityState> causals) {
            this.causals = causals;
        }

        public List<EntityState> getStates() {
            return states;
        }

        public void setStates(List<EntityState> states) {
            this.states = states;
        }

        public Phase getPhase() {
            return phase;
        }

        public void setPhase(Phase phase) {
            this.phase = phase;
        }
    }

    public static class Phase {
        List<AntimafiaProcessPhaseType> types;
        List<EntityState> states;

        public Phase() {
            types = new ArrayList<>();
            states = new ArrayList<>();
        }

        public List<AntimafiaProcessPhaseType> getTypes() {
            return types;
        }

        public void setTypes(List<AntimafiaProcessPhaseType> types) {
            this.types = types;
        }

        public List<EntityState> getStates() {
            return states;
        }

        public void setStates(List<EntityState> states) {
            this.states = states;
        }
    }
}
