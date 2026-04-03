import org.ofbiz.base.util.*;
import org.ofbiz.service.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;

def result = "success";
def serviceName = "indicatorCalcObiettivo"

try {
	if (parameters.thruDate instanceof java.lang.String) {
		thruDate = ObjectType.simpleTypeConvert(parameters.thruDate, "Timestamp", null, locale);
	    parameters.thruDate = thruDate;    
	}
	// Prepara mappa per il servizio (serviceName), e aggiunge in seguito gli altri campi
	def serviceInMap = dispatcher.getDispatchContext().makeValidContext(serviceName, ModelService.IN_PARAM, parameters);
	serviceInMap.put("userLogin", context.userLogin);
	serviceInMap.put("locale", context.locale);
	serviceInMap.put("timeZone", timeZone);
	
	def serviceResult = dispatcher.runSync(serviceName, serviceInMap);
	Debug.log("indicatorCalcObiettivo.groovy -> serviceResult: " + serviceResult);
	request.setAttribute("jobLogId", serviceResult.jobLogId);
	request.setAttribute("recordElaborated", serviceResult.recordElaborated);
	request.setAttribute("blockingErrors", serviceResult.blockingErrors);
	request.setAttribute("warningMessages", serviceResult.warningMessages);
	
	if(ServiceUtil.isError(serviceResult)) {
		def errore = ServiceUtil.getErrorMessage(serviceResult);
		Debug.logError("indicatorCalcObiettivo.groovy -> errore: " + errore, "indicatorCalcObiettivo.groovy");
		request.setAttribute("_ERROR_MESSAGE_", errore);
		return "errorGroovy";
	}
} catch (e) {
	Debug.logError("indicatorCalcObiettivo.groovy -> e: " + e, "indicatorCalcObiettivo.groovy");
	def errore = ServiceUtil.returnError(e.getMessage());
	Debug.logError("indicatorCalcObiettivo.groovy -> errore " + errore, "indicatorCalcObiettivo.groovy");
	request.setAttribute("_ERROR_MESSAGE_", e.getMessage());
	return "errorGroovy";	
}		

groovyContext = new java.util.HashMap();
groovyContext.put("dispatcher", dispatcher);
groovyContext.put("delegator", delegator);
groovyContext.put("locale", locale);
groovyContext.put("timeZone", timeZone);
groovyContext.put("userLogin", userLogin);
groovyContext.put("response",response);
groovyContext.put("request",request);

parameters.put("saveExecuteContentId", "WEFLD_ELIN");
parameters.put("saveExecuteWorkEffortId", parameters.get("workEffortId"));
parameters.put("entityName", "WorkEffort");
parameters.put("crudServiceEpilogGroovy", "crudServiceEpilog_SaveExecute");
groovyContext.put("parameters", parameters);

GroovyUtil.runScriptAtLocation("com/mapsengineering/base/CrudServiceEpilogGroovy.groovy", groovyContext);

return result;