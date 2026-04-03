import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;

def contentCondList = [];
contentCondList.add(EntityCondition.makeCondition("contentTypeId", "FOLDER"));
def contentList = delegator.findList("Content", EntityCondition.makeCondition(contentCondList), null, null, null, false);
def folders = EntityUtil.getFieldListFromEntityList(contentList, "contentId", true);

Debug.log("Script getTitleStylesFoldersMenu.groovy context.rootInqyTree " + context.rootInqyTree + " parameters.rootInqyTree " + parameters.rootInqyTree);
Debug.log("[getTitleStylesFoldersMenu.groovy] - context.rootInqyTree " + context.rootInqyTree + " parameters.rootInqyTree " + parameters.rootInqyTree );
Debug.log("[getTitleStylesFoldersMenu.groovy] - context.isForcedReadOnly " + context.isForcedReadOnly + " parameters.isForcedReadOnly " + parameters.isForcedReadOnly );

// def rootInqyTree = UtilValidate.isNotEmpty(parameters.rootInqyTree) ? parameters.rootInqyTree : context.rootInqyTree;
def nowStamp = UtilDateTime.nowTimestamp();

if ("Y".equals(parameters.rootInqyTree) || "Y".equals(parameters.isForcedReadOnly)) {
	if (UtilValidate.isNotEmpty(folders)) {
		Debug.log("[getTitleStylesFoldersMenu.groovy] - label-gray because context.isForcedReadOnly " + context.isForcedReadOnly + " - " + parameters.isForcedReadOnly + " context.rootInqyTree " + context.rootInqyTree + " - " + parameters.rootInqyTree);
		folders.each {folder ->
		    context["titleStyle_" + folder] = "label-gray";
		}
	}
} else {
	def workEffortTypeStatusCondList = [];
	workEffortTypeStatusCondList.add(EntityCondition.makeCondition("workEffortTypeId", context.workEffortTypeId));
	workEffortTypeStatusCondList.add(EntityCondition.makeCondition("statusId", context.currentStatusId));
	def workEffortTypeStatusCntList = delegator.findList("WorkEffortTypeStatusCnt", EntityCondition.makeCondition(workEffortTypeStatusCondList), null, null, null, false);
	if (UtilValidate.isNotEmpty(folders)) {
		folders.each {folder ->
		   if (UtilValidate.isNotEmpty(folder)) {
			   def titleStyle = "";
			   if (UtilValidate.isNotEmpty(workEffortTypeStatusCntList)) {
				   workEffortTypeStatusCntList.each {workEffortTypeStatusCntItem ->
					   if (UtilValidate.isNotEmpty(workEffortTypeStatusCntItem) && folder.equals(workEffortTypeStatusCntItem.contentId)) {
						   if("NONE".equals(workEffortTypeStatusCntItem.crudEnumId)) {
							   Debug.log("[getTitleStylesFoldersMenu.groovy] - label-gray because workEffortTypeStatusCntItem.crudEnumId NONE ");
						   	   titleStyle = "label-gray";
						   }
					   }
				   }
			   }
			   context["titleStyle_" + folder] = titleStyle;
		   }
		}	
	}	
}
