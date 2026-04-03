package com.mapsengineering.base.standardimport;

import java.util.List;
import java.util.Map;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.UtilGenerics;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.ModelService;
import org.ofbiz.service.ServiceUtil;

import com.mapsengineering.base.services.GenericServiceLoop;
import com.mapsengineering.base.services.ServiceLogger;
import com.mapsengineering.base.services.communication.enumeration.CommunicationEventFieldEnum;
import com.mapsengineering.base.standardimport.common.E;
import com.mapsengineering.base.standardimport.common.EntityCodeEnum;
import com.mapsengineering.base.util.JobLogLog;
import com.mapsengineering.base.util.MessageUtil;


/**
 * Standard Import with external table
 * TODO Result with recordElaborated and blockingErrors from standardImport
 */
public class ImportManagerExternal extends GenericServiceLoop {

    public static final String MODULE = ImportManagerExternal.class.getName();

    private static final String SERVICE_NAME = "ImportManagerExternal";
    private static final String SERVICE_TYPE_ID = "STR_IMPORT_EXT_T";
    
    private static final String RESOURCE_LABEL = "StandardImportUiLabels";

    private String toString;

    private String defaultPartyIdEmailAddress;

    private String description;

    private String defaultOrganizationPartyIdValue;
    
    /**
     * if checkOnlyUpload = onlyUpload, the record is wrote in standard import tables
     */
    private String checkOnlyUpload;

    /**
     * Search record in table _EXT and call standard import
     * @param dctx
     * @param context
     * @return
     */
    public static Map<String, Object> doImportSrv(DispatchContext dctx, Map<String, Object> context) {
        ImportManagerExternal obj = new ImportManagerExternal(dctx, context);
        obj.mainLoop();
        return obj.getResult();
    }

    /**
     * Constructor
     * @param dctx
     * @param context
     */
    public ImportManagerExternal(DispatchContext dctx, Map<String, Object> context) {
        super(dctx, context, SERVICE_NAME, SERVICE_TYPE_ID, MODULE);
        this.toString = UtilProperties.getPropertyValue("BaseConfig", "StandardImport.sendMail.toString");
        this.defaultPartyIdEmailAddress = UtilProperties.getPropertyValue("BaseConfig", "StandardImport.sendMail.defaultPartyIdEmailAddress");
        this.defaultOrganizationPartyIdValue = UtilProperties.getPropertyValue("BaseConfig", "StandardImport.defaultOrganizationPartyId");
        checkOnlyUpload = (String)context.get(E.checkOnlyUpload.name()); // onlyUpload
        if (UtilValidate.isEmpty(checkOnlyUpload)) {
            checkOnlyUpload = UtilProperties.getPropertyValue("BaseConfig", "StandardImport.folder.checkOnlyUpload");
        }
        
    }

    /**
     * Main loop
     */
    @Override
    protected void execute() throws Exception {
        try {
            if (UtilValidate.isEmpty(getDefaultOrganizationPartyId())) {
                setDefaultOrganizationPartyId(defaultOrganizationPartyIdValue);
            }
            GenericValue serviceType = findOne("JobLogServiceType", EntityCondition.makeCondition(ServiceLogger.SERVICE_TYPE_ID, getServiceType()), "Found more " + getServiceType(), "No service name found " + getServiceType());
            description = serviceType.getString("description");
            setDescriptionEntityName(description);

            executeMoveAndImport();
            
            createCommunicationEventAndSendMail();
        } catch (Exception e) {
            Map<String, Object> logParameters = UtilMisc.toMap(E.errorMsg.name(), (Object) MessageUtil.getExceptionMessage(e));
            JobLogLog errorGeneric = new JobLogLog().initLogCode(RESOURCE_LABEL, "ERROR_STD_EXT", logParameters, getLocale());
            addLogError(e, errorGeneric.getLogCode(), errorGeneric.getLogMessage(), null, RESOURCE_LABEL, errorGeneric.getParametersJSON());
            getResult().putAll(ServiceUtil.returnError(errorGeneric.getLogMessage()));
        }
    }

    
    /**
     * Search record in AcctgTransInterfaceExt, GlAcountInterfaceExt, PersonInterfaceExt, ecc...
     * @return
     * @throws Exception
     */
    private void executeMoveAndImport() throws Exception {
        EntityCodeEnum[] entityCodes = EntityCodeEnum.values();
        for (EntityCodeEnum entityCode : entityCodes) {
            Map<String, Object> logParamSearch = UtilMisc.toMap(E.entityCode.name(), entityCode, E.entityName.name(), entityCode.getInterfaceName());
            JobLogLog jllSearch = new JobLogLog().initLogCode(RESOURCE_LABEL, "SEARCH_ENTITY", logParamSearch, getLocale());
            addLogInfo(jllSearch.getLogCode(), jllSearch.getLogMessage(), null, null, jllSearch.getParametersJSON());

            String entityName = entityCode.getInterfaceName();
            executeImport(entityName);
        }
    }
    
