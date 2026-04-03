package it.mapsgroup.gzoom.ofbiz.service;

/**
 * @author Andrea Fossi.
 */
public class LoginResponseOfBiz {
    private String externalLoginKey;
    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getExternalLoginKey() {
        return externalLoginKey;
    }

    public void setExternalLoginKey(String externalLoginKey) {
        this.externalLoginKey = externalLoginKey;
    }
}
