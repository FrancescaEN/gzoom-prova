import org.ofbiz.base.util.*;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.util.EntityFindOptions;
import org.ofbiz.entity.condition.*;
import org.ofbiz.service.*;
import com.mapsengineering.base.datamigration.util.DatabaseUtil;

def result = ServiceUtil.returnSuccess();

def delegator = dctx.getDelegator();
def security = dctx.getSecurity();

def securityGroupPermissionList1 = 
["ACCOUNTINGEXT_ADMIN", 
"PARTYMGR_ADMIN", 
"OFBTOOLS_VIEW", 
"HUMANRESEXT_ADMIN", 
"BSCPERFMGR_ADMIN",
"REPORT_PRINT_CTX_BS_VIEW", 
"REPORT_PRINT_CTX_AC_VIEW",
"QUERY_CONFIG_CTX_PY_E_VIEW",
"ANALYSIS_CTX_BS_VIEW",
"ORGPERFMGR_ADMIN",
"TIMESHEET_CTX_OR_VIEW",
"REPORT_PRINT_CTX_OR_VIEW",
"ANALYSIS_CTX_OR_VIEW",
"EMPLPERFMGR_ADMIN",
"REPORT_PRINT_CTX_EP_VIEW",
"ANALYSIS_CTX_EP_VIEW",
"PARTPERFMGR_ADMIN",
"REPORT_PRINT_CTX_PA_VIEW",
"ANALYSIS_CTX_PA_VIEW",
"DIRIGPERFMGR_ADMIN",
"REPORT_PRINT_CTX_DI_VIEW",
"ANALYSIS_CTX_DI_VIEW",
"REPORT_PRINT_CTX_CO_VIEW",
"CORPERFMGR_ADMIN",
"ANALYSIS_CTX_CO_VIEW",
"TIMESHEET_CTX_PR_VIEW",
"REPORT_PRINT_CTX_PR_VIEW",
"PROCPERFMGR_ADMIN",
"ANALYSIS_CTX_PR_VIEW",
"REPORT_PRINT_CTX_GD_VIEW",
"QUERY_CONFIG_CTX_GD_E_VIEW",
"ANALYSIS_CTX_GD_VIEW",
"ANALYSIS_CTX_RE_VIEW",
"RENDPERFMGR_ADMIN",
"REPORT_PRINT_CTX_RE_VIEW",
"TRASPERFMGR_ADMIN",
"REPORT_PRINT_CTX_TR_VIEW",
"ANALYSIS_CTX_CG_VIEW",
"CDGPERFMGR_ADMIN",
"REPORT_PRINT_CTX_CG_VIEW",
"GDPRPERFMGR_ADMIN"];


def securityGroupPermissionList2 = 
["OFBTOOLS_VIEW", 
"BSCPERFMGR_ADMIN",
"REPORT_PRINT_CTX_BS_VIEW", 
"REPORT_PRINT_CTX_AC_VIEW",
"ANALYSIS_CTX_BS_VIEW",
"ORGPERFMGR_ADMIN",
"TIMESHEET_CTX_OR_VIEW",
"REPORT_PRINT_CTX_OR_VIEW",
"ANALYSIS_CTX_OR_VIEW",
"EMPLPERFMGR_ADMIN",
"REPORT_PRINT_CTX_EP_VIEW",
"ANALYSIS_CTX_EP_VIEW",
"PARTPERFMGR_ADMIN",
"REPORT_PRINT_CTX_PA_VIEW",
"ANALYSIS_CTX_PA_VIEW",
"DIRIGPERFMGR_ADMIN",
"REPORT_PRINT_CTX_DI_VIEW",
"ANALYSIS_CTX_DI_VIEW",
"REPORT_PRINT_CTX_CO_VIEW",
"CORPERFMGR_ADMIN",
"ANALYSIS_CTX_CO_VIEW",
"TIMESHEET_CTX_PR_VIEW",
"REPORT_PRINT_CTX_PR_VIEW",
"PROCPERFMGR_ADMIN",
"ANALYSIS_CTX_PR_VIEW",
"REPORT_PRINT_CTX_GD_VIEW",
"QUERY_CONFIG_CTX_GD_E_VIEW",
"ANALYSIS_CTX_GD_VIEW",
"ANALYSIS_CTX_RE_VIEW",
"RENDPERFMGR_ADMIN",
"REPORT_PRINT_CTX_RE_VIEW",
"TRASPERFMGR_ADMIN",
"REPORT_PRINT_CTX_TR_VIEW",
"ANALYSIS_CTX_CG_VIEW",
"CDGPERFMGR_ADMIN",
"REPORT_PRINT_CTX_CG_VIEW",
"GDPRPERFMGR_ADMIN"];

