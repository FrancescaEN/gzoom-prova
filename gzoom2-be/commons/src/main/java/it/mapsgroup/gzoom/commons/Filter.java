package it.mapsgroup.gzoom.commons;

import java.math.BigDecimal;
import java.time.Instant;

public class Filter {


    private String field;
    private String value;
    private SubFilter[] objValue;
    private String matchMode;
    private String secondValue;
    private Instant dateValue;
    private BigDecimal bigDecimalValue;
    private Double doubleValue;
    private boolean secondaryLang;

    public Filter(){}

    public Filter(String field, String value, String matchMode, Instant dateValue){
        this.field = field;
        this.matchMode = matchMode;
        this.value = value;
        this.dateValue = dateValue;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setMatchMode(String matchMode) {
        this.matchMode = matchMode;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getMatchMode() {
        return matchMode;
    }

    public String getValue() {
        return value;
    }

    public String getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(String secondValue) {
        this.secondValue = secondValue;
    }

    public Instant getDateValue() {
        return dateValue;
    }

    public void setDateValue(Instant dateValue) {
        this.dateValue = dateValue;
    }

    public BigDecimal getBigDecimalValue() {
        return bigDecimalValue;
    }

    public void setBigDecimalValue(BigDecimal bigDecimalValue) {
        this.bigDecimalValue = bigDecimalValue;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public boolean isSecondaryLang() {
        return secondaryLang;
    }

    public void setSecondaryLang(boolean secondaryLang) {
        this.secondaryLang = secondaryLang;
    }

    public void setObjValue(SubFilter[] objValue) {
        this.objValue = objValue;
    }

    public SubFilter[] getObjValue() {
        return objValue;
    }
}
