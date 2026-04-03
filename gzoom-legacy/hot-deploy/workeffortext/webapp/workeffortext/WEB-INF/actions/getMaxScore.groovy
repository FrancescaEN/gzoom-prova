import org.ofbiz.service.ServiceUtil;

import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;
import com.mapsengineering.base.client.*;

def nowStamp = UtilDateTime.nowTimestamp();
Debug.log("Script getMaxScore.groovy parameters.workEffortId " + parameters.workEffortId + " or parameters.parentWorkEffortId " + parameters.parentWorkEffortId);
def wefromWetoEnumId = UtilValidate.isNotEmpty(context.wefromWetoEnumId) ? context.wefromWetoEnumId : parameters.wefromWetoEnumId;
Debug.log("Script getMaxScore.groovy parameters.workEffortTypeId " + parameters.workEffortTypeId + " or parameters.parentWorkEffortTypeId " + parameters.parentWorkEffortTypeId);
Debug.log("Script getMaxScore.groovy context.workEffortTypeIdTo " + context.workEffortTypeIdTo + " or context.workEffortTypeIdFrom " + context.workEffortTypeIdFrom);
Debug.log("Script getMaxScore.groovy wefromWetoEnumId " + wefromWetoEnumId + " context.showMaxScore " + context.showMaxScore);

// TODO parentWe?

if(wefromWetoEnumId == "WETAFROM") {
    sonWorkEffortTypeId = context.workEffortTypeIdTo;
}
else if(wefromWetoEnumId == "WETATO") {
    sonWorkEffortTypeId = context.workEffortTypeIdFrom;
}

