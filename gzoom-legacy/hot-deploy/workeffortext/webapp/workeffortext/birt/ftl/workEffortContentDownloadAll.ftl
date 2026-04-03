<#if listIt?has_content && listIt.size() &gt; 0>
    <div align="right">
        <a href="<@ofbizUrl>downloadAllWorkEffortContent</@ofbizUrl>?workEffortId=${parameters.workEffortId?if_exists}&amp;allegatoContentId=${parameters.allegatoContentId?if_exists}" target="_blank">${uiLabelMap.BaseDownloadAllFiles}</a>
    </div>
</#if>