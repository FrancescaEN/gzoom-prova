import org.ofbiz.base.util.*;
import com.mapsengineering.base.birt.util.*;
import com.mapsengineering.base.util.ContextPermissionPrefixEnum;

def securityPermission = Utils.permissionLocalDispatcherName(dispatcher.name);


def checkWorkEffortPermissions() {
    return security.hasPermission("WORKEFFORTMGR_ADMIN", context.userLogin) || security.hasPermission("WORKEFFORTMGR_CREATE", context.userLogin) || security.hasPermission("WORKEFFORTMGR_UPDATE", context.userLogin) || security.hasPermission("WORKEFFORTORG_ADMIN", context.userLogin) || security.hasPermission("WORKEFFORTROLE_ADMIN", context.userLogin);
}
context.checkWorkEffortPermissions = checkWorkEffortPermissions();
Debug.log("context.checkWorkEffortPermissions " + context.checkWorkEffortPermissions);

/** Recupero workEffort, workEffortType, workEffortRoot, workEffortTypePeriodId */
def workEffortView = delegator.findOne("WorkEffortAndTypePeriodAndCustomTime", ["workEffortId" : parameters.workEffortId], false);
// Debug.log("workEffortView " + workEffortView);
// Debug.log("workEffortView.workEffortTypeRootId " + workEffortView.workEffortTypeRootId);
// Debug.log("parameters.specialized " + parameters.specialized);

def getIsAdminPermission(parentWorkEffortTypeId) {
    def contextPermissionPrefix = "WORKEFFORTMGR";
    if ("Y".equals(parameters.specialized) && UtilValidate.isNotEmpty(parentWorkEffortTypeId)) {
        def parentWorkEffortType = delegator.findOne("WorkEffortType", ["workEffortTypeId" : parentWorkEffortTypeId], false);
        // Debug.log("parentWorkEffortType " + parentWorkEffortType);
        if (UtilValidate.isNotEmpty(parentWorkEffortType)) {
            def weContextId = parentWorkEffortType.parentTypeId;
            contextPermissionPrefix = ContextPermissionPrefixEnum.getPermissionPrefix(weContextId); // + "MGR_ADMIN";
            Debug.log("contextPermissionPrefix " + contextPermissionPrefix);
        }
    }
    Debug.log("contextPermissionPrefix " + contextPermissionPrefix);
    return security.hasPermission(contextPermissionPrefix + "MGR_ADMIN", context.userLogin) || security.hasPermission(contextPermissionPrefix + "MGR_CREATE", context.userLogin) || security.hasPermission(contextPermissionPrefix + "MGR_UPDATE", context.userLogin) || security.hasPermission(contextPermissionPrefix + "ORG_ADMIN", context.userLogin) || security.hasPermission(contextPermissionPrefix + "ROLE_ADMIN", context.userLogin);
}
def isAdminPermission = getIsAdminPermission(workEffortView.workEffortTypeRootId);
context.isAdminPermission = isAdminPermission;
Debug.log("context.isAdminPermission " + context.isAdminPermission);

def commentsReadOnly = false;
def commentsRequired = false;

if (! "Y".equals(parameters.detail) && UtilValidate.isNotEmpty(parameters.weTypeSubId)) {
	def inputMap = [:];
	inputMap.workEffortId = parameters.workEffortId;
	inputMap.partyId = parameters.partyId;
	inputMap.roleTypeId = parameters.roleTypeId;
	inputMap.fromDate = parameters.fromDate;
	
	def currentWepa = delegator.findOne("WorkEffortPartyAssignment", inputMap, false);
	if (UtilValidate.isNotEmpty(currentWepa) && ("END_SOST_NN".equals(currentWepa.endReplacementEnumId) || "END_SOST_NAGR".equals(currentWepa.endReplacementEnumId))) {
		commentsReadOnly = false;
		commentsRequired = true;
	} else {
		commentsReadOnly = true;
		commentsRequired = false;
	}
}

context.commentsReadOnly = commentsReadOnly;
context.commentsRequired = commentsRequired;