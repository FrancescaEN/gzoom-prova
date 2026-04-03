package com.mapsengineering.workeffortext.services.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.entity.condition.EntityCondition;
import org.ofbiz.entity.condition.EntityOperator;
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
import com.mapsengineering.workeffortext.util.WorkEffortTypeStatusParamsEvaluator;

import bsh.EvalError;

/**
 * ExecuteWorkEffortStatusQueryConfigService
 */
public class ExecuteWorkEffortStatusQueryConfigService extends GenericService {

    public static final String MODULE = ExecuteWorkEffortStatusQueryConfigService.class.getName();
    private static final String SERVICE_NAME = "executeWorkEffortStatusQueryConfig";
    private static final String SERVICE_TYPE = null;

    private static final String WorkeffortExtErrorLabels = "WorkeffortExtErrorLabels";
    private static final String StandardImportUiLabels = "StandardImportUiLabels";

    
   /**
     * ExecuteWorkEffortStatusQueryConfigService
     */
    public static Map<String, Object> executeWorkEffortStatusQueryConfig(DispatchContext dctx, Map<String, Object> context) {
        ExecuteWorkEffortStatusQueryConfigService obj = new ExecuteWorkEffortStatusQueryConfigService(dctx, context);
        obj.mainLoop();
        return obj.getResult();
    }


    /**
     * Constructor
     */
    public ExecuteWorkEffortStatusQueryConfigService(DispatchContext dctx, Map<String, Object> context) {
        super(dctx, context, new JobLogger(MODULE), SERVICE_NAME, SERVICE_TYPE, MODULE);
        userLogin = (GenericValue)context.get(ServiceLogger.USER_LOGIN);
    }

    /**
     * Main loop <br/>
     * ...
    */ 
    public void mainLoop() {
        String queryCode = null;
        try {
            String statusExecute = getStatusExecute();
            if(UtilValidate.isNotEmpty(statusExecute)) {
                EntityCondition condition = EntityCondition.makeCondition(getQueryConfigConditions(statusExecute));
                List<String> orderByList = UtilMisc.toList("queryCode");
                List<GenericValue> queryList = findList(FIELDS.QueryConfig.name(), condition, orderByList, false, null);
                String msg = "Found " + queryList.size() + " QueryConfig to execute for " + condition;
                addLogInfo(msg);
                for (GenericValue query : queryList) {
                    queryCode = query.getString(FIELDS.queryCode.name());
                    executeQuery(query);
                }
            }
        } catch (Exception e) {
            Map<String, Object> queryCodeLogParameters = UtilMisc.toMap(FIELDS.queryCode.name(), queryCode , "errorMessage", e.getMessage());
            JobLogLog execError = new JobLogLog().initLogCode(StandardImportUiLabels, "QUERY_EXCEC_ERROR", queryCodeLogParameters, getLocale());
            String errorMsg = execError.getLogMessage() + ": " + e.getMessage();
            addLogError(e, errorMsg);
            setResult(ServiceUtil.returnError(errorMsg));
        }
     }
    
    /**
     * @param statusExecute 
     * @param getStatusId() per recuperare il queryCode
     * @return ritorna le condizioni di ricerca delle query
     * @throws GeneralException 
     * @throws EvalError 
     */
    private List<EntityCondition> getQueryConfigConditions(String statusExecute) throws EvalError, GeneralException {
        List<EntityCondition> conditionList = new ArrayList<EntityCondition>();
        conditionList.add(EntityCondition.makeCondition(FIELDS.queryType.name(), FIELDS.A.name()));
        conditionList.add(EntityCondition.makeCondition(FIELDS.queryActive.name(), E.Y.name()));
        conditionList.add(EntityCondition.makeCondition(FIELDS.queryCode.name(), EntityOperator.LIKE, "%" + statusExecute + "%"));
        return conditionList;
    }
    
    private String getStatusExecute() throws EvalError, GeneralException {
        GenericValue workEffort = findOne(E.WorkEffort.name(), EntityCondition.makeCondition(E.workEffortId.name(), getWorkEffortId()), "", "");
        WorkEffortTypeStatusParamsEvaluator paramsStatusEvaluator = new WorkEffortTypeStatusParamsEvaluator(context, delegator);
        paramsStatusEvaluator.evaluateParams(workEffort.getString(E.workEffortTypeId.name()), getStatusId(), true);
        return (String)context.get(E.statusExecute.name());
    }

    /**
     * esegue la query
     * @param query
     * @throws Exception
     */
    private void executeQuery(GenericValue query) throws GenericServiceException {
        Map<String, Object> serviceContext = getDctx().makeValidContext(FIELDS.queryExecutorService.name(), ModelService.IN_PARAM, context);
        serviceContext.put(FIELDS.queryId.name(), query.getString(FIELDS.queryId.name()));
        serviceContext.put(FIELDS.cond0Info.name(), getWorkEffortId());
        Map<String, Object> queryCodeLogParameters = UtilMisc.toMap(FIELDS.queryCode.name(), query.getString(FIELDS.queryCode.name()));
        JobLogLog execStart = new JobLogLog().initLogCode(StandardImportUiLabels, "QUERY_EXCEC", queryCodeLogParameters, getLocale());
        addLogInfo(execStart.getLogCode(), execStart.getLogMessage(), getWorkEffortId(), StandardImportUiLabels, execStart.getParametersJSON());
        
        Map<String, Object> resultMap = dispatcher.runSync(FIELDS.queryExecutorService.name(), serviceContext);
        if (! ServiceUtil.isSuccess(resultMap)) {
            // TODO JobLogLog execError = new JobLogLog().initLogCode(StandardImportUiLabels, "QUERY_EXCEC_ERROR", queryCodeLogParameters, getLocale());
            // TODO String errorMsg = execError.getLogMessage() + ": " + resultMap.get(ModelService.ERROR_MESSAGE);
            throw new GenericServiceException((String) resultMap.get(ModelService.ERROR_MESSAGE));
        } else {
            JobLogLog execSuccess = new JobLogLog().initLogCode(StandardImportUiLabels, "QUERY_EXCEC_SUCCESS", queryCodeLogParameters, getLocale());
            addLogInfo(execSuccess.getLogCode(), execSuccess.getLogMessage(), getWorkEffortId(), StandardImportUiLabels, execSuccess.getParametersJSON());
        }
    }

    private String getStatusId() {
        return (String)context.get(E.statusId.name());
    }

    private String getWorkEffortId() {
        return (String)context.get(E.workEffortId.name());
    }
}