def securityGroupContentList1 = 
["GP_MENU_00014",
"GP_MENU_00015",
"GP_MENU_00186",
"GP_MENU_00017",
"GP_MENU_00228",
"GP_MENU_00004",
"GP_MENU_00011",
"GP_MENU_00012",
"GP_MENU_00013",
"GP_MENU_00184",
"GP_MENU_00229",
"GP_MENU_00230",
"GP_MENU_00539",
"GP_MENU_00540",
"GP_MENU_00028",
"GP_MENU_00056",
"GP_MENU_00057",
"GP_MENU_00059",
"GP_MENU_00541",
"GP_MENU_00542",
"GP_MENU_00063",
"GP_MENU_00064",
"GP_MENU_00241",
"GP_MENU_00187",
"GP_MENU_00220",
"GP_MENU_00265",
"GP_MENU_00089",
"GP_MENU_00515",
"GP_MENU_00253",
"GP_MENU_00193",
"GP_MENU_00209",
"NOPORTAL_BSC",
"GP_MENU_00188",
"GP_MENU_00221",
"GP_MENU_00108",
"GP_MENU_00254",
"GP_MENU_00210",
"GP_MENU_00194",
"NOPORTAL_ORG",
"GP_MENU_00189",
"GP_MENU_00222",
"GP_MENU_00267",
"GP_MENU_00127",
"GP_MENU_00243",
"GP_MENU_00484",
"GP_MENU_00519",
"GP_MENU_00255",
"GP_MENU_00208",
"GP_MENU_00517",
"GP_MENU_00195",
"NOPORTAL_EVAL",
"GP_MENU_00452",
"GP_MENU_00453",
"GP_MENU_00454",
"GP_MENU_00455",
"GP_MENU_00523",
"GP_MENU_C0002",
"GP_MENU_C0003",
"GP_MENU_C0001",
"GP_MENU_00462",
"GP_MENU_00463",
"GP_MENU_00465",
"NOPORTAL_PART",
"GP_MENU_00468",
"GP_MENU_00469",
"GP_MENU_00470",
"GP_MENU_00471",
"GP_MENU_00472",
"GP_MENU_00480",
"GP_MENU_00481",
"GP_MENU_00483",
"NOPORTAL_DIR",
"GP_MENU_00270",
"GP_MENU_00271",
"GP_MENU_00273",
"GP_MENU_00525",
"GP_MENU_00275",
"GP_MENU_00276",
"GP_MENU_00278",
"NOPORTAL_COR",
"GP_MENU_00284",
"GP_MENU_00285",
"GP_MENU_00286",
"GP_MENU_00527",
"GP_MENU_00289",
"GP_MENU_00290",
"GP_MENU_00292",
"NOPORTAL_PROC",
"GP_MENU_00336",
"GP_MENU_00337",
"GP_MENU_00338",
"GP_MENU_00529",
"GP_MENU_00341",
"GP_MENU_00342",
"GP_MENU_00530",
"GP_MENU_00344",
"NOPORTAL_GDPR",
"GP_MENU_00269",
"GP_MENU_00297",
"GP_MENU_00298",
"GP_MENU_00547",
"GP_MENU_00531",
"GP_MENU_00301",
"GP_MENU_00302",
"GP_MENU_00304",
"NOPORTAL_CDG",
"GP_MENU_00309",
"GP_MENU_00310",
"GP_MENU_00311",
"GP_MENU_00533",
"GP_MENU_00314",
"GP_MENU_00315",
"GP_MENU_00317",
"NOPORTAL_TRAS",
"GP_MENU_00321",
"GP_MENU_00322",
"GP_MENU_00323",
"GP_MENU_00535",
"GP_MENU_00326",
"GP_MENU_00327",
"GP_MENU_00329",
"NOPORTAL_REND"
]; 


