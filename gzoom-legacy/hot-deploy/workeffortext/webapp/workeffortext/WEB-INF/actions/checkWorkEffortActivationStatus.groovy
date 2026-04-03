import org.ofbiz.base.util.*;

/** Force context.isReadOnly = true if parameters.rootInqyTree = Y or parameters.snapshot = Y or parameters.isForcedReadOnly = Y */

def nowStamp = UtilDateTime.nowTimestamp();
Debug.log("[checkWorkEffortActivationStatus.groovy] - parameters.workEffortIdRoot " + parameters.workEffortIdRoot + " - parameters.workEffortId " + parameters.workEffortId);
//Debug.log("[checkWorkEffortActivationStatus.groovy] - Form is read-only because context.isReadOnly " + context.isReadOnly + " - " + parameters.isReadOnly + " context.isForcedReadOnly " + context.isForcedReadOnly + " - " + parameters.isForcedReadOnly);
//Debug.log("[checkWorkEffortActivationStatus.groovy] - Form is read-only because context.insertMode " + context.insertMode + " parameters.rootInqyTree " + parameters.rootInqyTree + " parameters.snapshot " + parameters.snapshot);
if (!"Y".equals(context.get("insertMode"))) {
	
	isForcedReadOnly = "Y".equals(parameters.rootInqyTree) || "Y".equals(parameters.snapshot) || "Y".equals(parameters.isForcedReadOnly) ? "Y" : "N";
	Debug.log("[checkWorkEffortActivationStatus.groovy] - isForcedReadOnly " + isForcedReadOnly + " context.isReadOnly " + context.isReadOnly + " context.isForcedReadOnly " + context.isForcedReadOnly + " - " + parameters.isForcedReadOnly);

    if ("Y".equals(isForcedReadOnly)) {
        context.isReadOnly = true;
    } else {
		context.isReadOnly = false || (UtilValidate.isNotEmpty(context.isReadOnly) && context.isReadOnly) || "Y".equals(parameters.isReadOnly);
    }
} else {
	context.isReadOnly = false;
	context.isForcedReadOnly = "N";
	parameters.isForcedReadOnly = "N";
}
Debug.log("[checkWorkEffortActivationStatus.groovy] - context.isReadOnly " + context.isReadOnly + " context.isForcedReadOnly " + context.isForcedReadOnly + " - " + parameters.isForcedReadOnly);
def nowStamp2 = UtilDateTime.nowTimestamp();
Debug.log("Run action script in " + (nowStamp2.getTime() - nowStamp.getTime()) + " milliseconds. script = component://workeffortext/webapp/workeffortext/WEB-INF/actions/checkWorkEffortActivationStatus.groovy");
