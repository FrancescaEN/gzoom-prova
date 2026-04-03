import org.ofbiz.base.util.*;


/**
 * readOnly crudEnum definito dallo stato per ogni singolo folder,
 * tranne in obiettivo la differenza dei due viene fatta tramite il parametro 'isObiettivo'
 * oppure in schede obiettivo, dove la differenza dei due viene fatta tramite il parametro 'specialized'
 * utilizzato anche nei layout personalizzati dei folder di indicatori e risorse, passandogli contentIdSecondary e workEffortIdSecondary
*/

def nowStamp = UtilDateTime.nowTimestamp();
Debug.log("Script checkStatusCrudEnum.groovy parameters.workEffortId " + parameters.workEffortId + " context.workEffortId " + context.workEffortId + " parameters.contentId " + parameters.contentId + " context.contentId " + context.contentId);
Debug.log("[checkStatusCrudEnum.groovy] - context.rootInqyTree " + context.rootInqyTree + " parameters.rootInqyTree " + parameters.rootInqyTree );
Debug.log("[checkStatusCrudEnum.groovy] - context.isForcedReadOnly " + context.isForcedReadOnly + " parameters.isForcedReadOnly " + parameters.isForcedReadOnly );
Debug.log("[checkStatusCrudEnum.groovy] - context.isReadOnly " + context.isReadOnly + " parameters.isReadOnly " + parameters.isReadOnly );

def isObiettivo = UtilValidate.isNotEmpty(parameters.isObiettivo) ? parameters.isObiettivo : context.isObiettivo;
if (UtilValidate.isNotEmpty(isObiettivo) && isObiettivo == "Y") {
    Debug.log("[checkStatusCrudEnum.groovy] - isObiettivo " + isObiettivo );
    return;
}

def specialized = UtilValidate.isNotEmpty(parameters.specialized) ? parameters.specialized : context.specialized;
if (UtilValidate.isEmpty(specialized) || specialized == "N") {
    Debug.log("[checkStatusCrudEnum.groovy] - specialized " + specialized );
    return;
}
def workEffortId = UtilValidate.isNotEmpty(parameters.workEffortId) ? parameters.workEffortId : context.workEffortId;
// Debug.log("********************************** checkStatusCrudEnum parameters.workEffortId  "+ parameters.workEffortId );
// Debug.log("********************************** checkStatusCrudEnum context.workEffortId  "+ context.workEffortId );
Debug.log("[checkStatusCrudEnum.groovy] - parameters.workEffortId " + parameters.workEffortId + " or context.workEffortId " + context.workEffortId );
    
def contentId = UtilValidate.isNotEmpty(parameters.contentId) ?  parameters.contentId : context.contentId;
// Debug.log("********************************** checkStatusCrudEnum parameters.contentId  "+ parameters.contentId );
// Debug.log("********************************** checkStatusCrudEnum context.contentId  "+ context.contentId );
// Debug.log("********************************** checkStatusCrudEnum contentId  "+ contentId );

/**
 * Nel caso degli indicatori il content lo trovo nella variabile "contentIdInd"
 */
def contentIdInd = UtilValidate.isNotEmpty(parameters.contentIdInd) ? parameters.contentIdInd : context.contentIdInd;
if (UtilValidate.isNotEmpty(contentIdInd)) {
	contentId = contentIdInd;
	Debug.log("[checkStatusCrudEnum.groovy] - Folder degli indicatori contentId = " + contentId);
}
/**
 * Nel caso del contextLink per valori indicatori il content lo trovo nella variabile "contentIdSecondary"
 */
if (UtilValidate.isEmpty(contentId) && UtilValidate.isNotEmpty(parameters.contentIdSecondary)) {
	contentId = parameters.contentIdSecondary;
	context.contentId = parameters.contentIdSecondary;
	Debug.log("********************************** checkStatusCrudEnum ContextLink per valori indicatori contentId = "+ contentId );
}


if (UtilValidate.isEmpty(contentId)) {
	def folderIndex = UtilValidate.isEmpty(parameters.folderIndex) ? UtilValidate.isEmpty(context.folderIndex) ? 0 : Integer.valueOf(context.folderIndex) : Integer.valueOf(parameters.folderIndex);
	
	def folderContentIds = context.folderContentIds;
	if (UtilValidate.isNotEmpty(folderContentIds) && folderContentIds.size() > folderIndex) {
		contentId = folderContentIds[folderIndex];
	}
	
}
Debug.log("********************************** checkStatusCrudEnum contentId  "+ contentId );
/**
 * Nel caso di layout customizzato con formato che mostra sia gli indicatori che i rispettivi valori
 */
