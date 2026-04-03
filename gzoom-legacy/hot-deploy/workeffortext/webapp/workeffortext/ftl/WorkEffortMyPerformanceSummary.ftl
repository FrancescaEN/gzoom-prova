<#if context.listIt?has_content>
<table cellspacing="0" cellpadding="0" class="basic-table list-table padded-row-table hover-bar resizable draggable toggleable selectable customizable headerFixable" id="table_MPML0001_MyPerformance">
    <thead>
        <tr class="header-row-2">
            <th id="table_MPML0001_MyPerformance.Tipology">${uiLabelMap.WorkEffortTypology}</th>
            <th id="table_MPML0001_MyPerformance.orgUnitId">${uiLabelMap.FormFieldTitle_orgUnitId}</th>
            <th id="table_MPML0001_MyPerformance.estimatedStartDate" class="">${uiLabelMap.performanceEstimatedStartDate}</th>
            <th id="table_MPML0001_MyPerformance.estimatedCompletionDate">${uiLabelMap.performanceEstimatedCompletionDate}</th>   
            <th id="table_MPML0001_MyPerformance.stDescription">${uiLabelMap.CommonStatus}</th>
            <th id="table_MPML0001_MyPerformance.baseActions">${uiLabelMap.BaseActions}</th>
        </tr>
   </thead>
   <tbody style="height: auto;">
    <#assign index=0/>
            <#list listIt as item>
                <tr <#if index%2 != 0>class="alternate-row"</#if>>
                    <td>
                        <input type="hidden" class="mandatory" value="MyPerformance" name="entityName">
                        <input type="hidden" class="mandatory" name="operation">
                        <input type="hidden" value="BaseMessageSaveData" name="messageContext">
                        <input type="hidden" value="${item.estimatedStartDate}" name="estimatedStartDate">
                        <input type="hidden" value="${item.estimatedCompletionDate}" name="estimatedCompletionDate">
                        <input type="hidden" value="${item.orgUnitId}" name="orgUnitId">
                        <input type="hidden" value="${item.partyId?if_exists}" name="partyId">
                        <input type="hidden" value="${item.stDescription}" name="stDescription">
                        <input type="hidden" value="${item.workEffortId}" name="workEffortId">
                        <#if localeSecondarySet?has_content && localeSecondarySet?default('N') == 'Y'>
                            <#if item.weTypeEtchLang?has_content>
                                <#assign weType=item.weTypeEtchLang>
                            <#else>
                                <#assign weType=item.weTypeDescriptionLang?if_exists>
                            </#if>
                        <#else>
                            <#if item.weTypeEtch?has_content>
                                <#assign weType=item.weTypeEtch>
                            <#else>
                                <#assign weType=item.weTypeDescription?if_exists>
                            </#if>                        
                        </#if>
                        <div onclick="WorkEffortMyPerformanceSummaryListExtension.load('${item.workEffortId?if_exists}');">${weType?if_exists}</div>
                    </td>
                    <td class="orgUnitColumn">
                        <div onclick="WorkEffortMyPerformanceSummaryListExtension.load('${item.workEffortId?if_exists}');">${item.orgUnitRoleCode?if_exists} - ${item.orgUnitName?if_exists}</div>
                    </td>
                    <td>
                        <div onclick="WorkEffortMyPerformanceSummaryListExtension.load('${item.workEffortId?if_exists}');">${Static["org.ofbiz.base.util.UtilDateTime"].toDateString(item.estimatedStartDate, locale)}</div>
                    </td>
                    <td>
                        <div onclick="WorkEffortMyPerformanceSummaryListExtension.load('${item.workEffortId?if_exists}');">${Static["org.ofbiz.base.util.UtilDateTime"].toDateString(item.estimatedCompletionDate, locale)}</div>
                    </td>
                    <td class="center">
                        <#assign isForcedReadOnly = "N"/>                            	
	                    <#if item.canUpdateRoot?has_content && item.canUpdateRoot == "Y">
	                        <#assign isForcedReadOnly = "Y"/>
	                    </#if>
	                    <#if localeSecondarySet?has_content && localeSecondarySet?default('N') == 'Y'>
	                        <#assign statusDesc=item.stDescriptionLang?if_exists>
	                    <#else>
	                        <#assign statusDesc=item.stDescription?if_exists>
	                    </#if>
	                    <#if item.canViewRoot?has_content && item.canViewRoot == "Y">
                            <a href="#" onclick=" CleanCookie.loadTreeView(); ajaxUpdateAreas('common-container,/emplperf/control/managementContainerOnly,externalLoginKey=${requestAttributes.externalLoginKey}&entityName=WorkEffortView&noLeftBar=${parameters.noLeftBar?if_exists?string}&rootInqyTree=${rootInqyTree?if_exists}&isForcedReadOnly=${isForcedReadOnly?if_exists}&specialized=Y&rootTree=N&loadTreeView=Y&workEffortIdRoot=${item.workEffortId?if_exists}&workEffortId=${item.workEffortId?if_exists}&weHierarchyTypeId=${item.weHierarchyTypeId?if_exists}&successCode=management&sourceReferenceId=${item.sourceReferenceId?if_exists}&saveView=Y&searchFormLocation=component://emplperf/widget/forms/EmplPerfRootViewForms.xml&searchFormResultLocation=component://emplperf/widget/forms/EmplPerfRootViewForms.xml&advancedSearchFormLocation=component://emplperf/widget/forms/EmplPerfRootViewForms.xml&searchFormScreenName=WorkEffortRootViewSearchFormScreen&searchFormScreenLocation=component://emplperf/widget/screens/EmplPerfScreens.xml&searchResultContextFormName=WorkEffortRootViewSearchResultContextForm&searchResultContextFormLocation=component://emplperf/widget/forms/EmplPerfRootViewForms.xml'); return false;" class="event" title="${statusDesc?if_exists}">${statusDesc?if_exists}</a>
                        <#else>
                            ${statusDesc?if_exists}
                        </#if>
                    </td>
                    <td style="width: 8%">
                       <div class="contact-actions performance-actions">
                           <ul>
                           	   <#if item.canUpdateRoot?has_content && item.canUpdateRoot == "Y">
		                           <li class="class-collegato-active"><i class="fas fa-star"></i></li>                               		
	                           <#else>
	                               <li class="class-collegato-disabled"><i class="far fa-star"></i></li>
		                       </#if>
		                   </ul>
                       </div>
                    </td>
                </tr>
                <#assign index = index+1>
            </#list>
   </tbody>
