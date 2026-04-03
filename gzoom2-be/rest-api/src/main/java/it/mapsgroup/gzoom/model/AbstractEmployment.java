package it.mapsgroup.gzoom.model;

import java.util.Date;

public class AbstractEmployment extends Identity {

	private Company company;
	private Long cardNumber;
	private String badge;
	private Date employmentStartDate;
	private Date employmentEndDate;
	private EntityState state;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Long getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(Long cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public Date getEmploymentStartDate() {
		return employmentStartDate;
	}

	public void setEmploymentStartDate(Date employmentStartDate) {
		this.employmentStartDate = employmentStartDate;
	}

	public Date getEmploymentEndDate() {
		return employmentEndDate;
	}

	public void setEmploymentEndDate(Date employmentEndDate) {
		this.employmentEndDate = employmentEndDate;
	}

	public EntityState getState() {
		return state;
	}

	public void setState(EntityState state) {
		this.state = state;
	}
}
