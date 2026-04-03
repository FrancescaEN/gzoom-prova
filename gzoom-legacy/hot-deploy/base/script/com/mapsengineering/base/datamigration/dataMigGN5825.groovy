import org.ofbiz.base.util.*;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.condition.*;
import org.ofbiz.service.*;
import javolution.util.FastList;

def result = ServiceUtil.returnSuccess();

def delegator = dctx.getDelegator();
def security = dctx.getSecurity();


// check permission

userLogin = (GenericValue) context.get("userLogin");
if (!security.hasPermission("ENTITY_MAINT", userLogin)) {
    return ServiceUtil.returnError(UtilProperties.getMessage("BaseErrorLabels", "ManagementErrorModelEntityNotSet", locale));
}

Debug.log("ContentAttribute");
// delete from ContentAttribute
List<EntityCondition> conditionContentAttribute = FastList.newInstance();
conditionContentAttribute.add(EntityCondition.makeCondition("contentId", "GP_MENU_00348"));
contentAttributeList = delegator.findList("ContentAttribute", EntityCondition.makeCondition(conditionContentAttribute), null, null, null, false);

if(UtilValidate.isNotEmpty(contentAttributeList)) {
    Debug.log("Elimino contentAttributeList...");
    delegator.removeAll(contentAttributeList);
}

Debug.log("SecurityGroupContent");
// delete from SecurityGroupContent
List<EntityCondition> conditionsSecurityGroupContent = FastList.newInstance();
conditionsSecurityGroupContent.add(EntityCondition.makeCondition("contentId","GP_MENU_00348"));
securityGroupList = delegator.findList("SecurityGroupContent", EntityCondition.makeCondition(conditionsSecurityGroupContent), null, null, null, false);

if(UtilValidate.isNotEmpty(securityGroupList)) {
    Debug.log("Elimino securityGroupList...");
    delegator.removeAll(securityGroupList);
}

Debug.log("SecurityGroupPermission");
// delete from SecurityGroupPermission
List<EntityCondition> conditionsSecurityGroupPermission = FastList.newInstance();
conditionsSecurityGroupPermission.add(EntityCondition.makeCondition("permissionId","VISITORS_VIEW"));
securityGroupPermissionList = delegator.findList("SecurityGroupPermission", EntityCondition.makeCondition(conditionsSecurityGroupPermission), null, null, null, false);

if(UtilValidate.isNotEmpty(securityGroupPermissionList)) {
    Debug.log("Elimino securityGroupPermissionList...");
    delegator.removeAll(securityGroupPermissionList);
}

Debug.log("SecurityPermission");
// delete from SecurityPermission
def permissionIdList = ["VISITORS_VIEW", "VISITORS_CREATE", "VISITORS_UPDATE", "VISITORS_DELETE" ]
for (String key : permissionIdList) {
    List<EntityCondition> conditionsSecurityPermission = FastList.newInstance();
    conditionsSecurityPermission.add(EntityCondition.makeCondition("permissionId",key));
    securityPermissionList = delegator.findList("SecurityPermission", EntityCondition.makeCondition(conditionsSecurityPermission), null, null, null, false);

    if(UtilValidate.isNotEmpty(securityPermissionList)) {
        Debug.log("Elimino securityPermissionList...");
        delegator.removeAll(securityPermissionList);
    }
}
Debug.log("ContentAssoc");
// delete from ContentAssoc
List<EntityCondition> conditionsContentAssoc = FastList.newInstance();
conditionsContentAssoc.add(EntityCondition.makeCondition("contentIdTo", "GP_MENU_00348"));
contentAssocList = delegator.findList("ContentAssoc", EntityCondition.makeCondition(conditionsContentAssoc), null, null, null, false);

if(UtilValidate.isNotEmpty(contentAssocList)) {
    Debug.log("Elimino contentAssocList...");
    delegator.removeAll(contentAssocList);
}

Debug.log("Content");
//delete from Content
List<EntityCondition> conditionsContent = FastList.newInstance();
conditionsContent.add(EntityCondition.makeCondition("contentId", "GP_MENU_00348"));
contentList = delegator.findList("Content", EntityCondition.makeCondition(conditionsContent), null, null, null, false);

if(UtilValidate.isNotEmpty(contentList)) {
    Debug.log("Elimino contentList...");
    delegator.removeAll(contentList);
}




return result;

