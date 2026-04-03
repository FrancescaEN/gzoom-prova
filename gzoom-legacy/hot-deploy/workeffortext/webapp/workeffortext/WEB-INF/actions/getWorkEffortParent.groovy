import org.ofbiz.base.util.*;
import org.ofbiz.entity.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.service.*;
import com.mapsengineering.base.util.*;

def nowStamp = UtilDateTime.nowTimestamp();

def workEffortId = UtilValidate.isNotEmpty(parameters.workEffortId) ? parameters.workEffortId : parameters.parentWorkEffortId;
Debug.log("Script getWorkEffortParent.groovy parameters.workEffortId " + parameters.workEffortId + " or parameters.parentWorkEffortId " + parameters.parentWorkEffortId);
// parentWorkEffortId e' il workeffort attuale
if(UtilValidate.isNotEmpty(workEffortId)) {
    context.parentWe = delegator.findOne("WorkEffort", ["workEffortId" : workEffortId], false);
}
def nowStamp2 = UtilDateTime.nowTimestamp();
Debug.log("Run action script in " + (nowStamp2.getTime() - nowStamp.getTime()) + " milliseconds. script = component://workeffortext//webapp//workeffortext//WEB-INF//actions//getWorkEffortParent.groovy");
