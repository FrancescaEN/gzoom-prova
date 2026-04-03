import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;
import com.mapsengineering.base.util.ContextPermissionPrefixEnum;
import com.mapsengineering.workeffortext.util.WorkEffortTypeStatusParamsEvaluator;
import com.mapsengineering.workeffortext.util.WorkEffortTypeCntParamsEvaluator;

workEffortName = "";
weTypeDescription = "";
description = "";
weStatusDescr = ""; // descrizione dello stato, preso dalla radice e poi sovrascritto dal workEffortRootId
estimatedStartDate = null;
estimatedCompletionDate = null;
weStatusId = "";
processId = "";
noPreviousStatus = "";
context.statusChildModify = "Y";

def nowStamp = UtilDateTime.nowTimestamp();
Debug.log("Script retrieveOrganizationalAndStrategicPerformanceHeaderData.groovy ROOT albero parameters.workEffortIdRoot " + parameters.workEffortIdRoot + " and context.workEffortId " + context.workEffortId);

if (UtilValidate.isNotEmpty(parameters.workEffortIdRoot)) {
	rootWe = delegator.findOne("WorkEffort", ["workEffortId": parameters.workEffortIdRoot], false);
	
	if (UtilValidate.isNotEmpty(rootWe)) {
		
		//Root WorkEffort
		statusItem = rootWe.getRelatedOneCache("CurrentStatusItem");
		workEffortName = rootWe.workEffortName;
		workEffortNameLang = rootWe.workEffortNameLang;
		description = rootWe.description;
		descriptionLang = rootWe.descriptionLang;
		weStatusDescr = statusItem.description; // vengono sovrascritti da eventuale scheda presente nell'albero
		weStatusDescrLang = statusItem.descriptionLang; // vengono sovrascritti da eventuale scheda presente nell'albero
		weStatusId = rootWe.currentStatusId; // vengono sovrascritti da eventuale scheda presente nell'albero
		estimatedStartDate = rootWe.estimatedStartDate;
		estimatedCompletionDate = rootWe.estimatedCompletionDate;
		
		workEffortType = rootWe.getRelatedOneCache("WorkEffortType");
		weTypeDescription = workEffortType.description;
		weTypeDescriptionLang = workEffortType.descriptionLang;
		processId = rootWe.processId;

		oldWorkEffortId = context.workEffortId;
		
		context.workEffortId = parameters.workEffortIdRoot;
		/**Utilizzato per far visualizzare solo il responsabile*/
		context.onlyResponsible = "Y";
		
		context.currentStatusId = rootWe.currentStatusId;
		context.duplicateAdmit = "N"; // COPY, CLONE, SNAPSHOT
        GroovyUtil.runScriptAtLocation("component://workeffortext/webapp/workeffortext/WEB-INF/actions/loadWorkEffortTypeStatusParams.groovy", context);
        parameters.duplicateAdmit = context.duplicateAdmit;
        
        GroovyUtil.runScriptAtLocation("component://workeffortext/webapp/workeffortext/WEB-INF/actions/getWorkEffortResponsibleByRole.groovy", context);
		
		context.workEffortId = oldWorkEffortId;
	
		context.workEffortName = workEffortName;
		context.workEffortNameLang = workEffortNameLang;
		context.weTypeDescription = weTypeDescription;
		context.weTypeDescriptionLang = weTypeDescriptionLang;
		context.description = description;
		context.descriptionLang = descriptionLang;
		context.weStatusDescr = weStatusDescr;
		context.weStatusDescrLang = weStatusDescrLang;
		context.weStatusId = weStatusId;
		context.processId = processId;
		context.estimatedStartDate = estimatedStartDate;
		context.estimatedCompletionDate = estimatedCompletionDate;
		
		weContextId = UtilValidate.isNotEmpty(parameters.weContextId) ? parameters.weContextId : context.weContextId;
		retriveStatusInfo(parameters.workEffortIdRoot, rootWe.workEffortTypeId, rootWe.currentStatusId, weContextId, rootWe.workEffortTypePeriodId);
		
		def parentRel = "";
		def parentRelWe = "";
		context.showParentAssoc = "N";
		WorkEffortTypeCntParamsEvaluator paramsEvaluator = new WorkEffortTypeCntParamsEvaluator(context, parameters, delegator);
	    paramsEvaluator.evaluateParams(rootWe.workEffortTypeId, "WEFLD_MAIN", false);
	    if ("Y".equals(context.showParentAssoc)) {
	    	def workEffortTypeAssocCondList = [];
	    	workEffortTypeAssocCondList.add(EntityCondition.makeCondition("workEffortTypeId", rootWe.workEffortTypeId));
	    	workEffortTypeAssocCondList.add(EntityCondition.makeCondition("isParentRel", "Y"));
	    	def workEffortTypeAssocList = delegator.findList("WorkEffortTypeAssoc", EntityCondition.makeCondition(workEffortTypeAssocCondList), null, null, null, false);
	    	def workEffortTypeAssoc = EntityUtil.getFirst(workEffortTypeAssocList);
	    	if (UtilValidate.isNotEmpty(workEffortTypeAssoc)) {
	    		parentRel = "Y".equals(context.localeSecondarySet) ? workEffortTypeAssoc.commentsLang : workEffortTypeAssoc.comments;
	    		def workEffortAssocAndParentViewCondList = [];
	    		workEffortAssocAndParentViewCondList.add(EntityCondition.makeCondition("workEffortIdFrom", rootWe.workEffortId));
	    		workEffortAssocAndParentViewCondList.add(EntityCondition.makeCondition("workEffortAssocTypeId", workEffortTypeAssoc.workEffortAssocTypeId));
	    		def workEffortAssocAndParentViewList = delegator.findList("WorkEffortAssocAndParentView", EntityCondition.makeCondition(workEffortAssocAndParentViewCondList), null, null, null, false);
		    	def workEffortAssocAndParentView = EntityUtil.getFirst(workEffortAssocAndParentViewList);
		    	if (UtilValidate.isNotEmpty(workEffortAssocAndParentView)) {
		    		def weName = "Y".equals(context.localeSecondarySet) ? workEffortAssocAndParentView.wrToNameLang : workEffortAssocAndParentView.wrToName;
		    		parentRelWe = UtilValidate.isNotEmpty(workEffortAssocAndParentView.wrToEtch) ? workEffortAssocAndParentView.wrToEtch + " - " + weName : weName;
		    	} else {
		    		workEffortAssocAndParentViewCondList = [];
		    		workEffortAssocAndParentViewCondList.add(EntityCondition.makeCondition("workEffortIdTo", rootWe.workEffortId));
		    		workEffortAssocAndParentViewCondList.add(EntityCondition.makeCondition("workEffortAssocTypeId", workEffortTypeAssoc.workEffortAssocTypeId));
		    		workEffortAssocAndParentViewList = delegator.findList("WorkEffortAssocAndParentView", EntityCondition.makeCondition(workEffortAssocAndParentViewCondList), null, null, null, false);
			    	workEffortAssocAndParentView = EntityUtil.getFirst(workEffortAssocAndParentViewList);
			    	if (UtilValidate.isNotEmpty(workEffortAssocAndParentView)) {
			    		def weName = "Y".equals(context.localeSecondarySet) ? workEffortAssocAndParentView.wrFromNameLang : workEffortAssocAndParentView.wrFromName;
			    		parentRelWe = UtilValidate.isNotEmpty(workEffortAssocAndParentView.wrFromEtch) ? workEffortAssocAndParentView.wrFromEtch + " - " + weName : weName;
			    	}
		    	}
	    	}
	    }
		
		context.parentRel = parentRel;
		context.parentRelWe = parentRelWe;
	}
}