</table>
</#if>

<div style="display: none;" id="popup-reason-boxs-container" class="popup-reason-boxs-container">
<span class="hidden-label" id="popup-reason-box-title">${uiLabelMap.BaseMenusHistoricizeTab}</span>
<div class="popup-reason-box-container">
    <div class="popup-reason-body-container">
        <div id="popup-reason-text-container" class="popup-reason-text-container">
        	${uiLabelMap.WorkEffortConfirmChangeStatus}
           <br/>
           <textarea rows="6" cols="70" name="reasonDescription" id="reasonDescription" class="mandatory"></textarea>
           <br/>
           <br/>
           <br/>
        </div>
    </div>
    <div class="popup-copy-all-buttons-container">
        <a href="#" class="smallSubmit button-cancel" onclick="Modalbox.hide()">${uiLabelMap.BaseButtonCancel}</a>
        <a href="#" class="smallSubmit button-ok" onclick="javascript: ReasonPopupMgr.validate();">${uiLabelMap.BaseButtonOK}</a>
    </div>
</div>
</div>
<div style="display: none;" id="popup-check-boxs-container" class="popup-check-boxs-container">
<span class="hidden-label" id="popup-check-box-title">${uiLabelMap.BaseMenusHistoricizeTab}</span>
<div class="popup-check-box-container">
    <div class="popup-check-body-container">
        <div id="popup-check-text-container" class="popup-check-text-container">
        </div>
    </div>
    <div class="popup-copy-all-buttons-container">
        <a href="#" class="smallSubmit button-ok" onclick="javascript:ReasonPopupMgr.reset(); Modalbox.hide()">${uiLabelMap.BaseButtonOK}</a>
    </div>
</div>
</div>

<div style="display: none;" id="popup-boxs-container" class="popup-reason-boxs-container">
<span class="hidden-label" id="popup-box-title">${uiLabelMap.BaseMenusHistoricizeTab}</span>
<div class="popup-box-container">
    <div class="popup-body-container">
        <div id="popup-text-container" class="popup-reason-text-container">
        	${uiLabelMap.WorkEffortConfirmChangeStatus}
           <br/>
           <br/>
        </div>
    </div>
    <div class="popup-copy-all-buttons-container">
        <a href="#" class="smallSubmit button-cancel" onclick="javascript:Modalbox.hide();">${uiLabelMap.BaseButtonCancel}</a>
        <a href="#" class="smallSubmit button-ok" onclick="javascript: ReasonPopupMgr.validate();">${uiLabelMap.BaseButtonOK}</a>
    </div>
</div>
</div>