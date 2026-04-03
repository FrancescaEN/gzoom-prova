import org.ofbiz.base.util.*;
import org.ofbiz.entity.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.entity.util.*;
import com.mapsengineering.workeffortext.util.FromAndThruDatesProviderFromParams;
import javolution.util.*;

def nowStamp = UtilDateTime.nowTimestamp();
Debug.log("Script loadWorkEffortRoot.groovy parameters.workEffortIdRoot " + parameters.workEffortIdRoot + " or parameters.workEffortId " + parameters.workEffortId);
Debug.log("Script loadWorkEffortRoot.groovy parameters.ignoreGpMenuEnumId " + parameters.ignoreGpMenuEnumId + " or parameters.gpMenuEnumId " + parameters.gpMenuEnumId);

// il controllo viene fatto anche nel retrieveOrganizationalAndStrategicPerformanceHeaderData...
if (parameters.rootInqyTree != "Y") {
    def rootSearchRootInqyServiceMap = [:];
    rootSearchRootInqyServiceMap.put("workEffortRootId", UtilValidate.isNotEmpty(parameters.workEffortIdRoot) ? parameters.workEffortIdRoot : parameters.workEffortId);
    rootSearchRootInqyServiceMap.put("userLogin", context.userLogin);
    def rootSearchRootInqyServiceRes = dispatcher.runSync("getCanViewUpdateWorkEffortRoot", rootSearchRootInqyServiceMap);
    Debug.log("[loadWorkEffortRoot.groovy] -  rootSearchRootInqyServiceRes " + rootSearchRootInqyServiceRes );
    // rootInqyTree = (rootSearchRootInqyServiceRes.canUpdateRoot == "Y" ? "N" : "Y" );
    // rootInqyTree deve essere Y solo in caso di interrogazione
    context.isForcedReadOnly =  "Y".equals(rootSearchRootInqyServiceRes.canUpdateRoot) ? "N" : "Y"; // Boolean.valueOf("N".equals(rootSearchRootInqyServiceRes.canUpdateRoot));
    parameters.isForcedReadOnly =  "Y".equals(rootSearchRootInqyServiceRes.canUpdateRoot) ? "N" : "Y"; // Boolean.valueOf("N".equals(rootSearchRootInqyServiceRes.canUpdateRoot));
    Debug.log("[loadWorkEffortRoot.groovy] - context.isForcedReadOnly " + context.isForcedReadOnly + " parameters.isForcedReadOnly " + parameters.isForcedReadOnly );
    Debug.log("[loadWorkEffortRoot.groovy] - context.isReadOnly " + context.isReadOnly + " parameters.isReadOnly " + parameters.isReadOnly );
    
}

def workEffortViewRootEntity = delegator.findOne("WorkEffortView", ["workEffortId" : UtilValidate.isNotEmpty(parameters.workEffortIdRoot) ? parameters.workEffortIdRoot : parameters.workEffortId], false);
if (UtilValidate.isNotEmpty(workEffortViewRootEntity)) {
    context.workEffortRoot = workEffortViewRootEntity;
    
    // GN-1960
    if("Y".equals(workEffortViewRootEntity.enableMultiYearFlag) && (UtilValidate.isEmpty(parameters.targetCode) || "Y".equals(parameters.survey))) {
        def providerContextMap = new FastMap();
        providerContextMap.timeZone = context.timeZone;
        providerContextMap.locale = locale;
        providerContextMap.workEffortIdRoot = workEffortViewRootEntity.workEffortId;
        providerContextMap.workEffortTypeId = workEffortViewRootEntity.workEffortTypeId;
        
        FromAndThruDatesProviderFromParams fromAndThruDatesProvider = new FromAndThruDatesProviderFromParams(providerContextMap, parameters, delegator, true);
        fromAndThruDatesProvider.run();
        if(UtilValidate.isEmpty(parameters.searchDate) || fromAndThruDatesProvider.isEnableParentPeriodFilter() == true) {
            if (UtilValidate.isNotEmpty(fromAndThruDatesProvider.getDefaultSearchDate())) {
                parameters.searchDate = UtilDateTime.toDateString(fromAndThruDatesProvider.getDefaultSearchDate(), locale);
            }
        }
    }
    context.putAll(context.workEffortRoot);
}

def nowStamp2 = UtilDateTime.nowTimestamp();
Debug.log("Run action script in " + (nowStamp2.getTime() - nowStamp.getTime()) + " milliseconds. script = component://workeffortext/webapp/workeffortext/WEB-INF/actions/loadWorkEffortRoot.groovy");
