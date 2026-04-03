import org.ofbiz.base.util.*;
import com.mapsengineering.workeffortext.util.WorkEffortTypeCntParamsEvaluator;

def nowStamp = UtilDateTime.nowTimestamp();
Debug.log("Script checkWorkEffortReadOnly.groovy ");
Debug.log("[checkWorkEffortReadOnly.groovy] - context.isReadOnly " + context.isReadOnly + " - " + parameters.isReadOnly + " context.isForcedReadOnly " + context.isForcedReadOnly);
Debug.log("[checkWorkEffortReadOnly.groovy] - context.insertMode " + context.insertMode + " parameters.snapshot " + parameters.snapshot);
Debug.log("[checkWorkEffortReadOnly.groovy] - context.rootInqyTree " + context.rootInqyTree + " parameters.rootInqyTree " + parameters.rootInqyTree);

// def rootInqyTree = parameters.rootInqyTree;
def workEffortIdRoot = parameters.workEffortIdRoot;
def workEffortId = parameters.workEffortId;

if (! "Y".equals(rootInqyTree)) {
	def childRootModify = "Y";
	def workEffortRoot = delegator.findOne("WorkEffort", ["workEffortId" : workEffortIdRoot], false);
	if (UtilValidate.isNotEmpty(workEffortRoot)) {
		WorkEffortTypeCntParamsEvaluator paramsEvaluator = new WorkEffortTypeCntParamsEvaluator(parameters, parameters, delegator);
		def map = paramsEvaluator.getParams(workEffortRoot.workEffortTypeId, "WEFLD_MAIN", false);
		if (UtilValidate.isNotEmpty(map) && UtilValidate.isNotEmpty(map.childRootModify)) {
			childRootModify = map.childRootModify;
		}
	}
	Debug.log("[checkWorkEffortReadOnly.groovy] - childRootModify " + childRootModify);
	if ("N".equals(childRootModify)) {
		def workEffort = delegator.findOne("WorkEffort", ["workEffortId" : workEffortId], false);
		if (UtilValidate.isNotEmpty(workEffortRoot) && UtilValidate.isNotEmpty(workEffort)) {
			if (! workEffortRoot.workEffortId.equals(workEffort.workEffortParentId)) {
				// DEVE DIVENTARE isForcedReadOnly
				context.isForcedReadOnly = "Y";
				parameters.isForcedReadOnly = "Y";
				// context.rootInqyTree = "Y";
				// parameters.rootInqyTree = "Y";
			}
		}
	}
}
Debug.log("[checkWorkEffortReadOnly.groovy] - context.rootInqyTree " + context.rootInqyTree + " parameters.rootInqyTree " + parameters.rootInqyTree);
Debug.log("[checkWorkEffortReadOnly.groovy] - context.isForcedReadOnly " + context.isForcedReadOnly + " parameters.isForcedReadOnly " + parameters.isForcedReadOnly);
def nowStamp2 = UtilDateTime.nowTimestamp();
Debug.log("Run action script in " + (nowStamp2.getTime() - nowStamp.getTime()) + " milliseconds. script = component://workeffortext/webapp/workeffortext/WEB-INF/actions/checkWorkEffortReadOnly.groovy");
