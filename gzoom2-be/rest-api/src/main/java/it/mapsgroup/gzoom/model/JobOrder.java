package it.mapsgroup.gzoom.model;

public class JobOrder extends Identifiable {

	private String code;
	private Person rsp;
	private String note;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Person getRsp() {
		return rsp;
	}

	public void setRsp(Person rsp) {
		this.rsp = rsp;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