def contentIdSecondary = UtilValidate.isNotEmpty(parameters.contentIdSecondary) ? parameters.contentIdSecondary : context.contentIdSecondary;
// Debug.log("********************************** checkStatusCrudEnum contentIdSecondary  "+ contentIdSecondary );
def workEffortIdSecondary = UtilValidate.isNotEmpty(parameters.workEffortIdSecondary) ? parameters.workEffortIdSecondary : context.workEffortIdSecondary;
// Debug.log("********************************** checkStatusCrudEnum workEffortIdSecondary  "+ workEffortIdSecondary );
workEffortIdSecondary = UtilValidate.isNotEmpty(workEffortIdSecondary) ? workEffortIdSecondary : workEffortId;
if (UtilValidate.isNotEmpty(contentIdSecondary)) {
	def crudEnumIdSecondary = "";
	// Debug.log("********************************** checkStatusCrudEnum contentIdSecondary  "+ contentIdSecondary );
	// Debug.log("********************************** checkStatusCrudEnum workEffortIdSecondary  "+ workEffortIdSecondary );
	def childViewSec = delegator.findOne("WorkEffortTypeStatusCntChildView", ["workEffortId" : workEffortIdSecondary, "contentId" : contentIdSecondary], false);
	if (UtilValidate.isNotEmpty(childViewSec)) {
		// Debug.log("********************************** checkStatusCrudEnum childViewSec  "+ childViewSec );
		crudEnumIdSecondary = childViewSec.crudEnumId;
	} else {
		def parentViewSec = delegator.findOne("WorkEffortTypeStatusCntParentView", ["workEffortId" : workEffortIdSecondary, "contentId" : contentIdSecondary], false);
		if (UtilValidate.isNotEmpty(parentViewSec)) {
			//Debug.log("********************************** checkStatusCrudEnum parentView  "+ parentView );
			crudEnumIdSecondary = parentViewSec.crudEnumId;
		}
	}
	context.crudEnumIdSecondary = crudEnumIdSecondary;
	Debug.log("********************************** checkStatusCrudEnum context.crudEnumIdSecondary  "+ context.crudEnumIdSecondary );
}

def crudEnumId = "";
//Debug.log("********************************** checkStatusCrudEnum contentId  "+ contentId );
// Debug.log("********************************** checkStatusCrudEnum workEffortId  "+ workEffortId );

//Devo gestire anche i casi dove ho i layout del folder
//prima di cercare nella vista, controllo se il contentId che mi e' stato pssato e' un di tipo FOLDER
//altrimenti sono nel caso si layout e prendo il contentTypeId (che in questo caso e' il folder ) per fai la ricerca

def content = delegator.findOne("Content", ["contentId" : contentId], false);
if (UtilValidate.isNotEmpty(content) && UtilValidate.isNotEmpty(content.contentTypeId) && content.contentTypeId != "FOLDER") {
	contentId = content.contentTypeId;
}

def childView = delegator.findOne("WorkEffortTypeStatusCntChildView", ["workEffortId" : workEffortId, "contentId" : contentId], false);
if (UtilValidate.isNotEmpty(childView)) {
	// Debug.log("********************************** checkStatusCrudEnum childView  "+ childView );
	crudEnumId = childView.crudEnumId;
} else {
	def parentView = delegator.findOne("WorkEffortTypeStatusCntParentView", ["workEffortId" : workEffortId, "contentId" : contentId], false);
	if (UtilValidate.isNotEmpty(parentView)) {
		//Debug.log("********************************** checkStatusCrudEnum parentView  "+ parentView );
		crudEnumId = parentView.crudEnumId;
	}
}

context.crudEnumId = crudEnumId;


// Debug.log("********************************** checkStatusCrudEnum contentId  "+ contentId );
// Debug.log("********************************** checkStatusCrudEnum workEffortId  "+ workEffortId );
Debug.log("[checkStatusCrudEnum.groovy] - context.crudEnumId " + context.crudEnumId );
def nowStamp2 = UtilDateTime.nowTimestamp();
Debug.log("Run action script in " + (nowStamp2.getTime() - nowStamp.getTime()) + " milliseconds. script = component://workeffortext/webapp/workeffortext/WEB-INF/actions/checkStatusCrudEnum.groovy");
Debug.log("[checkStatusCrudEnum.groovy] - context.rootInqyTree " + context.rootInqyTree + " parameters.rootInqyTree " + parameters.rootInqyTree );
Debug.log("[checkStatusCrudEnum.groovy] - context.isForcedReadOnly " + context.isForcedReadOnly + " parameters.isForcedReadOnly " + parameters.isForcedReadOnly );
Debug.log("[checkStatusCrudEnum.groovy] - context.isReadOnly " + context.isReadOnly + " parameters.isReadOnly " + parameters.isReadOnly );
