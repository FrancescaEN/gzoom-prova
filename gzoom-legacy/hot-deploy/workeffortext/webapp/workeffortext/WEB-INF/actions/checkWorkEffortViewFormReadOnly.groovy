import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;

def nowStamp = UtilDateTime.nowTimestamp();
Debug.log("Script checkWorkEffortViewFormReadOnly.groovy ");

// problema che nel context ho un valore diverso dai parameters, ma quello corretto e' nei parameters
def isForcedReadOnly= UtilValidate.isNotEmpty(parameters.isForcedReadOnly) ? parameters.isForcedReadOnly : context.isForcedReadOnly;

Debug.log("[checkWorkEffortViewFormReadOnly.groovy] - context.isReadOnly " + context.isReadOnly + " - " + parameters.isReadOnly + " context.isForcedReadOnly " + context.isForcedReadOnly + " - " + parameters.isForcedReadOnly);
Debug.log("[checkWorkEffortViewFormReadOnly.groovy] - context.insertMode " + context.insertMode + " parameters.rootInqyTree " + parameters.rootInqyTree + " parameters.snapshot " + parameters.snapshot);
Debug.log("[checkWorkEffortViewFormReadOnly.groovy] - context.crudEnumId " + context.crudEnumId + " - " + parameters.crudEnumId + " context.isPosted " + context.isPosted + " - " + parameters.isPosted);

def isReadOnlyField = UtilValidate.isNotEmpty(context.isReadOnly) ? context.isReadOnly : parameters.isReadOnly;
def isPosted = UtilValidate.isNotEmpty(context.isPosted) ? context.isPosted : parameters.isPosted;
def crudEnumId = UtilValidate.isNotEmpty(context.crudEnumId) ? context.crudEnumId : parameters.crudEnumId;

def hasPermission = security.hasPermission("WORKEFFORTMGR_CREATE", userLogin) || security.hasPermission("WORKEFFORTMGR_ADMIN", userLogin) || security.hasPermission("WORKEFFORTMGR_UPDATE", userLogin) || security.hasPermission("WORKEFFORTORG_ADMIN", userLogin) || security.hasPermission("WORKEFFORTROLE_ADMIN", userLogin);
Debug.log("[checkWorkEffortViewFormReadOnly.groovy] - hasPermission " + hasPermission);

def isReadOnly = false;
if (isReadOnlyField instanceof Boolean) {
	isReadOnly = isReadOnlyField;
} else if (isReadOnlyField instanceof String) {
	isReadOnly = "true".equalsIgnoreCase(isReadOnlyField);
}



def isWorkEffortViewFormReadOnly = "N";
// Debug.log(" isReadOnly " + isReadOnly);
// Debug.log(" isForcedReadOnly " + isForcedReadOnly);
// Debug.log(" isPosted " + isPosted);
// Debug.log(" crudEnumId " + crudEnumId);
// Debug.log(" hasPermission " + hasPermission);
if (isReadOnly || "Y".equals(isPosted) || "Y".equals(isForcedReadOnly) || "NONE".equals(crudEnumId) || "INSERT".equals(crudEnumId) || ! hasPermission) {
	isWorkEffortViewFormReadOnly = "Y";
}
Debug.log("[checkWorkEffortViewFormReadOnly.groovy] - context.isWorkEffortViewFormReadOnly " + isWorkEffortViewFormReadOnly);
Debug.log("[checkWorkEffortViewFormReadOnly.groovy] - Form is read-only " + isWorkEffortViewFormReadOnly + " because isReadOnly " + isReadOnly + " - isPosted " + isPosted + " - isForcedReadOnly " + isForcedReadOnly + " - crudEnumId " + crudEnumId + " hasPermission " + hasPermission);
context.isWorkEffortViewFormReadOnly = isWorkEffortViewFormReadOnly;

// se Form read-only compare solo la descrizione della UO
if ("Y".equals(isWorkEffortViewFormReadOnly)) {	
	def partyList = delegator.findList("PartyAndPartyParentRole", EntityCondition.makeCondition(EntityCondition.makeCondition("partyId", context.orgUnitId), EntityCondition.makeCondition("roleTypeId", "ORGANIZATION_UNIT")), null, null, null, false);
	def orgUnit = EntityUtil.getFirst(partyList);	
	if (UtilValidate.isNotEmpty(orgUnit)) {
	    // Debug.log("context.codeField " + context.codeField);
	    if (UtilValidate.isNotEmpty(context.codeField)) {
			context.orgUnitDesc = "Y".equals(context.localeSecondarySet) ? orgUnit.get(context.codeField) + " - " + orgUnit.partyNameLang : orgUnit.get(context.codeField) + " - " + orgUnit.partyName;
		} else {
			context.orgUnitDesc = "Y".equals(context.localeSecondarySet) ? orgUnit.partyNameLang : orgUnit.partyName;
		}
	}
}
// Debug.log("context.codeField " + context.codeField + "context.orgUnitDesc " + context.orgUnitDesc);

def nowStamp2 = UtilDateTime.nowTimestamp();
Debug.log("Run action script in " + (nowStamp2.getTime() - nowStamp.getTime()) + " milliseconds. script = component://workeffortext/webapp/workeffortext/WEB-INF/actions/checkWorkEffortViewFormReadOnly.groovy");
