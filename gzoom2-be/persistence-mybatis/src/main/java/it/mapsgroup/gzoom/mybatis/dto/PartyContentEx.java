package it.mapsgroup.gzoom.mybatis.dto;

public class PartyContentEx extends Content {
    private PartyContent partyContent;
    private DataResource dataResource;

    public PartyContent getPartyContent() {
        return partyContent;
    }

    public void setPartyContent(PartyContent partyContent) {
        this.partyContent = partyContent;
    }

    public DataResource getDataResource() {
        return dataResource;
    }

    public void setDataResource(DataResource dataResource) {
        this.dataResource = dataResource;
    }
}
