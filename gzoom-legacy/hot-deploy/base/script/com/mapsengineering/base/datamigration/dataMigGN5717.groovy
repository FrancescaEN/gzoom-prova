import org.ofbiz.base.util.*;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.condition.*;
import org.ofbiz.service.*;
import javolution.util.FastList;

def result = ServiceUtil.returnSuccess();

def delegator = dctx.getDelegator();
def security = dctx.getSecurity();

def contentIdList = 
["GP_MENU_00261", 
"GP_MENU_00239", 
"GP_MENU_00199", 
"GP_MENU_00262", 
"GP_MENU_00206",
"GP_MENU_00240", 
"GP_MENU_00263",
"GP_MENU_00021",
"GP_MENU_00022",
"GP_MENU_00025",
"GP_MENU_00205",
"GP_MENU_00202",
"GP_MENU_00026",
"GP_MENU_00200",
"GP_MENU_00027",
"GP_MENU_00201",
"GP_MENU_00204",
"GP_MENU_00040",
"GP_MENU_00045",
"GP_MENU_00207",
"GP_MENU_00249",
"GP_MENU_00192",
"GP_MENU_00211",
"GP_MENU_00234",
"GP_MENU_N0003",
"GP_MENU_00050",
"GP_MENU_00051",
"GP_MENU_00052",
"GP_MENU_00053",
"GP_MENU_00054",
"GP_MENU_00203",
"GP_MENU_00058",
"GP_MENU_00060",
"GP_MENU_00067",
"GP_MENU_00068",
"GP_MENU_00069",
"GP_MENU_00082",
"GP_MENU_00083",
"GP_MENU_00250",
"GP_MENU_00225",
"GP_MENU_00227",
"GP_MENU_00282",
"GP_MENU_00251",
"GP_MENU_00216",
"GP_MENU_00281",
"GP_MENU_00252",
"GP_MENU_00464",
"GP_MENU_00473",
"GP_MENU_00482",
"GP_MENU_00190",
"GP_MENU_00151",
"GP_MENU_00152",
"GP_MENU_00154",
"GP_MENU_00155",
"GP_MENU_00157",
"GP_MENU_00158",
"GP_MENU_00160",
"GP_MENU_00196",
"GP_MENU_00191",
"GP_MENU_00171",
"GP_MENU_00172",
"GP_MENU_00173",
"GP_MENU_00277",
"GP_MENU_00291",
"GP_MENU_00343",
"GP_MENU_00303",
"GP_MENU_00316",
"GP_MENU_00328",
"GP_MENU_00238"];



// check permission
userLogin = (GenericValue) context.get("userLogin");
if (!security.hasPermission("ENTITY_MAINT", userLogin)) {
    return ServiceUtil.returnError(UtilProperties.getMessage("BaseErrorLabels", "ManagementErrorModelEntityNotSet", locale));
}

for (String key : contentIdList) {
    // delete from ContentAttribute
    List<EntityCondition> conditionContentAttribute = FastList.newInstance();
    conditionContentAttribute.add(EntityCondition.makeCondition("contentId", key));
    contentAttributeList = delegator.findList("ContentAttribute", EntityCondition.makeCondition(conditionContentAttribute), null, null, null, false);

    

    if(UtilValidate.isNotEmpty(contentAttributeList)) {
        Debug.log("Elimino contentAttributeList...");
        delegator.removeAll(contentAttributeList);
    }

    
    // delete from SecurityGroupContent
    List<EntityCondition> conditionsSecurityGroupContent = FastList.newInstance();
    conditionsSecurityGroupContent.add(EntityCondition.makeCondition("contentId",key));
    securityGroupList = delegator.findList("SecurityGroupContent", EntityCondition.makeCondition(conditionsSecurityGroupContent), null, null, null, false);

    

    if(UtilValidate.isNotEmpty(securityGroupList)) {
        Debug.log("Elimino securityGroupList...");
        delegator.removeAll(securityGroupList);
    }


    // delete from ContentAssoc
    List<EntityCondition> conditionsContentAssoc = FastList.newInstance();
    conditionsContentAssoc.add(EntityCondition.makeCondition("contentIdTo", key));
    contentAssocList = delegator.findList("ContentAssoc", EntityCondition.makeCondition(conditionsContentAssoc), null, null, null, false);

    

    if(UtilValidate.isNotEmpty(contentAssocList)) {
        Debug.log("Elimino contentAssocList...");
        delegator.removeAll(contentAssocList);
    }

    
    //delete from Content
    List<EntityCondition> conditionsContent = FastList.newInstance();
    conditionsContent.add(EntityCondition.makeCondition("contentId", key));
    contentList = delegator.findList("Content", EntityCondition.makeCondition(conditionsContent), null, null, null, false);

    

    if(UtilValidate.isNotEmpty(contentList)) {
        Debug.log("Elimino contentList...");
        delegator.removeAll(contentList);
    }


}

return result;

