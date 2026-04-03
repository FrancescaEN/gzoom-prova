package it.mapsgroup.gzoom.model;

import java.util.Date;

public class PersonEmployment extends AbstractEmployment {

	private Person person;
	private Identifiable role;
	private Identifiable job;
	private String jobDescription;
	private Date assumptionDate;
	private Date dismissalDate;
	private String level;
	private Company destinationCompany;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Identifiable getRole() {
		return role;
	}

	public void setRole(Identifiable role) {
		this.role = role;
	}

	public Identifiable getJob() {
		return job;
	}

	public void setJob(Identifiable job) {
		this.job = job;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public Date getAssumptionDate() {
		return assumptionDate;
	}

	public void setAssumptionDate(Date assumptionDate) {
		this.assumptionDate = assumptionDate;
	}

	public Date getDismissalDate() {
		return dismissalDate;
	}

	public void setDismissalDate(Date dismissalDate) {
		this.dismissalDate = dismissalDate;
	}

	public Company getDestinationCompany() {
		return destinationCompany;
	}

	public void setDestinationCompany(Company destinationCompany) {
		this.destinationCompany = destinationCompany;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}
}
