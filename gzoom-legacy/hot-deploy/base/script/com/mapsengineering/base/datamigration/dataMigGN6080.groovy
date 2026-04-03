import org.ofbiz.base.util.*;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.condition.*;
import org.ofbiz.service.*;
import javolution.util.FastList;

def result = ServiceUtil.returnSuccess();

def delegator = dctx.getDelegator();
def security = dctx.getSecurity();

def contentIdList = 
["GP_MENU_00269",
 "GP_MENU_00283",
 "GP_MENU_00335",
 "GP_MENU_00295",
 "GP_MENU_00308",
 "GP_MENU_00320"

];



// check permission
userLogin = (GenericValue) context.get("userLogin");
if (!security.hasPermission("ENTITY_MAINT", userLogin)) {
    return ServiceUtil.returnError(UtilProperties.getMessage("BaseErrorLabels", "ManagementErrorModelEntityNotSet", locale));
}


for (String key : contentIdList) {

   
    // delete from ContentAssoc
    List<EntityCondition> conditionsContentAssoc = FastList.newInstance();
    conditionsContentAssoc.add(EntityCondition.makeCondition("contentIdTo", key));
    contentAssocList = delegator.findList("ContentAssoc", EntityCondition.makeCondition(conditionsContentAssoc), null, null, null, false);

    

    if(UtilValidate.isNotEmpty(contentAssocList)) {
        Debug.log("Elimino contentAssocList...");
        delegator.removeAll(contentAssocList);
    }

    
}




return result;

