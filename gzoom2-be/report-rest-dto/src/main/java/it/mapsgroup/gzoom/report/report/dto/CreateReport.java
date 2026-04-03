package it.mapsgroup.gzoom.report.report.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Andrea Fossi.
 */
/*@JsonTypeInfo(
        use = JsonTypeInfo.Id.MINIMAL_CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class")*/
public class CreateReport {
    /**
     * {@see https://github.com/FasterXML/jackson-docs/wiki/JacksonPolymorphicDeserialization}
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
    private Map<String, Object> params;
    // nome mostrato alll'utente, che deriva da content.description oppure da workEffortTypeContent.etch se valorizzato
    private String reportName;
    private String reportLocale;
    private String createdByUserLogin;
    private String modifiedByUserLogin;
    //Nome del file JSON dove ci sono i parametri
    private String contentName;
    private String mimeTypeId;
    //Nome dell'rptdesign e della cartella
    private String resourceName;

    public String getContentTypeId() {
        return contentTypeId;
    }

    public void setContentTypeId(String contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    private String contentTypeId;

    public CreateReport() {
        this.params = new HashMap<>();
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportLocale() {
        return reportLocale;
    }

    public void setReportLocale(String reportLocale) {
        this.reportLocale = reportLocale;
    }

    public String getCreatedByUserLogin() {
        return createdByUserLogin;
    }

    public void setCreatedByUserLogin(String createdByUserLogin) {
        this.createdByUserLogin = createdByUserLogin;
    }

    public String getModifiedByUserLogin() {
        return modifiedByUserLogin;
    }

    public void setModifiedByUserLogin(String modifiedByUserLogin) {
        this.modifiedByUserLogin = modifiedByUserLogin;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

	public String getMimeTypeId() {
		return mimeTypeId;
	}

	public void setMimeTypeId(String mimeTypeId) {
		this.mimeTypeId = mimeTypeId;
	}

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
