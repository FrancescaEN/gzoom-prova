package it.mapsgroup.gzoom.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FolderMenu {
    private String id;
    private String label;
    private String classes;
    private boolean toRemove;

    List<FolderMenu> children = new ArrayList<FolderMenu>();

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label
     *            the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return the classes
     */
    public String getClasses() {
        return classes;
    }

    /**
     * @param classes
     *            the classes to set
     */
    public void setClasses(String classes) {
        this.classes = classes;
    }

    /**
     * @return the children
     */
    public List<FolderMenu> getChildren() {
        return children;
    }

    /**
     * @param children
     *            the children to set
     */
    public void setChildren(List<FolderMenu> children) {
        this.children = children;
    }

    /**
     * Add child if it isn't in the list
     * 
     * @param child
     * @param id
     */
    public void addChild(FolderMenu child, String id) {
        boolean addChild = true; // whether add child
        if (!children.isEmpty()) {
            Iterator<FolderMenu> c = children.iterator();
            while (c.hasNext()) {
                FolderMenu item = c.next();
                if (item.getId().equals(id)) {
                    addChild = false;
                    break;
                }
            }
        }
        if (addChild) {
            children.add(child);
        }
    }

    /**
     * @return the toRemove
     */
    public boolean isToRemove() {
        return toRemove;
    }

    /**
     * @param toRemove
     *            the toRemove to set
     */
    public void setToRemove(boolean toRemove) {
        this.toRemove = toRemove;
    }

}
