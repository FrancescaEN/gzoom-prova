package it.mapsgroup.gzoom.mybatis.dto;

public class UserLogin extends UserLoginPersistent {
    private Party party;
    private Person person;
    private String externalLoginKey;

    public String getUsername() {
        return this.getUserLoginId();
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getExternalLoginKey() {
        return externalLoginKey;
    }

    public void setExternalLoginKey(String externalLoginKey) {
        this.externalLoginKey = externalLoginKey;
    }
}
