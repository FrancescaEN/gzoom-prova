package it.mapsgroup.gzoom.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Andrea Fossi.
 */
public class Changeset {
    private String id;
    private String entity;
    private String description;
    private String event;
    private String username;
    private Date timestamp;
    private List<Map<String, Object>> change;

    public Changeset() {
        this.change = new ArrayList<>();
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

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<Map<String, Object>> getChange() {
        if (change == null) {
            change = new ArrayList<>();
        }
        
        return change;
    }

    public void setChange(List<Map<String, Object>> change) {
        this.change = change;
    }
}
