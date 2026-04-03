import org.ofbiz.base.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;
import javolution.util.FastList;

def isInsertModeField = context.isInsertMode;
def isInsertMode = false;

if (isInsertModeField instanceof Boolean) {
	isInsertMode = isInsertModeField;
} else if (isInsertModeField instanceof String) {
	isInsertMode = "true".equalsIgnoreCase(isInsertModeField);
}

//Debug.log(" setWorkEffortNoteAndDataFormDefaultFields context.usePeriod " + context.usePeriod);
//Debug.log(" setWorkEffortNoteAndDataFormDefaultFields context.noteDateTime " + context.noteDateTime);
//Debug.log(" setWorkEffortNoteAndDataFormDefaultFields context.periodNoteDateTime " + context.periodNoteDateTime);
def periodNoteDateTime = "";
if (UtilValidate.isNotEmpty(context.periodNoteDateTime)) {
    periodNoteDateTime = context.periodNoteDateTime;
} else {
    if (UtilValidate.isNotEmpty(context.usePeriod)) {
        def customTimePeriodFromCondList = [];
        // customTimePeriodFromCondList.add(EntityCondition.makeCondition("fromDate", context.noteDateTime));
        // customTimePeriodFromCondList.add(EntityCondition.makeCondition("thruDate", context.noteDateTime));
        
        customTimePeriodFromCondList.add(EntityCondition.makeCondition( 
                EntityCondition.makeCondition("fromDate", EntityOperator.EQUALS, context.noteDateTime),
                EntityOperator.OR,
                EntityCondition.makeCondition("thruDate", EntityOperator.EQUALS, context.noteDateTime)));
        
        customTimePeriodFromCondList.add(EntityCondition.makeCondition("periodTypeId", context.usePeriod));
        def customTimePeriodFromList = delegator.findList("CustomTimePeriod", EntityCondition.makeCondition(customTimePeriodFromCondList), null, null, null, false);
        def customTimePeriodFromItem = EntityUtil.getFirst(customTimePeriodFromList);
        if (UtilValidate.isNotEmpty(customTimePeriodFromItem)) {
            periodNoteDateTime = customTimePeriodFromItem.customTimePeriodId;
        }
    }
}
context.periodNoteDateTime = periodNoteDateTime

if (isInsertMode == true) {
	def searchDate = parameters.searchDate;
	if (UtilValidate.isNotEmpty(searchDate)) {
		context.noteDateTime = searchDate;
	}
	
	def workEffortTypeId = "";
	def workEffortId = UtilValidate.isNotEmpty(context.workEffortId) ? context.workEffortId : parameters.workEffortId;
	def noteContentId = UtilValidate.isNotEmpty(context.noteContentId) ? context.noteContentId : parameters.noteContentId;
	
	if (UtilValidate.isNotEmpty(workEffortId)) {
		def workEffort = delegator.findOne("WorkEffort", ["workEffortId" : workEffortId], false);
		if (UtilValidate.isNotEmpty(workEffort)) {
			workEffortTypeId = workEffort.workEffortTypeId;
			if (UtilValidate.isEmpty(searchDate)) {
				context.noteDateTime = workEffort.estimatedStartDate;
			}
		}
	}
	if (UtilValidate.isNotEmpty(workEffortTypeId)) {
		def condList = FastList.newInstance();
		condList.add(EntityCondition.makeCondition("workEffortTypeId", workEffortTypeId));
		condList.add(EntityCondition.makeCondition("contentId", noteContentId));
		condList.add(EntityCondition.makeCondition("isMain", "N"));
		
		def typeAttrList = delegator.findList("WorkEffortTypeAttrAndNoteData", EntityCondition.makeCondition(condList), null, ["sequenceId"], null, false);
		def typeAttr = EntityUtil.getFirst(typeAttrList);				
		if (UtilValidate.isNotEmpty(typeAttr)) {
			def multiTypeLang = context.multiTypeLang;
			def localeSecondarySet = context.localeSecondarySet;
			
			context.noteName = typeAttr.attrName;
			if (UtilValidate.isNotEmpty(multiTypeLang) && ! "NONE".equals(multiTypeLang)) {
				context.noteNameLang = typeAttr.attrNameLang;
			}
			if (UtilValidate.isNotEmpty(typeAttr.noteInfo)) {
				context.noteInfo = typeAttr.noteInfo;
			}
			if (UtilValidate.isNotEmpty(typeAttr.noteInfoLang) && UtilValidate.isNotEmpty(multiTypeLang) && ! "NONE".equals(multiTypeLang)) {
				context.noteInfoLang = typeAttr.noteInfoLang;
			}
			
			context.isHtml = typeAttr.isHtml;
			context.isMain = typeAttr.isMain;
			context.sequenceId = typeAttr.sequenceId;
			context.internalNote = typeAttr.internalNote;
		}
	}	
}