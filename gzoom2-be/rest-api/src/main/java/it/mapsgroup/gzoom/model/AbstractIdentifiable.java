package it.mapsgroup.gzoom.model;

/**
 * Any identifiable class shares these properties.
 *
 * @author Fabio G. Strozzi
 */
public abstract class AbstractIdentifiable {
    private String id;
    private String description;

    public AbstractIdentifiable() {}

    public AbstractIdentifiable(String id) {
        this.id = id;
    }

    public AbstractIdentifiable(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
