package it.mapsgroup.gzoom.model;

public class PostalAddress extends Identity {

	private String toName;
	private String attnName;
	private String address;
	private String address2;
	private String directions;
	private String city;
	private Geo country;
	private Geo stateProvince;
	private Geo postalCode;
	private String foreignRegion;
	private Geo region;
	private String foreignProvince;
	private Geo municipality;
	private String streetNumber;
	private String village;
	private Geo province;

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getAttnName() {
		return attnName;
	}

	public void setAttnName(String attnName) {
		this.attnName = attnName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getDirections() {
		return directions;
	}

	public void setDirections(String directions) {
		this.directions = directions;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Geo getCountry() {
		return country;
	}

	public void setCountry(Geo country) {
		this.country = country;
	}

	public Geo getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(Geo postalCode) {
		this.postalCode = postalCode;
	}

	public String getForeignRegion() {
		return foreignRegion;
	}

	public void setForeignRegion(String foreignRegion) {
		this.foreignRegion = foreignRegion;
	}

	public Geo getRegion() {
		return region;
	}

	public void setRegion(Geo region) {
		this.region = region;
	}

	public String getForeignProvince() {
		return foreignProvince;
	}

	public void setForeignProvince(String foreignProvince) {
		this.foreignProvince = foreignProvince;
	}

	public Geo getMunicipality() {
		return municipality;
	}

	public void setMunicipality(Geo municipality) {
		this.municipality = municipality;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public Geo getProvince() {
		return province;
	}

	public void setProvince(Geo province) {
		this.province = province;
	}
}
