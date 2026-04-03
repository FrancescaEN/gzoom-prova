<#-- COLONNA O COLONNE PERIODI -->
<#-- RENDER DEL TH - TESTATA -->
<#if renderThead == "Y">
	<#assign thColspan = "1" />
	<#if context.showColumnScoreKpi == "Y" && context.showPeriods != "NONE">
		<#assign thColspan = "2" />
    </#if>
    
    <#if context.showPeriods == "NONE">
        <#if showColumnScoreKpi == "Y">
			<th colspan=${thColspan} class="master-th"><div>${uiLabelMap.FormFieldTitle_ValoreAssoluto}</div></th>
	        <#assign colspan = colspan + 1/>
	        
	        <th>${uiLabelMap.FormFieldTitle_Risultato}</th>
	    <#else>
	    	<th colspan=${thColspan} class="master-th"><div>${uiLabelMap.FormFieldTitle_weTransValueKpiScore}</div></th>
	        <#assign colspan = colspan + 1/>
		</#if>
    <#elseif context.showPeriods == "OPEN">
        <th colspan=${thColspan} class="master-th"><div>
        <#assign styleForAlign = "" />
        
        <#if context.periodScrolling == "Y" && customTimePeriodId != firstCustomTimePeriodId>
            <#assign styleForAlign = "position: relative; bottom: 4px;" />
            <a class="scroll-left fa fa-2x" onclick="javascript: WorkEffortTransactionStandard.refreshForm(null, false);"></a> 
        </#if>
        <#if context.periodScrolling == "Y" && customTimePeriodId != lastCustomTimePeriodId>
            <#assign styleForAlign = "position: relative; bottom: 4px;" />
        </#if>
        <span style="${styleForAlign}">${customTimePeriodCode?if_exists}</span>
        <#if context.periodScrolling == "Y" && customTimePeriodId != lastCustomTimePeriodId>
            <a class="scroll-right fa fa-2x" onclick="javascript: WorkEffortTransactionStandard.refreshForm(null, true);"></a> 
        </#if>
        </div></th>
        <#assign colspan = colspan + 1/>
        <#if showColumnScoreKpi == "Y">
			<!-- Attenzione il secondo tr viene chiuso nel file WorkEffortTransactionStandard.ftl -->
			</tr>
			<tr class="header-row-2">
	    		<th>${uiLabelMap.FormFieldTitle_ValoreAssoluto}</th>
	    		<th>${uiLabelMap.FormFieldTitle_Risultato}</th>
		</#if>
    <#else>
        <#assign size = customTimePeriodList?size />
        <#assign colspan = colspan + size/>
        <#list customTimePeriodList?sort_by("fromDate") as customTimePeriod>
            <#assign styleForAlign = "" />
            
            <#if customTimePeriod_index == (size - 1)>
                <th colspan=${thColspan} class="master-th"><div>
            <#else>
                <th colspan=${thColspan}><div>
            </#if>
                <#if customTimePeriod_index == 0 && customTimePeriod.customTimePeriodId != firstCustomTimePeriodId>
                    <#if context.periodScrolling == "Y">
                        <#assign styleForAlign = "position: relative; bottom: 4px;" />
                        <a class="scroll-left fa fa-2x" onclick="javascript: WorkEffortTransactionStandard.refreshForm(null, false);"></a>
                    </#if>
                </#if>
                <#if customTimePeriod_index == (size - 1) && customTimePeriod.customTimePeriodId != lastCustomTimePeriodId>
                    <#if context.periodScrolling == "Y">
                        <#assign styleForAlign = "position: relative; bottom: 4px;" />
                    </#if>
                </#if>
                <span style="${styleForAlign}">${customTimePeriod.customTimePeriodCode?if_exists}</span>
                <#if customTimePeriod_index == (size - 1) && customTimePeriod.customTimePeriodId != lastCustomTimePeriodId>
                    <#if context.periodScrolling == "Y">
                        <a class="scroll-right fa fa-2x" onclick="javascript: WorkEffortTransactionStandard.refreshForm(null, true);"></a>
                    </#if>
                </#if>
            </div></th>
        </#list>
        <#if showColumnScoreKpi == "Y">
				<!-- Attenzione il secondo tr viene chiuso nel file WorkEffortTransactionStandard.ftl -->
				</tr>
				<tr class="header-row-2">
		    		<#list customTimePeriodList?sort_by("fromDate") as customTimePeriod>
	        			<th>${uiLabelMap.FormFieldTitle_ValoreAssoluto}</th>
			    		<th>${uiLabelMap.FormFieldTitle_Risultato}</th>
			    	</#list>
		</#if>
    </#if>
