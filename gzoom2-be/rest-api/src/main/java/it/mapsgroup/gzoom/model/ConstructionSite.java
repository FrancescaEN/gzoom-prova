package it.mapsgroup.gzoom.model;

import java.util.List;

public class ConstructionSite extends Identifiable{

	private String code;
	private JobOrder jobOrder;
	private Lot lot;
	private Person weeklyReferent;
	private Person companyManager;
	private Person cse;
	private Company assignedCompany;
	private PostalAddress address;
	private String note;
	private List<PostalAddress> locations;
    private Company worksCompany;
    private String cup;
    private String cig;

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
	public Lot getLot() {
		return lot;
	}
	public void setLot(Lot lot) {
		this.lot = lot;
	}
	public Person getWeeklyReferent() {
		return weeklyReferent;
	}
	public void setWeeklyReferent(Person weeklyReferent) {
		this.weeklyReferent = weeklyReferent;
	}
	public Person getCompanyManager() {
		return companyManager;
	}
	public void setCompanyManager(Person companyManager) {
		this.companyManager = companyManager;
	}
	public Person getCse() {
		return cse;
	}
	public void setCse(Person cse) {
		this.cse = cse;
	}
	public Company getAssignedCompany() {
		return assignedCompany;
	}
	public void setAssignedCompany(Company assignedCompany) {
		this.assignedCompany = assignedCompany;
	}
	public PostalAddress getAddress() {
		return address;
	}
	public void setAddress(PostalAddress address) {
		this.address = address;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public List<PostalAddress> getLocations() {
		return locations;
	}
	public void setLocations(List<PostalAddress> locations) {
		this.locations = locations;
	}

    public String getCig() {
        return cig;
    }

    public void setCig(String cig) {
        this.cig = cig;
    }

    public String getCup() {
        return cup;
    }

    public void setCup(String cup) {
        this.cup = cup;
    }
    public Company getWorksCompany() {
        return worksCompany;
    }
    public void setWorksCompany(Company worksCompany) {
        this.worksCompany = worksCompany;
    }
}
