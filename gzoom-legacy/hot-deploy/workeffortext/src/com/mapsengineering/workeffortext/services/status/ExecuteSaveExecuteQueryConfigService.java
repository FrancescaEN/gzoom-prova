package com.mapsengineering.workeffortext.services.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.service.DispatchContext;
import org.ofbiz.service.GenericServiceException;
import org.ofbiz.service.ModelService;
import org.ofbiz.service.ServiceUtil;

import com.mapsengineering.base.services.GenericService;
import com.mapsengineering.base.services.QueryExecutorService.FIELDS;
import com.mapsengineering.base.services.ServiceLogger;
import com.mapsengineering.base.util.JobLogLog;
import com.mapsengineering.base.util.JobLogger;
import com.mapsengineering.workeffortext.util.E;
import com.mapsengineering.workeffortext.util.WorkEffortTypeStatusCntParamsEvaluator;

import bsh.EvalError;
import javolution.util.FastList;

/**
 * ExecuteSaveExecuteQueryConfigService
 */
public class ExecuteSaveExecuteQueryConfigService extends GenericService {

    public static final String MODULE = ExecuteSaveExecuteQueryConfigService.class.getName();
    private static final String SERVICE_NAME = "executeSaveExecuteQueryConfig";
    private static final String SERVICE_TYPE = null;

    private static final String StandardImportUiLabels = "StandardImportUiLabels";

    /** Essendo un servizio epilogo, i messaggi di errori vanno ritornati con errorMessageList*/
    List<String> errorMessageList;
   
    /**
     * ExecuteSaveExecuteQueryConfigService
     */
    public static Map<String, Object> executeSaveExecuteQueryConfig(DispatchContext dctx, Map<String, Object> context) {
        ExecuteSaveExecuteQueryConfigService obj = new ExecuteSaveExecuteQueryConfigService(dctx, context);
        obj.mainLoop();
        return obj.getResult();
    }


    /**
     * Constructor
     */
    public ExecuteSaveExecuteQueryConfigService(DispatchContext dctx, Map<String, Object> context) {
        super(dctx, context, new JobLogger(MODULE), SERVICE_NAME, SERVICE_TYPE, MODULE);
        userLogin = (GenericValue)context.get(ServiceLogger.USER_LOGIN);
        errorMessageList = FastList.newInstance();
    }

    /**
     * Main loop <br/>
     * ...
    */
    public void mainLoop() {
        String queryCode = null;
        try {
            String saveExecute = getSaveExecute();
            if(UtilValidate.isNotEmpty(saveExecute)) {
                EntityCondition condition = EntityCondition.makeCondition(getQueryConfigConditions(saveExecute));
                List<String> orderByList = UtilMisc.toList(FIELDS.queryCode.name());
                List<GenericValue> queryList = findList(FIELDS.QueryConfig.name(), condition, orderByList, false, null);
                Map<String, Object> queryCodeLogParameters = UtilMisc.toMap("size", queryList.size(), "condition", condition);
                JobLogLog execStart = new JobLogLog().initLogCode(StandardImportUiLabels, "QUERY_LIST", queryCodeLogParameters, getLocale());
                addLogInfo(execStart.getLogCode(), execStart.getLogMessage(), getSaveExecuteWorkEffortId(), StandardImportUiLabels, execStart.getParametersJSON());
                String msg = "Found " + queryList.size() + " QueryConfig to execute for " + condition;
                addLogInfo(msg);
                for (GenericValue query : queryList) {
                    queryCode = query.getString(FIELDS.queryCode.name());
                    executeQuery(query);
                }
            }
        } catch (Exception e) {
            Map<String, Object> queryCodeLogParameters = UtilMisc.toMap(FIELDS.queryCode.name(), queryCode , ModelService.ERROR_MESSAGE, e.getMessage());
            JobLogLog execError = new JobLogLog().initLogCode(StandardImportUiLabels, "QUERY_EXCEC_ERROR", queryCodeLogParameters, getLocale());
            String errorMsg = execError.getLogMessage() + ": " + e.getMessage();
            addLogError(e, errorMsg);
            errorMessageList.add(errorMsg);
            setResult(ServiceUtil.returnError(errorMessageList));
        }
     }
    
    /**
     * @param saveExecute 
     * @return ritorna le condizioni di ricerca delle query
     * @throws GeneralException 
     * @throws EvalError 
     */
    private List<EntityCondition> getQueryConfigConditions(String saveExecute) throws EvalError, GeneralException {
        List<EntityCondition> conditionList = new ArrayList<EntityCondition>();
        conditionList.add(EntityCondition.makeCondition(FIELDS.queryType.name(), FIELDS.A.name()));
        conditionList.add(EntityCondition.makeCondition(FIELDS.queryActive.name(), FIELDS.Y.name()));
        conditionList.add(EntityCondition.makeCondition(FIELDS.queryCode.name(), EntityOperator.LIKE, "%" + saveExecute + "%"));
        return conditionList;
    }
    