<#else>
    <#-- RENDER DEL TD - BODY -->
    <#if context.showPeriods != "NONE" || (context.showPeriods == "NONE" && (workEffortTransactionIndicatorView.weTransDate?has_content || (!periodNonehasTrans && workEffortTransactionIndicatorView_index == (rowListSize - 1))) || (renderColumnScoreKpi))>
	    <#-- periodNonehasTrans e' inizializzato nell'altro file -->
        <#assign workEffortTransactionIndicatorView_customTimePeriodId = workEffortTransactionIndicatorView.customTimePeriodId?if_exists/>
        <#if context.showPeriods == "NONE" && !periodNonehasTrans>
        	<#assign workEffortTransactionIndicatorView_customTimePeriodId = weFirstCustomTimePeriodId?if_exists/>
        </#if>
        <#assign periodNonehasTrans = true>
    	
    	<#-- la riga e' readOnly per diversi motivi: -->
        <#assign valModId = workEffortTransactionIndicatorView.glValModId?default("") >
        <#if (workEffortTransactionIndicatorView.inputEnumId?if_exists == "ACCINP_PRD") >
            <#assign valModId = workEffortTransactionIndicatorView.wmValModId?default("") >
        </#if> 
        <#if context.showPeriods == "NONE" && workEffortTransactionIndicatorView.workEffortTypePeriodId?if_exists?has_content>
            <#assign workEffortTypePeriod = delegator.findOne("WorkEffortTypePeriod", Static["org.ofbiz.base.util.UtilMisc"].toMap("workEffortTypePeriodId", workEffortTransactionIndicatorView.workEffortTypePeriodId?if_exists), false)>
            <#assign workEffortTypePeriodId = workEffortTypePeriod.workEffortTypePeriodId>
        <#else>
            <#assign workEffortTypePeriodList = delegator.findByAnd("WorkEffortTypePeriod",Static["org.ofbiz.base.util.UtilMisc"].toMap("workEffortTypeId", workEffortView.workEffortTypeRootId, "customTimePeriodId", workEffortTransactionIndicatorView_customTimePeriodId?if_exists, "glFiscalTypeEnumId", workEffortTransactionIndicatorView.glFiscalTypeEnumId, "organizationId", defaultOrganizationPartyId?if_exists))>
            <#assign workEffortTypePeriod = Static["org.ofbiz.entity.util.EntityUtil"].getFirst(workEffortTypePeriodList?default(null))?default(null)>
            <#assign workEffortTypePeriodId = workEffortTransactionIndicatorView.workEffortTypePeriodId?if_exists>
            <#if workEffortTypePeriod?has_content && workEffortTypePeriod.workEffortTypePeriodId?if_exists?has_content>
            	<#assign workEffortTypePeriodId = workEffortTypePeriod.workEffortTypePeriodId?if_exists>
            </#if> 
        </#if>
        <#assign isRil = workEffortTypePeriod?if_exists?has_content && prilStatusSet.contains(workEffortTypePeriod.statusEnumId?default(true))>
        
        <#assign hasMandatoryBudgetEmpty = (parameters.onlyWithBudget == "Y" && !workEffortTransactionIndicatorView.hasMandatoryBudgetEmpty?if_exists && workEffortTransactionIndicatorView.weTransTypeValueId?if_exists == "ACTUAL") />
        <#assign isReadOnlyRow = false />       
    
        <#if "Y" == parameters.rootInqyTree?if_exists?default('N') || "Y" == parameters.isForcedReadOnly?if_exists?default('N') || "Y" == workEffortTransactionIndicatorView.isPosted?if_exists >
            <#assign isReadOnlyRow = true />
        <#elseif  !checkWorkEffortPermissions >
            <#assign isReadOnlyRow = true />
        <#else>
            <#-- GN-5256
            <#if  security.hasPermission(adminPermission, context.userLogin) >
                <#assign isReadOnlyRow = false />
            <#else>
            -->
                <#assign isReadOnlyRow = 
                    (hasMandatoryBudgetEmpty
                    || !isRil
                    || crudEnumIdSecondary?if_exists == "NONE"
                    || valModId == "ALL_NOT_MOD"
                    || (valModId == "ACTUAL_NOT_MOD" && workEffortTransactionIndicatorView.weTransTypeValueId?if_exists == "ACTUAL")
                    || (valModId == "BUDGET_NOT_MOD" && workEffortTransactionIndicatorView.weTransTypeValueId?if_exists == "BUDGET")
                    || workEffortTransactionIndicatorView.isReadOnly?if_exists
                    || "Y" == parameters.rootInqyTree?if_exists?default('N')
                    || "Y" == parameters.isForcedReadOnly?if_exists?default('N')) 
                   />
            <#--
            </#if>
            --> 
        </#if> 
	        <#-- rowspan sara' il numero di periodi - periodIndex che inizia da 1 e quindi va normalizzato sottraendo 1, poi essendoci piu righe in base alla tipologia (glFiscalTypeId) va fatto il modulo del periodo -->
	        <#assign tdRowspan = (rowspanPeriod) - (periodIndex  - 1) % rowspanPeriod>
            <#-- se showColumnScoreKpi == "Y" avremo 2 valori per periodo quindi rowspan sara' il numero di periodi * 2 - periodIndex che inizia da 1 e quindi va normalizzato sottraendo 1, poi essendoci piu righe in base alla tipologia (glFiscalTypeId) va fatto il modulo del periodo per i 2 valori -->
	        <#if context.showColumnScoreKpi == "Y">
				<#assign tdRowspan = rowspanPeriod * 2 - (periodIndex - 1)  % rowspanPeriod * 2/>
		    </#if>
		    <#-- se renderColumnScoreKpi un rowspan in meno -->
	        <#if renderColumnScoreKpi>
		    	<#assign tdRowspan = tdRowspan - 1/>
		    </#if>
        
	        <td rowspan="${tdRowspan}" <#if isReadOnlyRow >readonly="readonly"</#if> style="cursor:pointer;"
            <#assign openPortletStyle = "">
            <#if showValuesPanel == 'Y'>
                <#assign openPortletStyle = "open-portlet">
            </#if>
            <#-- render ultima colonna deve avere come classe:
             - slave-th per sistemare gli stili css di width
            va usato se si hanno piu' periodi, e va messo in tutti
             - slave-td per sistemare gli stili css di height 
             - se ci sono Valore e Risultato, uno va col master-td e l'altro con lo slave-td
             - rimosso display-inline-block
             -->
            <#assign tdClassValue="widget-area-style master-td slave-th"/> <#--  slave-td -->
            <#if (context.glFiscalTypeId == "ALL" || context.glFiscalTypeIdList?size != 1) && (context.showPeriods == "NONE" || context.showPeriods == "OPEN")>
            	<#if showColumnScoreKpi == "Y" && !renderColumnScoreKpi>
	            	<#assign tdClassValue="widget-area-style master-td"/>
	            </#if>
	            <#if showColumnScoreKpi == "Y" && renderColumnScoreKpi> 
	            	<#assign tdClassValue="widget-area-style slave-td"/>
	            </#if>
            <#elseif (context.glFiscalTypeId != "ALL" && context.glFiscalTypeIdList?size == 1) && (context.showPeriods == "NONE" || context.showPeriods == "OPEN")>
            	<#if showColumnScoreKpi == "Y" && !renderColumnScoreKpi>
	            	<#assign tdClassValue="widget-area-style master-td"/>
	            </#if>
	            <#if showColumnScoreKpi == "Y" && renderColumnScoreKpi> 
	            	<#assign tdClassValue="widget-area-style slave-td"/>
	            </#if>
            <#elseif (context.glFiscalTypeId == "ALL" || context.glFiscalTypeIdList?size != 1)>
	            <#if periodIndex % rowspanPeriod == 1 && showColumnScoreKpi == "Y" && renderColumnScoreKpi >
	            	<#assign tdClassValue="widget-area-style slave-td slave-th"/>
	            <#elseif periodIndex % rowspanPeriod == 1 > <#-- || (showColumnScoreKpi == "Y" && !renderColumnScoreKpi) -->
	            	<#assign tdClassValue="widget-area-style master-td slave-th"/>
	            <#elseif periodIndex % rowspanPeriod != 1 && showColumnScoreKpi == "Y" && !renderColumnScoreKpi >
	            	<#assign tdClassValue="widget-area-style master-td slave-th"/>
	            <#elseif periodIndex % rowspanPeriod != 1 || (showColumnScoreKpi == "Y" && renderColumnScoreKpi)> 
	            	<#assign tdClassValue="widget-area-style slave-td slave-th"/>
	            </#if>
	        <#elseif (context.glFiscalTypeId != "ALL" && context.glFiscalTypeIdList?size == 1)>
	            <#if showColumnScoreKpi == "Y" && !renderColumnScoreKpi>
	            	<#assign tdClassValue="widget-area-style slave-td slave-th"/>
	            </#if>
	            <#if showColumnScoreKpi == "Y" && renderColumnScoreKpi> 
	            	<#assign tdClassValue="widget-area-style slave-td slave-th"/>
	            </#if>
	        </#if>
            class="${openPortletStyle} ${tdClassValue}">
            <#assign renderOtherTd = false>
            <#assign renderDetailTd = false>
            <#if workEffortTransactionIndicatorView.hasComments?if_exists>
            	<span class="fa transactionWithNote" style="float: left;"></span>
            <#else>
                <#if showValuesPanel == 'Y'>
                    <span class="far fa-sticky-note" style="float: left;"></span>
                </#if>
            </#if>
            
            <input type="hidden" value="${isReadOnlyRow?string}" name="isReadOnlyRow_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="${workEffortTypePeriodId?if_exists}" name="workEffortTypePeriodId_o_${index}" class="ignore_check_modification"/>
            
            <input type="hidden" value="${workEffortTransactionIndicatorView.fromDate?if_exists}" name="workEffortTransactionIndicatorViewfromDate_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="${workEffortTransactionIndicatorView.thruDate?if_exists}" name="workEffortTransactionIndicatorViewthruDate_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="${workEffortTransactionIndicatorView.customTimePeriodFromDate?if_exists}" name="workEffortTransactionIndicatorViewcustomTimePeriodFromDate_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="${workEffortTransactionIndicatorView.customTimePeriodThruDate?if_exists}" name="workEffortTransactionIndicatorViewcustomTimePeriodThruDate_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="${workEffortTransactionIndicatorView_customTimePeriodId?if_exists}" name="workEffortTransactionIndicatorView_customTimePeriodId_o_${index}" class="ignore_check_modification"/>
            
            <input type="hidden" value="${hasMandatoryBudgetEmpty?string}" name="hasMandatoryBudgetEmpty_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="${isRil?string}" name="isRil_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="${workEffortTransactionIndicatorView.customTimePeriodId?if_exists}" name="workEffortTransactionIndicatorViewcustomTimePeriodId_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" disabled="disabled" value="${workEffortTypePeriod?if_exists}" name="workEffortTypePeriod_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="${crudEnumIdSecondary?if_exists}" name="crudEnumIdSecondary_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="${valModId?if_exists}" name="valModId_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="${workEffortTransactionIndicatorView.isReadOnly?if_exists?string}" name="workEffortTransactionIndicatorViewisReadOnly_o_${index}" class="ignore_check_modification"/>
            
            <input type="hidden" <#if isReadOnlyRow >value="Y"<#else>value="N"</#if> name="isReadOnly_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="${workEffortTransactionIndicatorView.weTransMeasureId?if_exists}" name="workEffortMeasureId_o_${index}" class="ignore_check_modification"/>
                
            <input type="hidden" value="${context.folderIndex}" name="folderIndex_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="${context.contentIdInd}" name="contentIdInd_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" name="backAreaId_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" name="forcedBackAreaId_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" name="successCode_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="WorkEffortTransactionIndicatorView" name="operationalEntityName_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="N" name="saveView_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="N" name="contextManagement_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" name="subFolder_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="Y" name="_rowSubmit_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="N" name="insertMode_o_${index}" class="ignore_check_modification"/>
            
            <input type="hidden" value="WorkEffortTransactionIndicatorView" name="entityName_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="UPDATE" name="operation_o_${index}" class="ignore_check_modification"/>
            <input type="hidden" value="${workEffortTransactionIndicatorView.inputEnumId?if_exists}" name="inputEnumId_o_${index}" class="ignore_check_modification"/>
            
            <#if !workEffortTransactionIndicatorView.partyId?if_exists?has_content>
	            <#if showDetail != 'N' >
	                <input type="hidden" value="${workEffortTransactionIndicatorView.entryPartyId?if_exists}" name="partyId_o_${index}" class="ignore_check_modification">
	                <input type="hidden" value="${workEffortTransactionIndicatorView.entryRoleTypeId?if_exists}" name="roleTypeId_o_${index}" class="ignore_check_modification">
	            <#elseif workEffortTransactionIndicatorView.detectOrgUnitIdFlag?if_exists?has_content && "Y" == workEffortTransactionIndicatorView.detectOrgUnitIdFlag>
	                <input type="hidden" value="${workEffortTransactionIndicatorView.orgUnitId?if_exists}" name="partyId_o_${index}" class="ignore_check_modification">
	                <input type="hidden" value="${workEffortTransactionIndicatorView.orgUnitRoleTypeId?if_exists}" name="roleTypeId_o_${index}" class="ignore_check_modification">
	            <#else>
	                <input type="hidden" value="${workEffortTransactionIndicatorView.orgUnitId?if_exists}" name="partyId_o_${index}" class="ignore_check_modification">
	                <input type="hidden" value="${workEffortTransactionIndicatorView.orgUnitRoleTypeId?if_exists}" name="roleTypeId_o_${index}" class="ignore_check_modification">
	            </#if>
            <#else>
                <input type="hidden" value="${workEffortTransactionIndicatorView.partyId?if_exists}" name="partyId_o_${index}" class="ignore_check_modification">
                <input type="hidden" value="${workEffortTransactionIndicatorView.roleTypeId?if_exists}" name="roleTypeId_o_${index}" class="ignore_check_modification">
             </#if>
            
            <input type="hidden" value="${workEffortTransactionIndicatorView.entryPartyId?if_exists}" name="entryPartyId_o_${index}" class="ignore_check_modification">
            <input type="hidden" value="${workEffortTransactionIndicatorView.entryRoleTypeId?if_exists}" name="entryRoleTypeId_o_${index}" class="ignore_check_modification">
            
            <#assign workEffort = delegator.findOne("WorkEffort", Static["org.ofbiz.base.util.UtilMisc"].toMap("workEffortId", workEffortTransactionIndicatorView.weTransWeId?if_exists), false)>
            <input type="hidden" value="${workEffort.workEffortTypeId}" name="workEffortTypeId_o_${index}" class="ignore_check_modification">
            <input type="hidden" value="FOLDER" name="weTypeContentTypeId_o_${index}" class="ignore_check_modification"/>
            
            <input type="hidden" value="${workEffortView.workEffortTypeRootId}" name="parentWorkEffortTypeId_o_${index}" class="ignore_check_modification">
            <input type="hidden" value="${workEffortTransactionIndicatorView.glFiscalTypeEnumId?if_exists}" name="glFiscalTypeEnumId_o_${index}" class="ignore_check_modification">
            
            
            <#if context.showColumnScoreKpi == "Y" && renderColumnScoreKpi?if_exists && workEffortTransactionIndicatorView.weTransIdKpiScore?if_exists?has_content>
				<#assign hasScoreKpi = true><#-- non usato -->
				<input type="hidden" name="weTransUomType_o_${index}" value="${workEffortTransactionIndicatorView.kpiScoreUomType?if_exists}" class="ignore_check_modification">
            	<input type="hidden" name="acctgTransTypeId_o_${index}" value="SCOREKPI" class="ignore_check_modification">
            	<input type="hidden" name="weTransAccountId_o_${index}" value="SCOREKPI" class="ignore_check_modification">
            	<input type="hidden" name="weTransIdKpiScore_o_${index}" value="${workEffortTransactionIndicatorView.weTransIdKpiScore?if_exists}" class="ignore_check_modification"/>
            	<input type="hidden" name="weTransEntryIdKpiScore_o_${index}" value="${workEffortTransactionIndicatorView.weTransEntryIdKpiScore?if_exists}" class="ignore_check_modification"/>
	            <input type="hidden" name="weTransId_o_${index}" value="${workEffortTransactionIndicatorView.weTransId?if_exists}" class="ignore_check_modification"/>
	            <input type="hidden" name="weTransEntryId_o_${index}" value="${workEffortTransactionIndicatorView.weTransEntryId?if_exists}" class="ignore_check_modification"/>
            <#elseif context.showColumnScoreKpi == "Y" && renderColumnScoreKpi?if_exists && !workEffortTransactionIndicatorView.weTransIdKpiScore?if_exists?has_content>
				<#assign hasScoreKpi = true><#-- non usato -->
				<input type="hidden" name="weTransUomType_o_${index}" value="${workEffortTransactionIndicatorView.kpiScoreUomType?if_exists}" class="ignore_check_modification">
            	<input type="hidden" name="acctgTransTypeId_o_${index}" value="SCOREKPI" class="ignore_check_modification">
            	<input type="hidden" name="weTransAccountId_o_${index}" value="SCOREKPI" class="ignore_check_modification">
            	<input type="hidden" name="weTransIdKpiScore_o_${index}" value="${workEffortTransactionIndicatorView.weTransIdKpiScore?if_exists}" class="ignore_check_modification"/>
            	<input type="hidden" name="weTransEntryIdKpiScore_o_${index}" value="${workEffortTransactionIndicatorView.weTransEntryIdKpiScore?if_exists}" class="ignore_check_modification"/>
            	<input type="hidden" name="weTransId_o_${index}" value="${workEffortTransactionIndicatorView.weTransId?if_exists}" class="ignore_check_modification"/>
            	<input type="hidden" name="weTransEntryId_o_${index}" value="${workEffortTransactionIndicatorView.weTransEntryId?if_exists}" class="ignore_check_modification"/>
            <#else>
        		<input type="hidden" name="weTransUomType_o_${index}" value="${workEffortTransactionIndicatorView.weTransUomType?if_exists}" class="ignore_check_modification">
            	<input type="hidden" name="weTransAccountId_o_${index}" value="${workEffortTransactionIndicatorView.weTransAccountId}" class="ignore_check_modification">
            	<input type="hidden" name="weTransId_o_${index}" value="${workEffortTransactionIndicatorView.weTransId?if_exists}" class="ignore_check_modification"/>
            	<input type="hidden" name="weTransEntryId_o_${index}" value="${workEffortTransactionIndicatorView.weTransEntryId?if_exists}" class="ignore_check_modification"/>
            </#if>
            <input type="hidden" name="weTransTypeValueId_o_${index}" value="${workEffortTransactionIndicatorView.weTransTypeValueId?if_exists}" class="ignore_check_modification">
            <input type="hidden" name="weTransDate_o_${index}" value="${workEffortTransactionIndicatorView.weTransDate?if_exists}" class="ignore_check_modification">
            <input type="hidden" name="customTimePeriodId_o_${index}" value="${workEffortTransactionIndicatorView_customTimePeriodId?if_exists}" class="ignore_check_modification">
            <input type="hidden" name="weTransCurrencyUomId_o_${index}" value="${workEffortTransactionIndicatorView.weTransCurrencyUomId?if_exists}" class="ignore_check_modification">
            <input type="hidden" name="weTransWeId_o_${index}" value="${workEffortTransactionIndicatorView.weTransWeId?if_exists}" class="ignore_check_modification">
            <input type="hidden" name="weTransMeasureId_o_${index}" value="${workEffortTransactionIndicatorView.weTransMeasureId}" class="ignore_check_modification">
            <input type="hidden" name="valModId_o_${index}" value="${valModId}"  class="ignore_check_modification">
            <input type="hidden" name="defaultOrganizationPartyId_o_${index}" value="${defaultOrganizationPartyId?if_exists}"/>
            <input type="hidden" name="localeSecondarySet_o_${index}" value="${localeSecondarySet?if_exists}"/>
            
            <input type="hidden" value="crudServiceDefaultOrchestration_WorkEffortTransactionView_Simplified" name="crudService_o_${index}" class="ignore_check_modification">
            
            <#if context.elabScoreIndic?has_content && context.elabScoreIndic?if_exists?default('N') != 'N'>
		        <input type="hidden" name="elabScoreIndic_o_${index}" value="${context.elabScoreIndic?if_exists}" class="submit-field"/>
		        <input type="hidden" name="crudServiceEpilog_o_${index}" value="crudServiceEpilog_elaboreteScoreIndic" class="submit-field"/>
		        <input type="hidden" name="openNewTransaction_o_${index}" value="N" class="submit-field"/>
		        <input type="hidden" name="searchDate_o_${index}" value="${context.searchDateCalculate?if_exists}" class="submit-field"/>     
	        </#if>
        	<input type="hidden" name="crudServiceEpilogGroovy_o_${index}" value="crudServiceEpilog_SaveExecute" class="ignore_check_modification"/>
	        <input type="hidden" name="saveExecuteContentId_o_${index}" value="${context.contentIdSecondary}" class="ignore_check_modification"/>
	        <input type="hidden" name="saveExecuteWorkEffortId_o_${index}" value="${parameters.workEffortId?if_exists}" class="ignore_check_modification"/>
	        
        
        	<#if renderColumnScoreKpi>
				<#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/weTransValueKpiScore.ftl" />
			<#else>
			    <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/weTransValue.ftl" />
            </#if>
	                                        

        </td>
    </#if>
</#if>
                                            