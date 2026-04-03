package it.mapsgroup.gzoom.model;

public class Identifiable extends Identity {

	private String description;

	public Identifiable() {
		this(null, null);
	}

	public Identifiable(Long id, String description) {
		super(id);
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
