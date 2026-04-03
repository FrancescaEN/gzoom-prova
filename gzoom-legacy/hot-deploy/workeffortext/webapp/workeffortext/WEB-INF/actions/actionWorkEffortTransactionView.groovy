import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;
import com.mapsengineering.workeffortext.util.WorkEffortTypeCntParamsEvaluator;


/** Servizio per il recuepro dei dati per il pannello del Valore del folder Indicatori */
context.operation = parameters.operation;

context.workEffortId = UtilValidate.isEmpty(parameters.workEffortId) ? parameters.weTransWeId : parameters.workEffortId;

long startTime = System.currentTimeMillis();
GroovyUtil.runScriptAtLocation("component://workeffortext/webapp/workeffortext/WEB-INF/actions/checkStatusCrudEnum.groovy", context);
long endTime = System.currentTimeMillis();
Debug.log("Run script in " + (endTime - startTime) + " milliseconds at location = component://workeffortext/webapp/workeffortext/WEB-INF/actions/checkStatusCrudEnum.groovy");


context.periodTypeId = UtilValidate.isEmpty(context.periodTypeId) ? parameters.weMeasurePeriodTypeId : context.periodTypeId;

context.customTimePeriodId = UtilValidate.isEmpty(context.customTimePeriodId) ? parameters.customTimePeriodId : context.customTimePeriodId;


context.glFiscalTypeIdList = delegator.findList("GlFiscalType", EntityCondition.makeCondition( 
									EntityCondition.makeCondition("isFinancialUsed", EntityOperator.EQUALS, "Y"),
									EntityOperator.OR,
									EntityCondition.makeCondition("isAccountUsed", EntityOperator.EQUALS, "Y"))						
									, null, null, null, true);

context.rootInqyTree = parameters.rootInqyTree;
context.isForcedReadOnly = parameters.isForcedReadOnly;


/*inizio GN-2727*/
def valuesDetails = "N";
def hideReference = "N";
def elabScoreIndic = "N";  //N, SCORE, INDIC
def contentIdInd = UtilValidate.isNotEmpty(parameters.contentIdInd) ? parameters.contentIdInd : context.contentIdInd;
def contentId = UtilValidate.isNotEmpty(parameters.contentIdSecondary) ? parameters.contentIdSecondary : context.contentIdSecondary;
def fromValoriIndicatori = parameters.fromValoriIndicatori;

Debug.log(" fromValoriIndicatori " + fromValoriIndicatori + " - contentIdInd " + contentIdInd + " - contentId " + contentId);
if ("Y".equals(fromValoriIndicatori)) {
	if ("WEFLD_IND".equals(contentIdInd)) {
		contentId = "WEFLD_AIND";
	}
	if ("WEFLD_IND2".equals(contentIdInd)) {
		contentId = "WEFLD_AIND2";
	}
	if ("WEFLD_IND3".equals(contentIdInd)) {
		contentId = "WEFLD_AIND3";
	}
	if ("WEFLD_IND4".equals(contentIdInd)) {
		contentId = "WEFLD_AIND4";
	}
	if ("WEFLD_IND5".equals(contentIdInd)) {
		contentId = "WEFLD_AIND5";
	}
    if ("WEFLD_IND6".equals(contentIdInd)) {
        contentId = "WEFLD_AIND6";
    }
    if ("WEFLD_IND7".equals(contentIdInd)) {
        contentId = "WEFLD_AIND7";
    }
    if ("WEFLD_IND8".equals(contentIdInd)) {
        contentId = "WEFLD_AIND8";
    }
    if ("WEFLD_IND9".equals(contentIdInd)) {
        contentId = "WEFLD_AIND9";
    }
    if ("WEFLD_IND10".equals(contentIdInd)) {
        contentId = "WEFLD_AIND10";
    }
}

if (UtilValidate.isNotEmpty(contentId)) {
	def workEffortTypeId = "";
	def workEffort = delegator.findOne("WorkEffort", ["workEffortId" : context.workEffortId], false);
	if (UtilValidate.isNotEmpty(workEffort)) {
		workEffortTypeId = workEffort.workEffortTypeId;
	}
	if (UtilValidate.isNotEmpty(workEffortTypeId)) {
	    // Imposto come valore di default della data di calcolo, la data fine del periodo della scheda
	    if(UtilValidate.isNotEmpty(context.workEffortId)) {
            def workEffortParamView = delegator.findOne("WorkEffortAndTypePeriodAndThruDate", ["workEffortId" : context.workEffortId], false);
            if(UtilValidate.isNotEmpty(workEffortParamView)) {
                context.workEffortTypeId = workEffortParamView.workEffortTypeId;
                if(UtilValidate.isNotEmpty(workEffortParamView.workEffortTypePeriodId)) {
                    workEffortTypePeriodId = workEffortParamView.workEffortTypePeriodId;
                    def defaultSearchDate = ObjectType.simpleTypeConvert(workEffortParamView.thruDate, "Timestamp", null, locale);
                    // Debug.log(" defaultSearchDate " + defaultSearchDate);
                    parameters.searchDateCalculate = defaultSearchDate;
                    // la vera thruDate, utilizzata nel calcolo, in base anche al params elabActualDate,
                    // viene calcolata nel servizio elaboreteScoreIndicCrudServiceEpilog
                }
            }
        }
        WorkEffortTypeCntParamsEvaluator paramsEvaluator = new WorkEffortTypeCntParamsEvaluator(null, null, delegator);
		def mapParams = paramsEvaluator.getParams(workEffortTypeId, contentId, false);
		if (UtilValidate.isNotEmpty(mapParams)) {
			valuesDetails = mapParams.valuesDetails;
			hideReference = mapParams.hideReference;
			elabScoreIndic = mapParams.elabScoreIndic;
		}
	}
}
context.onlyWithBudget = UtilValidate.isNotEmpty(context.onlyWithBudget) ? context.onlyWithBudget : "N";
context.accountFilter = UtilValidate.isNotEmpty(context.accountFilter) ? context.accountFilter : "ALL"; //OBJ, NOOBJ, ALL

// Debug.log(" hideReference " + hideReference);
context.valuesDetails = valuesDetails;
context.hideReference = hideReference;
context.elabScoreIndic = elabScoreIndic;
/*fine GN-2727*/