package it.mapsgroup.gzoom.model;

/**
 * @author Andrea Fossi.
 */
public class SecurityEntity {
    private String id;
    private String description;
    private Boolean readable;
    private Boolean writable;
    private Boolean deletable;
    private Boolean validable;
    private Boolean basic;

    public SecurityEntity() {
        this.readable = false;
        this.writable = false;
        this.deletable = false;
        this.validable = false;
        this.basic = false;
    }

    public Boolean getReadable() {
        return readable;
    }

    public void setReadable(Boolean readable) {
        this.readable = readable;
    }

    public Boolean getWritable() {
        return writable;
    }

    public void setWritable(Boolean writable) {
        this.writable = writable;
    }

    public Boolean getDeletable() {
        return deletable;
    }

    public void setDeletable(Boolean deletable) {
        this.deletable = deletable;
    }

    public Boolean getValidable() {
        return validable;
    }

    public void setValidable(Boolean validable) {
        this.validable = validable;
    }

    public Boolean getBasic() {
        return basic;
    }

    public void setBasic(Boolean basic) {
        this.basic = basic;
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
