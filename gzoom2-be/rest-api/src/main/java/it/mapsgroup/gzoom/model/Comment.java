package it.mapsgroup.gzoom.model;

import java.util.Date;

/**
 * @author Andrea Fossi.
 */
public class Comment {
    private Long id;
    private String username;
    private Date timestamp;
    private String note;
    private CommentType commentType;
    private ConstructionSiteLog constructionSiteLog;
    private Contract contract;
    private AntimafiaProcess antimafiaProcess;
    private String itemType;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CommentType getCommentType() {
        return commentType;
    }

    public void setCommentType(CommentType commentType) {
        this.commentType = commentType;
    }

    public ConstructionSiteLog getConstructionSiteLog() {
        return constructionSiteLog;
    }

    public void setConstructionSiteLog(ConstructionSiteLog constructionSiteLog) {
        this.constructionSiteLog = constructionSiteLog;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public AntimafiaProcess getAntimafiaProcess() {
        return antimafiaProcess;
    }

    public void setAntimafiaProcess(AntimafiaProcess antimafiaProcess) {
        this.antimafiaProcess = antimafiaProcess;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
