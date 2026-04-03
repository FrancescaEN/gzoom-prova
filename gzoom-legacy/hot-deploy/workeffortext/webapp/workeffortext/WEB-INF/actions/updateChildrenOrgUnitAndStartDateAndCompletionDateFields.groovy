import org.ofbiz.base.util.*;
import org.ofbiz.entity.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;
import org.ofbiz.service.*;


def nowStamp = UtilDateTime.nowTimestamp();

def workEffortId = context.inputFields.workEffortId;
def workEffortParentId = context.inputFields.workEffortParentId;
def weHierarchyTypeId = context.inputFields.weHierarchyTypeId;
def orgUnitRoleTypeId = context.inputFields.orgUnitRoleTypeId;
def orgUnitId = context.inputFields.orgUnitId;
def oldOrgUnitId = context.inputFields.oldOrgUnitId;
def estimatedStartDate = context.inputFields.estimatedStartDate;
def oldEstimatedStartDate = context.inputFields.oldEstimatedStartDate;
def estimatedCompletionDate = context.inputFields.estimatedCompletionDate;
def oldEstimatedCompletionDate = context.inputFields.oldEstimatedCompletionDate;

def updateOrgUnitId = UtilValidate.isNotEmpty(orgUnitId) && ! orgUnitId.equals(oldOrgUnitId);
def updateEstimatedStartDate = UtilValidate.isNotEmpty(estimatedStartDate) && ! estimatedStartDate.equals(oldEstimatedStartDate);
def updateEstimatedCompletionDate = UtilValidate.isNotEmpty(estimatedCompletionDate) && ! estimatedCompletionDate.equals(oldEstimatedCompletionDate);
Debug.log("Script updateChildrenOrgUnitAndStartDateAndCompletionDateFields.groovy updateOrgUnitId " + updateOrgUnitId + "updateEstimatedStartDate " + updateEstimatedStartDate + " updateEstimatedCompletionDate " + updateEstimatedCompletionDate);

def workEffortConditionList = [];
workEffortConditionList.add(EntityCondition.makeCondition("workEffortIdFrom", workEffortId));
workEffortConditionList.add(EntityCondition.makeCondition("wrToParentId", workEffortParentId));
workEffortConditionList.add(EntityCondition.makeCondition("wrToParentId", workEffortParentId));
workEffortConditionList.add(EntityCondition.makeCondition("workEffortAssocTypeId", weHierarchyTypeId));

Debug.log("Script updateChildrenOrgUnitAndStartDateAndCompletionDateFields.groovy  Search WorkEffortAssocAndParentView with " + workEffortConditionList);
def toUpdateByCompletionDateList = [];
def workEffortList = delegator.findList("WorkEffortAssocAndParentView", EntityCondition.makeCondition(workEffortConditionList), null, null, null, false);
if (UtilValidate.isNotEmpty(workEffortList)) {
	for (GenericValue workEffortItem : workEffortList) {
	    def workEffort = delegator.findOne("WorkEffort", ["workEffortId" : workEffortItem.workEffortIdTo], false);
	    if (UtilValidate.isNotEmpty(workEffort)) {
			def toStore = false;
			Debug.log("  workEffort.orgUnitId " + workEffort.orgUnitId + " updateOrgUnitId " + updateOrgUnitId);
			if (updateOrgUnitId && UtilValidate.isNotEmpty(workEffort.orgUnitId) && workEffort.orgUnitId.equals(oldOrgUnitId)) {
			    Debug.log("Script updateChildrenOrgUnitAndStartDateAndCompletionDateFields.groovy workEffort da aggiornare " + workEffort.workEffortId + " per orgUnitId " + workEffort.orgUnitId + " in " + orgUnitId);
			    toStore = true;
				workEffort.orgUnitRoleTypeId = orgUnitRoleTypeId;
				workEffort.orgUnitId = orgUnitId;
			}						
			def oldWeEstimatedStartDate = workEffort.estimatedStartDate;
			if (updateEstimatedStartDate && UtilValidate.isNotEmpty(oldWeEstimatedStartDate) && oldWeEstimatedStartDate.equals(oldEstimatedStartDate)) {
			    Debug.log("Script updateChildrenOrgUnitAndStartDateAndCompletionDateFields.groovy workEffort da aggiornare " + workEffort.workEffortId + " per estimatedStartDate " + workEffort.estimatedStartDate + " in " + estimatedStartDate);
                toStore = true;
				workEffort.estimatedStartDate = estimatedStartDate;
			}			
			def oldWeEstimatedCompletionDate = workEffort.estimatedCompletionDate;
			if (updateEstimatedCompletionDate && UtilValidate.isNotEmpty(oldWeEstimatedCompletionDate) && oldWeEstimatedCompletionDate.equals(oldEstimatedCompletionDate)) {
			    Debug.log("Script updateChildrenOrgUnitAndStartDateAndCompletionDateFields.groovy workEffort da aggiornare " + workEffort.workEffortId + " per estimatedCompletionDate " + workEffort.estimatedCompletionDate + " in " + estimatedCompletionDate);
                toStore = true;
				workEffort.estimatedCompletionDate = estimatedCompletionDate;
			}
			if (toStore) {
				delegator.store(workEffort);
			}
            if ((updateEstimatedStartDate && UtilValidate.isNotEmpty(oldWeEstimatedStartDate) && oldWeEstimatedStartDate.equals(oldEstimatedStartDate)) || (updateEstimatedCompletionDate && UtilValidate.isNotEmpty(oldWeEstimatedCompletionDate) && oldWeEstimatedCompletionDate.equals(oldEstimatedCompletionDate))) {
				toUpdateByCompletionDateList.add(workEffort);
			}			
		}
	}
}

context.toUpdateByCompletionDateList = toUpdateByCompletionDateList;
def nowStamp2 = UtilDateTime.nowTimestamp();
Debug.log("Run action script in " + (nowStamp2.getTime() - nowStamp.getTime()) + " milliseconds. script = component://workeffortext/webapp/workeffortext/WEB-INF/actions/updateRootsChildrenOrgUnitAndStartDateAndCompletionDateFields.groovy");
