import org.ofbiz.base.util.*;
import org.ofbiz.entity.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;
import com.mapsengineering.workeffortext.util.WorkEffortTypeCntParamsEvaluator;

/** Servizio per recuperare la data di calcolo del punteggio o degli indicatori */
def workEffortId = "";
if ("Y".equals(parameters.isObiettivo)) {
	workEffortId = UtilValidate.isNotEmpty(parameters.workEffortId) ? parameters.workEffortId : context.workEffortId;
} else {
	workEffortId = UtilValidate.isNotEmpty(parameters.workEffortIdRoot) ? parameters.workEffortIdRoot : context.workEffortIdRoot;
}

def workEffortTypeId = "";
def workEffortTypePeriodId = "";

context.elabActualDate = "N"; // Y, N

// Debug.log("populateSearchDateAndParams.groovy context.elabActualDate " + context.elabActualDate);
// Debug.log("populateSearchDateAndParams.groovy workEffortId " + workEffortId);

// Imposto come valore di default della data di calcolo, la data fine del periodo della scheda
if(UtilValidate.isNotEmpty(workEffortId)) {
	def workEffortParamView = delegator.findOne("WorkEffortAndTypePeriodAndThruDate", ["workEffortId" : workEffortId], false);
    if(UtilValidate.isNotEmpty(workEffortParamView)) {
		workEffortTypeId = workEffortParamView.workEffortTypeId;
		if(UtilValidate.isNotEmpty(workEffortParamView.workEffortTypePeriodId)) {
			workEffortTypePeriodId = workEffortParamView.workEffortTypePeriodId;
			parameters.searchDate = UtilDateTime.toDateString(workEffortParamView.thruDate, locale);
			parameters.thruDate = parameters.searchDate;
		}
	}
}

if(UtilValidate.isEmpty(parameters.isObiettivo) || !"Y".equals(parameters.isObiettivo)) {
    /** Recupero params */
	WorkEffortTypeCntParamsEvaluator paramsEvaluator = new WorkEffortTypeCntParamsEvaluator(context, parameters, delegator);
	paramsEvaluator.evaluateParams(workEffortTypeId, parameters.contentId, false);
}

// sovrascrive la data di calcolo con la data fine del periodo di consuntivo aperto
if ("Y".equals(context.elabActualDate)) {
    orderByThruDate = ["-thruDate"];
    def periodConditionList = [];
    periodConditionList.add(EntityCondition.makeCondition("workEffortTypeId", workEffortTypeId));
    periodConditionList.add(EntityCondition.makeCondition("glFiscalTypeEnumId", "GLFISCTYPE_ACTUAL"));
    periodConditionList.add(EntityCondition.makeCondition("organizationId", context.defaultOrganizationPartyId));
    periodConditionList.add(EntityCondition.makeCondition("statusEnumId", EntityOperator.IN, ["OPEN", "DETECTABLE"]));
    def periodList = delegator.findList("WorkEffortTypePeriodAndFromThruDate", EntityCondition.makeCondition(periodConditionList), null, orderByThruDate, null, false);
    if(UtilValidate.isNotEmpty(periodList)) {
        def period = EntityUtil.getFirst(periodList);
        if(UtilValidate.isNotEmpty(period)) {
            parameters.thruDate = UtilDateTime.toDateString(period.thruDate, locale);
        }
    }
}
    
Debug.log("populateSearchDateAndParams.groovy context.elabActualDate " + context.elabActualDate);