import org.ofbiz.base.util.*;
import com.mapsengineering.workeffortext.util.WorkEffortTypeCntParamsEvaluator;
import org.ofbiz.service.ServiceUtil;

result = ServiceUtil.returnSuccess();

def nowStamp = UtilDateTime.nowTimestamp();
Debug.log("Script getWefldMainParams.groovy parameters.workEffortTypeId " + parameters.workEffortTypeId);

def showEtch = "Y";
def showCode = "Y";
def showEtchField = "Y";
def codeReadOnly = "N";
def uorespReadOnly = "N";
def hideTreeView = "N";
def hideResponsible = "N";
def startDate = "PARENT";  //PARENT OPEN
def onlyRefDate = "N";
def showRoleType = "Y";
def noThruDate = "N";
def insertTitlePosition = "FIRST"; //FIRST LAST
def usePeriod = "";
def onlyAdminDateModify = "";


/** Esistono anche
deleteCopy, che serve per cancellare l' eventuale associativa con il catalogo
updateCopy, che serve per aggiornare il thuDate dell'associativa  con il catalogo*/

WorkEffortTypeCntParamsEvaluator paramsEvaluator = new WorkEffortTypeCntParamsEvaluator(parameters, parameters, delegator);
def map = paramsEvaluator.getParams(parameters.workEffortTypeId, "WEFLD_MAIN", false);
if (UtilValidate.isNotEmpty(map)) {
	if (UtilValidate.isNotEmpty(map.showCode)) {
		showCode = map.showCode;
	}
	if (UtilValidate.isNotEmpty(map.showEtchField)) {
		showEtchField = map.showEtchField;
	}
	if (UtilValidate.isNotEmpty(map.codeReadOnly)) {
		codeReadOnly = map.codeReadOnly;
	}
	if (UtilValidate.isNotEmpty(map.uorespReadOnly)) {
		uorespReadOnly = map.uorespReadOnly;
	}
	if (UtilValidate.isNotEmpty(map.showEtch)) {
		showEtch = map.showEtch;
	}
	if (UtilValidate.isNotEmpty(map.hideTreeView)) {
		hideTreeView = map.hideTreeView;
	}	
	if (UtilValidate.isNotEmpty(map.hideResponsible)) {
		hideResponsible = map.hideResponsible;
	}	
	if (UtilValidate.isNotEmpty(map.startDate)) {
		startDate = map.startDate;
	}
	if (UtilValidate.isNotEmpty(map.onlyRefDate)) {
		onlyRefDate = map.onlyRefDate;
	}
	if (UtilValidate.isNotEmpty(map.showRoleType)) {
		showRoleType = map.showRoleType;
	}
	if (UtilValidate.isNotEmpty(map.noThruDate)) {
		noThruDate = map.noThruDate;
	}
	if (UtilValidate.isNotEmpty(map.insertTitlePosition)) {
		insertTitlePosition = map.insertTitlePosition;
	}	
	if (UtilValidate.isNotEmpty(map.usePeriod)) {
		usePeriod = map.usePeriod;
	}
}

def nowStamp2 = UtilDateTime.nowTimestamp();
Debug.log("Run action script in " + (nowStamp2.getTime() - nowStamp.getTime()) + " milliseconds. script = component://workeffortext/script/com/mapsengineering/workeffortext/getWefldMainParams.groovy");

result.put("showCode", showCode);
result.put("showEtchField", showEtchField);
result.put("showEtch", showEtch);
result.put("codeReadOnly", codeReadOnly);
result.put("uorespReadOnly", uorespReadOnly);
result.put("hideTreeView", hideTreeView);
result.put("hideResponsible", hideResponsible);
result.put("startDate", startDate);
result.put("onlyRefDate", onlyRefDate);
result.put("showRoleType", showRoleType);
result.put("noThruDate", noThruDate);
result.put("insertTitlePosition", insertTitlePosition);
result.put("usePeriod", usePeriod);
result.put("onlyAdminDateModify", onlyAdminDateModify);
return result;