package it.mapsgroup.gzoom.model;

import java.util.Date;

public class Equipment {

    private Long id;
    private String registrationNumber;
    private String brand;
    private String model;
    private String note;
    private Identifiable equipmentType;
    private Identifiable equipmentCategory;
    private Long peCardNumber;
    private String peBadge;
    private String eeStateDescription;
    private String eeStateTag;
    private String compDescription;
    private Date startDate;
    private Date endDate;
    private String companyStateTag;
    private String companyStateDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Identifiable getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(Identifiable equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Identifiable getEquipmentCategory() {
        return equipmentCategory;
    }

    public void setEquipmentCategory(Identifiable equipmentCategory) {
        this.equipmentCategory = equipmentCategory;
    }

    public Long getPeCardNumber() {
        return peCardNumber;
    }

    public void setPeCardNumber(Long peCardNumber) {
        this.peCardNumber = peCardNumber;
    }

    public String getPeBadge() {
        return peBadge;
    }

    public void setPeBadge(String peBadge) {
        this.peBadge = peBadge;
    }

    public String getEeStateDescription() {
        return eeStateDescription;
    }

    public void setEeStateDescription(String eeStateDescription) {
        this.eeStateDescription = eeStateDescription;
    }

    public String getCompDescription() {
        return compDescription;
    }

    public void setCompDescription(String compDescription) {
        this.compDescription = compDescription;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEeStateTag() {
        return eeStateTag;
    }

    public void setEeStateTag(String eeStateTag) {
        this.eeStateTag = eeStateTag;
    }

    public String getCompanyStateTag() {
        return companyStateTag;
    }

    public void setCompanyStateTag(String companyStateTag) {
        this.companyStateTag = companyStateTag;
    }

    public String getCompanyStateDescription() {
        return companyStateDescription;
    }

    public void setCompanyStateDescription(String companyStateDescription) {
        this.companyStateDescription = companyStateDescription;
    }
}