def weStatusId = "";
def weTypeParentEtch = "";
def weTypeParentEtchLang = "";
def workEffortParentName = "";
def workEffortParentNameLang = "";
def weTypeEtch = "";
def weTypeEtchLang = "";
def workEffortName = "";
def workEffortNameLang = "";
def orgUnit = "";
def orgUnitLang = "";
def workEffortParentId = "";
def workEffortId = context.workEffortId;

def workEffort = delegator.findOne("WorkEffort", ["workEffortId": workEffortId], false);

if (UtilValidate.isNotEmpty(workEffort)) {
	workEffortName = workEffort.workEffortName;
	workEffortNameLang = workEffort.workEffortNameLang;
	
	def workEffortParent = delegator.findOne("WorkEffort", ["workEffortId": workEffort.workEffortParentId], false); 
	def workEffortType = delegator.findOne("WorkEffortType", ["workEffortTypeId": workEffort.workEffortTypeId], false);
	def partyGroup = delegator.findOne("PartyGroup", ["partyId": workEffort.orgUnitId], false);
	def partyParentRole = delegator.findOne("PartyParentRole", ["partyId": workEffort.orgUnitId, "roleTypeId": "ORGANIZATION_UNIT"], false);
	
	if (UtilValidate.isNotEmpty(workEffortParent)) {
		weStatusId = workEffortParent.currentStatusId;
		workEffortParentId = workEffortParent.workEffortId;
		workEffortParentName = workEffortParent.workEffortName;
		workEffortParentNameLang = workEffortParent.workEffortNameLang;
		
		def workEffortTypeParent = delegator.findOne("WorkEffortType", ["workEffortTypeId": workEffortParent.workEffortTypeId], false);
		if (UtilValidate.isNotEmpty(workEffortTypeParent)) {
			weTypeParentEtch = UtilValidate.isNotEmpty(workEffortTypeParent.etch) ? workEffortTypeParent.etch : workEffortTypeParent.description;
			weTypeParentEtchLang = UtilValidate.isNotEmpty(workEffortTypeParent.etchLang) ? workEffortTypeParent.etchLang : workEffortTypeParent.descriptionLang;
		}
	}
	
	if (UtilValidate.isNotEmpty(workEffortType)) {
		weTypeEtch = UtilValidate.isNotEmpty(workEffortType.etch) ? workEffortType.etch : workEffortType.description;
		weTypeEtchLang = UtilValidate.isNotEmpty(workEffortType.etchLang) ? workEffortType.etchLang : workEffortType.descriptionLang;
		
		context.weColorText = "";
        context.weColorBackground = "";
        WorkEffortTypeCntParamsEvaluator paramsEvaluator = new WorkEffortTypeCntParamsEvaluator(context, parameters, delegator);
        paramsEvaluator.evaluateParams(workEffort.workEffortTypeId, "WEFLD_MAIN", false);
	}
	
	if (UtilValidate.isNotEmpty(partyParentRole)) {
		orgUnit = partyParentRole.parentRoleCode;
		orgUnitLang = partyParentRole.parentRoleCode;
	}
	if (UtilValidate.isNotEmpty(partyGroup)) {
		orgUnit += " - " + partyGroup.groupName;
		orgUnitLang += " - " + partyGroup.groupNameLang;
	}
}

