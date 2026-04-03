import org.ofbiz.base.util.*;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.util.EntityFindOptions;
import org.ofbiz.entity.condition.*;
import org.ofbiz.service.*;
import com.mapsengineering.base.datamigration.util.DatabaseUtil;
import javolution.util.FastList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Disabili tutti gli utenti presenti sul db con profilo FULLADMIN,
 * Crea i nuovi utenti a partire dal file GzoomTeam.properties,
 * Puo' essere rilanciato piu' volte
 */
def result = ServiceUtil.returnSuccess();

def delegator = dctx.getDelegator();
def security = dctx.getSecurity();

// check permission
userLogin = (GenericValue) context.get("userLogin");
if (!security.hasPermission("ENTITY_MAINT", userLogin)) {
    return ServiceUtil.returnError(UtilProperties.getMessage("BaseErrorLabels", "ManagementErrorModelEntityNotSet", locale));
}

def securityGroupList = 
["BIZADMIN",
"FLEXADMIN",
"VIEWADMIN",
"DEMO_ADMIN",
"DEMO_USER",
"ORDERPROC",
"ORDERENTRY",
"ORDERADMIN",
"ORDERADMIN_LTD",
"ORDERSUPPLIER_LTD",
"ORDERPURCH",
"ORDERENTRY_ALL",
"PARTYROLE_ADMIN",
"PARTYROLE_VIEW",
"PARTYBASE_ADMIN",
"PARTYBASE_VIEW",
"PARTY_F_EMPL",
"PARTY_F_GEN",
"PARTY_F_GOAL",
"PARTY_F_ORG",
"PARTYVIEW",
"PARTYADMIN",
"PARTY_ALLOCATION",
"PROJECTADMIN"];

for (String key : securityGroupList) {
    userLoginListToDisable = delegator.findList("UserLoginSecurityGroup"
            , EntityCondition.makeCondition(EntityCondition.makeCondition("groupId", key))
            , null, null, null, false);
    
    if (UtilValidate.isNotEmpty(userLoginListToDisable)) {
        userLoginListToDisable.each{ userToDisable ->
            Debug.log("userToDisable " + userToDisable.userLoginId);
            // rimuovo UserLoginSecurityGroup
            userLoginSecurityGroupList = delegator.findList("UserLoginSecurityGroup", EntityCondition.makeCondition("userLoginId", userToDisable.userLoginId), null, null, null, false);
            if(UtilValidate.isNotEmpty(userLoginSecurityGroupList) && !"system".equals(userToDisable.userLoginId)) {
                Debug.log("Pulizia userLoginSecurityGroupList...");
                delegator.removeAll(userLoginSecurityGroupList);
            }
            
            // rimuovo UserLoginValidPartyRole
            userLoginValidPartyRoleList = delegator.findList("UserLoginValidPartyRole", EntityCondition.makeCondition("userLoginId", userToDisable.userLoginId), null, null, null, false);
            if(UtilValidate.isNotEmpty(userLoginValidPartyRoleList)) {
                Debug.log("Pulizia userLoginValidPartyRoleList...");
                delegator.removeAll(userLoginValidPartyRoleList);
            }
            
            // rimuovo password, set enabled = N, sbianco partyId, sbianco password
            userLoginToDisable = delegator.findOne("UserLogin", ["userLoginId": userToDisable.userLoginId], false);
            if (UtilValidate.isNotEmpty(userLoginToDisable)) {
                Debug.log("disabilitazione userLoginToDisable " + userLoginToDisable.userLoginId);
                userLoginToDisable.enabled = "N";
                // sbianco partyId
                userLoginToDisable.partyId = null;
                userLoginToDisable.disabledDateTime = null;
                // sbianco password
                userLoginToDisable.currentPassword = null;
                delegator.store(userLoginToDisable);
            }
        }
    }
    
    securityGroupContentList = delegator.findList("SecurityGroupContent", EntityCondition.makeCondition("groupId", key), null, null, null, false);
    if(UtilValidate.isNotEmpty(securityGroupContentList)) {
        Debug.log("Pulizia securityGroupContentList...");
        delegator.removeAll(securityGroupContentList);
    }
    
    securityGroupPermissionList = delegator.findList("SecurityGroupPermission", EntityCondition.makeCondition("groupId", key), null, null, null, false);
    if(UtilValidate.isNotEmpty(securityGroupPermissionList)) {
        Debug.log("Pulizia securityGroupPermissionList...");
        delegator.removeAll(securityGroupPermissionList);
    }
    
    // <PortalPageColumn portalPageId="ProjectReportPage" columnSeqId="00001"/>
    portalPageColumnList = delegator.findList("PortalPageColumn", EntityCondition.makeCondition("portalPageId", "ProjectReportPage"), null, null, null, false);
    if(UtilValidate.isNotEmpty(portalPageColumnList)) {
        Debug.log("Pulizia portalPageColumnList...");
        delegator.removeAll(portalPageColumnList);
    }
    
    // PortalPage securityGroupId="PROJECTADMIN"
    portalPageList = delegator.findList("PortalPage", EntityCondition.makeCondition("securityGroupId", key), null, null, null, false);
    if(UtilValidate.isNotEmpty(portalPageList)) {
        Debug.log("Pulizia portalPageList...");
        delegator.removeAll(portalPageList);
    }
    
    // delete from PortalPagePortlet where portalPageId = 'ProjectReportPage'
    portalPagePortletList = delegator.findList("PortalPagePortlet", EntityCondition.makeCondition("portalPageId", "ProjectReportPage"), null, null, null, false);
    if(UtilValidate.isNotEmpty(portalPagePortletList)) {
        Debug.log("Elimino portalPagePortlet...");
        delegator.removeAll(portalPagePortletList);
    }
    
    // PortletPortletCategory
    portletPortletCategoryList = delegator.findList("PortletPortletCategory", EntityCondition.makeCondition("portalPortletId", "TimeEntryByProject"), null, null, null, false);
    if(UtilValidate.isNotEmpty(portletPortletCategoryList)) {
        Debug.log("Elimino portletPortletCategoryList...");
        delegator.removeAll(portletPortletCategoryList);
    }

    // <PortletCategory portletCategoryId="REPORT" description="Reports"/>
    portletCategoryList = delegator.findList("PortletCategory", EntityCondition.makeCondition("portletCategoryId", "REPORT"), null, null, null, false);
    if(UtilValidate.isNotEmpty(portletCategoryList)) {
        Debug.log("Elimino portletCategoryList...");
        delegator.removeAll(portletCategoryList);
    }
    
    // PortalPortlet
    portalPortletList = delegator.findList("PortalPortlet", EntityCondition.makeCondition("portalPortletId", "TimeEntryByProject"), null, null, null, false);
    if(UtilValidate.isNotEmpty(portalPortletList)) {
        Debug.log("Elimino portalPortletList...");
        delegator.removeAll(portalPortletList);
    }
    securityGroup =  delegator.findOne("SecurityGroup", ["groupId": key], false);
    if(UtilValidate.isNotEmpty(securityGroup)) {
        delegator.removeValue(securityGroup);
    }
}

return result;
