import org.ofbiz.base.util.*;
import java.text.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;
import org.ofbiz.service.ServiceUtil;


result = ServiceUtil.returnSuccess();

def personComments = "";
def personAllocation = "";
def personEmployment = "";
def partyId = parameters.partyId;
def weTypeSubFilter = parameters.weTypeSubFilter;
def fromDate = parameters.fromDate;
def thruDate = parameters.thruDate;
def periodFromDate = parameters.periodFromDate;
def periodThruDate = parameters.periodThruDate;
def showEmployment = parameters.showEmployment;
    
def person = delegator.findOne("Person", ["partyId" : partyId], false);
if (UtilValidate.isNotEmpty(person) && UtilValidate.isNotEmpty(person.comments)) {
	personComments = person.comments;
}
if ("PERSON".equals(weTypeSubFilter)) {
	if (UtilValidate.isNotEmpty(periodFromDate)) {
		def customTimePriodFrom = delegator.findOne("CustomTimePeriod",["customTimePeriodId": periodFromDate], false);
		if (UtilValidate.isNotEmpty(customTimePriodFrom)) {
			fromDate = customTimePriodFrom.fromDate;
		}
	}
	if (UtilValidate.isNotEmpty(periodThruDate)) {
		def customTimePriodThru = delegator.findOne("CustomTimePeriod",["customTimePeriodId": periodThruDate], false);
		if (UtilValidate.isNotEmpty(customTimePriodThru)) {
			thruDate = customTimePriodThru.thruDate;
		}
	}	
	
	def partyRelationShipCondList = [];
	partyRelationShipCondList.add(EntityCondition.makeCondition("partyIdTo", partyId));
	partyRelationShipCondList.add(EntityCondition.makeCondition("partyRelationshipTypeId", "ORG_ALLOCATION"));
	partyRelationShipCondList.add(EntityCondition.makeCondition("fromDate", fromDate));
	partyRelationShipCondList.add(EntityCondition.makeCondition("thruDate", thruDate));
	def partyRelationshipList = delegator.findList("PartyRelationship", EntityCondition.makeCondition(partyRelationShipCondList), null, null, null, false);
	if (UtilValidate.isNotEmpty(partyRelationshipList)) {
		def partyIdFromList = EntityUtil.getFieldListFromEntityList(partyRelationshipList, "partyIdFrom", true);
		personAllocation = StringUtil.join(partyIdFromList, ",");
	} else {
		personAllocation = StringUtil.join(["![null-field],"], ",");
	}
}

if ("Y".equals(showEmployment)) {
    def partyRelEmploymentCondList = [];
    partyRelEmploymentCondList.add(EntityCondition.makeCondition("partyIdTo", partyId));
    partyRelEmploymentCondList.add(EntityCondition.makeCondition("partyRelationshipTypeId", "ORG_EMPLOYMENT"));
    partyRelEmploymentCondList.add(EntityCondition.makeCondition("fromDate", EntityOperator.LESS_THAN_EQUAL_TO, thruDate));
    partyRelEmploymentCondList.add( //EntityCondition.makeCondition("thruDate", thruDate));
    
    EntityCondition.makeCondition(
            EntityCondition.makeCondition("thruDate", null),
            EntityOperator.OR,
            EntityCondition.makeCondition("thruDate", EntityOperator.GREATER_THAN_EQUAL_TO, fromDate)
            ));
    
    Debug.log("partyRelEmploymentCondList " + partyRelEmploymentCondList);
    def partyRelEmplList = delegator.findList("PartyRelationship", EntityCondition.makeCondition(partyRelEmploymentCondList), null, null, null, false);
    if (UtilValidate.isNotEmpty(partyRelEmplList)) {
        def partyIdFromList = EntityUtil.getFieldListFromEntityList(partyRelEmplList, "partyIdFrom", true);
        for(item in partyIdFromList) {
            def partyGroup = delegator.findOne("PartyGroup", UtilMisc.toMap("partyId", item), false);
            personEmployment = (UtilValidate.isNotEmpty(personEmployment)) ? personEmployment + ", " + partyGroup.groupName : partyGroup.groupName;
        }
    }
}
result.put("personComments", personComments);
result.put("personAllocation", personAllocation);
result.put("personEmployment", personEmployment);
return result;