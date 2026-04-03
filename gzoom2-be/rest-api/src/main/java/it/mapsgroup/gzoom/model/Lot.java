package it.mapsgroup.gzoom.model;

import java.util.List;

public class Lot extends Identifiable {

	private String code;
	private JobOrder jobOrder;
	private Person worksManager;
	private Person dtl;
	private Company companyAssigned;
	private String note;
	private List<PostalAddress> locations;

	public List<PostalAddress> getLocations() {
		return locations;
	}

	public void setLocations(List<PostalAddress> locations) {
		this.locations = locations;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public JobOrder getJobOrder() {
		return jobOrder;
	}

	public void setJobOrder(JobOrder jobOrder) {
		this.jobOrder = jobOrder;
	}

	public Person getWorksManager() {
		return worksManager;
	}

	public void setWorksManager(Person worksManager) {
		this.worksManager = worksManager;
	}

	public Person getDtl() {
		return dtl;
	}

	public void setDtl(Person dtl) {
		this.dtl = dtl;
	}

	public Company getCompanyAssigned() {
		return companyAssigned;
	}

	public void setCompanyAssigned(Company companyAssigned) {
		this.companyAssigned = companyAssigned;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
