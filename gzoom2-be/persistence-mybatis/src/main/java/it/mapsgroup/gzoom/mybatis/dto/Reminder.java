package it.mapsgroup.gzoom.mybatis.dto;

public class Reminder {

	private String partyId;
	private String contactMechIdTo;	
	private String subject;
	private String content;
	/**
	 * @return the partyId
	 */
	public String getPartyId() {
		return partyId;
	}
	/**
	 * @param partyId the partyId to set
	 */
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}
	/**
	 * @return the contactMechIdTo
	 */
	public String getContactMechIdTo() {
		return contactMechIdTo;
	}
	/**
	 * @param contactMechIdTo the contactMechIdTo to set
	 */
	public void setContactMechIdTo(String contactMechIdTo) {
		this.contactMechIdTo = contactMechIdTo;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Reminder [partyId=" + partyId + ", contactMechIdTo=" + contactMechIdTo + ", subject=" + subject
				+ ", content=" + content + "]";
	}
	
}
