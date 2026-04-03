import org.ofbiz.base.util.*;
import com.mapsengineering.workeffortext.util.WorkEffortTypeCntParamsEvaluator;

def canModifyEtch = false;
def canModifySourceRef = false;
def canModifyUO = false;

/** GN-6948
Se uorespReadOnly = 'Y' UO mai editabile, altrimenti si prende in considerazione il codeLocked.
Se codeReadOnly = 'Y' SourceReferenceId mai editabile, altrimenti si prende in considerazione il codeLocked.
GN-448 Per le specializzazioni:
Se insertMode = 'Y' sempre editabili
Altrimenti
    se permessi %MGR_ADMIN sempre editabili
    altrimenti codeLocked = N sempre editabili
*/
// Aggiungo gestione ...
def nowStamp = UtilDateTime.nowTimestamp();
Debug.log("Script checkEtchAndSourceRefIdReadonlyWERoot.groovy parameters.workEffortId " + parameters.workEffortId + " workEffortTypeId " + context.workEffortTypeId + " - " + parameters.workEffortTypeId);

if ("Y".equals(context.insertMode)) {
    canModifyEtch = true;
    canModifyUO = true;
    canModifySourceRef = true;
} else if(checkModifyPermissions()) {
    canModifyEtch = true;
    canModifyUO = true;
    canModifySourceRef = true;
} else{
    def codeLocked = getCodeLocked();
	Debug.log("Script checkEtchAndSourceRefIdReadonlyWERoot.groovy codeLocked " + codeLocked);
    if ("Y".equals(codeLocked)) {
    	canModifyEtch = false;
    	canModifyUO = false;
    	canModifySourceRef = false;
    } else if("ONLY_CODE".equals(codeLocked)) {
    	canModifyEtch = true;
    	canModifyUO = true;
    	canModifySourceRef = false;
    } else {
    	canModifyEtch = true;
    	canModifyUO = true;
        canModifySourceRef = true;
    }
}
// se codeReadOnly = 'Y', il codice e' sempre read-only
// se uorespReadOnly = 'Y', l'UO e' sempre read-only

WorkEffortTypeCntParamsEvaluator paramsEvaluator = new WorkEffortTypeCntParamsEvaluator(parameters, parameters, delegator);
def map = paramsEvaluator.getParams(context.workEffortTypeId, "WEFLD_MAIN", false);
if (UtilValidate.isNotEmpty(map) && UtilValidate.isNotEmpty(map.codeReadOnly) && "Y".equals(map.codeReadOnly)) {
		canModifySourceRef = false; 
}
if (UtilValidate.isNotEmpty(map) && UtilValidate.isNotEmpty(map.uorespReadOnly) && "Y".equals(map.uorespReadOnly)) {
		canModifyUO = false;
}

context.isEtchReadOnly = (canModifyEtch == false);
context.isSourceRefReadOnly = (canModifySourceRef == false);
context.isUOReadOnly = (canModifyUO == false);

def checkModifyPermissions() {
    return security.hasPermission(context.canModifyEtchPermission, userLogin);
}

def getCodeLocked() {
    def codeReadOnly = 'N';
	def codeLocked = "";
	
    def folderIndex = UtilValidate.isNotEmpty(context.folderIndex) ? context.folderIndex : 0;
    def folderContentIds = context.folderContentIds;
    def contentId = ''; 
    if (folderContentIds != null && folderContentIds.size() > folderIndex) {
        contentId = folderContentIds[folderIndex];
    }
    Debug.log("[checkEtchAndSourceRefIdReadonlyWERoot.groovy] - parameters.workEffortId " + parameters.workEffortId + " and contentId " + contentId);
    
    def childView = delegator.findOne("WorkEffortTypeStatusCntChildView", ["workEffortId" : parameters.workEffortId, "contentId" : contentId], 
            false);
    if (UtilValidate.isNotEmpty(childView)) {
        // Debug.log("childView " + childView);
        codeLocked = childView.codeLocked;
    }
    if (UtilValidate.isEmpty(codeLocked)) {
        def parentView = delegator.findOne("WorkEffortTypeStatusCntParentView", ["workEffortId" : parameters.workEffortId, "contentId" : contentId], 
                false);
        if (UtilValidate.isNotEmpty(parentView)) {
            // Debug.log("parentView " + parentView);
            codeLocked = parentView.codeLocked;
        }       
    }
    Debug.log("[checkEtchAndSourceRefIdReadonlyWERoot.groovy] - codeLocked " + codeLocked);
    return codeLocked;
}

Debug.log("[checkEtchAndSourceRefIdReadonlyWERoot.groovy] - context.isUOReadOnly " + context.isUOReadOnly);
Debug.log("[checkEtchAndSourceRefIdReadonlyWERoot.groovy] - context.isEtchReadOnly " + context.isEtchReadOnly);
Debug.log("[checkEtchAndSourceRefIdReadonlyWERoot.groovy] - context.isSourceRefReadOnly " + context.isSourceRefReadOnly);
def nowStamp2 = UtilDateTime.nowTimestamp();
Debug.log("Run action script in " + (nowStamp2.getTime() - nowStamp.getTime()) + " milliseconds. script = component://workeffortext/webapp/workeffortext/WEB-INF/actions/checkEtchAndSourceRefIdReadonlyWERoot.groovy");
