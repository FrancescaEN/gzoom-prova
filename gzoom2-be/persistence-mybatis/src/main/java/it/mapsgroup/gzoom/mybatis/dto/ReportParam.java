package it.mapsgroup.gzoom.mybatis.dto;

public class ReportParam {
    private String paramName;
    private Object paramDefault;
    private String paramType;
    private Boolean mandatory;
    private Boolean display = true;
    private String options;
    private String label;
    private Boolean forceUseLabel;

    /**
     * @return the ForceUseLabel
     */

    public Boolean getForceUseLabel() {
        return forceUseLabel;
    }

    public void setForceUseLabel(Boolean forceUseLabel) {
        this.forceUseLabel = forceUseLabel;
    }



    /**
     * @return the paramName
     */
    public String getParamName() {
        return paramName;
    }

    /**
     * @param paramName the paramName to set
     */
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    /**
     * @return the paramDefault
     */
    public Object getParamDefault() {
        return paramDefault;
    }

    /**
     * @return the options
     */
    public String getOptions() {return options;}

    /**
     * @param paramDefault the paramDefault to set
     */
    public void setParamDefault(Object paramDefault) {
        this.paramDefault = paramDefault;
    }



    /**
     *
     * @return the mandatory
     */
    public Boolean getMandatory() {
        return mandatory;
    }

    /**
     * @return the paramType
     */
    public String getParamType() {
        return paramType;
    }

    /**
     * LIST
     * DATA
     * INPUT
     * BOOLEAN
     *
     * @param paramType the paramType to set
     */
    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    /**
     * @param mandatory
     *            the mandatory to set
     */
    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }


    /**
     * @return the display
     */
    public Boolean getDisplay() {return display;}

    public void setDisplay(Boolean display) {this.display = display;}

    public void setOptions(String options) {this.options = options; }


    /**
     * @return the label
     */
    public String getLabel() { return label;}

    public void setLabel(String label) {this.label = label;}
}
