package it.mapsgroup.gzoom.mybatis.dto;

public class ContentAndAttributes extends Content {
    private ContentAttribute link;
    private ContentAttribute title;
    private ContentAttribute classes;
    private ContentAssoc parent;

    public ContentAttribute getLink() {
        return link;
    }

    public void setLink(ContentAttribute link) {
        this.link = link;
    }

    public ContentAttribute getTitle() {
        return title;
    }

    public void setTitle(ContentAttribute title) {
        this.title = title;
    }

    public ContentAttribute getClasses() {
        return classes;
    }

    public void setClasses(ContentAttribute classes) {
        this.classes = classes;
    }

    public ContentAssoc getParent() {
        return parent;
    }

    public void setParent(ContentAssoc parent) {
        this.parent = parent;
    }
}
