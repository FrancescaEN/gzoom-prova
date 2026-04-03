package it.mapsgroup.gzoom.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Contract {
    private Long id;
    private Company company;
    private Company performingCompany;
    private Contract referenceContract;
    private AntimafiaProcess antimafiaProcess;
    private Identifiable contractType;
    private String description;
    private BigDecimal authorizedAmount;
    private BigDecimal contractAmount;
    private String contractNumber;
    private String mainCategory;
    private Boolean customerAuthorization;
    private String subcontractAuthorizationNumber;
    private Date subcontractDocAuthorizationDate;
    private Lot lot;
    private List<Identifiable> constructionSites;
    private Date signingDate;
    private Date revocationDate;
    private Contract contractExtension;
    private String note;
    private EntityState state;
    private String constructionSitesDescriptionList;

    private String uniqueCode;
    private Date startDate;
    private Date endDate;
    private String companyBank;
    private List<String> companyIban;
    private Boolean letterOfIndemnity;
    private Date letterOfIndemnityDeliveryDate;
    private Identifiable authorization;
    private ContractCatalogItem commitmentMode;

    private Boolean subjectToMgo;

    private List<Contract> extensions;

    public Contract() {
        constructionSites = new ArrayList<>();
        companyIban = new ArrayList<>();
        extensions = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getPerformingCompany() {
        return performingCompany;
    }

    public void setPerformingCompany(Company performingCompany) {
        this.performingCompany = performingCompany;
    }

    public Contract getReferenceContract() {
        return referenceContract;
    }

    public void setReferenceContract(Contract referenceContract) {
        this.referenceContract = referenceContract;
    }

    public AntimafiaProcess getAntimafiaProcess() {
        return antimafiaProcess;
    }

    public void setAntimafiaProcess(AntimafiaProcess antimafiaProcess) {
        this.antimafiaProcess = antimafiaProcess;
    }

    public Identifiable getContractType() {
        return contractType;
    }

    public void setContractType(Identifiable contractType) {
        this.contractType = contractType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public Boolean getCustomerAuthorization() {
        return customerAuthorization;
    }

    public void setCustomerAuthorization(Boolean customerAuthorization) {
        this.customerAuthorization = customerAuthorization;
    }

    public String getSubcontractAuthorizationNumber() {
        return subcontractAuthorizationNumber;
    }

    public void setSubcontractAuthorizationNumber(String subcontractAuthorizationNumber) {
        this.subcontractAuthorizationNumber = subcontractAuthorizationNumber;
    }

    public Date getSubcontractDocAuthorizationDate() {
        return subcontractDocAuthorizationDate;
    }

    public void setSubcontractDocAuthorizationDate(Date subcontractDocAuthorizationDate) {
        this.subcontractDocAuthorizationDate = subcontractDocAuthorizationDate;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public List<Identifiable> getConstructionSites() {
        return constructionSites;
    }

    public void setConstructionSites(List<Identifiable> constructionSites) {
        this.constructionSites = constructionSites;
    }

    public Date getSigningDate() {
        return signingDate;
    }

    public void setSigningDate(Date signingDate) {
        this.signingDate = signingDate;
    }

    public Date getRevocationDate() {
        return revocationDate;
    }

    public void setRevocationDate(Date revocationDate) {
        this.revocationDate = revocationDate;
    }

    public Contract getContractExtension() {
        return contractExtension;
    }

    public void setContractExtension(Contract contractExtension) {
        this.contractExtension = contractExtension;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public EntityState getState() {
        return state;
    }

    public void setState(EntityState state) {
        this.state = state;
    }

    public String getConstructionSitesDescriptionList() {
        return constructionSitesDescriptionList;
    }

    public void setConstructionSitesDescriptionList(String constructionSitesDescriptionList) {
        this.constructionSitesDescriptionList = constructionSitesDescriptionList;
    }

    public String getCompanyBank() {
        return companyBank;
    }

    public void setCompanyBank(String companyBank) {
        this.companyBank = companyBank;
    }

    public List<String> getCompanyIban() {
        return companyIban;
    }

    public void setCompanyIban(List<String> companyIban) {
        this.companyIban = companyIban;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getLetterOfIndemnity() {
        return letterOfIndemnity;
    }

    public void setLetterOfIndemnity(Boolean letterOfIndemnity) {
        this.letterOfIndemnity = letterOfIndemnity;
    }

    public Date getLetterOfIndemnityDeliveryDate() {
        return letterOfIndemnityDeliveryDate;
    }

    public void setLetterOfIndemnityDeliveryDate(Date letterOfIndemnityDeliveryDate) {
        this.letterOfIndemnityDeliveryDate = letterOfIndemnityDeliveryDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public Boolean getSubjectToMgo() {
        return subjectToMgo;
    }

    public void setSubjectToMgo(Boolean subjectToMgo) {
        this.subjectToMgo = subjectToMgo;
    }

    public List<Contract> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<Contract> extensions) {
        this.extensions = extensions;
    }

    public BigDecimal getAuthorizedAmount() {
        return authorizedAmount;
    }

    public void setAuthorizedAmount(BigDecimal authorizedAmount) {
        this.authorizedAmount = authorizedAmount;
    }

    public Identifiable getAuthorization() {
        return authorization;
    }

    public void setAuthorization(Identifiable authorization) {
        this.authorization = authorization;
    }

    public ContractCatalogItem getCommitmentMode() {
        return commitmentMode;
    }

    public void setCommitmentMode(ContractCatalogItem commitmentMode) {
        this.commitmentMode = commitmentMode;
    }
}