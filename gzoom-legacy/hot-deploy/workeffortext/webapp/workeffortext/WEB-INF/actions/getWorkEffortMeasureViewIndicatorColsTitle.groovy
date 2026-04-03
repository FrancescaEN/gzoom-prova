import org.ofbiz.base.util.*;

def multiTypeLang = context.multiTypeLang;

/**
 * getWorkEffortMeasureViewIndicatorColsTitle.groovy serve per estrarre la configurazione del titolo per comments e il comments2,
 * anche in base ai params e al bilunguismo
 */
// Debug.log("Script getWorkEffortMeasureViewIndicatorColsTitle.groovy");
// Debug.log("Script getWorkEffortMeasureViewIndicatorColsTitle.groovy context.commentsEtchDescr " + context.commentsEtchDescr);

if ("action".equals(context.commentsEtchDescr)) {
    commentsTitle = uiLabelMap["Indicator_comments_action"];
} else if ("dataSource".equals(context.commentsEtchDescr)) {
    commentsTitle = uiLabelMap["Indicator_comments_Data_Source"];
} else if ("verificationSource".equals(context.commentsEtchDescr)) {
    commentsTitle = uiLabelMap["Indicator_comments_Verification_Source"];
} else if ("category".equals(context.commentsEtchDescr)) {
    commentsTitle = uiLabelMap["Indicator_comments_category"];
} else if ("algorytm".equals(context.commentsEtchDescr)) {
    commentsTitle = uiLabelMap["Indicator_comments_Algorytm"];
} else {
    commentsTitle = uiLabelMap["Indicator_comments"];
}
context.commentsTitle = commentsTitle;

if ("action".equals(context.comments2EtchDescr)) {
    comments2Title = uiLabelMap["Indicator_comments_action"];
} else if ("dataSource".equals(context.comments2EtchDescr)) {
    comments2Title = uiLabelMap["Indicator_comments_Data_Source"];
} else if ("verificationSource".equals(context.comments2EtchDescr)) {
    comments2Title = uiLabelMap["Indicator_comments_Verification_Source"];
} else if ("category".equals(context.comments2EtchDescr)) {
    comments2Title = uiLabelMap["Indicator_comments_category"];
} else if ("algorytm".equals(context.comments2EtchDescr)) {
    comments2Title = uiLabelMap["Indicator_comments_Algorytm"];
} else {
    comments2Title = uiLabelMap["Indicator_comments"] + " 2";
}
context.comments2Title = comments2Title;

if (UtilValidate.isNotEmpty(multiTypeLang) && !"NONE".equals(multiTypeLang)) {
	def primaryLangFlagPath = context.primaryLangFlagPath;
	def secondaryLangFlagPath = context.secondaryLangFlagPath;
	def primaryLangTooltip = context.primaryLangTooltip;
	def secondaryLangTooltip = context.secondaryLangTooltip;
	
    context.commentsTransactionViewTitle = getFieldTitle(context.commentsTitle, primaryLangFlagPath, primaryLangTooltip);
    context.commentsTransactionViewLangTitle = getFieldTitle("", secondaryLangFlagPath, secondaryLangTooltip);      

	context.uomDescrTitle = getFieldTitle(uiLabelMap.Indicator_uomDescr, primaryLangFlagPath, primaryLangTooltip);
	context.uomDescrLangTitle = getFieldTitle("", secondaryLangFlagPath, secondaryLangTooltip);
	context.commentsTitle = getFieldTitle(context.commentsTitle, primaryLangFlagPath, primaryLangTooltip);
	context.comments2Title = getFieldTitle(context.comments2Title, primaryLangFlagPath, primaryLangTooltip);
    context.commentsLangTitle = getFieldTitle("", secondaryLangFlagPath, secondaryLangTooltip); 
    context.comments2LangTitle = getFieldTitle("", secondaryLangFlagPath, secondaryLangTooltip); 
}
// Debug.log("Script getWorkEffortMeasureViewIndicatorColsTitle.groovy context.commentsTransactionViewTitle " + context.commentsTransactionViewTitle);
// Debug.log("Script getWorkEffortMeasureViewIndicatorColsTitle.groovy context.commentsTitle " + context.commentsTitle);

def getFieldTitle(fieldLabel, langFlagPath, langTooltip) {
	def fieldTitle = "<table cellspacing='0' cellpadding='0'><tr><td class='base-align-left'>";
	if (UtilValidate.isNotEmpty(fieldLabel)) {
		fieldTitle += fieldLabel;
	} else {
		fieldTitle += "&nbsp;";
	}
	fieldTitle += "</td><td class='base-align-right'>";
	fieldTitle += "&nbsp;&nbsp;<img src='" + langFlagPath + "' title='" +langTooltip + "'/>";
	fieldTitle += "</td></tr></table>";
	
	return fieldTitle;
}
