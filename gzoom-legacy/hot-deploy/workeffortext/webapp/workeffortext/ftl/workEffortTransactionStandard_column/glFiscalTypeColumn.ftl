<#-- COLONNA CON GL_FISCAL_TYPE (OPT) -->
<#-- RENDER DEL TH - TESTATA -->
<#if renderThead == "Y">
    <#if context.glFiscalTypeId == "ALL" || context.glFiscalTypeIdList?size != 1>
    	<th rowspan=${thRowspan} class="${glFiscalTypeIdTitleAreaClass}"><div>${uiLabelMap.FormFieldTitle_weTransTypeValueId}</div></th>
    	<#assign colspan = colspan + 1/>
    </#if>
<#else>
<#-- RENDER DEL TD - BODY -->    		                
    <#if renderOtherTd>
        <#if context.glFiscalTypeId == "ALL" || context.glFiscalTypeIdList?size != 1>
			<#-- rowspan sara' il numero di periodi -->
	        <#assign tdRowspanF = rowspanPeriod/>
		    <#if context.showColumnScoreKpi == "Y">
				<#-- se showColumnScoreKpi == "Y" avremo 2 valori per periodo quindi rowspan sara' il numero di periodi * 2 -->
	        	<#assign tdRowspanF = rowspanPeriod * 2/>
		    </#if>
		    <td rowspan="${tdRowspanF}" class="widget-area-style glFiscalTypeId">
			    <#if localeSecondarySet?has_content && localeSecondarySet?default('N') == 'Y'>
			        ${workEffortTransactionIndicatorView.weTransTypeValueDescLang?if_exists}
			    <#else>
			        ${workEffortTransactionIndicatorView.weTransTypeValueDesc?if_exists}
			    </#if>
			</td>
		</#if>
    </#if>
</#if>                                            