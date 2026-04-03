import org.ofbiz.base.util.*;
import org.ofbiz.entity.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;
import com.mapsengineering.workeffortext.util.WorkEffortTypeCntParamsEvaluator;
import com.mapsengineering.workeffortext.util.FromAndThruDatesProviderFromParams;

def uiLabelMap = UtilProperties.getResourceBundleMap("CustomUiLabels", locale);
uiLabelMap.addBottomResourceBundle("WorkeffortExtUiLabels");
// poiche abbiamo aggiunto il params per ordinamento (sortField) occorre
// prima recuperare i params e 
// poi fare la ricerca
context.showOrgUnit = "N"; // Y, N, R, A
context.showDates = "N";
context.showAssocWeight = "Y";
context.showScoreValue = "N";  //Y, N, WEIGHTED
context.showMaxScore = "N";  //Y, SUM, WEIGHT
context.showScoreFiscalType = "ACTUAL"; // ACTUAL, ecc...
context.showRelationship = "Y";
context.showComment = "N";
context.assocLevelSameUO = "N";
context.assocLevelParentUO = "N";
context.assocLevelChildUO = "N";
context.assocLevelSisterUO = "N";
context.assocLevelTopUO = "N";
context.orgUnitIdRelation = context.defaultOrganizationPartyId;
context.detailEnabled = "Y";
context.showEtch = "C"; // Y, N, C
context.showSequence = "Y"; // Y, RIGHT
context.showDescription = "N"; // Y, N
context.limitCopyWe = ""; //ONLY_WE
context.assocLevelSameUOAss = "N";
context.assocLevelParentUOAss = "N";
context.assocLevelChildUOAss = "N";
context.assocLevelSisterUOAss = "N";
context.assocLevelTopUOAss = "N";
context.arrayNumRows = "0";
context.insertChoice = "N";
context.usePeriod = "";
context.onlyOpenDate = "N";
context.etchPunteggio = uiLabelMap.Punteggio;
context.etchRisultatoPesato = uiLabelMap.RisultatoPesato;

def nowStamp = UtilDateTime.nowTimestamp();
Debug.log("Script executeChildPerformFindWorkEffortAssocExtFromSpecial.groovy parameters.workEffortId " + parameters.workEffortId + " workEffortTypeId " + context.workEffortTypeId + " - " + parameters.workEffortTypeId);

context.assocParentSelected = ""; // per esempio 15APSARR
context.entityNameExtended = ""; // valorizzato per esempio con "AssocRef" se assocParentSelected valorizzato,
// Attenzione, esiste nei params anche il sortField, che ha come valore di default quello riportato nel file di properties
// quindi qui non e' possibile impostarlo vuoto
// context.sortField = context.sortField;
if(UtilValidate.isNotEmpty(context.sortField)) {
    // il sortField, contenuto nei params viene utilizzato nell'executeChildPerformFind.groovy
    parameters.sortField = context.sortField;
}

def workEffortTypeId = parameters.workEffortTypeId != null ? parameters.workEffortTypeId : null;

if(UtilValidate.isEmpty(workEffortTypeId)){
	def workEffort = delegator.findOne("WorkEffort", ["workEffortId" : parameters.workEffortId], false);
	if(UtilValidate.isNotEmpty(workEffort)){
		workEffortTypeId = workEffort.workEffortTypeId;
	}
}

parameters.contentId = UtilValidate.isNotEmpty(parameters.contentId) ? parameters.contentId : "WEFLD_WEFROM";


context.inputFields.workEffortIdFrom = parameters.workEffortId;
if(UtilValidate.isEmpty(parameters.snapshot) || ! "Y".equals(parameters.snapshot)) {
	context.inputFields.wrToSnapShotId_fld0_op = "empty";
	context.inputFields.wrFromSnapShotId_fld0_op = "empty";
}
context.inputFields.wrToActivation_op = "notEqual";
context.inputFields.wrToActivation = "ACTSTATUS_REPLACED";
context.inputFields.wrFromAssocContentId = parameters.contentId;
context.inputFields.wrFromAssocIsUnique = "N";
context.inputFields.wrFromWefromWetoEnumId = "WETAFROM";

