package it.mapsgroup.gzoom.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AntimafiaProcessPhase extends Identifiable {

	private Long antimafiaProcessId;
	private EntityState state;
	private AntimafiaProcessPhaseType type;
	private Date startDate;
	private Date endDate;
	private String note;
	private String protocolNumber;
	
	public Long getAntimafiaProcessId() {
		return antimafiaProcessId;
	}

	public void setAntimafiaProcessId(Long antimafiaProcessId) {
		this.antimafiaProcessId = antimafiaProcessId;
	}
	
	@JsonProperty("notes")
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

	@JsonProperty("completed")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@JsonProperty("started")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public AntimafiaProcessPhaseType getType() {
		return type;
	}

	public void setType(AntimafiaProcessPhaseType type) {
		this.type = type;
	}

	public String getProtocolNumber() {
		return protocolNumber;
	}

	public void setProtocolNumber(String protocolNumber) {
		this.protocolNumber = protocolNumber;
	}
}
