<#if !parameters.errorLoadTrans?has_content>
    <form id="WETVST003${accountTypeEnumId?if_exists}_WorkEffortTransactionView-${context.relationTitle?if_exists}" class="basic-form cachable noTableResizeHeight formToRefresh" name="WorkEffortTransactionViewStandardManagementMultiForm" method="post" action="<@ofbizUrl>elaborateMultiFormForUpdateAjax</@ofbizUrl>" 
    onsubmit="javascript:ajaxSubmitFormUpdateAreas('WETVST003${accountTypeEnumId?if_exists}_WorkEffortTransactionView-${context.relationTitle?if_exists}','','child-management-screenlet-container-WorkEffortTransactionView-${context.relationTitle?if_exists},<@ofbizUrl>childManagementListContainerOnly</@ofbizUrl>,searchDate=${parameters.searchDate?if_exists}&contextManagement=N&forcedBackAreaId=&folderIndex=${context.folderIndex?if_exists}&screenNameListIndex=${context.screenNameListIndex?if_exists}&backAreaId=&saveView=N&relationTitle=${context.relationTitle?if_exists}&successCode=&entityName=WorkEffortTransactionView&parentEntityName=WorkEffortView&managementFormType=multi&workEffortId=${context.weTransWeId?if_exists}&weTransWeId=${context.weTransWeId?if_exists}&contentIdInd=${context.contentIdInd?if_exists}&contentId=${context.contentIdInd?if_exists}&contentIdSecondary=${context.contentIdSecondary?if_exists}&wizard=N&subFolder=Y&scrollInt=${context.scrollInt}&rootInqyTree=${parameters.rootInqyTree?if_exists}&isForcedReadOnly=${parameters.isForcedReadOnly?if_exists}&specialized=${parameters.specialized?if_exists}'); return false;">
        
        <div id="WorkEffortTransactionViewStandardScreen" class="tableContainer" style="height: auto;">
		    <table id="table_TRANSSTD_WorkEffortTransactionIndicatorView-${context.relationTitle}" class="basic-table list-table padded-row-table resizable draggable toggleable selectable no-jar-selectable customizable headerFixable multi-editable noTableResizeHeight" cellspacing="0" cellpadding="0">
		        <#assign colspan = 0/>
		        <#assign thRowspan = 1/>
		        <#assign renderThead = "Y"/>
		        <#assign renderNewRow = "N"/>
		        <#if showColumnScoreKpi == "Y">
		        	<#assign thRowspan = 2/>
		        </#if>
		        <thead>
		            <tr class="header-row-2">
		                <#-- COLONNA CON SEQUENCE_ID -->
                        <#if context.showSequenceId == "Y">
                            <th rowspan=${thRowspan} style="width: 8em;">${uiLabelMap.FormFieldTitle_sequenceId}</th>
                        </#if> 
		            
                        <#if context.showComments == "LEFT">
                            <#if context.commentsEtchDescr?if_exists == "action">
                                <#assign commentsLabel = uiLabelMap.Indicator_comments_action>
                            <#elseif context.commentsEtchDescr?if_exists == "dataSource">
                            	<#assign commentsLabel = uiLabelMap.Indicator_comments_Data_Source>
                            <#elseif context.commentsEtchDescr?if_exists == "verificationSource">
                            	<#assign commentsLabel = uiLabelMap.Indicator_comments_Verification_Source>
                            <#elseif context.commentsEtchDescr?if_exists == "category">
                            	<#assign commentsLabel = uiLabelMap.Indicator_comments_category>                            	
                            <#elseif context.commentsEtchDescr?if_exists == "algorytm">
                            	<#assign commentsLabel = uiLabelMap.Indicator_comments_Algorytm>
                            <#else>
                                <#assign commentsLabel = uiLabelMap.Indicator_comments>
                            </#if>
                            <#if commentsTitleAreaClass?has_content>
                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                            		<th rowspan=${thRowspan} class="${commentsTitleAreaClass}">
                                		<div>
                                    		${commentsLabel}
                                    		<img src="${primaryLangFlagPath?if_exists}" title="${primaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>
                            		<th rowspan=${thRowspan} class="${commentsTitleAreaClass}">
                                		<div>
                                    		${commentsLabel}
                                    		<img src="${secondaryLangFlagPath?if_exists}" title="${secondaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>                            
                                <#else>
                                    <th rowspan=${thRowspan} class="${commentsTitleAreaClass}"><div>${commentsLabel}</div></th>
                                </#if> 
                            <#else>
                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                            		<th rowspan=${thRowspan} style="width: 10%">
                                		<div>
                                    		${commentsLabel}
                                    		<img src="${primaryLangFlagPath?if_exists}" title="${primaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>
                            		<th rowspan=${thRowspan} style="width: 10%">
                                		<div>
                                    		${commentsLabel}
                                    		<img src="${secondaryLangFlagPath?if_exists}" title="${secondaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>                            
                                <#else>
                                    <th rowspan=${thRowspan} style="width: 10%"><div>${commentsLabel}</div></th>
                                </#if>                                 
                            </#if>
                        </#if>
                                               
                        <#if context.showComments2 == "LEFT">
                            <#if context.comments2EtchDescr?if_exists == "action">
                                <#assign comments2Label = uiLabelMap.Indicator_comments_action>
                            <#elseif context.comments2EtchDescr?if_exists == "dataSource">
                            	<#assign comments2Label = uiLabelMap.Indicator_comments_Data_Source>
                            <#elseif context.comments2EtchDescr?if_exists == "verificationSource">
                            	<#assign comments2Label = uiLabelMap.Indicator_comments_Verification_Source>
                            <#elseif context.comments2EtchDescr?if_exists == "category">
                            	<#assign comments2Label = uiLabelMap.Indicator_comments_category>                             	
                            <#elseif context.comments2EtchDescr?if_exists == "algorytm">
                            	<#assign comments2Label = uiLabelMap.Indicator_comments_Algorytm>
                            <#else>
                                <#assign comments2Label = uiLabelMap.Indicator_comments>
                            </#if>                           
                            <#if comments2TitleAreaClass?has_content>
                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                            		<th rowspan=${thRowspan} class="${comments2TitleAreaClass}">
                                		<div>
                                    		${comments2Label}
                                    		<img src="${primaryLangFlagPath?if_exists}" title="${primaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>
                            		<th rowspan=${thRowspan} class="${comments2TitleAreaClass}">
                                		<div>
                                    		${comments2Label}
                                    		<img src="${secondaryLangFlagPath?if_exists}" title="${secondaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>                            
                                <#else>
                                    <th rowspan=${thRowspan} class="${comments2TitleAreaClass}"><div>${comments2Label}</div></th>
                                </#if> 
                            <#else>
                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                            		<th rowspan=${thRowspan} style="width: 10%">
                                		<div>
                                    		${comments2Label}
                                    		<img src="${primaryLangFlagPath?if_exists}" title="${primaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>
                            		<th rowspan=${thRowspan} style="width: 10%">
                                		<div>
                                    		${comments2Label}
                                    		<img src="${secondaryLangFlagPath?if_exists}" title="${secondaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>                            
                                <#else>
                                    <th rowspan=${thRowspan} style="width: 10%"><div>${comments2Label}</div></th>
                                </#if>                                 
                            </#if>                            
                            
                        </#if>
		                <#if context.showType == "SX">
	                        <th rowspan=${thRowspan} class="${typeTitleAreaClass}">${uiLabelMap.WorkEffortTypology}</th>
	                    </#if>  
	                    
	                    <#if context.showResourceType == "Y">
	                        <#if context.etchResourceType?if_exists == "dimension">
                                <#assign resourceTypeLabel = uiLabelMap.Dimensione>
                            <#elseif context.etchResourceType?if_exists == "tipologia">
                            	<#assign resourceTypeLabel = uiLabelMap.Tipologia>
                            <#else>
                                <#assign resourceTypeLabel = uiLabelMap.Natura>
                            </#if>
                            <th rowspan=${thRowspan} class="${resourceTypeTitleAreaClass}">${resourceTypeLabel}</th>
	                    </#if>
	                    
	                    <#if context.showAccountReference == "UO">
	                        <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/accountReferenceUOColumn.ftl" />
	                    <#elseif context.showAccountReference != "N">
	                        <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/accountReferenceColumn.ftl" />
	                    </#if>
		            	<#-- COLONNA CON INDICATORE -->
		                <#if context.showAccountReference != "N">
		                    <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/glAccountColumnNoMandatory.ftl" />
		                <#else>
		                    <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/glAccountColumn.ftl" />
		                </#if>
		                <#-- COLONNA CON UOM_DESCR (OPT) -->
                        <#if context.showUomDescr == "LEFT">
                            <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                            	<th rowspan=${thRowspan} class="${uomDescrTitleAreaClass}">
                                	<div>
                                    	${uiLabelMap.Indicator_uomDescr}
                                    	<img src="${primaryLangFlagPath?if_exists}" title="${primaryLangTooltip?if_exists}"/>
                                	</div>
                            	</th>
                            	<th rowspan=${thRowspan} class="${uomDescrTitleAreaClass}">
                                	<div>
                                    	${uiLabelMap.Indicator_uomDescr}
                                    	<img src="${secondaryLangFlagPath?if_exists}" title="${secondaryLangTooltip?if_exists}"/>
                                	</div>
                            	</th>                            
                            <#else>
                                <th rowspan=${thRowspan} class="${uomDescrTitleAreaClass}"><div>${uiLabelMap.Indicator_uomDescr}</div></th>
                            </#if>
                        </#if>
                        <#if context.showAccountClass == "Y">
	                        <#include "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/glAccountClassColumn.ftl" />
	                    </#if>
		                
					    <#if context.showKpiDescr == "Y">
				            <#if context.etchKpiDescr?if_exists == "target">
                                <#assign etchKpiDescrLabel = uiLabelMap.GlAccount_target>
                            <#else>
                                <#assign etchKpiDescrLabel = uiLabelMap.GlAccount_description>
                            </#if>
                            <th rowspan=${thRowspan} class="${kpiDescrTitleAreaClass}">${etchKpiDescrLabel}</th>
				        </#if>
		                        
				        <#if context.showType == "Y">
		                    <th rowspan=${thRowspan} class="${typeTitleAreaClass}">${uiLabelMap.WorkEffortTypology}</th>
		                </#if>
		                
		                <#if context.showDirection == "Y">
	                        <th rowspan=${thRowspan} class="${directionTitleAreaClass}">${uiLabelMap.FormFieldTitle_debitCreditDefault}</th>
	                    <#elseif context.showDirection == "S">
                            <th rowspan=${thRowspan} class="${directionTitleAreaClass}">${uiLabelMap.FormFieldTitle_debitCreditDefaultShort}</th>
	                    <#elseif context.showDirection == "T">
                            <th rowspan=${thRowspan} class="${directionTitleAreaClass}">${uiLabelMap.FormFieldTitle_debitCreditDefaultSymbol}</th>
	                    </#if>
		                
                        <#if context.showComments == "RIGHT"  || context.showComments == "Y">
                            <#if context.commentsEtchDescr?if_exists == "action">
                                <#assign commentsLabel = uiLabelMap.Indicator_comments_action>
                            <#elseif context.commentsEtchDescr?if_exists == "dataSource">
                            	<#assign commentsLabel = uiLabelMap.Indicator_comments_Data_Source>
                            <#elseif context.commentsEtchDescr?if_exists == "verificationSource">
                            	<#assign commentsLabel = uiLabelMap.Indicator_comments_Verification_Source>
                            <#elseif context.commentsEtchDescr?if_exists == "category">
                            	<#assign commentsLabel = uiLabelMap.Indicator_comments_category>                             	
                            <#elseif context.commentsEtchDescr?if_exists == "algorytm">
                            	<#assign commentsLabel = uiLabelMap.Indicator_comments_Algorytm>
                            <#else>
                                <#assign commentsLabel = uiLabelMap.Indicator_comments>
                            </#if>                           
                            <#if commentsTitleAreaClass?has_content>
                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                            		<th rowspan=${thRowspan} class="${commentsTitleAreaClass}">
                                		<div>
                                    		${commentsLabel}
                                    		<img src="${primaryLangFlagPath?if_exists}" title="${primaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>
                            		<th rowspan=${thRowspan} class="${commentsTitleAreaClass}">
                                		<div>
                                    		${commentsLabel}
                                    		<img src="${secondaryLangFlagPath?if_exists}" title="${secondaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>                            
                                <#else>
                                    <th rowspan=${thRowspan} class="${commentsTitleAreaClass}"><div>${commentsLabel}</div></th>
                                </#if> 
                            <#else>
                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                            		<th rowspan=${thRowspan} style="width: 10%">
                                		<div>
                                    		${commentsLabel}
                                    		<img src="${primaryLangFlagPath?if_exists}" title="${primaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>
                            		<th rowspan=${thRowspan} style="width: 10%">
                                		<div>
                                    		${commentsLabel}
                                    		<img src="${secondaryLangFlagPath?if_exists}" title="${secondaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>                            
                                <#else>
                                    <th rowspan=${thRowspan} style="width: 10%"><div>${commentsLabel}</div></th>
                                </#if>                                 
                            </#if>                            
                            
                        </#if>
                        
                        <#if context.showComments2 == "RIGHT"  || context.showComments2 == "Y">
                            <#if context.comments2EtchDescr?if_exists == "action">
                                <#assign comments2Label = uiLabelMap.Indicator_comments_action>
                            <#elseif context.comments2EtchDescr?if_exists == "dataSource">
                            	<#assign comments2Label = uiLabelMap.Indicator_comments_Data_Source>
                            <#elseif context.comments2EtchDescr?if_exists == "verificationSource">
                            	<#assign comments2Label = uiLabelMap.Indicator_comments_Verification_Source>
                            <#elseif context.comments2EtchDescr?if_exists == "category">
                            	<#assign comments2Label = uiLabelMap.Indicator_comments_category>                             	
                            <#elseif context.comments2EtchDescr?if_exists == "algorytm">
                            	<#assign comments2Label = uiLabelMap.Indicator_comments_Algorytm>
                            <#else>
                                <#assign comments2Label = uiLabelMap.Indicator_comments>
                            </#if>                           
                            <#if comments2TitleAreaClass?has_content>
                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                            		<th rowspan=${thRowspan} class="${comments2TitleAreaClass}">
                                		<div>
                                    		${comments2Label}
                                    		<img src="${primaryLangFlagPath?if_exists}" title="${primaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>
                            		<th rowspan=${thRowspan} class="${comments2TitleAreaClass}">
                                		<div>
                                    		${comments2Label}
                                    		<img src="${secondaryLangFlagPath?if_exists}" title="${secondaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>                            
                                <#else>
                                    <th rowspan=${thRowspan} class="${comments2TitleAreaClass}"><div>${comments2Label}</div></th>
                                </#if> 
                            <#else>
                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                            		<th rowspan=${thRowspan} style="width: 10%">
                                		<div>
                                    		${comments2Label}
                                    		<img src="${primaryLangFlagPath?if_exists}" title="${primaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>
                            		<th rowspan=${thRowspan} style="width: 10%">
                                		<div>
                                    		${comments2Label}
                                    		<img src="${secondaryLangFlagPath?if_exists}" title="${secondaryLangTooltip?if_exists}"/>
                                		</div>
                            		</th>                            
                                <#else>
                                    <th rowspan=${thRowspan} style="width: 10%"><div>${comments2Label}</div></th>
                                </#if>                                 
                            </#if>                            
                            
                        </#if>
                        
                        <#-- COLONNA CON KPI_SOURCE (OPT) -->
		                <#if context.showKpiSource == "Y">
                            <th rowspan=${thRowspan} class="${kpiSourceTitleAreaClass}">${uiLabelMap.FormFieldTitle_source}</th>
                        </#if>
                        <#-- COLONNA CON KPI_WEIGHT (OPT) -->
		                <#if context.showKpiWeight == "Y">
                            <th rowspan=${thRowspan} class="${kpiWeightTitleAreaClass}">${uiLabelMap.FormFieldTitle_kpiScoreWeight}</th>
                        </#if>
                        <#-- COLONNA CON KPI_OTHER_WEIGHT (OPT) -->
                        <#if context.showKpiOtherWeight == "Y">
                            <th rowspan=${thRowspan} class="${kpiOtherWeightTitleAreaClass}">${uiLabelMap.FormFieldTitle_kpiOtherWeight}</th>
                        </#if>
                        
                        <#-- COLONNA CON UOM_DESCR (OPT) -->
                        <#if context.showUomDescr == "Y">
                            <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                            	<th rowspan=${thRowspan} class="${uomDescrTitleAreaClass}">
                                	<div>
                                    	${uiLabelMap.Indicator_uomDescr}
                                    	<img src="${primaryLangFlagPath?if_exists}" title="${primaryLangTooltip?if_exists}"/>
                                	</div>
                            	</th>
                            	<th rowspan=${thRowspan} class="${uomDescrTitleAreaClass}">
                                	<div>
                                    	${uiLabelMap.Indicator_uomDescr}
                                    	<img src="${secondaryLangFlagPath?if_exists}" title="${secondaryLangTooltip?if_exists}"/>
                                	</div>
                            	</th>                            
                            <#else>
                                <th rowspan=${thRowspan} class="${uomDescrTitleAreaClass}"><div>${uiLabelMap.Indicator_uomDescr}</div></th>
                            </#if>
                        </#if>
                        
                        <#if context.showPeriods != "NULL">
	                        <#-- COLONNA CON DETAIL (OPT) -->
	                        <#if context.showDetail != "N">
	                        	<#assign size = 1 />
	        					<#if context.showPeriods != "NONE" && context.showPeriods != "OPEN">
			                        <#assign size = customTimePeriodList?size />
		        				</#if>
		        				<th rowspan=${thRowspan} class="${detailTitleAreaClass}"><div>${uiLabelMap.Detail}</div></th>
		                        <#assign colspan = colspan + size/>
	                        </#if>
	                        <#-- COLONNA CON ABBREVIATION - DEFAULT_UOM_ID -->
	                        <#if context.showUom == "Y">
	                            <th rowspan=${thRowspan} class="${uomTitleAreaClass}">${uiLabelMap.defaultUomIdShort}</th>
	                        </#if>                        
	                        <#-- COLONNA CON GL_FISCAL_TYPE (OPT) -->
	                        <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/glFiscalTypeColumn.ftl" />
	                        
	                        <#-- COLONNA O COLONNE PERIODI -->
	                        <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/periodColumn.ftl" />
	                    </#if>
		            </tr>
		        </thead>
		        <#assign renderThead = "N"/>
		        <tbody class="valIndicatore">
		        	<#assign index=0/>
		        	<#assign indexClass=0/>
		        	<#assign kpiScoreForWeTransValueTot=0/>
                    <#assign workEffortTransactionIndicatorViewTotal=null />
                    <#if localeSecondarySet?has_content && localeSecondarySet?default('N') == 'Y'>
		                <#assign droplistSelectFields = "[[uomRatingValue, uomCodeLang]]" />
		                <#assign droplistDisplayFields = "[[uomCodeLang]]" />
		                <#assign droplistEntityDescriptionField = "uomCodeLang" />
		            <#else>
		                <#assign droplistSelectFields = "[[uomRatingValue, uomCode]]" />
		                <#assign droplistDisplayFields = "[[uomCode]]" />
		                <#assign droplistEntityDescriptionField = "uomCode" />		                    	
		            </#if>
                    
		            <#list workEffortTransactionIndicatorViewList?if_exists as measureMap>
    		            <#assign partyList = measureMap.rowList />
    		            <#assign weTransMeasureId = "">
    		            <#assign renderFirstTd = true>
    		            <#assign renderOtherTd = true>
    		            <#assign renderDetailTd = true>
                        <#assign rowspanMeasure = partyList.size()>
                        <#-- un tr per cella, in modo da gestire il reset della form -->
                        <#assign periodIndex = 1>
                        <#list partyList?if_exists as mappaParty>
        		            
        		            <#assign entryPartyId = mappaParty.entryPartyId />
                            <#assign typeList = mappaParty.rowList />
                            <#assign rowspanDetail = typeList.size()> <#-- numero dei tipi di valore, per esempio ACTUAL, BUDGET, ecc... -->

                            <#list typeList?if_exists as mappaType>
                                <#if mappaType.weTransTypeValueId?has_content>
                        		    <#assign weTransTypeValueId = mappaType.weTransTypeValueId?if_exists />
                        		</#if>

                          
                                        		        		
        		        		<#assign rowList = mappaType.rowList />
                            	<#assign rowspanPeriod = rowList.size()>
                            	
                            	<#if context.showPeriods == "NONE" || context.showPeriods == "OPEN">
                            		<#assign rowspanPeriod = 1>
                            	<#elseif context.showPeriods == "NULL">
                            		<#assign rowspanPeriod = 0>
                            	</#if>
                            	
                            	<#assign rowspanParty = rowspanDetail * rowspanPeriod />
                                <#if context.showColumnScoreKpi == "Y">
									<#assign rowspanParty = rowspanDetail * rowspanPeriod * 2 />
                            	</#if>
                                
                                <#assign rowspan = rowspanMeasure * rowspanDetail * rowspanPeriod + 1 />
                            	<#if context.showDetail != "N">
                            		<#assign rowspan = rowspan + 1 />
                            	</#if>
                            	<#if context.showColumnScoreKpi == "Y">
									<#assign rowspan = rowspanMeasure * rowspanDetail * rowspanPeriod * 2 + 1 />
                            	</#if>
                            	<#assign firstTd = rowList[0]>
                            	<#assign rowListSize = rowList?size>
                            
                            	<#if "TOTAL" != measureMap.weTransMeasureId>
                            		<#-- sono gestiti nell'altro file -->
                                    <#assign periodNonehasTrans = false>
                                    <#assign hasScoreKpi = false>
                                    <#list rowList?if_exists as workEffortTransactionIndicatorView>
                                        <#if renderFirstTd>
                                            <tr <#if indexClass%2 != 0>
                                                class="indexClass_${indexClass} alternate-row"
                                                <#else>
                                                class="indexClass_${indexClass}"
                                                </#if>
                                            >
                                            <#assign renderFirstTd = false>
                                            
                                            <#-- COLONNA CON SEQUENCE_ID -->
                                            <#if context.showSequenceId == "Y">
                                                <td class="widget-area-style showSequenceId rowspanMeasure${rowspanMeasure} rowspanDetail${rowspanDetail} rowspanPeriod${rowspanPeriod}" rowspan="${rowspan}">
                                                    <input readonly="readonly" class="numericInList ignore_check_modification" type="text" maxlength="15" size="3" value="${firstTd.sequenceId?if_exists}" name="sequenceId_o_${index}">
                                                </td>
                                            </#if>                                            
                                            
                                            <#if context.showComments == "LEFT">
                                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                            		                <td class="widget-area-style showComments" rowspan="${rowspan}"><div>${firstTd.comments?if_exists}</div></td>
                            		                <td class="widget-area-style showComments" rowspan="${rowspan}"><div>${firstTd.commentsLang?if_exists}</div></td>                       
                                				<#else>
                                    				<td class="widget-area-style showComments" rowspan="${rowspan}"><div>${firstTd.comments?if_exists}</div></td>
                                				</#if> 
                                            </#if>
                                            
                                            <#if context.showComments2 == "LEFT">
                                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                            		                <td class="widget-area-style showComments2" rowspan="${rowspan}"><div>${firstTd.comments2?if_exists}</div></td>
                            		                <td class="widget-area-style showComments2" rowspan="${rowspan}"><div>${firstTd.comments2Lang?if_exists}</div></td>                       
                                				<#else>
                                    				<td class="widget-area-style showComments2" rowspan="${rowspan}"><div>${firstTd.comments2?if_exists}</div></td>
                                				</#if> 
                                            </#if>
                                            
                                            <#if context.showType == "SX">
                                                <td class="widget-area-style showType" rowspan="${rowspan}">
                                                    <div>${firstTd.gltDescr?if_exists}</div>
                                                </td>
                                            </#if>
                                            
                                            <#if context.showResourceType == "Y">
						                        <td class="widget-area-style showResourceType" rowspan="${rowspan}">
						                        	<div>${firstTd.glResourceTypeDesc?if_exists}</div>
						                        </td>
						                    </#if>
						                    
	                                        <#if context.showAccountReference == "UO">
	                        					<#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/accountReferenceUOColumn.ftl" />
	                    					<#elseif context.showAccountReference != "N">
	                    					    <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/accountReferenceColumn.ftl" />
	                                        </#if>	                                                                                                                                
                                            <#-- COLONNA CON INDICATORE -->
                                            <#if context.showAccountReference != "N">
		                                        <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/glAccountColumnNoMandatory.ftl" />
		                                    <#else>
		                                        <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/glAccountColumn.ftl" />
		                                    </#if>
                                            <#-- COLONNA CON UOM_DESCR (OPT) -->
                                            <#if context.showUomDescr == "LEFT">
                                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                                                    <td class="widget-area-style showUomDescr" rowspan="${rowspan}">
                                                        <div>${firstTd.weTransUomDesc?if_exists?replace('&nbsp;', ' ', 'r')}</div>
                                                    </td>
                                                    <td class="widget-area-style showUomDescr" rowspan="${rowspan}">
                                                        <div>${firstTd.weTransUomDescLang?if_exists?replace('&nbsp;', ' ', 'r')}</div>
                                                    </td>
                                                <#else>
                                                    <td class="widget-area-style showUomDescr" rowspan="${rowspan}">
                                                        <div>${firstTd.weTransUomDesc?if_exists?replace('&nbsp;', ' ', 'r')}</div>
                                                    </td>
                                                </#if>
                                            </#if>
                                            <#if context.showAccountClass == "Y">
						                        <#include "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/glAccountClassColumn.ftl" />
						                    </#if>
                                            
                                            <#if context.showKpiDescr == "Y">
									            <#if localeSecondarySet?has_content && localeSecondarySet?default('N') == 'Y'>
										            <td class="${kpiDescrTitleAreaClass} widget-area-style showKpiDescr" rowspan="${rowspan}">
	                                    				<div>${firstTd.glDescriptionLang?if_exists}</div>
	                                                </td>
										        <#else>
										            <td class="${kpiDescrTitleAreaClass} widget-area-style showKpiDescr" rowspan="${rowspan}">
	                                    				<div>${firstTd.glDescription?if_exists}</div>
	                                                </td>
										        </#if>
									        </#if>
							                
							                <#if context.showType == "Y">
                                                <td class="widget-area-style showType" rowspan="${rowspan}">
                                                    <div>${firstTd.gltDescr?if_exists}</div>
                                                </td>   
                                            </#if>
                                            
                                            <#if context.showDirection == "Y">
                                                <td class="widget-area-style showDirection" rowspan="${rowspan}">
                                                    <input readonly="readonly" class="ignore_check_modification" type="text" value="${firstTd.dcDescr?if_exists}" title="${firstTd.dcDescr?if_exists}" name="dcDescr_o_${index}">
                                                </td>   
                                            <#elseif context.showDirection == "S">
                                            	<#assign etichetta = uiLabelMap.GreaterThanEquals>
                                    			<#if firstTd.dc?if_exists != "D">
	                    							<#assign etichetta = uiLabelMap.LowerThanEquals>
	                    						</#if>
                                    			<td class="widget-area-style showDirection" rowspan="${rowspan}">
                                                    <input readonly="readonly" class="ignore_check_modification" type="text" value="${etichetta}" title="${firstTd.dcDescr?if_exists}" name="dcDescr_o_${index}">
                                                </td>
                                            <#elseif context.showDirection == "T">
                                            	<#assign etichetta = "+">
                                    			<#if firstTd.dc?if_exists != "D">
	                    							<#assign etichetta = "-">
	                    						</#if>
                                    			<td class="widget-area-style showDirection" rowspan="${rowspan}">
                                                    <input readonly="readonly" class="ignore_check_modification" type="text" value="${etichetta}" title="${firstTd.dcDescr?if_exists}" name="dcDescr_o_${index}">
                                                </td>
                                            </#if>                                            
                                            
                                            <#if context.showComments == "RIGHT"  || context.showComments == "Y">
                                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                            		                <td class="widget-area-style showComments" rowspan="${rowspan}"><div>${firstTd.comments?if_exists}</div></td>
                            		                <td class="widget-area-style showComments" rowspan="${rowspan}"><div>${firstTd.commentsLang?if_exists}</div></td>                       
                                				<#else>
                                    				<td class="widget-area-style showComments" rowspan="${rowspan}"><div>${firstTd.comments?if_exists}</div></td>
                                				</#if>                                                 
                                            </#if>
                                            <#if context.showComments2 == "RIGHT"  || context.showComments2 == "Y">
                                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                            		                <td class="widget-area-style showComments2" rowspan="${rowspan}"><div>${firstTd.comments2?if_exists}</div></td>
                            		                <td class="widget-area-style showComments2" rowspan="${rowspan}"><div>${firstTd.comments2Lang?if_exists}</div></td>                       
                                				<#else>
                                    				<td class="widget-area-style showComments2" rowspan="${rowspan}"><div>${firstTd.comments2?if_exists}</div></td>
                                				</#if>                                                 
                                            </#if>
                                            
                                            <#-- COLONNA CON KPI_SOURCE (OPT) -->
                                            <#if context.showKpiSource == "Y">
                                                <td class="widget-area-style showKpiSource" rowspan="${rowspan}">
                                    				<div>${firstTd.source?if_exists}</div>
                                                </td>
                                            </#if>
                                        
                                            <#-- COLONNA CON KPI_WEIGHT (OPT) -->
                                            <#if context.showKpiWeight == "Y">
                                                <td class="widget-area-style showKpiWeight" rowspan="${rowspan}">
                                                    <input readonly="readonly" class="numericInList ignore_check_modification" type="text" maxlength="15" size="3" value="${firstTd.kpiScoreWeight?if_exists}" name="kpiScoreWeight_o_${index}">
                                                </td>
                                            </#if>
                                        
                                            <#-- COLONNA CON KPI_OTHER_WEIGHT (OPT) -->
                                            <#if context.showKpiOtherWeight == "Y">
                                                <td class="widget-area-style showKpiOtherWeight" rowspan="${rowspan}">
                                                    <input readonly="readonly" class="numericInList ignore_check_modification" type="text" maxlength="15" size="3" value="${firstTd.kpiOtherWeight?if_exists}" name="kpiOtherWeight_o_${index}">
                                                </td>
                                            </#if>
                                        
                                            
                                            <#-- COLONNA CON UOM_DESCR (OPT) -->
                                            <#if context.showUomDescr == "Y">
                                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                                                    <td class="widget-area-style showUomDescr" rowspan="${rowspan}">
                                                        <div>${firstTd.weTransUomDesc?if_exists?replace('&nbsp;', ' ', 'r')}</div>
                                                    </td>
                                                    <td class="widget-area-style showUomDescr" rowspan="${rowspan}">
                                                        <div>${firstTd.weTransUomDescLang?if_exists?replace('&nbsp;', ' ', 'r')}</div>
                                                    </td>
                                                <#else>
                                                    <td class="widget-area-style showUomDescr" rowspan="${rowspan}">
                                                        <div>${firstTd.weTransUomDesc?if_exists?replace('&nbsp;', ' ', 'r')}</div>
                                                    </td>
                                                </#if>
                                            </#if>
                                        </tr>
                                        <#assign index = index+1>
                                            
                                        </#if>
                                        
                                        <#if context.showPeriods != "NULL">
	                                        <#if context.showPeriods == "NONE" && (workEffortTransactionIndicatorView.weTransDate?has_content || (!periodNonehasTrans && workEffortTransactionIndicatorView_index == (rowListSize - 1)))>
		    									<tr 
	                                            <#if indexClass%2 != 0>
	                                                class="indexClass_${indexClass} alternate-row"
	                                            <#else>
	                                                class="indexClass_${indexClass}"
	                                            </#if>
	                                        	>
	                                        	<#if renderDetailTd>
	                                            	<#if context.showUom == "Y">
	                                                	<td class="widget-area-style showUom" rowspan="${rowspanParty}" style="width: 8em;">
	                                                    	<#if localeSecondarySet?has_content && localeSecondarySet?default('N') == 'Y'>
	                                                        	<div>${firstTd.weTransUomAbbLang?if_exists}</div>
	                                                    	<#else>
	                                                        	<div>${firstTd.weTransUomAbb?if_exists}</div>
	                                                    	</#if>
	                                                	</td>
	                                            	</#if>
	                                            </#if>
	                                        	<#-- COLONNA CON GL_FISCAL_TYPE (OPT) -->
	                                            <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/glFiscalTypeColumn.ftl" />
	                                            
	                                            <#-- COLONNA O COLONNE PERIODI -->
	                                            <#assign renderColumnScoreKpi = false>
	                        					<#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/periodColumn.ftl" />
	                                            <#if context.showColumnScoreKpi == "Y">
	                                            	<#assign renderColumnScoreKpi = true>
	                                            	<#assign index = index+1>
	                                            	<tr 
			                                            <#if indexClass%2 != 0>
			                                                class="indexClass_${indexClass} alternate-row"
			                                            <#else>
			                                                class="indexClass_${indexClass}"
			                                            </#if>
		                                        	>
	                                        		<#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/periodColumn.ftl" />
	                                            	<#assign renderColumnScoreKpi = false>
	                                            </#if>
	                                            <#assign index = index+1>
	                                            <#assign periodIndex = periodIndex+1>
	                                        
	                                        <#elseif context.showPeriods != "NONE" && "ONE" == context.showDetail && workEffortTransactionIndicatorView.weTransMeasureId != parameters.workEffortMeasureId?if_exists>
	                                            <tr 
	                                            <#if indexClass%2 != 0>
	                                                class="indexClass_${indexClass} alternate-row"
	                                            <#else>
	                                                class="indexClass_${indexClass}"
	                                            </#if>
	                                        	>
	                                        	<td class="detail-closed" rowspan="${rowspanParty}" > - </td>
	                                            <#-- COLONNA CON ABBREVIATION - DEFAULT_UOM_ID -->
	                                            <#if context.showUom == "Y">
	                                                <td class="detail-closed" rowspan="${rowspanParty}">
	                                                    <#if localeSecondarySet?has_content && localeSecondarySet?default('N') == 'Y'>
	                                                        <div>${firstTd.weTransUomAbbLang?if_exists}</div>
	                                                    <#else>
	                                                        <div>${firstTd.weTransUomAbb?if_exists}</div>
	                                                    </#if>
	                                                </td>
	                                                <#assign colspan2 = colspan - 1>
	                                                <#if context.showColumnScoreKpi == "Y">
	                                                	<#assign colspan2 = colspan>
	                                                </#if>
	                                                <td class="detail-closed" colspan="${colspan2}"></td>
	                                            <#else>
	                                                <td class="detail-closed" colspan="${colspan}" class="slave-td widget-area-style"></td>
	                                            </#if>
	                                            <#assign index = index+1>
	                                            <#assign periodIndex = periodIndex+1>
	                                        <#elseif context.showPeriods != "NONE">
	                                            <tr 
	                                                <#if indexClass%2 != 0>
	                                                    class="indexClass_${indexClass} alternate-row"
	                                                <#else>
	                                                    class="indexClass_${indexClass}"
	                                                </#if>
	                                            >
	                                            
	                                            <#-- COLONNA CON DETAIL (OPT) -->
	                                            <#if renderDetailTd>
	                                                <#if context.showDetail != "N">
	                                                    <td class="widget-area-style" rowspan="${rowspanParty}" >${workEffortTransactionIndicatorView.entryPartyName?if_exists}</td>
	                                                </#if>
	                                                
	                                                <#-- COLONNA CON ABBREVIATION - DEFAULT_UOM_ID -->
	                                            	<#if context.showUom == "Y">
	                                                	<td class="widget-area-style" rowspan="${rowspanParty}">
	                                                    	<#if localeSecondarySet?has_content && localeSecondarySet?default('N') == 'Y'>
	                                                        	<div>${firstTd.weTransUomAbbLang?if_exists}</div>
	                                                    	<#else>
	                                                        	<div>${firstTd.weTransUomAbb?if_exists}</div>
	                                                    	</#if>
	                                                	</td>
	                                            	</#if>    
	                                            </#if>
	                                            
	                                            <#-- COLONNA CON GL_FISCAL_TYPE (OPT) -->
	                                            <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/glFiscalTypeColumn.ftl" />
	                                            <#-- COLONNA O COLONNE PERIODI -->
	                                            <#assign renderColumnScoreKpi = false>
	                                            <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/periodColumn.ftl" />
	                                            <#if context.showColumnScoreKpi == "Y">
	                                            	<#assign renderColumnScoreKpi = true>
	                                            	<#assign index = index+1>
	                                            	<tr 
			                                            <#if indexClass%2 != 0>
			                                                class="indexClass_${indexClass} alternate-row"
			                                            <#else>
			                                                class="indexClass_${indexClass}"
			                                            </#if>
		                                        	>
		                                        	<#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/periodColumn.ftl" />
	                                            	<#assign renderColumnScoreKpi = false>
	                                            </#if>
	                                            <#assign index = index+1>
	                                            <#assign periodIndex = periodIndex+1>
	                                        </#if>
	                                    </#if>
                                    </#list>
                                    <#assign renderOtherTd = true>
                               </#if>
                               
                           </#list>
                           <#assign renderDetailTd = true>
                           </tr>
                       </#list>
                       <#assign indexClass = indexClass+1>
                       
                    </#list>
                    
                    <#if "Y" == insertMode>
                        <tr <#if index%2 != 0>class="alternate-row new-row"
                            <#else>class="new-row"
                            </#if>>
                            
                            <#-- COLONNA CON SEQUENCE_ID -->
                            <#if context.showSequenceId == "Y">
                                <td>
                                    <input class="numericInList" type="text" maxlength="15" size="3" value="1" name="sequenceId_o_${index}">
                                </td>
                            </#if>                            
                            
                            <#if context.showComments == "LEFT">
                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                                    <td><input size="20" name="comments_o_${index}"/></td>
                            		<td><input size="20" name="commentsLang_o_${index}"/></td>                      
                                <#else>
                                    <td><input size="20" name="comments_o_${index}"/></td>
                                </#if>                                 
                            </#if>
                            
                            <#if context.showComments2 == "LEFT">
                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                                    <td><input size="20" name="comments2_o_${index}"/></td>
                            		<td><input size="20" name="comments2Lang_o_${index}"/></td>                      
                                <#else>
                                    <td><input size="20" name="comments2_o_${index}"/></td>
                                </#if>                                 
                            </#if>
                            
                            <#if context.showType == "SX">
                                <td>
                                    <input readonly="readonly" type="text" name="gltDescr_o_${index}">
                                </td>                            
                            </#if>   
                            
                            <#if context.showResourceType == "Y">
		                        <td>
		                        	<input readonly="readonly" type="text" name="gltDescr_o_${index}">
		                        </td>
		                    </#if>
						                    
                            <#if context.showAccountReference == "UO">
                                <#assign renderNewRow = "Y">
	                            <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/accountReferenceUOColumn.ftl" />
	                        <#elseif context.showAccountReference != "N">
	                            <#assign renderNewRow = "Y">
	                            <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/accountReferenceColumn.ftl" />
	                        </#if>
		                    
                            <#-- COLONNA CON INDICATORE -->
                            <#assign renderNewRow = "Y">                           
                            <#if context.showAccountReference != "N">
		                        <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/glAccountColumnNoMandatory.ftl" />
		                    <#else>
		                        <#include  "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/glAccountColumn.ftl" />
		                    </#if>
                            	                                                
                            <#if context.showAccountClass == "Y">
		                        <#include "/workeffortext/webapp/workeffortext/ftl/workEffortTransactionStandard_column/glAccountClassColumn.ftl" />
		                    </#if>
                            <#if context.showKpiDescr == "Y">
                                <td>
                                    <input readonly="readonly" type="text" name="glDescription_o_${index}">
                                </td>                            
                            </#if>
                            
                            <#if context.showType == "Y">
                                <td>
                                    <input readonly="readonly" type="text" name="gltDescr_o_${index}">
                                </td>                            
                            </#if>
                            
                            <#if context.showDirection == "Y" || context.showDirection == "S" || context.showDirection == "T">
                                <td>
                                    <input readonly="readonly" type="text" name="dcDescr_o_${index}">
                                </td>                            
                            </#if>
                            
                            <#if context.showComments == "RIGHT" || context.showComments == "Y">
                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                                    <td><input size="20" name="comments_o_${index}"/></td>
                            		<td><input size="20" name="commentsLang_o_${index}"/></td>                      
                                <#else>
                                    <td><input size="20" name="comments_o_${index}"/></td>
                                </#if> 
                            </#if>
                            
                            <#if context.showComments2 == "RIGHT" || context.showComments2 == "Y">
                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                                    <td><input size="20" name="comments2_o_${index}"/></td>
                            		<td><input size="20" name="comments2Lang_o_${index}"/></td>                      
                                <#else>
                                    <td><input size="20" name="comments2_o_${index}"/></td>
                                </#if> 
                            </#if>
                            
                            <#-- COLONNA CON KPI_SOURCE (OPT) -->
                            <#if context.showKpiSource == "Y">
                                <td class="widget-area-style showKpiSource">
                                	<input readonly="readonly" size="20" type="text" name="source_o_${index}"/>
                                </td>
                            </#if>
                            
                            <#-- COLONNA CON KPI_WEIGHT (OPT) -->
                            <#if context.showKpiWeight == "Y">
                                <td>
                                    <input decimal_digits="0" class="numericInList" type="text" maxlength="15" size="3" value="100" name="kpiScoreWeight_o_${index}">
                                </td>
                            </#if>
                            
                            <#-- COLONNA CON KPI_OTHER_WEIGHT (OPT) -->
                            <#if context.showKpiOtherWeight == "Y">
                                <td>
                                    <input decimal_digits="0" class="numericInList" type="text" maxlength="15" size="3" value="100" name="kpiOtherWeight_o_${index}">
                                </td>
                            </#if>
                            
                            
                            <#if context.showUomDescr == 'Y'>
                                <#if multiTypeLang?has_content && multiTypeLang?if_exists != "NONE">
                                    <td>
                                       <#if context.showAccountReference != "N">
                                           <input name="uomDescr_o_${index}" size="20" maxlength="2000" id="WETVST003${accountTypeEnumId?if_exists}_WorkEffortTransactionView-${context.relationTitle?if_exists}_uomDescr_o_${index}" type="text" class="accountName">
                                       <#else>
                                           <input name="uomDescr_o_${index}" size="20" maxlength="2000" id="WETVST003${accountTypeEnumId?if_exists}_WorkEffortTransactionView-${context.relationTitle?if_exists}_uomDescr_o_${index}" type="text">
                                        </#if>
                                    </td>
                                    <td>
                                       <#if context.showAccountReference != "N">
                                           <input name="uomDescrLang_o_${index}" size="20" maxlength="2000" id="WETVST003${accountTypeEnumId?if_exists}_WorkEffortTransactionView-${context.relationTitle?if_exists}_uomDescrLang_o_${index}" type="text" class="accountNameLang">
                                       <#else>
                                           <input name="uomDescrLang_o_${index}" size="20" maxlength="2000" id="WETVST003${accountTypeEnumId?if_exists}_WorkEffortTransactionView-${context.relationTitle?if_exists}_uomDescrLang_o_${index}" type="text">
                                        </#if>
                                    </td>
                                <#else> 
                                	<td>
                                    	<#if context.showAccountReference != "N">
                                        	<input name="uomDescr_o_${index}" size="20" maxlength="2000" id="WETVST003${accountTypeEnumId?if_exists}_WorkEffortTransactionView-${context.relationTitle?if_exists}_uomDescr_o_${index}" type="text" class="accountName">
                                    	<#else>
                                        	<input name="uomDescr_o_${index}" size="20" maxlength="2000" id="WETVST003${accountTypeEnumId?if_exists}_WorkEffortTransactionView-${context.relationTitle?if_exists}_uomDescr_o_${index}" type="text">
                                    	</#if>
                                	</td>                                                                   
                                </#if>
                            </#if>
                                                      
                            <#if context.showPeriods != "NULL">
		                        <#if context.showDetail != "N">
	                                <td><div/></td>
	                            </#if>
	                            
	                            <#-- COLONNA CON ABBREVIATION -->
	                            <#if context.showUom == "Y">
	                                <td>
	                                    <#if localeSecondarySet?has_content && localeSecondarySet?default('N') == 'Y'>
	                                        <div class="abbreviationLang"></div>
	                                    <#else>
	                                        <div class="abbreviation"></div>
	                                    </#if>
	                                </td>
	                            </#if>
                            
                                <#assign colspanInsertRow = 1/>
	                            <#if context.showPeriods == "OPEN" || context.showPeriods == "NONE">
	                            	<#assign colspanInsertRow = rowspanDetail?default(1)/>
	                            <#else>
	                                <#assign size = customTimePeriodList?size />
	                                <#assign colspanInsertRow = rowspanDetail?default(1) * rowspanPeriod?default(1)/>
	                            </#if>
	                            <#if context.showColumnScoreKpi == "Y">
									<#assign colspanInsertRow = colspanInsertRow * 2 />
	                        	</#if>
	                            <#if (context.glFiscalTypeId == "ALL" || context.glFiscalTypeIdList?size != 1) && rowspanDetail?default(1) == 1>
	                            	<#assign colspanInsertRow = colspanInsertRow + 1 />
	                            </#if>
	                            <td colspan="${colspanInsertRow}"><div/></td>
	                        </#if>
                        </tr>
                    </#if>
		        </tbody>
		    </table>
		</div>
		
		<input class="management-reset-button ignore_check_modification" type="submit" value="Reset Button" name="resetButton" style="display: none;">
		<input class="save-button ignore_check_modification" type="submit" value="Invia" name="submitButton" style="display: none;">
		<input class="management-delete-button ignore_check_modification" type="submit" value="Cancella" name="deleteButton" style="display: none;">    
	</form>
<br>
<br>
<br>
</#if>