if(UtilValidate.isEmpty(parameters.isObiettivo) || !"Y".equals(parameters.isObiettivo)) {
	/** Recupero params */
	WorkEffortTypeCntParamsEvaluator paramsEvaluator = new WorkEffortTypeCntParamsEvaluator(context, parameters, delegator);
	paramsEvaluator.evaluateParams(workEffortTypeId, parameters.contentId, false);
}

if(UtilValidate.isNotEmpty(context.sortField)) {
    // il sortField, contenuto nei params viene utilizzato nell'executeChildPerformFind.groovy
    parameters.sortField = context.sortField;
}
Debug.log("executeChildPerformFindWorkEffortAssocExtFromSpecial context.sortField " + context.sortField + " parameters.sortField " + parameters.sortField);

context.Ass = getAss();
Debug.log("executeChildPerformFindWorkEffortAssocExtFromSpecial context.entityNameExtended " + context.entityNameExtended);
if (UtilValidate.isNotEmpty(context.assocParentSelected) && UtilValidate.isNotEmpty(parameters.workEffortIdRef)) { 
    // siamo in inserimento
    context.entityNameExtended = "AssocRef"; // "WorkEffortViewAssocRef";
    context.workEffortAssocTypeIdRef = context.assocParentSelected; // per esempio 15APSARR
    context.workEffortIdRef = parameters.workEffortIdRef; // per esempio E161269
}
Debug.log("executeChildPerformFindWorkEffortAssocExtFromSpecial context.entityNameExtended " + context.entityNameExtended);


/**
 * Filtro la lista per searchDate
 */
GroovyUtil.runScriptAtLocation("component://workeffortext/webapp/workeffortext/WEB-INF/actions/populateFolderDateParams.groovy", context);

Debug.log("executeChildPerformFindWorkEffortAssocExtFromSpecial First search " + context.entityName + " with condition " + context.inputFields + ", then filter with contentId = " + (UtilValidate.isNotEmpty(parameters.contentId) ? parameters.contentId : "WEFLD_WEFROM"));
GroovyUtil.runScriptAtLocation("component://base/webapp/common/WEB-INF/actions/executeChildPerformFind.groovy", context);

// nomi dei campi, che devono essere disponibili nella droplist, in modo da poterli vedere all'apertura della droplist
context.workEffortNameField = "Y".equals(context.localeSecondarySet) ?  "workEffortNameLang" : "workEffortName";
context.workEffortTypeDescriptionField = "Y".equals(context.localeSecondarySet) ?  "weTypeDescriptionLang" : "weTypeDescription";
context.workEffortDescriptionField = "Y".equals(context.localeSecondarySet) ? "descriptionLang" : "description";
context.weOrgPartyDescrField = "Y".equals(context.localeSecondarySet) ?  "weOrgPartyDescrLang" : "weOrgPartyDescr";
context.weParentNameField = "Y".equals(context.localeSecondarySet) ?  "weParentNameLang" : "weParentName";

// valorizzato o meno in base ai parametri, perche se showOrgUnit = Y, il workEffortNameValue resta vuoto e non viene valorizzato
context.workEffortNameValue = "";
// campi utilizzati in caso di showOrgUnit per il render dell'obiettivo
// contiene il valore dell'unico campo da mostrare, per esempio weOrgPartyDescrField se showOrgUnit = Y
context.completeValue = "";
//contiene il valore del campo da mostrare insieme al workEffortName
context.partialValue = "";
//contiene il valore del campo da mostrare tra etch e sourceReferenceId


