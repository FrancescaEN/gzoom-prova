package it.mapsgroup.gzoom.commons;

public class SubFilter {

    private String field;
    private String value;

    public SubFilter(){}

    public SubFilter(String field, String value){
        this.field = field;
        this.value = value;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }


}
