<#if renderNewRow != "Y">
    <#if renderThead == "Y">
	    <th rowspan=${thRowspan} class="${glAccountClassTitleAreaClass}"><div>${uiLabelMap.WemGlAccountClassId}</div></th>
	<#else>
	    <td class="widget-area-style" rowspan="${rowspan}">${workEffortTransactionIndicatorView.glAccountClassDescr?if_exists}</td>
	</#if>
<#else>
    <td rowspan="${rowspan}"><div class="glAccountClassDescr"></div></td>
</#if>