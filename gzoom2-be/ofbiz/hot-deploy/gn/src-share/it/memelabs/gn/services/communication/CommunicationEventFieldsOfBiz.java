//CommunicationEventFieldsOfBiz.java, created on 18/set/2013
package it.memelabs.gn.services.communication;

/**
*@author Aldo Figlioli
*/
public enum CommunicationEventFieldsOfBiz {
    
    ENTRY_DATE("entryDate");
    
    private String field;
    
    private CommunicationEventFieldsOfBiz(String field) {
        this.setField(field);
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

}
