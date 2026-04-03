package it.mapsgroup.gzoom.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.mapsgroup.gzoom.mybatis.dto.ReportParam;
import it.mapsgroup.gzoom.mybatis.dto.ReportParamService;
import it.mapsgroup.gzoom.mybatis.dto.ReportType;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypeExt;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Report extends Identifiable {

	private String reportName;
	private String reportContentId;
	private String workEffortTypeId;
	private String workEffortAnalysisId;
	private String parentTypeId;
	private String etch;
	private String etchLang;
	private String description;
	private String descriptionLang;	
	private String serviceName;
	private String useFilter;
	private BigDecimal sequenceNum;
	private String outputFormat;
	private String contentName;
	private boolean analysis;
	private String defaultOrganizationUnitId;
	private String langLocale;
	private Locale birtLocale;
	private String resourceName;

	public String getReportContentTypeId() {
		return reportContentTypeId;
	}

	public void setReportContentTypeId(String reportContentTypeId) {
		this.reportContentTypeId = reportContentTypeId;
	}

	private String reportContentTypeId;

	private List<ReportParam> params;
	private List<ReportParamService> services;
    private List<ReportType> outputFormats;
    private List<WorkEffortTypeExt> workEffortTypes;
    private Map<String, Object> paramsValue;


	/**
	 * @return the analysis
	 */
	public boolean isAnalysis() {
		return analysis;
	}

	/**
	 * @param analysis the analysis to set
	 */
	public void setAnalysis(boolean analysis) {
		this.analysis = analysis;
	}

	/**
	 * @return the getDefaultOrganizationUnitId
	 */
	public String getDefaultOrganizationUnitId() { return defaultOrganizationUnitId;}
	/**
	 * @param  defaultOrganizationUnitId
	 */
	public void setDefaultOrganizationUnitId(String defaultOrganizationUnitId) {this.defaultOrganizationUnitId = defaultOrganizationUnitId;}

	/**
	 * @return the langLocale
	 */
	public String getLangLocale() { return langLocale;}

	/**
	 * @param  langLocale
	 */
	public void setLangLocale(String langLocale) {this.langLocale = langLocale;}

	/**
	 +	 * @return the birtLocale
	 +	 */
	public Locale getBirtLocale() { return birtLocale;}

	/**
 	 * @param  birtLocale
 	 */
	public void setBirtLocale(Locale birtLocale) {this.birtLocale = birtLocale;}

	/**
	 * @return the reportName
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * @param reportName the reportName to set
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	/**
	 * @return the reportContentId
	 */
	public String getReportContentId() {
		return reportContentId;
	}

	/**
	 * @param reportContentId the reportContentId to set
	 */
	public void setReportContentId(String reportContentId) {
		this.reportContentId = reportContentId;
	}

	/**
	 * @return the workEffortTypeId
	 */
	public String getWorkEffortTypeId() {
		return workEffortTypeId;
	}

	/**
	 * @param workEffortTypeId the workEffortTypeId to set
	 */
	public void setWorkEffortTypeId(String workEffortTypeId) {
		this.workEffortTypeId = workEffortTypeId;
	}

	/**
	 * @return the workEffortAnalysisId
	 */
	public String getWorkEffortAnalysisId() {
		return workEffortAnalysisId;
	}

	/**
	 * @param workEffortAnalysisId the workEffortAnalysisId to set
	 */
	public void setWorkEffortAnalysisId(String workEffortAnalysisId) {
		this.workEffortAnalysisId = workEffortAnalysisId;
	}

	/**
	 * @return the parentTypeId
	 */
	public String getParentTypeId() {
		return parentTypeId;
	}

	/**
	 * @param parentTypeId the parentTypeId to set
	 */
	public void setParentTypeId(String parentTypeId) {
		this.parentTypeId = parentTypeId;
	}

	/**
	 * @return the etch
	 */
	public String getEtch() {
		return etch;
	}

	/**
	 * @param etch the etch to set
	 */
	public void setEtch(String etch) {
		this.etch = etch;
	}

	/**
	 * @return the etchLang
	 */
	public String getEtchLang() {
		return etchLang;
	}

	/**
	 * @param etchLang the etchLang to set
	 */
	public void setEtchLang(String etchLang) {
		this.etchLang = etchLang;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the descriptionLang
	 */
	public String getDescriptionLang() {
		return descriptionLang;
	}

	/**
	 * @param descriptionLang the descriptionLang to set
	 */
	public void setDescriptionLang(String descriptionLang) {
		this.descriptionLang = descriptionLang;
	}


	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * @return the useFilter
	 */
	public String getUseFilter() {
		return useFilter;
	}

	/**
	 * @param useFilter the useFilter to set
	 */
	public void setUseFilter(String useFilter) {
		this.useFilter = useFilter;
	}

	/**
	 * @return the sequenceNum
	 */
	public BigDecimal getSequenceNum() {
		return sequenceNum;
	}

	/**
	 * @param sequenceNum the sequenceNum to set
	 */
	public void setSequenceNum(BigDecimal sequenceNum) {
		this.sequenceNum = sequenceNum;
	}

	/**
	 * @return the params
	 */
	public List<ReportParam> getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(List<ReportParam> params) {
		this.params = params;
	}

	/**
	 * @return the outputFormats
	 */
	public List<ReportType> getOutputFormats() {
		return outputFormats;
	}

	/**
	 * @param outputFormats the outputFormats to set
	 */
	public void setOutputFormats(List<ReportType> outputFormats) {
		this.outputFormats = outputFormats;
	}


	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * @return the workEffortTypes
	 */
	public List<WorkEffortTypeExt> getWorkEffortTypes() {
		return workEffortTypes;
	}

	/**
	 * @param workEffortTypes the workEffortTypes to set
	 */
	public void setWorkEffortTypes(List<WorkEffortTypeExt> workEffortTypes) {
		this.workEffortTypes = workEffortTypes;
	}

	/**
	 * @return the outputFormat
	 */
	public String getOutputFormat() {
		return outputFormat;
	}

	/**
	 * @param outputFormat the outputFormat to set
	 */
	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	/**
	 * @return the contentName
	 */
	public String getContentName() {
		return contentName;
	}

	/**
	 * @param contentName the contentName to set
	 */
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	/**
	 * @return the paramsValue
	 */
	public Map<String, Object> getParamsValue() {
		return paramsValue;
	}

	/**
	 * @param paramsValue the paramsValue to set
	 */
	public void setParamsValue(Map<String, Object> paramsValue) {
		this.paramsValue = paramsValue;
	}

	/**
	 * @return the services
	 */
	public List<ReportParamService> getServices() {
		return services;
	}

	/**
	 * @param services the services to set
	 */
	public void setServices(List<ReportParamService> services) {
		this.services = services;
	}

	
	
}
