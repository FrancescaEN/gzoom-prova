package it.mapsgroup.gzoom.model;

import java.util.Date;

/**
 * @author Andrea Fossi.
 */
public class UpdateAttachmentDescriptionRequest {
    private String description;
    private Date validSince;
    private Date validUntil;
    private String referenceMonth;
    private String referenceYear;
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getValidSince() {
        return validSince;
    }

    public void setValidSince(Date validSince) {
        this.validSince = validSince;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }


    public String getReferenceMonth() {
        return referenceMonth;
    }

    public void setReferenceMonth(String referenceMonth) {
        this.referenceMonth = referenceMonth;
    }

    public String getReferenceYear() {
        return referenceYear;
    }

    public void setReferenceYear(String referenceYear) {
        this.referenceYear = referenceYear;
    }
}
