import org.ofbiz.base.util.*;
import org.ofbiz.base.crypto.HashCrypt;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;
import org.ofbiz.service.*;
import com.mapsengineering.base.util.ContextPermissionPrefixEnum;
import com.mapsengineering.workeffortext.util.WorkEffortTypeStatusParamsEvaluator;

/** DOPO

if (UtilValidate.isNotEmpty(request)) {
	languageSettinngs = request.getSession().getAttribute("languageSettinngs");
} */

res = "success";
localResult = ServiceUtil.returnSuccess();
errorList = [];

crudServiceEpilogGroovy = parameters.crudServiceEpilogGroovy;
Debug.log("Script CrudServiceEpilogGroovy.groovy parameters.crudServiceEpilogGroovy "+ parameters.crudServiceEpilogGroovy);
Debug.log("Script CrudServiceEpilogGroovy.groovy parameters.crudServiceEpilogGroovy_o_0 "+ parameters.crudServiceEpilogGroovy_o_0);
Debug.log("Script CrudServiceEpilogGroovy.groovy parameters.entityName "+ parameters.entityName);
Debug.log("Script CrudServiceEpilogGroovy.groovy parameters.operation "+ parameters.operation);
Debug.log("Script CrudServiceEpilogGroovy.groovy parameters.operation_o_0 "+ parameters.operation_o_0);
Debug.log("Script CrudServiceEpilogGroovy.groovy parameters.operation_o_1 "+ parameters.operation_o_1);
Debug.log("Script CrudServiceEpilogGroovy.groovy parameters.saveExecuteOperation "+ parameters.saveExecuteOperation);
//Debug.log("Script CrudServiceEpilogGroovy.groovy parameters.saveExecuteContentId "+ parameters.saveExecuteContentId);

if (UtilValidate.isEmpty(crudServiceEpilogGroovy) || ("DELETE".equals(parameters.operation) && "WorkEffort".equals(parameters.entityName))) {
    return res;
} else {
	serviceInMap = dispatcher.getDispatchContext().makeValidContext(crudServiceEpilogGroovy, ModelService.IN_PARAM, parameters);
	serviceInMap.put("saveExecuteOperation", "U");
	// inserimento vale solo per obiettivo
	if ("WorkEffort".equals(parameters.entityName) && "CREATE".equals(parameters.operation)) {
	    serviceInMap.put("saveExecuteOperation", "C");
	}
	Debug.log("Script CrudServiceEpilogGroovy.groovy Run sync service " + crudServiceEpilogGroovy + " with "+ serviceInMap + ", userLoginId = " + context.userLogin.userLoginId);
	serviceInMap.put("userLogin", context.userLogin);
	resService = dispatcher.runSync(crudServiceEpilogGroovy, serviceInMap);
    Debug.log("Script CrudServiceEpilogGroovy.groovy resService " + resService);
	if(!ServiceUtil.isSuccess(resService)) {
	    Debug.log("Script CrudServiceEpilogGroovy.groovy errore " + ServiceUtil.getErrorMessage(resService));
	    errorList.add(ServiceUtil.getErrorMessage(resService));
	};
}

if (UtilValidate.isNotEmpty(errorList)) {
    res = "error";
    request.setAttribute("_ERROR_MESSAGE_LIST_", errorList);
}

return res;
