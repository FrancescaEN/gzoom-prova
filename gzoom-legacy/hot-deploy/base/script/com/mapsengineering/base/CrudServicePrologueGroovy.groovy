import org.ofbiz.base.util.*;
import org.ofbiz.base.crypto.HashCrypt;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;
import org.ofbiz.service.*;
import com.mapsengineering.base.util.ContextPermissionPrefixEnum;
import com.mapsengineering.workeffortext.util.WorkEffortTypeStatusParamsEvaluator;

/**
 */

languageSettinngs = request.getSession().getAttribute("languageSettinngs");

res = "success";
localResult = ServiceUtil.returnSuccess();
errorList = [];

crudServicePrologueGroovy = parameters.crudServicePrologueGroovy;
Debug.log("Script CrudServicePrologueGroovy.groovy parameters.crudServicePrologueGroovy "+ parameters.crudServicePrologueGroovy);
Debug.log("Script CrudServicePrologueGroovy.groovy parameters.crudServicePrologueGroovy_o_0 "+ parameters.crudServicePrologueGroovy_o_0);
Debug.log("Script CrudServicePrologueGroovy.groovy parameters.entityName "+ parameters.entityName);
Debug.log("Script CrudServicePrologueGroovy.groovy parameters.operation "+ parameters.operation);
Debug.log("Script CrudServicePrologueGroovy.groovy parameters.operation_o_0 "+ parameters.operation_o_0);
Debug.log("Script CrudServicePrologueGroovy.groovy parameters.operation_o_1 "+ parameters.operation_o_1);
Debug.log("Script CrudServicePrologueGroovy.groovy parameters.saveExecuteOperation "+ parameters.saveExecuteOperation);

if (UtilValidate.isEmpty(crudServicePrologueGroovy) || !"DELETE".equals(parameters.operation)) {
    return res;
} else {
	serviceInMap = dispatcher.getDispatchContext().makeValidContext(crudServicePrologueGroovy, ModelService.IN_PARAM, parameters);
	serviceInMap.put("saveExecuteOperation", "D");
    Debug.log("Script CrudServicePrologueGroovy.groovy Run sync service " + crudServicePrologueGroovy + " with "+ serviceInMap + ", userLoginId = " + context.userLogin.userLoginId);
	serviceInMap.put("userLogin", context.userLogin);
    resService = dispatcher.runSync(crudServicePrologueGroovy, serviceInMap);
    Debug.log("Script CrudServicePrologueGroovy.groovy resService " + resService);
	if(!ServiceUtil.isSuccess(resService)) {
	    Debug.log("Script CrudServicePrologueGroovy.groovy errore " + ServiceUtil.getErrorMessage(resService));
	    errorList.add(ServiceUtil.getErrorMessage(resService));
	};
}

if (UtilValidate.isNotEmpty(errorList)) {
    res = "error";
    request.setAttribute("_ERROR_MESSAGE_LIST_", errorList);
}

return res;
