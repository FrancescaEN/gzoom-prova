package it.mapsgroup.gzoom.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Fossi.
 */
public class NotificationGroup extends Identifiable {
    private List<User> users;
    private List<NotificationEvent> events;


    public NotificationGroup() {
        this.users=new ArrayList<>();
        this.events=new ArrayList<>();
    }

    public List<NotificationEvent> getEvents() {
        return events;
    }

    public void setEvents(List<NotificationEvent> events) {
        this.events = events;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