    private void executeImport(String entityName) throws GeneralException {
        Map<String, Object> serviceContext = getDctx().makeValidContext("standardImportUploadFile", ModelService.IN_PARAM, context);
        serviceContext.put("defaultOrganizationPartyId", getDefaultOrganizationPartyId());
        serviceContext.put("syncMode", true);
        serviceContext.put(entityName + "ImportFromExt", entityName + "Ext");
            
        if (entityName.equals(E.OrganizationInterface.name())) {
            serviceContext.put(E.entityListToImport.name(), entityName.concat("|OrgRespInterface"));
        } else if (entityName.equals(E.PersonInterface.name())) {
            serviceContext.put(E.entityListToImport.name(), entityName.concat("|PersRespInterface"));
        } else {
            serviceContext.put(E.entityListToImport.name(), entityName);
        }
        
        serviceContext.put(ServiceLogger.SESSION_ID, getSessionId());
        serviceContext.put(E.checkOnlyUpload.name(), checkOnlyUpload);
        Map<String, Object> importManagerUploadFileResult = ImportManagerUploadFile.doImportSrv(dispatcher.getDispatchContext(), serviceContext);
        Debug.log(" - importManagerUploadFileResult " + importManagerUploadFileResult);
        
        // TODO sistemare i log
        Map<String, Object> logParamFinish = UtilMisc.toMap("result", (Object)importManagerUploadFileResult);
        JobLogLog jllFinish = new JobLogLog().initLogCode(RESOURCE_LABEL, "FINISH_IMPORT_FILE", logParamFinish, getLocale());
        addLogInfo(jllFinish.getLogCode(), jllFinish.getLogMessage(), null, null, jllFinish.getParametersJSON());
        
        List<Map<String, Object>> importManagerUploadFileResultList = UtilGenerics.toList(importManagerUploadFileResult.get("resultListUploadFile"));
        Debug.log(" - importManagerUploadFileResultList " + importManagerUploadFileResultList);
        getResult().putAll(importManagerUploadFileResult);
        
        // TODO set recordElaborated and blockingErrors from standardImport
        // Raccogliere i log e i record elaborati/errori bloccanti
        /*if (UtilValidate.isNotEmpty(importManagerResultList)) {
            for (Map<String, Object> result : importManagerResultList) {
                setBlockingErrors(getBlockingErrors() + (Long)result.get(ServiceLogger.BLOCKING_ERRORS));
                setRecordElaborated(getRecordElaborated() + (Long)result.get(ServiceLogger.RECORD_ELABORATED));
            }
        }*/
    }

    private void createCommunicationEventAndSendMail() throws GenericServiceException {
        Map<String, Object> logParamMailContent1 = UtilMisc.toMap(ServiceLogger.SESSION_ID, (Object)getSessionId(), ServiceLogger.RECORD_ELABORATED, (Object)getRecordElaborated(), ServiceLogger.BLOCKING_ERRORS, (Object)getBlockingErrors());
        JobLogLog mailContent1 = new JobLogLog().initLogCode(RESOURCE_LABEL, "STD_SESSION_ID", logParamMailContent1, getLocale());
        addLogInfo(mailContent1.getLogCode(), mailContent1.getLogMessage(), null, RESOURCE_LABEL, mailContent1.getParametersJSON());

        Map<String, Object> logParamMailContent2 = UtilMisc.toMap(ServiceLogger.SESSION_ID, (Object)getSessionId(), ServiceLogger.RECORD_ELABORATED, (Object)getRecordElaborated(), ServiceLogger.BLOCKING_ERRORS, (Object)getBlockingErrors());
        JobLogLog mailContent2 = new JobLogLog().initLogCode(RESOURCE_LABEL, "STD_BLOC_ERR", logParamMailContent2, getLocale());
        addLogInfo(mailContent2.getLogCode(), mailContent2.getLogMessage(), null, RESOURCE_LABEL, mailContent2.getParametersJSON());

        Map<String, Object> logParamMailContent3 = UtilMisc.toMap(ServiceLogger.SESSION_ID, (Object)getSessionId(), ServiceLogger.RECORD_ELABORATED, (Object)getRecordElaborated(), ServiceLogger.BLOCKING_ERRORS, (Object)getBlockingErrors());
        JobLogLog mailContent3 = new JobLogLog().initLogCode(RESOURCE_LABEL, "STD_REC_ELAB", logParamMailContent3, getLocale());
        addLogInfo(mailContent3.getLogCode(), mailContent3.getLogMessage(), null, RESOURCE_LABEL, mailContent3.getParametersJSON());

        if (UtilValidate.isEmpty(toString)) {
            JobLogLog noMailFound = new JobLogLog().initLogCode(RESOURCE_LABEL, "NO_MAIL_FOUND", null, getLocale());
            addLogWarning(noMailFound.getLogCode(), noMailFound.getLogMessage(), null, RESOURCE_LABEL, noMailFound.getParametersJSON());
            return;
        }

        Map<String, Object> logParamCommunication = UtilMisc.toMap(CommunicationEventFieldEnum.toString.name(), (Object)toString, CommunicationEventFieldEnum.partyIdEmailAddress.name(), defaultPartyIdEmailAddress);
        JobLogLog communication = new JobLogLog().initLogCode(RESOURCE_LABEL, "CREATE_COMMEV", logParamCommunication, getLocale());
        addLogInfo(communication.getLogCode(), communication.getLogMessage(), null, RESOURCE_LABEL, communication.getParametersJSON());

        String serviceName = "communicationEventCreate";
        Map<String, Object> serviceContext = getDctx().makeValidContext(serviceName, ModelService.IN_PARAM, context);
        serviceContext.put(CommunicationEventFieldEnum.partyIdEmailAddress.name(), defaultPartyIdEmailAddress);
        serviceContext.put(CommunicationEventFieldEnum.toString.name(), toString);
        serviceContext.put(CommunicationEventFieldEnum.resourceLabel.name(), RESOURCE_LABEL);
        serviceContext.put(CommunicationEventFieldEnum.content.name(), mailContent1.getLogMessage() + mailContent2.getLogMessage() + mailContent3.getLogMessage());
        serviceContext.put(CommunicationEventFieldEnum.subject.name(), description);
        serviceContext.put(ServiceLogger.JOB_LOGGER, jobLogger);
        dispatcher.runSync(serviceName, serviceContext);
    }
}
