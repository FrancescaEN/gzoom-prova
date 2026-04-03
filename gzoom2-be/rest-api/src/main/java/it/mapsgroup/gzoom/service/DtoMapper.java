package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.mybatis.dto.Activity;
import org.springframework.stereotype.Component;

/**
 * @author Fabio G. Strozzi
 */
@Component
public class DtoMapper {

    private static final String SEP = " ";
    private static final String DOT = ".";
    private static final String RPTDESIGN = ".rptdesign";
    private static final String JASPER = ".jasper";

    public it.mapsgroup.gzoom.model.Activity copy(Activity from, it.mapsgroup.gzoom.model.Activity to) {
        to.setWorkEffortId(from.getWorkEffort3().getWorkEffortId());
        to.setTimesheetId(from.getTimesheetId());
        to.setAttivitaLiv1(from.getWorkEffort1().getWorkEffortName());
        to.setAttivitaLiv2(from.getWorkEffort2().getWorkEffortName());
        to.setAttivitaLiv3(from.getWorkEffort3().getWorkEffortName());
        to.setDescription(from.getWorkEffort1().getWorkEffortName()+" - "+
                from.getWorkEffort2().getWorkEffortName()+" - "+
                from.getWorkEffort3().getWorkEffortName());
        return to;
    }
 
    
    public it.mapsgroup.gzoom.model.Report copy(it.mapsgroup.gzoom.mybatis.dto.Report from, it.mapsgroup.gzoom.model.Report to) {
        if (from == null) {
        	return to;
        }
    	to.setWorkEffortTypeId(from.getWorkEffortType().getWorkEffortTypeId());
        to.setParentTypeId(from.getWorkEffortType().getParentTypeId());
        to.setDescription(from.getDescription());        
        to.setDescriptionLang(from.getDescriptionLang());
        to.setServiceName(from.getServiceName());
        to.setReportContentId(from.getContentId());
        to.setReportContentTypeId(from.getContentTypeId());
        to.setContentName(from.getContentName());

        
    	//workEffortAnalysis campi provenente dall'analisi
        if (from.getWorkEffortAnalysis() != null) {
        	to.setWorkEffortAnalysisId(from.getWorkEffortAnalysis().getWorkEffortAnalysisId());
        	to.setAnalysis(true);
            to.setEtch(from.getWorkEffortAnalysis().getDescription5());
            to.setEtchLang(from.getWorkEffortAnalysis().getDescription5()); 
            
        } else {
        	to.setAnalysis(false);        	
        }
        
    	//workEffortTypeContent campi provenenti dal report
        if (from.getWorkEffortTypeContent() != null) {
        	to.setSequenceNum(from.getWorkEffortTypeContent().getSequenceNum());
            to.setEtch(from.getWorkEffortTypeContent().getEtch());
            to.setEtchLang(from.getWorkEffortTypeContent().getEtchLang()); 
            to.setUseFilter(from.getWorkEffortTypeContent().getUseFilter());
        	
        }

        //dataResource campi provenienti dal dataresource
        if(from.getDataResource()!= null) {
            if(from.getDataResource().getDataResourceName().indexOf(RPTDESIGN) > 0){
                to.setResourceName(from.getDataResource().getDataResourceName().substring(0, from.getDataResource().getDataResourceName().indexOf(RPTDESIGN)));
            }
            else if(from.getDataResource().getDataResourceName().indexOf(JASPER) > 0){
                to.setResourceName(from.getDataResource().getDataResourceName().substring(0, from.getDataResource().getDataResourceName().indexOf(JASPER)));
            }
            else{
                to.setResourceName(from.getDataResource().getDataResourceName());
            }
        }
        
        //TODO gestire il LANG
        to.setReportName(to.getEtch() == null ? to.getDescription() : to.getEtch());
        
        return to;
    }

    public it.mapsgroup.gzoom.model.Person copy(it.mapsgroup.gzoom.mybatis.dto.PersonEx from , it.mapsgroup.gzoom.model.Person to) {
        if (from == null) {
            return to;
        }
        to.setFirstName(from.getFirstName());
        to.setLastName(from.getLastName());
        to.setParentRoleCode(from.getPartyParentRole() != null? from.getPartyParentRole().getParentRoleCode() : "");

        to.setEmail(from.getContactMech() != null? from.getContactMech().getInfoString() : "");
        // TODO to.setFromDate(from.getFirstName());
        to.setEndDate(from.getParty() != null? from.getParty().getEndDate() : null);
        to.setStatusDescription(from.getStatusItem() != null? from.getStatusItem().getDescription() : "");

        to.setEmplPositionTypeDescription(from.getEmplPositionType() != null? from.getEmplPositionType().getDescription() : "");
        to.setEmploymentAmount(from.getEmploymentAmount());

        return to;
    }

}
