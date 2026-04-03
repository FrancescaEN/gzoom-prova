<#-- VALORE OR KPI_VALORE -->
<#if workEffortTransactionIndicatorView.scoreKpiUomType?if_exists == 'RATING_SCALE'>
    <#assign uomCode = "" />
    <#assign uomRatingScaleList = delegator.findByAndCache("WorkEffortMeasRatSc",Static["org.ofbiz.base.util.UtilMisc"].toMap("uomId", workEffortTransactionIndicatorView.weTransCurrencyUomId, "workEffortMeasureId", workEffortTransactionIndicatorView.weTransMeasureId))>
    <#list uomRatingScaleList as uomRatingScale>
			<#if workEffortTransactionIndicatorView.weTransValueKpiScore?if_exists?has_content && Static["java.lang.Double"].toString(workEffortTransactionIndicatorView.weTransValueKpiScore) == Static["java.lang.Double"].toString(uomRatingScale.uomRatingValue) >
                <#if localeSecondarySet?has_content && localeSecondarySet?default('N') == 'Y'>
                    <#assign uomCode = uomRatingScale.uomCodeLang />
                <#else>
                    <#assign uomCode = uomRatingScale.uomCode />
                </#if>
            </#if>
	</#list>                                                            
    <div style="float: right; width: 90%" class="droplist_field" id="WorkEffortTransactionViewManagementMultiForm_weTransValue_o_${index}">
       <input  class="autocompleter_option" type="hidden" name="target" value="<@ofbizUrl>ajaxAutocompleteOptions</@ofbizUrl>"/>
       <input  class="autocompleter_parameter" type="hidden" name="entityName" value="[WorkEffortMeasRatSc]"/><input  class="autocompleter_parameter" type="hidden" name="distincts" value="[N]"/>
       <input  class="autocompleter_parameter" type="hidden" name="selectFields" value="${droplistSelectFields?if_exists}"/>
       <input  class="autocompleter_parameter" type="hidden" name="sortByFields" value="[[uomRatingValue]]"/>
       <input  class="autocompleter_parameter" type="hidden" name="displayFields" value="${droplistDisplayFields?if_exists}"/>
       <input  class="autocompleter_parameter" type="hidden" name="constraintFields" value="[[[uomId| equals| ${workEffortTransactionIndicatorView.weTransCurrencyUomId?if_exists}]! [workEffortMeasureId| equals| ${workEffortTransactionIndicatorView.weTransMeasureId}]]]"/>
       <input  class="autocompleter_parameter" type="hidden" name="saveView" value="N"/>
       <input  class="autocompleter_parameter" type="hidden" name="entityKeyField" value="uomRatingValue"/>
       <input  class="autocompleter_parameter" type="hidden" name="entityDescriptionField" value="${droplistEntityDescriptionField?if_exists}"/>
       <div class="droplist_container">
            <input type="hidden" 
        	name="weTransValue_o_${index}"
        	value="${workEffortTransactionIndicatorView.weTransValueKpiScore?if_exists}"
            <#if isReadOnlyRow > 
                class="droplist_code_field ignore_check_modification"
            <#else>
                class="droplist_code_field"
            </#if>
        	/>
            <div class="droplist_input_field" style="width: 70% !important;">
                <input style="cursor:pointer; width: 100% !important;" 
                    <#if isReadOnlyRow > 
                        readonly="readonly"
                        class="droplist_edit_field ignore_check_modification"
                    <#else>         
                        class="droplist_edit_field"
                    </#if> 
                    type="text" id="WorkEffortTransactionViewManagementMultiForm_weTransValue_o_${index}_edit_field" <#if localeSecondarySet?has_content && localeSecondarySet?default('N') == 'Y'> name="uomCodeLang_uomRatingValue_o_${index}"<#else>name="uomCode_uomRatingValue_o_${index}"</#if> value="${uomCode}" title="${uomCode}"/>
            </div>
            <div class="droplist_icon">
                <span class="droplist-anchor" style="float: right;"><a class="droplist_submit_field fa" style="font-size: 1.5em;" href="#"></a></span>
            </div>
        </div>
    </div>
<#elseif workEffortTransactionIndicatorView.scoreKpiUomType?if_exists == 'DATE_MEASURE'>
        <#assign id=Static["com.mapsengineering.base.util.FreemarkerWorker"].getFieldIdWithTimeStamp("weTransValue_o_${index}_datePanel")>
        <div class="datePanel" id="${id}">
        <input type="hidden" class="dateParams" name="paramName" value="weTransValue"/>
        <input type="hidden" class="dateParams" name="time" value="false"/>
        <input type="hidden" class="dateParams" name="shortDateInput" value="true"/>
        <input type="hidden" class="dateParams" name="dateTimeValue" value=""/>
        <input type="hidden" class="dateParams" name="localizedInputTitle" value="${uiLabelMap.CommonFormatDate}"/>
        <input type="hidden" class="dateParams" name="localizedIconTitle" value="${uiLabelMap.ShowedCommonFormatDate}"/>
        <input type="hidden" class="dateParams" name="yearRange" value=""/>
            
        <input type="hidden" class="dateParams" name="localizedValue" value="${workEffortTransactionIndicatorView.weTransValueKpiScore?if_exists}"/>
            
        <input type="hidden" class="dateParams" name="size" value="10"/>
        <input type="hidden" class="dateParams" name="maxlength" value="10"/>
        <input type="hidden" class="dateParams" name="locale" value="${locale.getLanguage()}"/>
        <input type="hidden" class="dateParams" name="classNames" 
            
        <#if isReadOnlyRow >
            value="readonly ignore_check_modification"
        <#else> 
            value="" 
        </#if> /></div>
<#else>
	<!-- Fix GN-5242 -->
    <input style="float: right; width: 80%; cursor:pointer;" 
        <#if isReadOnlyRow >readonly="readonly"
            class="numericInList input_mask mask_double ignore_check_modification" 
        <#else>
            class="numericInList input_mask mask_double" 
        </#if> 
            type="text" maxlength="20" size="12" 
            name="weTransValue_o_${index}" value="${workEffortTransactionIndicatorView.weTransValueKpiScore?if_exists}" decimal_digits="${workEffortTransactionIndicatorView.scoreKpiDecimalScale?if_exists}">
</#if>
                                            