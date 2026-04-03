import org.ofbiz.base.util.*;
import org.ofbiz.entity.util.EntityUtil;
import org.ofbiz.entity.condition.*;
import org.ofbiz.service.*;
import javolution.util.FastList;

def result = ServiceUtil.returnSuccess();

def delegator = dctx.getDelegator();
def security = dctx.getSecurity();

def contentIdList = 
["NOPORTAL_REND",
"NOPORTAL_GDPR",
"GP_MENU_00592",
"GP_MENU_00591",
"GP_MENU_00590",
"GP_MENU_00589",
"GP_MENU_00588",
"GP_MENU_00587",
"GP_MENU_00586",
"GP_MENU_00585",
"GP_MENU_00584",
"GP_MENU_00583",
"GP_MENU_00582",
"GP_MENU_00581",
"GP_MENU_00580",
"GP_MENU_00579",
"GP_MENU_00578",
"GP_MENU_00577",
"GP_MENU_00576",
"GP_MENU_00575",
"GP_MENU_00574",
"GP_MENU_00573",
"GP_MENU_00572",
"GP_MENU_00571",
"GP_MENU_00570",
"GP_MENU_00569",
"GP_MENU_00568",
"GP_MENU_00567",
"GP_MENU_00566",
"GP_MENU_00561",
"GP_MENU_00560",
"GP_MENU_00557",
"GP_MENU_00546",
"GP_MENU_00545",
"GP_MENU_00544",
"GP_MENU_00543",
"GP_MENU_00542",
"GP_MENU_00541",
"GP_MENU_00540",
"GP_MENU_00539",
"GP_MENU_00530",
"GP_MENU_00529",
"GP_MENU_00514",
"GP_MENU_00497",
"GP_MENU_00491",
"GP_MENU_00486",
"GP_MENU_00485",
"GP_MENU_00448",
"GP_MENU_00440",
"GP_MENU_00439",
"GP_MENU_00438",
"GP_MENU_00437",
"GP_MENU_00436",
"GP_MENU_00435",
"GP_MENU_00434",
"GP_MENU_00433",
"GP_MENU_00432",
"GP_MENU_00431",
"GP_MENU_00324",
"GP_MENU_00564",
"GP_MENU_00565",
"GP_MENU_00511",
"GP_MENU_00535",
"GP_MENU_00425",
"GP_MENU_00321",
"GP_MENU_00322",
"GP_MENU_00323",
"GP_MENU_00424",
"GP_MENU_00347",
"GP_MENU_00344",
"GP_MENU_00342",
"GP_MENU_00343",
"GP_MENU_00341",
"GP_MENU_00340",
"GP_MENU_00339",
"GP_MENU_00338",
"GP_MENU_00336",
"GP_MENU_00337",
"GP_MENU_00417",
"GP_MENU_00416",
"GP_MENU_00415",
"GP_MENU_00335",
"GP_MENU_00334",
"GP_MENU_00332",
"GP_MENU_00501",
"GP_MENU_00560",
"GP_MENU_00329",
"GP_MENU_00506",
"GP_MENU_00536",
"GP_MENU_00328",
"GP_MENU_00327",
"GP_MENU_00326",
"GP_MENU_00325",
"GP_MENU_00309",
"GP_MENU_00310",
"GP_MENU_00311",
"GP_MENU_00421",
"GP_MENU_00427",
"GP_MENU_00312",
"GP_MENU_00428",
"GP_MENU_00498",
"GP_MENU_00533",
"GP_MENU_00422",
"GP_MENU_00313",
"GP_MENU_00314",
"GP_MENU_00315",
"GP_MENU_00449",
"GP_MENU_00316",
"GP_MENU_00534",
"GP_MENU_00492",
"GP_MENU_00317",
"GP_MENU_00559",
"NOPORTAL_TRAS",
"GP_MENU_00423",
"GP_MENU_00308",
"GP_MENU_00426",
"GP_MENU_00320",
"GP_MENU_00296",
"GP_MENU_00297",
"GP_MENU_00298",
"GP_MENU_00418",
"GP_MENU_00547",
"GP_MENU_00548",
"GP_MENU_00299",
"GP_MENU_00512",
"GP_MENU_00531",
"GP_MENU_00419",
"GP_MENU_00300",
"GP_MENU_00301",
"GP_MENU_00302",
"GP_MENU_00502",
"GP_MENU_00303",
"GP_MENU_00532",
"GP_MENU_00507",
"GP_MENU_00558",
"NOPORTAL_CDG",
"GP_MENU_00304",
"GP_MENU_00420",
"GP_MENU_00295",
"GP_MENU_00284",
"GP_MENU_00285",
"GP_MENU_00286",
"GP_MENU_00412",
"GP_MENU_00334",
"GP_MENU_00441",
"GP_MENU_00287",
"GP_MENU_00442",
"GP_MENU_00513",
"GP_MENU_00527",
"GP_MENU_00563",
"GP_MENU_00413",
"GP_MENU_00288",
"GP_MENU_00289",
"GP_MENU_00290",
"GP_MENU_00503",
"GP_MENU_00291",
"GP_MENU_00556",
"NOPORTAL_PROC",
"GP_MENU_00528",
"GP_MENU_00508",
"GP_MENU_00292",
"GP_MENU_00414",
"GP_MENU_00283",
"GP_MENU_00270",
"GP_MENU_00271",
"GP_MENU_00272",
"GP_MENU_00409",
"GP_MENU_00429",
"GP_MENU_00430",
"GP_MENU_00273",
"GP_MENU_00443",
"GP_MENU_00493",
"GP_MENU_00525",
"GP_MENU_00410",
"GP_MENU_00274",
"GP_MENU_00275",
"GP_MENU_00276",
"GP_MENU_00444",
"GP_MENU_00277",
"GP_MENU_00526",
"GP_MENU_00487",
"GP_MENU_00278",
"GP_MENU_00555",
"NOPORTAL_COR",
"GP_MENU_00411",
"GP_MENU_00269",
"GP_MENU_00268",
"GP_MENU_00264",
"GP_MENU_00245",
"GP_MENU_00244",
"GP_MENU_00242",
"GP_MENU_00241",
"GP_MENU_00233",
"GP_MENU_00232",
"GP_MENU_00231",
"GP_MENU_00230",
"GP_MENU_00229",
"GP_MENU_00228",
"GP_MENU_00223",
"GP_MENU_00219",
"GP_MENU_00218",
"GP_MENU_00198",
"GP_MENU_00197",
"GP_MENU_00186",
"GP_MENU_00184",
"GP_MENU_00182",
"GP_MENU_00178",
"GP_MENU_00179",
"GP_MENU_00180",
"GP_MENU_00181",
"GP_MENU_00177",
"GP_MENU_00167",
"GP_MENU_00191",
"GP_MENU_00171",
"GP_MENU_00172",
"GP_MENU_00173",
"GP_MENU_00170",
"GP_MENU_00174",
"GP_MENU_00175",
"GP_MENU_00176",
"GP_MENU_00166",
"GP_MENU_00162",
"GP_MENU_00163",
"GP_MENU_00164",
"GP_MENU_00165",
"GP_MENU_00161",
"GP_MENU_00160",
"GP_MENU_00196",
"GP_MENU_00159",
"GP_MENU_00157",
"GP_MENU_00158",
"GP_MENU_00156",
"GP_MENU_00154",
"GP_MENU_00155",
"GP_MENU_00153",
"GP_MENU_00190",
"GP_MENU_00151",
"GP_MENU_00152",
"GP_MENU_00150",
"GP_MENU_00149",
"GP_MENU_00145",
"GP_MENU_00146",
"GP_MENU_00147",
"GP_MENU_00148",
"GP_MENU_00144",
"GP_MENU_00143",
"GP_MENU_00100",
"GP_MENU_00081",
"GP_MENU_00081",
"GP_MENU_00005",
"GP_MENU_00082",
"GP_MENU_00083",
"GP_MENU_00233",
"GP_MENU_00594",
"GP_MENU_00593",
"GP_MENU_00080",
"GP_MENU_00079",
"GP_MENU_00197",
"GP_MENU_00079",
"GP_MENU_00005",
"GP_MENU_00595",
"GP_MENU_00078",
"GP_MENU_00077",
"GP_MENU_00076",
"GP_MENU_00072",
"GP_MENU_00073",
"GP_MENU_00074",
"GP_MENU_00071",
"GP_MENU_00067",
"GP_MENU_00068",
"GP_MENU_00069",
"GP_MENU_00070",
"GP_MENU_00066",
"GP_MENU_00065",
"GP_MENU_00064",
"GP_MENU_00063",
"GP_MENU_00062",
"GP_MENU_00061",
"GP_MENU_00059",
"GP_MENU_00057",
"GP_MENU_00056",
"GP_MENU_00058",
"GP_MENU_00060",
"GP_MENU_00028",
"GP_MENU_00055",
"GP_MENU_00050",
"GP_MENU_00051",
"GP_MENU_00052",
"GP_MENU_00053",
"GP_MENU_00054",
"GP_MENU_00203",
"GP_MENU_00049",
"GP_MENU_00042",
"GP_MENU_00041",
"GP_MENU_00039",
"GP_MENU_00038",
"GP_MENU_00037",
"GP_MENU_00032",
"GP_MENU_00031",
"GP_MENU_00030",
"GP_MENU_00029",
"GP_MENU_00017",
"GP_MENU_00016",
"GP_MENU_00015",
"GP_MENU_00014",
"GP_MENU_00013",
"GP_MENU_00012",
"GP_MENU_00011",
"GP_MENU_00238",
"GP_MENU_00261",
"GP_MENU_00239",
"GP_MENU_00262",
"GP_MENU_00199",
"GP_MENU_00206",
"GP_MENU_00240",
"GP_MENU_00263",
"GP_MENU_00010",
"GP_MENU_00009",
"GP_MENU_00008",
"GP_MENU_00007",
"GP_MENU_00006",
"GP_MENU_00005",
"GP_MENU_00004",
"GP_MENU_00348",
"GP_MENU_00003",
"GP_MENU_00215",
"GP_MENU_00040",
"GP_MENU_00045",
"GP_MENU_00207",
"GP_MENU_00249",
"GP_MENU_00192",
"GP_MENU_00211",
"GP_MENU_00234",
"GP_MENU_N0003",
"GP_MENU_00549",
"GP_MENU_00217",
"GP_MENU_N0001",
"GP_MENU_N0002",
"GP_MENU_00036",
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
"GP_MENU_00019",
"GP_MENU_00307"

];



