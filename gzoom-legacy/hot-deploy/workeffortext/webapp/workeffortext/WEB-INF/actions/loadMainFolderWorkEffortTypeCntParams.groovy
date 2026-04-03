import org.ofbiz.base.util.*;
import com.mapsengineering.workeffortext.util.WorkEffortTypeCntParamsEvaluator;

/** Mostra date Effettive */
context.showActualDates = "N"; // Y, N
/** Mostra date Originali */
context.showScheduledDates = "N"; // Y, N
context.etchDescr  = "";
context.fixedDatesHumanResource = "N";
context.showEtch = "Y";
context.showCode = "Y";
context.showEtchField = "Y";
/** Mostra solo una data */
context.onlyRefDate = "N"; // Y, N
context.showRoleType = "Y";
context.showType = "Y";
context.usePeriod = "";

/** Esistono anche
deleteCopy, che serve per cancellare l' eventuale associativa con il catalogo
updateCopy, che serve per aggiornare il thuDate dell'associativa  con il catalogo*/

/** Recupero params per il folder principale */
def workEffortTypeId = "";
if (! "Y".equals(parameters.insertMode)) {
	workEffortTypeId = UtilValidate.isNotEmpty(context.workEffortTypeId) ? context.workEffortTypeId : parameters.workEffortTypeId;
}
if (UtilValidate.isNotEmpty(workEffortTypeId)) {
    WorkEffortTypeCntParamsEvaluator paramsEvaluator = new WorkEffortTypeCntParamsEvaluator(context, parameters, delegator);
    paramsEvaluator.evaluateParams(workEffortTypeId, "WEFLD_MAIN", false);
    if (UtilValidate.isNotEmpty(context.isInsertMode) && context.isInsertMode) {
    	context.showType = "Y";
    }
}
