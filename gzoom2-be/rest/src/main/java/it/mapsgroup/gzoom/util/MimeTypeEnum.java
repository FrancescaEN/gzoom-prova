package it.mapsgroup.gzoom.util;

/**
 * @author Leonardo Minaudo
 */
public enum MimeTypeEnum {
    CSV("csv", "text/csv",".csv", "TYPE_PRINT_CSV" ),
    PDF("pdf", "application/pdf", ".pdf", "TYPE_PRINT_PDF"),
    XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx", "TYPE_PRINT_XLSX"),
    HTML("html", "text/html", ".html", null);

    private String mimeType;
    private String fileExtension;
    private String exportMimeType;
    private String contentId;

    MimeTypeEnum (String mimeType, String exportMimeType, String fileExtension, String contentId) {
        this.mimeType = mimeType;
        this.exportMimeType = exportMimeType;
        this.fileExtension = fileExtension;
        this.contentId = contentId;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getExportMimeType() {
        return exportMimeType;
    }

    public void setExportMimeType(String exportMimeType) {
        this.exportMimeType = exportMimeType;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

}
