import org.ofbiz.base.util.*;
import org.ofbiz.service.*;
import com.mapsengineering.base.util.*;
// import java.text.*;
// import org.ofbiz.entity.condition.EntityCondition;
// import org.ofbiz.entity.condition.EntityOperator;

// import com.mapsengineering.base.services.async.AsyncJobUtil;
// import java.nio.ByteBuffer;

def result = ServiceUtil.returnSuccess();
def serviceName = "gzStandardImport"

def jobLogId = "";
def recordElaborated = "0";
def blockingErrors = "0";
def warningMessages = "0";
		
// Prepara mappa per il servizio (serviceName), e aggiunge in seguito gli altri campi
def serviceInMap = dctx.makeValidContext(serviceName, ModelService.IN_PARAM, parameters);
def serviceResult = dctx.getDispatcher().runSync(serviceName, serviceInMap);

Debug.log("gzStandardImportGroovy.groovy -> serviceResult="+serviceResult);
if (UtilValidate.isNotEmpty(serviceResult.resultList) && serviceResult.resultList.size() > 0) {
	Debug.log("#### serviceResult.resultList " + serviceResult.resultList);
	resultMap = serviceResult.resultList[0];
	// Nel caso di WeRootInterface, i valori effettivi dell'importazione si vedono nel primo record
	if ("Importazione Standard".equals(resultMap.entityName)) {
		Debug.log("#### resultMap " + resultMap);
		jobLogId = resultMap.jobLogId;
		recordElaborated = (String) resultMap.recordElaborated;
		blockingErrors = (String) resultMap.blockingErrors;
		warningMessages = (String) resultMap.warningMessages;
	}
}
result.put("jobLogId", jobLogId);
result.put("recordElaborated", recordElaborated);
result.put("blockingErrors", blockingErrors);
result.put("warningMessages", warningMessages);
Debug.log("result " + result);
return result;
