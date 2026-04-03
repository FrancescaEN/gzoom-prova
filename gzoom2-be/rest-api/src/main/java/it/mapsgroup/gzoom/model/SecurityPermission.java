package it.mapsgroup.gzoom.model;

/**
 * @author Andrea Fossi.
 */
public class SecurityPermission {
    private SecurityEntity entity;
    private Boolean read;
    private Boolean write;
    private Boolean delete;
    private Boolean validate;
    private Boolean basic;

    public SecurityEntity getEntity() {
        return entity;
    }

    public void setEntity(SecurityEntity entity) {
        this.entity = entity;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public Boolean getWrite() {
        return write;
    }

    public void setWrite(Boolean write) {
        this.write = write;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public Boolean getValidate() {
        return validate;
    }

    public void setValidate(Boolean validate) {
        this.validate = validate;
    }

    public Boolean getBasic() {
        return basic;
    }

    public void setBasic(Boolean basic) {
        this.basic = basic;
    }
}