context.workEffortNameDescription = "";
context.orderByField = context.workEffortNameField;
if (UtilValidate.isNotEmpty(context.showOrgUnit) && !"N".equals(context.showOrgUnit)) {
	context.orderByField = context.weOrgPartyDescrField;
	context.completeValue = context.weOrgPartyDescrField;
	context.workEffortNameDescription = "Y".equals(context.localeSecondarySet) ? "@{weOrgPartyDescrLang}" : "@{weOrgPartyDescr}";
	if ("R".equals(context.showOrgUnit)) {
	    context.orderByField = "weParentName";
	    context.completeValue = "";
	    context.partialValue = context.weParentNameField;
	    context.workEffortNameValue = context.workEffortNameField;
	    context.workEffortNameDescription = "Y".equals(context.localeSecondarySet) ?  "@{weParentNameLang} - @{workEffortNameLang}" : "@{weParentName} - @{workEffortName}";
	} else if ("A".equals(context.showOrgUnit)) {
	    context.orderByField = "weOrgPartyDescr";
        context.completeValue = "";
	    context.partialValue = context.weOrgPartyDescrField;
	    context.workEffortNameValue = context.workEffortNameField;
	    context.workEffortNameDescription = "Y".equals(context.localeSecondarySet) ?  "@{weOrgPartyDescrLang} - @{workEffortNameLang}" : "@{weOrgPartyDescr} - @{workEffortName}";
	} 
	Debug.log("executeChildPerformFindWorkEffortAssocExtFromSpecial context.showOrgUnit " + context.showOrgUnit);
} else{
	if ("Y".equals(context.showEtch)) {
		context.partialValue = "weEtch";
		context.orderByField = context.partialValue;
		context.workEffortNameDescription = "@{weEtch} - ";
		Debug.log("executeChildPerformFindWorkEffortAssocExtFromSpecial context.showEtch " + context.showEtch);
	}  else if ("C".equals(context.showEtch)){
		context.partialValue = "sourceReferenceId";
		context.orderByField = context.partialValue;
		context.workEffortNameDescription = "@{sourceReferenceId} -";
		Debug.log("executeChildPerformFindWorkEffortAssocExtFromSpecial context.showEtch " + context.showEtch);
	}
	context.workEffortNameValue = context.workEffortNameField;
	context.workEffortNameDescription = context.workEffortNameDescription + ("Y".equals(context.localeSecondarySet) ? " @{workEffortNameLang}" : " @{workEffortName}");
	Debug.log("executeChildPerformFindWorkEffortAssocExtFromSpecial context.workEffortNameValue " + context.workEffortNameValue);
}

// in base alla relazione selezione, viene popolato il campo workEffortTypeIdRef
def workEffortTypeAssocAndAssocTypeList = delegator.findByAnd("WorkEffortTypeAssocAndAssocType", [workEffortTypeId: workEffortTypeId, wefromWetoEnumId: "WETAFROM", contentId: parameters.contentId, isUnique: "N"]);
def mapKey = [];
for(GenericValue w: workEffortTypeAssocAndAssocTypeList){
	def map = [:];
	map.workEffortAssocTypeId = w.workEffortAssocTypeId;
	map.workEffortTypeIdRef = w.workEffortTypeIdRef;
	mapKey.add(map);
}

def listReturn = [];

if (UtilValidate.isNotEmpty(context.listIt)) {
	for (GenericValue value: context.listIt) {
		def map = [:];
		map.workEffortAssocTypeId = value.workEffortAssocTypeId;
		map.workEffortTypeIdRef = value.workEffortTypeIdTo;
		
	    if(mapKey.contains(map) || value.workEffortAssocTypeId == null){
	    	def mapValue = [:];
			mapValue.putAll(value);
			
			if (UtilValidate.isNotEmpty(value.workEffortIdTo)) {
				def parentFrom = delegator.findOne("WorkEffort", ["workEffortId" : value.workEffortIdTo], false);				
				if (UtilValidate.isNotEmpty(parentFrom)) {
					mapValue.worEffortParentIdFrom = parentFrom.workEffortParentId;
				}
			}
			
			if (UtilValidate.isNotEmpty(value.workEffortIdFrom)) {
				def parentTo = delegator.findOne("WorkEffort", ["workEffortId" : value.workEffortIdFrom], false);				
				if (UtilValidate.isNotEmpty(parentTo)) {
					mapValue.worEffortParentIdTo = parentTo.workEffortParentId;
				}
			}
			
		    listReturn.add(mapValue);
	    }
	}
}
context.listIt = listReturn;

def getAss() {
	if ("Y".equals(context.assocLevelSameUOAss) || "Y".equals(context.assocLevelParentUOAss) || "Y".equals(context.assocLevelChildUOAss) || "Y".equals(context.assocLevelSisterUOAss) || "Y".equals(context.assocLevelTopUOAss)) {
		return "Ass";
	}
	return "";
}