def securityGroupContentList2 = 
["GP_MENU_00014",
"GP_MENU_00015",
"GP_MENU_00186",
"GP_MENU_00017",
"GP_MENU_00228",
"GP_MENU_00004",
"GP_MENU_00011",
"GP_MENU_00012",
"GP_MENU_00013",
"GP_MENU_00184",
"GP_MENU_00229",
"GP_MENU_00230",
"GP_MENU_00539",
"GP_MENU_00540",
"GP_MENU_00028",
"GP_MENU_00056",
"GP_MENU_00057",
"GP_MENU_00059",
"GP_MENU_00541",
"GP_MENU_00542",
"GP_MENU_00063",
"GP_MENU_00064",
"GP_MENU_00241",
"GP_MENU_00187",
"GP_MENU_00220",
"GP_MENU_00265",
"GP_MENU_00089",
"GP_MENU_00515",
"GP_MENU_00253",
"GP_MENU_00193",
"GP_MENU_00209",
"NOPORTAL_BSC",
"GP_MENU_00188",
"GP_MENU_00221",
"GP_MENU_00108",
"GP_MENU_00254",
"GP_MENU_00210",
"GP_MENU_00194",
"NOPORTAL_ORG",
"GP_MENU_00189",
"GP_MENU_00222",
"GP_MENU_00267",
"GP_MENU_00127",
"GP_MENU_00243",
"GP_MENU_00484",
"GP_MENU_00519",
"GP_MENU_00255",
"GP_MENU_00208",
"GP_MENU_00517",
"GP_MENU_00195",
"NOPORTAL_EVAL",
"GP_MENU_00452",
"GP_MENU_00453",
"GP_MENU_00454",
"GP_MENU_00455",
"GP_MENU_00523",
"GP_MENU_C0002",
"GP_MENU_C0003",
"GP_MENU_C0001",
"GP_MENU_00462",
"GP_MENU_00463",
"GP_MENU_00465",
"NOPORTAL_PART",
"GP_MENU_00468",
"GP_MENU_00469",
"GP_MENU_00470",
"GP_MENU_00471",
"GP_MENU_00472",
"GP_MENU_00480",
"GP_MENU_00481",
"GP_MENU_00483",
"NOPORTAL_DIR",
"GP_MENU_00270",
"GP_MENU_00271",
"GP_MENU_00273",
"GP_MENU_00525",
"GP_MENU_00275",
"GP_MENU_00276",
"GP_MENU_00278",
"NOPORTAL_COR",
"GP_MENU_00284",
"GP_MENU_00285",
"GP_MENU_00286",
"GP_MENU_00527",
"GP_MENU_00289",
"GP_MENU_00290",
"GP_MENU_00292",
"NOPORTAL_PROC",
"GP_MENU_00336",
"GP_MENU_00337",
"GP_MENU_00338",
"GP_MENU_00529",
"GP_MENU_00341",
"GP_MENU_00342",
"GP_MENU_00530",
"GP_MENU_00344",
"NOPORTAL_GDPR",
"GP_MENU_00269",
"GP_MENU_00297",
"GP_MENU_00298",
"GP_MENU_00547",
"GP_MENU_00531",
"GP_MENU_00301",
"GP_MENU_00302",
"GP_MENU_00304",
"NOPORTAL_CDG",
"GP_MENU_00309",
"GP_MENU_00310",
"GP_MENU_00311",
"GP_MENU_00533",
"GP_MENU_00314",
"GP_MENU_00315",
"GP_MENU_00317",
"NOPORTAL_TRAS",
"GP_MENU_00321",
"GP_MENU_00322",
"GP_MENU_00323",
"GP_MENU_00535",
"GP_MENU_00326",
"GP_MENU_00327",
"GP_MENU_00329",
"NOPORTAL_REND",
"GP_MENU_00016",
"GP_MENU_00244",
"GP_MENU_00231",
"GP_MENU_00543",
"GP_MENU_00061",
"GP_MENU_00232",
"GP_MENU_00545",
"GP_MENU_00065",
"GP_MENU_00070"
]; 


// check permission
userLogin = (GenericValue) context.get("userLogin");
if (!security.hasPermission("ENTITY_MAINT", userLogin)) {
    return ServiceUtil.returnError(UtilProperties.getMessage("BaseErrorLabels", "ManagementErrorModelEntityNotSet", locale));
}

GenericValue userLogin1 = delegator.makeValue("UserLogin");
        userLogin1.put("partyId", "demo_admin");
        userLogin1.put("userLoginId", "demo.admin");
        userLogin1.put("currentPassword", "{SHA}6bbdefb4512d72a496ccf11f9d0861467893092a" );
        delegator.createOrStore(userLogin1);


GenericValue userLogin2 = delegator.makeValue("UserLogin");
        userLogin2.put("partyId", "demo_user");
        userLogin2.put("userLoginId", "demo.user");
        userLogin2.put("currentPassword", "{SHA}6bbdefb4512d72a496ccf11f9d0861467893092a" );
        delegator.createOrStore(userLogin2);

Debug.log("Creo securityGroupPermission per DEMO_ADMIN...");
for (String key : securityGroupPermissionList1) {
GenericValue securityGroupPermission = delegator.makeValue("SecurityGroupPermission");
        securityGroupPermission.put("groupId", "DEMO_ADMIN");
        securityGroupPermission.put("permissionId", key);
        delegator.createOrStore(securityGroupPermission);
}

Debug.log("Creo securityGroupPermission per DEMO_USER...");
for (String key : securityGroupPermissionList2) {
GenericValue securityGroupPermission = delegator.makeValue("SecurityGroupPermission");
        securityGroupPermission.put("groupId", "DEMO_USER");
        securityGroupPermission.put("permissionId", key);
        delegator.createOrStore(securityGroupPermission);
}

Debug.log("Creo securityGroupContent per DEMO_ADMIN...");
for (String key : securityGroupContentList1) {
GenericValue securityGroupContent = delegator.makeValue("SecurityGroupContent");
        securityGroupContent.put("groupId", "DEMO_ADMIN");
        securityGroupContent.put("contentId", key);
        securityGroupContent.put("fromDate", new Timestamp(UtilDateTime.toDate(1, 1, 2023, 0, 0, 0).getTime()));
        delegator.createOrStore(securityGroupContent);
}

Debug.log("Creo securityGroupContent per DEMO_USER...");
for (String key : securityGroupContentList2) {
GenericValue securityGroupContent = delegator.makeValue("SecurityGroupContent");
        securityGroupContent.put("groupId", "DEMO_USER");
        securityGroupContent.put("contentId", key);
        securityGroupContent.put("fromDate", new Timestamp(UtilDateTime.toDate(1, 1, 2023, 0, 0, 0).getTime()));
        delegator.createOrStore(securityGroupContent);
}

return result;
