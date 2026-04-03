package it.mapsgroup.gzoom.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AntimafiaProcessPhaseType extends Identifiable {

	private Integer ordinal;
	private String name;
	private String labelStartDate;
	private String labelEndDate;
	private String labelProtocolNumber;

	public Integer getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("labelCompleted")
	public String getLabelEndDate() {
		return labelEndDate;
	}

	public void setLabelEndDate(String labelEndDate) {
		this.labelEndDate = labelEndDate;
	}

	public String getLabelProtocolNumber() {
		return labelProtocolNumber;
	}

	public void setLabelProtocolNumber(String labelProtocolNumber) {
		this.labelProtocolNumber = labelProtocolNumber;
	}
	@JsonProperty("labelStarted")
	public String getLabelStartDate() {
		return labelStartDate;
	}

	public void setLabelStartDate(String labelStartDate) {
		this.labelStartDate = labelStartDate;
	}
}
