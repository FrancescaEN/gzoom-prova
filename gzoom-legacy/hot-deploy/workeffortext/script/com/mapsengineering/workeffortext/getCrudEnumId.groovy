import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;
import org.ofbiz.service.ServiceUtil;


def result = ServiceUtil.returnSuccess();

def crudEnumId = "";

def workEffortId = parameters.workEffortId;
def folder = parameters.folder;

def nowStamp = UtilDateTime.nowTimestamp();
Debug.log("Script getCrudEnumId.groovy workEffortId " + workEffortId + " for folder " + folder);

def workEffortTypeStatusCntAndWorkEffortConditions = [];
workEffortTypeStatusCntAndWorkEffortConditions.add(EntityCondition.makeCondition("workEffortId", workEffortId));
workEffortTypeStatusCntAndWorkEffortConditions.add(EntityCondition.makeCondition("contentId", folder));
def workEffortTypeStatusCntAndWorkEffortList = delegator.findList("WorkEffortTypeStatusCntAndWorkEffort", EntityCondition.makeCondition(workEffortTypeStatusCntAndWorkEffortConditions), null, null, null, false);
def workEffortTypeStatusCntAndWorkEffortItem = EntityUtil.getFirst(workEffortTypeStatusCntAndWorkEffortList);

if (UtilValidate.isNotEmpty(workEffortTypeStatusCntAndWorkEffortItem)) {
	crudEnumId = workEffortTypeStatusCntAndWorkEffortItem.crudEnumId;
}
Debug.log("getCrudEnumId.groovy crudEnumId " + crudEnumId);

def nowStamp2 = UtilDateTime.nowTimestamp();
Debug.log("Run action script in " + (nowStamp2.getTime() - nowStamp.getTime()) + " milliseconds. script = component://workeffortext/script/com/mapsengineering/workeffortext/getCrudEnumId.groovy");

result.put("crudEnumId", crudEnumId);
return result;