context.weStatusId = weStatusId; // contiene stato dell'obiettivo (work_effort_id)

context.weTypeParentEtch = weTypeParentEtch;
context.weTypeParentEtchLang = weTypeParentEtchLang;
context.workEffortParentName = workEffortParentName;
context.workEffortParentNameLang = workEffortParentNameLang;
context.weTypeEtch = weTypeEtch;
context.weTypeEtchLang = weTypeEtchLang;
context.workEffortName = workEffortName;
context.workEffortNameLang = workEffortNameLang;
context.orgUnit = orgUnit;
context.orgUnitLang = orgUnitLang;
context.workEffortParentId = workEffortParentId;

context.workEffortRootId = parameters.workEffortIdRoot;
if (!workEffortParentId.equals(parameters.workEffortIdRoot)){
	context.workEffortRootId = workEffortParentId;
}
Debug.log("[retrieveOrganizationalAndStrategicPerformanceHeaderData.groovy] - altra scheda albero context.workEffortRootId " + context.workEffortRootId + " parameters.gpMenuEnumId " + parameters.gpMenuEnumId);
Debug.log("[retrieveOrganizationalAndStrategicPerformanceHeaderData.groovy] - context.rootInqyTree " + context.rootInqyTree + " parameters.rootInqyTree " + parameters.rootInqyTree);
// non e' la radice dell'albero, ma l'id della eventuale scheda che si trova nell'albero
if (UtilValidate.isNotEmpty(context.workEffortRootId)) {
	weRoot = delegator.findOne("WorkEffort", ["workEffortId": context.workEffortRootId], false);
	def statusItem = delegator.findOne("StatusItem", ["statusId": weRoot.currentStatusId], false); 
	weStatusDescr = statusItem.description;
	weStatusDescrLang = statusItem.descriptionLang;
	weStatusId = weRoot.currentStatusId;
	context.weStatusDescr = weStatusDescr;
	context.weStatusDescrLang = weStatusDescrLang;
	context.weStatusId = weStatusId;
	context.currentStatusId = weStatusId;
	
	if (!"Y".equals(parameters.rootInqyTree)) {
		def rootSearchRootInqyServiceMap = [:];
	    rootSearchRootInqyServiceMap.put("workEffortRootId", context.workEffortRootId);
	    rootSearchRootInqyServiceMap.put("userLogin", context.userLogin);
	    Debug.log("[retrieveOrganizationalAndStrategicPerformanceHeaderData.groovy] -  rootSearchRootInqyServiceMap " + rootSearchRootInqyServiceMap );
	    def rootSearchRootInqyServiceRes = dispatcher.runSync("getCanViewUpdateWorkEffortRoot", rootSearchRootInqyServiceMap);
	    Debug.log("[retrieveOrganizationalAndStrategicPerformanceHeaderData.groovy] -  rootSearchRootInqyServiceRes " + rootSearchRootInqyServiceRes );
	    context.isForcedReadOnly = "Y".equals(rootSearchRootInqyServiceRes.canUpdateRoot) ? "N" : "Y";
	    parameters.isForcedReadOnly = "Y".equals(rootSearchRootInqyServiceRes.canUpdateRoot) ? "N" : "Y";
	    Debug.log("[retrieveOrganizationalAndStrategicPerformanceHeaderData.groovy] - context.isForcedReadOnly " + context.isForcedReadOnly + " parameters.isForcedReadOnly " + parameters.isForcedReadOnly );
	}
	
	workEffortTypeRoot = delegator.findOne("WorkEffortType", ["workEffortTypeId": weRoot.workEffortTypeId], false);
	retriveStatusInfo(context.workEffortRootId, weRoot.workEffortTypeId, weStatusId, workEffortTypeRoot.parentTypeId, weRoot.workEffortTypePeriodId);
}	
	
