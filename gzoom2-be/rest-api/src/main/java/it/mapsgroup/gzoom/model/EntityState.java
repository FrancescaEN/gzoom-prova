package it.mapsgroup.gzoom.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntityState extends Identifiable {

	private Long parentId;
	private Integer ordinal;
	private String name;
	private String entity;
	private Date validUntil;
	private String tag;

	public EntityState(Long id, String description) {
		super(id, description);
	}

	public EntityState() {
	}

	@JsonProperty("state")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

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

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public Date getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(Date validUntil) {
		this.validUntil = validUntil;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
