import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.service.*;

def dispatcher = dctx.getDispatcher();
def operation = parameters.operation;

Debug.log(" Run sync service checkRoleTypeWeightUsed with showAvailability = " + parameters.parameters.showAvailability + " and typeEmployment = " + parameters.parameters.typeEmployment + " and roleTypeId " + parameters.parameters.roleTypeId);
// GN-88  warning  se supera la disponibilita'
if(operation.equals("CREATE") || operation.equals("UPDATE")) {
	if("Assignment".equals(parameters.parameters.typeEmployment)) {

		def wepaRoleTypeWeight = UtilValidate.isNotEmpty(parameters.parameters.wepaRoleTypeWeight) ? new Double(parameters.parameters.wepaRoleTypeWeight) : null;
		def roleTypeWeight = UtilValidate.isNotEmpty(parameters.parameters.roleTypeWeight) ? new Double(parameters.parameters.roleTypeWeight) : null;
		def roleTypeWeightActual = UtilValidate.isNotEmpty(parameters.parameters.roleTypeWeightActual) ? new Double(parameters.parameters.roleTypeWeightActual) : null;

		if ((UtilValidate.isNotEmpty(roleTypeWeight) && roleTypeWeight > wepaRoleTypeWeight)
		|| (UtilValidate.isNotEmpty(roleTypeWeightActual) && roleTypeWeightActual > wepaRoleTypeWeight)) {
			def failMessage = UtilProperties.getResourceBundleMap("WorkeffortExtErrorLabels", locale).ErroreAssegnazioneEccedenza;
			return ServiceUtil.returnFailure(failMessage);
		}
	} else if (parameters.parameters.roleTypeId.equals("WE_ASSIGNMENT")) {
	    // folder Risorse Umane, controllo se showAvailability == "Y" o "NOCHECK"
	    if(!"N".equals(parameters.parameters.showAvailability)) {
	        def workEffortPartyAssignmentSumRoleTypeWeight = [:];
	        workEffortPartyAssignmentSumRoleTypeWeight.userLogin = userLogin;
	        
	        workEffortPartyAssignmentSumRoleTypeWeight.partyId = parameters.parameters.partyId;
	        workEffortPartyAssignmentSumRoleTypeWeight.fromDate = parameters.parameters.fromDate;
	        workEffortPartyAssignmentSumRoleTypeWeight.thruDate = parameters.parameters.thruDate;
	        def searchDate = ObjectType.simpleTypeConvert(parameters.parameters.searchDate, "Timestamp", null, locale);
	        workEffortPartyAssignmentSumRoleTypeWeight.searchDate = searchDate;
	        
	        result = dispatcher.runSync("WorkEffortPartyAssignmentSumRoleTypeWeight", workEffortPartyAssignmentSumRoleTypeWeight);
	        if("Y".equals(parameters.parameters.showAvailability)) {
	            if(result.sumMax > 100) {
	                def failMessage = UtilProperties.getResourceBundleMap("WorkeffortExtErrorLabels", locale).ErroreAssegnazioneEccedenza;
	                return ServiceUtil.returnFailure(failMessage);
	            }
	        }
	    }
	}
}
