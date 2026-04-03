<#macro table tableName groupName = "">
    ${freeMarkerQuery.getTableName(tableName, groupName)}<#t>
</#macro>
<#macro param value type = 0>
    <#local void=freeMarkerQuery.addParam(value, type)?if_exists>
    ?<#t>
</#macro>
<#-- addParam aggiunge il param ai param della query, lo sostituisce con un punto interrogativo, e inserisce un a capo -->
<#-- upperCase aggiunge il param ai param della query, lo sostituisce con un punto interrogativo, e inserisce un a capo -->
<#-- % viene aggiunto in caso di operatore like -->
<#macro param_upper_case value type = 0 like = false>
	<#assign valueUpperCase = freeMarkerQuery.upperCase(value)?if_exists />
    <#if like>
		<#local void=freeMarkerQuery.addParam('%' + valueUpperCase + '%', type)?if_exists>
    	?<#t>
	<#else>
		<#local void=freeMarkerQuery.addParam(valueUpperCase, type)?if_exists>
    	?<#t>
	</#if>
</#macro>
