package it.mapsgroup.gzoom.mybatis.dto;

public class ReportType {
    private String description;
    private String mimeTypeId;

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return the mimeTypeId
     */
    public String getMimeTypeId() {
        return mimeTypeId;
    }
    /**
     * @param mimeTypeId the mimeTypeId to set
     */
    public void setMimeTypeId(String mimeTypeId) {
        this.mimeTypeId = mimeTypeId;
    }

}
