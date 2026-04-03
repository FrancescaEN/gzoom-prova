import org.ofbiz.base.util.*;
import org.ofbiz.service.*;
import com.mapsengineering.base.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;

res = "success";

context.entityNamePrefix = "WorkEffortRoot";
def nowStamp = UtilDateTime.nowTimestamp();
Debug.log("Script executePerformFindWorkEffortRoot.groovy context.entityNamePrefix " + context.entityNamePrefix);

res = GroovyUtil.runScriptAtLocation("com/mapsengineering/workeffortext/executePerformFindWorkEffortRootInqy.groovy", context);

if (res == "success") {
	// check if this is massive-print-search or export-search 
    res = GroovyUtil.runScriptAtLocation("com/mapsengineering/base/checkExportSearchResult.groovy", context);
}
def nowStamp2 = UtilDateTime.nowTimestamp();
Debug.log("Run action script in " + (nowStamp2.getTime() - nowStamp.getTime()) + " milliseconds. script = component://workeffortext/script/com/mapsengineering/workeffortext/executePerformFindWorkEffortRoot.groovy");

return res;