    private String getSaveExecute() throws EvalError, GeneralException {
        List<GenericValue> workEfforts = findList(E.WorkEffort.name(), EntityCondition.makeCondition(E.workEffortId.name(), getSaveExecuteWorkEffortId()), null, false, "");
        GenericValue workEffort = EntityUtil.getFirst(workEfforts);
        if (UtilValidate.isNotEmpty(workEffort)) {
            WorkEffortTypeStatusCntParamsEvaluator workEffortTypeStatusCntParamsEvaluator = new WorkEffortTypeStatusCntParamsEvaluator(delegator);
            workEffortTypeStatusCntParamsEvaluator.init(getSaveExecuteWorkEffortId(), workEffort.getString(E.workEffortTypeId.name()), workEffort.getString(E.currentStatusId.name()), getSaveExecuteContentId());
            workEffortTypeStatusCntParamsEvaluator.run();
            Map<String, Map<String, Object>> paramsMap = workEffortTypeStatusCntParamsEvaluator.getParamsContentMap();
            if (UtilValidate.isNotEmpty(paramsMap) && UtilValidate.isNotEmpty(getSaveExecuteContentId())) {
                Map<String, Object> saveExecuteparamsMap = paramsMap.get(getSaveExecuteContentId());
                if (UtilValidate.isNotEmpty(saveExecuteparamsMap)) {
                    String saveExecute = (String) saveExecuteparamsMap.get(E.saveExecute.name());
                    Map<String, Object> queryCodeLogParameters = UtilMisc.toMap(E.workEffortTypeId.name(), workEffort.getString(E.workEffortTypeId.name()), E.currentStatusId.name(), workEffort.getString(E.currentStatusId.name()), E.contentId.name(), getSaveExecuteContentId(), E.saveExecute.name(), saveExecute);
                    JobLogLog execStart = new JobLogLog().initLogCode(StandardImportUiLabels, "QUERY_SE_P", queryCodeLogParameters, getLocale());
                    addLogInfo(execStart.getLogCode(), execStart.getLogMessage(), getSaveExecuteWorkEffortId(), StandardImportUiLabels, execStart.getParametersJSON());
                    return saveExecute;
                } else {
                    Map<String, Object> queryCodeLogParameters = UtilMisc.toMap(E.workEffortTypeId.name(), workEffort.getString(E.workEffortTypeId.name()), E.currentStatusId.name(), workEffort.getString(E.currentStatusId.name()), E.contentId.name(), getSaveExecuteContentId());
                    JobLogLog execStart = new JobLogLog().initLogCode(StandardImportUiLabels, "QUERY_NO_P", queryCodeLogParameters, getLocale());
                    addLogDebug(execStart.getLogCode(), execStart.getLogMessage(), getSaveExecuteWorkEffortId(), StandardImportUiLabels, execStart.getParametersJSON());
                }
            }
        } else {
            Map<String, Object> queryCodeLogParameters = UtilMisc.toMap(E.workEffortId.name(), getSaveExecuteWorkEffortId());
            JobLogLog execStart = new JobLogLog().initLogCode(StandardImportUiLabels, "QUERY_NO_WE", queryCodeLogParameters, getLocale());
            addLogDebug(execStart.getLogCode(), execStart.getLogMessage(), getSaveExecuteWorkEffortId(), StandardImportUiLabels, execStart.getParametersJSON());
        }
        
        return null;
    }

    /**
     * esegue la query
     * @param query
     * @throws Exception
     */
    private void executeQuery(GenericValue query) throws GenericServiceException {
        Map<String, Object> serviceContext = getDctx().makeValidContext(FIELDS.queryExecutorService.name(), ModelService.IN_PARAM, context);
        serviceContext.put(FIELDS.queryId.name(), query.getString(FIELDS.queryId.name()));
        serviceContext.put(FIELDS.cond0Info.name(), getSaveExecuteWorkEffortId());
        serviceContext.put(FIELDS.cond1Info.name(), getOperation());
        Map<String, Object> queryCodeLogParameters = UtilMisc.toMap(FIELDS.queryCode.name(), query.getString(FIELDS.queryCode.name()));
        JobLogLog execStart = new JobLogLog().initLogCode(StandardImportUiLabels, "QUERY_EXCEC", queryCodeLogParameters, getLocale());
        addLogInfo(execStart.getLogCode(), execStart.getLogMessage(), getSaveExecuteWorkEffortId(), StandardImportUiLabels, execStart.getParametersJSON());
        
        Map<String, Object> resultMap = dispatcher.runSync(FIELDS.queryExecutorService.name(), serviceContext);
        if (! ServiceUtil.isSuccess(resultMap)) {
            throw new GenericServiceException((String) resultMap.get(ModelService.ERROR_MESSAGE));
        } else {
            JobLogLog execSuccess = new JobLogLog().initLogCode(StandardImportUiLabels, "QUERY_EXCEC_SUCCESS", queryCodeLogParameters, getLocale());
            addLogInfo(execSuccess.getLogCode(), execSuccess.getLogMessage(), getSaveExecuteWorkEffortId(), StandardImportUiLabels, execSuccess.getParametersJSON());
        }
    }

    @SuppressWarnings("unchecked")
    private String getSaveExecuteWorkEffortId() {
        String workEffortId = (String)context.get(E.saveExecuteWorkEffortId.name());
        Map<String, Object> id = (Map<String, Object>)context.get("id");
        if (UtilValidate.isEmpty(workEffortId)) {
            workEffortId = (String)id.get(E.workEffortId.name());
        }
        return workEffortId;
    }
    
    private String getOperation() {
        String saveExecuteOperation = (String)context.get(E.saveExecuteOperation.name());
        Debug.log("saveExecuteOperation " + saveExecuteOperation);
        /*Map<String, Object> id = (Map<String, Object>)context.get("id");
        if (UtilValidate.isEmpty(workEffortId)) {
            workEffortId = (String)id.get(E.workEffortId.name());
        }*/
        return saveExecuteOperation;
    }
    
    private String getSaveExecuteContentId() {
        return (String)context.get(E.saveExecuteContentId.name());
    }

}