// check permission
userLogin = (GenericValue) context.get("userLogin");
if (!security.hasPermission("ENTITY_MAINT", userLogin)) {
    return ServiceUtil.returnError(UtilProperties.getMessage("BaseErrorLabels", "ManagementErrorModelEntityNotSet", locale));
}

// delete from SecurityGroupContent GP_MENU_00569
List<EntityCondition> conditionsSecurityGroupContent = FastList.newInstance();
conditionsSecurityGroupContent.add(EntityCondition.makeCondition("contentId","GP_MENU_00569"));
securityGroupList = delegator.findList("SecurityGroupContent", EntityCondition.makeCondition(conditionsSecurityGroupContent), null, null, null, false);

if(UtilValidate.isNotEmpty(securityGroupList)) {
    Debug.log("Elimino securityGroupList...");
    delegator.removeAll(securityGroupList);
}

// delete from ContentAttribute
List<EntityCondition> conditionContentAttribute2 = FastList.newInstance();
conditionContentAttribute2.add(EntityCondition.makeCondition("contentId", "GP_MENU_00569"));
contentAttributeList2 = delegator.findList("ContentAttribute", EntityCondition.makeCondition(conditionContentAttribute2), null, null, null, false);



if(UtilValidate.isNotEmpty(contentAttributeList2)) {
    Debug.log("Elimino contentAttributeList...");
    delegator.removeAll(contentAttributeList2);
}

// delete from ContentAssoc
List<EntityCondition> conditionsContentAssoc2 = FastList.newInstance();
conditionsContentAssoc2.add(EntityCondition.makeCondition("contentIdTo", "GP_MENU_00569"));
contentAssocList2 = delegator.findList("ContentAssoc", EntityCondition.makeCondition(conditionsContentAssoc2), null, null, null, false);



if(UtilValidate.isNotEmpty(contentAssocList2)) {
    Debug.log("Elimino contentAssocList...");
    delegator.removeAll(contentAssocList2);
}


//delete from Content GP_MENU_00569
List<EntityCondition> conditionsContent = FastList.newInstance();
conditionsContent.add(EntityCondition.makeCondition("contentId", "GP_MENU_00569"));
contentList = delegator.findList("Content", EntityCondition.makeCondition(conditionsContent), null, null, null, false);


if(UtilValidate.isNotEmpty(contentList)) {
    Debug.log("Elimino contentList...");
    delegator.removeAll(contentList);
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

