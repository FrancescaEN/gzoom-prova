import org.ofbiz.base.util.*;
import org.ofbiz.base.crypto.HashCrypt;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;
import org.ofbiz.service.*;
import com.mapsengineering.base.util.ContextPermissionPrefixEnum;
import com.mapsengineering.workeffortext.util.WorkEffortTypeStatusParamsEvaluator;

/**
 * Dato com eparametro queryparam, mi ricavo la lista selezionata dall'utente
 * e per ogni elemento chiamo il cambio stato
 */

languageSettinngs = request.getSession().getAttribute("languageSettinngs");

def uiLabelMap = UtilProperties.getResourceBundleMap("WorkeffortExtUiLabels", locale)

def itemSuccess = 0;
def itemWarning = 0;
def itemFailed = 0;
def noPrevStatusError = "";
def queryConfigError = "";

localResult = ServiceUtil.returnSuccess();
errorList = [];
warningList = [];
	
def inputMap = [:];
inputMap.workEffortId = parameters.workEffortId;
inputMap.statusDatetime = UtilDateTime.nowTimestamp();
inputMap.reason = parameters.reason;
inputMap.sessionId = parameters.sessionId;			
inputMap.statusId = parameters.statusId;

localResult = dispatcher.runSync("crudServiceDefaultOrchestration_WorkEffortRootStatus", ["parameters": inputMap, "userLogin": context.userLogin, "operation": "CREATE", "entityName": "WorkEffortStatus", "locale" : locale]);
Debug.log("*** ChangeAllStatus localResult " + localResult);
			
/**
 * Controllo se è andata a buon fine oppure no per aumentare il contatore
 */			
if(localResult.containsKey("responseMessage") && localResult.get("responseMessage").equals("success")){
	itemSuccess++;
	
	serviceInMap = dispatcher.getDispatchContext().makeValidContext("executeWorkEffortStatusQueryConfig", ModelService.IN_PARAM, parameters);
	serviceInMap.put("locale", locale);
	serviceInMap.put("workEffortId", parameters.workEffortId);
	serviceInMap.put("statusId", parameters.statusId);
	Debug.log("*** ChangeAllStatus Run sync service executeWorkEffortStatusQueryConfig with "+ serviceInMap + ", userLoginId = " + context.userLogin.userLoginId);
	serviceInMap.put("userLogin", context.userLogin);
    resService = dispatcher.runSync("executeWorkEffortStatusQueryConfig", serviceInMap);
	Debug.log("*** ChangeAllStatus resService " + resService);
	if(!ServiceUtil.isSuccess(resService)) {
	    Debug.log("*** ChangeAllStatus errore ");
	    errorList.add(ServiceUtil.getErrorMessage(resService));
        queryConfigError = uiLabelMap.WorkEffortStatusExecuteQueryconfigError;
	};
	
	if (localResult.containsKey(ModelService.FAIL_MESSAGE)) {
	    warningList.add(localResult.get(ModelService.FAIL_MESSAGE));
	    itemWarning++;
	}
    // reset for next iteration
    localResult = ServiceUtil.returnSuccess();
	
} else {
    errorList.add(ServiceUtil.getErrorMessage(localResult));
	itemFailed++;
	// reset for next iteration
	localResult = ServiceUtil.returnSuccess();
}
res = "success";

Debug.log("*** ChangeAllStatus itemSuccess " + itemSuccess);
Debug.log("*** ChangeAllStatus itemWarning " + itemWarning);
Debug.log("*** ChangeAllStatus itemFailed " + itemFailed);

if (ServiceUtil.isError(localResult)) {
    res = "error";
    request.setAttribute("_ERROR_MESSAGE_", localResult);
} else if (UtilValidate.isNotEmpty(queryConfigError)) {
    res = "error";
//    errorList.add(0, uiLabelMap.ChangeStatusAll_finished + "<br>" + uiLabelMap.ChangeStatusAll_itemSuccess + itemSuccess + "<br>" + uiLabelMap.ChangeStatusAll_itemFailed + itemFailed + "<br>" + queryConfigError);
//    
    request.setAttribute("_ERROR_MESSAGE_LIST_", errorList);
} else if (UtilValidate.isNotEmpty(noPrevStatusError)) {
    res = "error";
//    errorList.add(0, uiLabelMap.ChangeStatusAll_finished + "<br>" + uiLabelMap.ChangeStatusAll_itemSuccess + itemSuccess + "<br>" + uiLabelMap.ChangeStatusAll_itemFailed + itemFailed + "<br>" + noPrevStatusError);
//    
    request.setAttribute("_ERROR_MESSAGE_LIST_", errorList);
} else if (UtilValidate.isNotEmpty(errorList)) {
    res = "error";
//    errorList.add(0, uiLabelMap.ChangeStatusAll_finished + "<br>" + uiLabelMap.ChangeStatusAll_itemSuccess + itemSuccess + "<br>" + uiLabelMap.ChangeStatusAll_itemFailed + itemFailed + "<br>" + uiLabelMap.WorkEffortStatusChangeError);
//    
    request.setAttribute("_ERROR_MESSAGE_LIST_", errorList);
}
if (UtilValidate.isNotEmpty(warningList)) {
    request.setAttribute("failMessageList", warningList);
}
//request.setAttribute("itemSuccess", itemSuccess);
//request.setAttribute("itemWarning", itemWarning);
//request.setAttribute("itemFailed", itemFailed);

return res;