def retriveStatusInfo(workEffortId, workEffortTypeId, currentStatusId, weContextId, workEffortTypePeriodId) {
	context.backStatusId = ""; // vengono sovrascritti da eventuale scheda presente nell'albero
	retrieveBackStatusList = true;
	hasPermissionBackStatus = true;
	permission = "";
	if (UtilValidate.isNotEmpty(weContextId)) {
		permission = ContextPermissionPrefixEnum.getPermissionPrefix(weContextId);
	}
	if (UtilValidate.isEmpty(permission)) {
		permission = "WORKEFFORT";
	}
	Debug.log("[retrieveOrganizationalAndStrategicPerformanceHeaderData.groovy] - hasPermission for change status " + security.hasPermission(permission + "MGR_ADMIN", userLogin) );
	if (! security.hasPermission(permission + "MGR_ADMIN", userLogin)) {
		def workEffortTypeStatusParamsEvaluator = new WorkEffortTypeStatusParamsEvaluator(context, delegator);
		def paramsMap = workEffortTypeStatusParamsEvaluator.evaluateParams(workEffortTypeId, currentStatusId, false);
		if (UtilValidate.isNotEmpty(paramsMap) && UtilValidate.isNotEmpty(paramsMap.noPreviousStatus)) {
			noPreviousStatus = paramsMap.noPreviousStatus;
		}
		if ("Y".equals(noPreviousStatus)) {
			hasPermissionBackStatus = false;
		}
	}
	Debug.log("[retrieveOrganizationalAndStrategicPerformanceHeaderData.groovy] - hasPermissionBackStatus from params " + hasPermissionBackStatus );
	if (hasPermissionBackStatus) {
		if (UtilValidate.isNotEmpty(workEffortTypePeriodId)) {
			def workEffortTypePeriod = delegator.findOne("WorkEffortTypePeriod", ["workEffortTypePeriodId": workEffortTypePeriodId], false);
			if (UtilValidate.isNotEmpty(workEffortTypePeriod)) {
				retrieveBackStatusList = ("OPEN".equals(workEffortTypePeriod.statusEnumId) || "REOPEN".equals(workEffortTypePeriod.statusEnumId));
			}
		}
		Debug.log("[retrieveOrganizationalAndStrategicPerformanceHeaderData.groovy] - retrieveBackStatusList only for OPEN - REOPEN period " + retrieveBackStatusList );
		if (retrieveBackStatusList) {
			def conditionList = [];
			conditionList.add(EntityCondition.makeCondition("workEffortId", workEffortId));
			conditionList.add(EntityCondition.makeCondition("statusIdTo", currentStatusId));
			def statusList = delegator.findList("WorkEffortStatusValidChange", EntityCondition.makeCondition(conditionList), null, ["-statusDatetime"], null, false);
			def listItem = EntityUtil.getFirst(statusList);
			Debug.log("[retrieveOrganizationalAndStrategicPerformanceHeaderData.groovy] - history listItem " + listItem );
			if (UtilValidate.isNotEmpty(listItem)) {
			    context.backStatusId = listItem.statusId;
			} else {
				def status = delegator.findOne("StatusItem", ["statusId": currentStatusId], false); 
	
				def condition = EntityCondition.makeCondition([EntityCondition.makeCondition("workEffortId", EntityOperator.EQUALS, workEffortId),
				  EntityCondition.makeCondition("statusId", EntityOperator.NOT_EQUAL, currentStatusId), EntityCondition.makeCondition("sequenceId", EntityOperator.LESS_THAN, status.sequenceId),
				  EntityCondition.makeCondition("statusTypeId", statusItem.statusTypeId)]);
				def backStatusList = delegator.findList("WorkEffortStatusAndItemView", condition, null, ["-statusDatetime"], null, false);
				
				Debug.log("[retrieveOrganizationalAndStrategicPerformanceHeaderData.groovy] - previuos back item backStatusList " + backStatusList );
				def backStatusItem = EntityUtil.getFirst(backStatusList);
				if (UtilValidate.isNotEmpty(backStatusItem)) {
				    context.backStatusId = backStatusItem.statusId;
				}
			}
		}			
	}
}

def nowStamp2 = UtilDateTime.nowTimestamp();
Debug.log("Run action script in " + (nowStamp2.getTime() - nowStamp.getTime()) + " milliseconds. script = component://workeffortext/webapp/workeffortext/WEB-INF/actions/retrieveOrganizationalAndStrategicPerformanceHeaderData.groovy");
Debug.log("[retrieveOrganizationalAndStrategicPerformanceHeaderData.groovy] - context.rootInqyTree " + context.rootInqyTree + " parameters.rootInqyTree " + parameters.rootInqyTree );
	    