package it.mapsgroup.gzoom.model;

public class Identity {

	private Long id;

	public Identity() {
		this(null);
	}

	public Identity(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
