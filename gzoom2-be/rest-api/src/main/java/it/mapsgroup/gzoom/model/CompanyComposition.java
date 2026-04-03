package it.mapsgroup.gzoom.model;

import java.math.BigDecimal;

public class CompanyComposition {

	private Long parentCompanyId;
	private Long companyId;
	private BigDecimal percentage;
	private Boolean emissary;
	private Company company;

	public Long getParentCompanyId() {
		return parentCompanyId;
	}

	public void setParentCompanyId(Long parentCompanyId) {
		this.parentCompanyId = parentCompanyId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	public Boolean getEmissary() {
		return emissary;
	}

	public void setEmissary(Boolean emissary) {
		this.emissary = emissary;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}
