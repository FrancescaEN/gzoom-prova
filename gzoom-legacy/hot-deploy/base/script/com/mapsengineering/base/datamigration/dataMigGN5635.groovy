import org.ofbiz.base.util.*;
import org.ofbiz.service.*;
import com.mapsengineering.base.datamigration.util.DatabaseUtil;
import org.ofbiz.entity.condition.*;

def result = ServiceUtil.returnSuccess();

def delegator = dctx.getDelegator();

def security = dctx.getSecurity();

// check permission
userLogin = (GenericValue) context.get("userLogin");
if (!security.hasPermission("ENTITY_MAINT", userLogin)) {
    return ServiceUtil.returnError(UtilProperties.getMessage("BaseErrorLabels", "ManagementErrorModelEntityNotSet", locale));
}

def groupName = (String) context.get("groupName");
if(UtilValidate.isEmpty(groupName)) {
	groupName = DatabaseUtil.DEFAULT_GROUP_NAME;
}

def helperInfo = delegator.getGroupHelperInfo(groupName);
def dbUtil = new DatabaseUtil(helperInfo);
// dbUtil.deleteForeignKeyAndIndex("WORK_EFFORT_TYPE_CONTENT_TYPE", "FK_WTCT_CN");
dbUtil.addForeignKey("WORK_EFFORT_TYPE_CONTENT_TYPE", "FK_WTCT_CN", "CONTENT_ID", "CONTENT", "CONTENT_ID");

def workEffortTypeContentTypeList = delegator.findList("WorkEffortTypeContentType", EntityCondition.makeCondition("contentId", null), null, null, null, false);
if (UtilValidate.isNotEmpty(workEffortTypeContentTypeList)) {
    for (GenericValue workEffortTypeContentType: workEffortTypeContentTypeList) {
        workEffortTypeContentType.contentId = "WEFLD_CONT";
        delegator.store(workEffortTypeContentType);
    }
}

return result;