if(!"N".equals(context.showMaxScore) && (UtilValidate.isEmpty(parameters.insertMode) || "N".equals(parameters.insertMode))) {
	Debug.log("Script getMaxScore.groovy parameters.workEffortId " + parameters.workEffortId + " or parameters.parentWorkEffortId " + parameters.parentWorkEffortId);
	Debug.log("Script getMaxScore.groovy parameters.workEffortTypeId " + parameters.workEffortTypeId + " or parameters.parentWorkEffortTypeId " + parameters.parentWorkEffortTypeId);
	
	parentWorkEffortType = delegator.findOne("WorkEffortType", ["workEffortTypeId" : parameters.workEffortTypeId], false);
	sonWorkEffortType = delegator.findOne("WorkEffortType", ["workEffortTypeId" : sonWorkEffortTypeId], false);
    Debug.log("Script getMaxScore.groovy parentWorkEffortType " + parentWorkEffortType);
    Debug.log("Script getMaxScore.groovy sonWorkEffortType " + sonWorkEffortType);
        
    context.maxScore = 1;    
	Debug.log("Script getMaxScore.groovy context.showMaxScore " + context.showMaxScore);
    Debug.log("Script getMaxScore.groovy parentWorkEffortType.weightKpi " + parentWorkEffortType.weightKpi);
    Debug.log("Script getMaxScore.groovy parentWorkEffortType.totalEnumIdKpi " + parentWorkEffortType.totalEnumIdKpi);
    Debug.log("Script getMaxScore.groovy parentWorkEffortType.weightSons " + parentWorkEffortType.weightSons);
    Debug.log("Script getMaxScore.groovy parentWorkEffortType.totalEnumIdSons " + parentWorkEffortType.totalEnumIdSons);
    Debug.log("Script getMaxScore.groovy parentWorkEffortType.weightAssocWorkEffort " + parentWorkEffortType.weightAssocWorkEffort);
    Debug.log("Script getMaxScore.groovy parentWorkEffortType.totalEnumIdAssoc " + parentWorkEffortType.totalEnumIdAssoc);
    Debug.log("Script getMaxScore.groovy context.assocWeight " + context.assocWeight);
    Debug.log("Script getMaxScore.groovy sonWorkEffortType.weightKpi " + sonWorkEffortType.weightKpi);
    Debug.log("Script getMaxScore.groovy sonWorkEffortType.totalEnumIdKpi " + sonWorkEffortType.totalEnumIdKpi);
    Debug.log("Script getMaxScore.groovy sonWorkEffortType.weightKpiControlSum " + sonWorkEffortType.weightKpiControlSum);
    Debug.log("Script getMaxScore.groovy sonWorkEffortType.weightSons " + sonWorkEffortType.weightSons);
    Debug.log("Script getMaxScore.groovy sonWorkEffortType.totalEnumIdSons " + sonWorkEffortType.totalEnumIdSons);
    Debug.log("Script getMaxScore.groovy sonWorkEffortType.weightAssocWorkEffort " + sonWorkEffortType.weightAssocWorkEffort);
    Debug.log("Script getMaxScore.groovy sonWorkEffortType.totalEnumIdAssoc " + sonWorkEffortType.totalEnumIdAssoc);
    Debug.log("Script getMaxScore.groovy sonWorkEffortType.weightControlSum " + sonWorkEffortType.weightControlSum);
    if ((parentWorkEffortType.weightKpi > 0 && "AVERAGE".equals(parentWorkEffortType.totalEnumIdKpi))
	|| (parentWorkEffortType.weightSons > 0 && "AVERAGE".equals(parentWorkEffortType.totalEnumIdSons))
	|| (parentWorkEffortType.weightAssocWorkEffort > 0 && "AVERAGE".equals(parentWorkEffortType.totalEnumIdAssoc))) {
		// CASO 1
    	Debug.log("CASO 1 Padre Media -> Peso Relazione = context.assocWeight " + context.assocWeight);
		context.maxScore = context.assocWeight;
	} else if ((sonWorkEffortType.weightKpi > 0 && "AVERAGE".equals(sonWorkEffortType.totalEnumIdKpi))) {
		// CASO 2
		Debug.log("CASO 2 Padre Somma -> Figlio Kpi Media");
		if ("WEIGHT".equals(context.showMaxScore)) {
			Debug.log("CASO 2 Padre Somma -> Figlio Kpi Media -> Peso = sonWorkEffortType.weightKpi " + sonWorkEffortType.weightKpi);
			context.maxScore = sonWorkEffortType.weightKpi;
		} else if ("SUM".equals(context.showMaxScore)) {
			Debug.log("CASO 2 Padre Somma -> Figlio Kpi Media -> Somma = sonWorkEffortType.weightKpiControlSum " + sonWorkEffortType.weightKpiControlSum);
			context.maxScore = sonWorkEffortType.weightKpiControlSum;
		}
	} else if ((sonWorkEffortType.weightSons > 0 && "AVERAGE".equals(sonWorkEffortType.totalEnumIdSons))) {
		// CASO 3
		Debug.log("CASO 3 Padre Somma -> Figlio Figli Media");
		if ("WEIGHT".equals(context.showMaxScore)) {
			Debug.log("CASO 3 Padre Somma -> Figlio Figli Media -> Peso = sonWorkEffortType.weightSons " + sonWorkEffortType.weightSons);
			context.maxScore = sonWorkEffortType.weightSons;
		} else if ("SUM".equals(context.showMaxScore)) {
			Debug.log("CASO 3 Padre Somma -> Figlio Figli Media -> Somma = sonWorkEffortType.weightControlSum " + sonWorkEffortType.weightControlSum);
			context.maxScore = sonWorkEffortType.weightControlSum;
		}
	} else if ((sonWorkEffortType.weightAssocWorkEffort > 0 && "AVERAGE".equals(sonWorkEffortType.totalEnumIdAssoc))) {
		// CASO 4
		Debug.log("CASO 4 Padre Somma -> Figlio Coll Media");
		if ("WEIGHT".equals(context.showMaxScore)) {
			Debug.log("CASO 4 Padre Somma -> Figlio Collegati Media -> Peso = sonWorkEffortType.weightAssocWorkEffort " + sonWorkEffortType.weightAssocWorkEffort);
			context.maxScore = sonWorkEffortType.weightAssocWorkEffort;
		} else if ("SUM".equals(context.showMaxScore)) {
			Debug.log("CASO 4 Padre Somma -> Figlio Collegati Media -> Somma = sonWorkEffortType.weightControlSum " + sonWorkEffortType.weightControlSum);
			context.maxScore = sonWorkEffortType.weightControlSum;
		}
	} else if ((sonWorkEffortType.weightKpi > 0 && "SUM".equals(sonWorkEffortType.totalEnumIdKpi))) {
		// CASO 5
		Debug.log("CASO 5 Padre Somma -> Figlio Kpi Somma = sonWorkEffortType.weightKpiControlSum " + sonWorkEffortType.weightKpiControlSum);
		context.maxScore = sonWorkEffortType.weightKpiControlSum;
	} else if ((sonWorkEffortType.weightSons > 0 && "SUM".equals(sonWorkEffortType.totalEnumIdSons))) {
		// CASO 6
		Debug.log("CASO 6 Padre Somma -> Figlio Figli Somma = sonWorkEffortType.weightControlSum " + sonWorkEffortType.weightControlSum);
		context.maxScore = sonWorkEffortType.weightControlSum;
	} else if ((sonWorkEffortType.weightAssocWorkEffort > 0 && "SUM".equals(sonWorkEffortType.totalEnumIdAssoc))) {
		// CASO 7
		Debug.log("CASO 7 Padre Somma -> Figlio Collegati Somma = sonWorkEffortType.weightControlSum " + sonWorkEffortType.weightControlSum);
		context.maxScore = sonWorkEffortType.weightControlSum;
	}
}

def nowStamp2 = UtilDateTime.nowTimestamp();
Debug.log("Run action script in " + (nowStamp2.getTime() - nowStamp.getTime()) + " milliseconds. script = component://workeffortext//webapp//workeffortext//WEB-INF//actions//getMaxScore.groovy");
