package it.mapsgroup.gzoom.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Company extends Identifiable {

    private String businessName;
    private String taxIdentificationNumber;
    private String vatNumber;
    private String companyPurpose;
    private BigDecimal stockCapital;
    private Identifiable classification;
    private Identifiable category;
    private String legalDelegate;
    private String phone;
    private String fax;
    private String email;
    private PostalAddress address;
    private Boolean rdlControl;
    private String companyType;
    private Date cciaaDate;
    private EntityState state;
    private List<CompanyComposition> composition;
    private String companyState;
    private String companyClassification;
    private String companyCategory;
    private String compositionListDescription;
    private Boolean consortium;
    private Company consortiumMembership;
    private Boolean whiteListMember;
    private Date whiteListMembershipStartDate;
    private Date whiteListMembershipEndDate;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName == null ? null : businessName.trim();
    }

    public String getTaxIdentificationNumber() {
        return taxIdentificationNumber;
    }

    public void setTaxIdentificationNumber(String taxIdentificationNumber) {
        this.taxIdentificationNumber = taxIdentificationNumber == null ? null : taxIdentificationNumber.trim();
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber == null ? null : vatNumber.trim();
    }

    public String getCompanyPurpose() {
        return companyPurpose;
    }

    public void setCompanyPurpose(String companyPurpose) {
        this.companyPurpose = companyPurpose == null ? null : companyPurpose.trim();
    }

    public BigDecimal getStockCapital() {
        return stockCapital;
    }

    public void setStockCapital(BigDecimal stockCapital) {
        this.stockCapital = stockCapital;
    }

    public Identifiable getClassification() {
        return classification;
    }

    public void setClassification(Identifiable classification) {
        this.classification = classification;
    }

    public Identifiable getCategory() {
        return category;
    }

    public void setCategory(Identifiable category) {
        this.category = category;
    }

    public String getLegalDelegate() {
        return legalDelegate;
    }

    public void setLegalDelegate(String legalDelegate) {
        this.legalDelegate = legalDelegate == null ? null : legalDelegate.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax == null ? null : fax.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public PostalAddress getAddress() {
        return address;
    }

    public void setAddress(PostalAddress address) {
        this.address = address;
    }

    public Boolean getRdlControl() {
        return rdlControl;
    }

    public void setRdlControl(Boolean rdlControl) {
        this.rdlControl = rdlControl;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType == null ? null : companyType.trim();
    }

    public Date getCciaaDate() {
        return cciaaDate;
    }

    public void setCciaaDate(Date cciaaDate) {
        this.cciaaDate = cciaaDate;
    }

    public EntityState getState() {
        return state;
    }

    public void setState(EntityState state) {
        this.state = state;
    }

    public List<CompanyComposition> getComposition() {
        if (composition == null) {
            composition = new ArrayList<>();
        }
        return composition;
    }

    public void setComposition(List<CompanyComposition> composition) {
        this.composition = composition;
    }

    public String getCompanyState() {
        return companyState;
    }

    public void setCompanyState(String companyState) {
        this.companyState = companyState;
    }

    public String getCompanyClassification() {
        return companyClassification;
    }

    public void setCompanyClassification(String companyClassification) {
        this.companyClassification = companyClassification;
    }

    public String getCompanyCategory() {
        return companyCategory;
    }

    public void setCompanyCategory(String companyCategory) {
        this.companyCategory = companyCategory;
    }

    public String getCompositionListDescription() {
        return compositionListDescription;
    }

    public void setCompositionListDescription(String compositionListDescription) {
        this.compositionListDescription = compositionListDescription;
    }

    public Boolean getConsortium() {
        return consortium;
    }

    public void setConsortium(Boolean consortium) {
        this.consortium = consortium;
    }

    public Company getConsortiumMembership() {
        return consortiumMembership;
    }

    public void setConsortiumMembership(Company consortiumMembership) {
        this.consortiumMembership = consortiumMembership;
    }

    public Boolean getWhiteListMember() {
        return whiteListMember;
    }

    public void setWhiteListMember(Boolean whiteListMember) {
        this.whiteListMember = whiteListMember;
    }

    public Date getWhiteListMembershipStartDate() {
        return whiteListMembershipStartDate;
    }

    public void setWhiteListMembershipStartDate(Date whiteListMembershipStartDate) {
        this.whiteListMembershipStartDate = whiteListMembershipStartDate;
    }

    public Date getWhiteListMembershipEndDate() {
        return whiteListMembershipEndDate;
    }

    public void setWhiteListMembershipEndDate(Date whiteListMembershipEndDate) {
        this.whiteListMembershipEndDate = whiteListMembershipEndDate;
    }
}
