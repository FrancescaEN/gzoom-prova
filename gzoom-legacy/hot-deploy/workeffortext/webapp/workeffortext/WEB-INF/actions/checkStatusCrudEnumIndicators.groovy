import org.ofbiz.base.util.*;

def crudEnumId = "";

//Devo gestire anche i casi dove ho i layout del folder
//prima di cercare nella vista, controllo se il contentId che mi è stato pssato è un di tipo FOLDER
//altrimenti sono nel caso si layout e prendo il contentTypeId (che in questo caso è il folder ) per fai la ricerca
def contentId = UtilValidate.isNotEmpty(parameters.contentId) ?  parameters.contentId : context.contentId;
def content = delegator.findOne("Content", ["contentId" : contentId], false);
if (UtilValidate.isNotEmpty(content) && UtilValidate.isNotEmpty(content.contentTypeId) && content.contentTypeId != "FOLDER") {
    contentId = content.contentTypeId;
}
// Debug.log("***************************** checkStatusCrudEnumIndicators contentId  "+ contentId );
// Debug.log("*****************************  workEffortId  "+ workEffortId );

// Debug.log("***************************** checkStatusCrudEnumIndicators parameters.contentIdInd  "+ parameters.contentIdInd );
// Debug.log("***************************** checkStatusCrudEnumIndicators context.contentIdInd  "+ context.contentIdInd );
def contentIdInd = UtilValidate.isNotEmpty(parameters.contentIdInd) ? parameters.contentIdInd : context.contentIdInd;
Debug.log("***************************** checkStatusCrudEnumIndicators contentId " + contentId + " contentIdInd  "+ contentIdInd );
if ("WEFLD_AIND".equals(contentId)) {
	contentIdInd = "WEFLD_IND";
}
if ("WEFLD_AIND2".equals(contentId)) {
	contentIdInd = "WEFLD_IND2";
}
if ("WEFLD_AIND3".equals(contentId)) {
	contentIdInd = "WEFLD_IND3";
}
if ("WEFLD_AIND4".equals(contentId)) {
	contentIdInd = "WEFLD_IND4";
}
if ("WEFLD_AIND5".equals(contentId)) {
	contentIdInd = "WEFLD_IND5";
}
if ("WEFLD_AIND6".equals(contentId)) {
    contentIdInd = "WEFLD_IND6";
}
if ("WEFLD_AIND7".equals(contentId)) {
    contentIdInd = "WEFLD_IND7";
}
if ("WEFLD_AIND8".equals(contentId)) {
    contentIdInd = "WEFLD_IND8";
}
if ("WEFLD_AIND9".equals(contentId)) {
    contentIdInd = "WEFLD_IND9";
}
if ("WEFLD_AIND10".equals(contentId)) {
    contentIdInd = "WEFLD_IND10";
}
context.contentIdInd = contentIdInd;

def childView = delegator.findOne("WorkEffortTypeStatusCntChildView", ["workEffortId" : workEffortId, "contentId" : contentIdInd], false);
if (UtilValidate.isNotEmpty(childView)) {
	// Debug.log("*****************************  childView  "+ childView );
	crudEnumId = childView.crudEnumId;
} else {
	def parentView = delegator.findOne("WorkEffortTypeStatusCntParentView", ["workEffortId" : workEffortId, "contentId" : contentIdInd], false);
	if (UtilValidate.isNotEmpty(parentView)) {
		//Debug.log("*****************************  parentView  "+ parentView );
		crudEnumId = parentView.crudEnumId;
	} 
}

context.crudEnumId = crudEnumId;
