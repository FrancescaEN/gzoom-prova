import org.ofbiz.base.util.*;
import org.ofbiz.entity.util.*;
import org.ofbiz.entity.condition.*;
import org.ofbiz.service.*;
import com.mapsengineering.base.datamigration.util.DatabaseUtil;
import javolution.util.FastList;

def result = ServiceUtil.returnSuccess();
def delegator = dctx.getDelegator();
def security = dctx.getSecurity();

class Record {
    String groupId
    String permissionId


    boolean equals(Object o) {
        if (this.is(o)) return true
        if (!(o instanceof Record)) return false
        Record record = (Record) o
        return groupId == record.groupId && permissionId == record.permissionId
    }

    int hashCode() {
        return Objects.hash(groupId, permissionId)
    }
}

def aggiungiSeNonPresente(lista, record) {
    if (!lista.contains(record)) {
        lista.add(record)
        def newRecord = delegator.makeValue("SecurityGroupPermission", ["groupId" : record.groupId, "permissionId"  : record.permissionId])
        delegator.create(newRecord);
        println "Record aggiunto: ${record.groupId}, ${record.permissionId}"
    } else {
        println "Record già presente: ${record.groupId}, ${record.permissionId}"
    }
}


def listRecordRecordInseriti = [];

def map_permission = [
    1: [link: "accountingext", perm: "ACCOUNTINGEXT"],
    2: [link: "cdgperf", perm: "CDGPERFMGR"],
    3: [link: "commondataext", perm: "COMMONEXT"],
    4: [link: "corperf", perm: "CORPERFMGR"],
    5: [link: "custom", perm: "CUSTOM"],
    6: [link: "dirigperf", perm: "DIRIGPERFMGR"],
    7: [link: "emplperf", perm: "EMPLPERFMGR"],
    8: [link: "gdprperf", perm: "GDPRPERFMGR"],
    9: [link: "gzope", perm: "GZOPE"],
    10: [link: "humanresext", perm: "HUMANRESEXT"],
    11: [link: "jbpm", perm: "JBPM"],
    12: [link: "managacc", perm: "MANAGACCMGR"],
    13: [link: "orgperf", perm: "ORGPERFMGR"],
    14: [link: "partperf", perm: "PARTPERFMGR"],
    15: [link: "partyext", perm: "PARTYMGR"],
    16: [link: "procperf", perm: "PROCPERFMGR"],
    17: [link: "productext", perm: "PRODUCTEXT"],
    18: [link: "projectmgrext", perm: "PROJECTMGR"],
    19: [link: "rendperf", perm: "RENDPERFMGR"],
    20: [link: "stratperf", perm: "BSCPERFMGR"],
    21: [link: "trasperf", perm: "TRASPERFMGR"],
    22: [link: "workeffortext", perm: "WORKEFFORTMGR"],
    23: [link: "productext", perm: "PRODUCTEXT"],
]


def map_menu = [
    1: [gzoom2: "GP_MENU_00570", legacy: "GP_MENU_00004"],
    2: [gzoom2: "GP_MENU_00347", legacy: "GP_MENU_00006"],
    3: [gzoom2: "GP_MENU_00332", legacy: "GP_MENU_00006"],
    4: [gzoom2: "GP_MENU_00582", legacy: "GP_MENU_00007"],
    5: [gzoom2: "GP_MENU_00566", legacy: "GP_MENU_00008"],
    6: [gzoom2: "GP_MENU_00572", legacy: "GP_MENU_00009"],
    7: [gzoom2: "GP_MENU_00586", legacy: "GP_MENU_00228"],
    8: [gzoom2: "GP_MENU_00571", legacy: "GP_MENU_00230"],
    9: [gzoom2: "GP_MENU_00574", legacy: "GP_MENU_00198"],
    10: [gzoom2: "GP_MENU_00584", legacy: "GP_MENU_00011"],
    11: [gzoom2: "GP_MENU_00604", legacy: "GP_MENU_00012"],
    12: [gzoom2: "GP_MENU_00624", legacy: "GP_MENU_00012"],
    13: [gzoom2: "GP_MENU_00585", legacy: "GP_MENU_00184"],
    14: [gzoom2: "GP_MENU_00608", legacy: "GP_MENU_00014"],
    15: [gzoom2: "GP_MENU_00623", legacy: "GP_MENU_00014"],
    16: [gzoom2: "GP_MENU_00583", legacy: "GP_MENU_00186"],
    17: [gzoom2: "GP_MENU_00622", legacy: "GP_MENU_00016"],
    18: [gzoom2: "GP_MENU_00609", legacy: "GP_MENU_00016"],
    19: [gzoom2: "GP_MENU_00610", legacy: "GP_MENU_00244"],
    20: [gzoom2: "GP_MENU_00625", legacy: "GP_MENU_00244"],
    21: [gzoom2: "GP_MENU_00543", legacy: "GP_MENU_00539"],
    22: [gzoom2: "GP_MENU_00567", legacy: "GP_MENU_00030"],
    23: [gzoom2: "GP_MENU_00576", legacy: "GP_MENU_00032"],
    24: [gzoom2: "GP_MENU_00602", legacy: "GP_MENU_00037"],
    25: [gzoom2: "GP_MENU_00568", legacy: "GP_MENU_00215"],
    26: [gzoom2: "GP_MENU_00577", legacy: "GP_MENU_00038"],
    27: [gzoom2: "GP_MENU_00580", legacy: "GP_MENU_00039"],
    28: [gzoom2: "GP_MENU_00578", legacy: "GP_MENU_00218"],
    29: [gzoom2: "GP_MENU_00589", legacy: "GP_MENU_00485"],
    30: [gzoom2: "GP_MENU_00590", legacy: "GP_MENU_00549"],
    31: [gzoom2: "GP_MENU_00486", legacy: "GP_MENU_00514"],
    32: [gzoom2: "GP_MENU_00603", legacy: "GP_MENU_00217"],
    33: [gzoom2: "GP_MENU_00573", legacy: "GP_MENU_00028"],
    34: [gzoom2: "GP_MENU_00575", legacy: "GP_MENU_00056"],
    35: [gzoom2: "GP_MENU_00587", legacy: "GP_MENU_00059"],
    36: [gzoom2: "GP_MENU_00546", legacy: "GP_MENU_00542"],
    37: [gzoom2: "GP_MENU_00546", legacy: "GP_MENU_00541"],
    38: [gzoom2: "GP_MENU_00592", legacy: "GP_MENU_00079"],
    39: [gzoom2: "GP_MENU_00601", legacy: "GP_MENU_00197"],
    40: [gzoom2: "GP_MENU_00595", legacy: "GP_MENU_00005"],
    41: [gzoom2: "GP_MENU_00594", legacy: "GP_MENU_00233"],
    42: [gzoom2: "GP_MENU_00544", legacy: "GP_MENU_00540"]
]



        

// check permission
userLogin = (GenericValue) context.get("userLogin");
if (!security.hasPermission("ENTITY_MAINT", userLogin)) {
    return ServiceUtil.returnError(UtilProperties.getMessage("BaseErrorLabels", "ManagementErrorModelEntityNotSet", locale));
}



// lista delle condizioni che serve per fare la query sulla tabella ContentAttribute per ricavare il permesso della voce di menu legacy
List<EntityCondition> conditionsCA = FastList.newInstance();

// lista delle condizioni che serve per fare la query sulla tabella SecurityGroupContent per ricavare i gruppi dove è esclusa la voce di menu legacy
List<EntityCondition> conditionsSGC = FastList.newInstance();  




// foreach su tutte le voci di menu della mappa
map_menu.each{ k, v ->

    Debug.logInfo("- dataMigGN6470 :: Inizio elaborazione per menu con contentId: " + v.legacy + "\n", "dataMigGN6470");
 

    // prima query su ContentAttribute dove vado a ricavare la prima parola tra due / / dentro al campo attrName
    conditionsCA.add(EntityCondition.makeCondition("contentId", v.legacy));
    conditionsCA.add(EntityCondition.makeCondition("attrName", "link"));
    List<GenericValue> contentAttributeList = delegator.findList("ContentAttribute", EntityCondition.makeCondition(conditionsCA), null, null, null, false);

    // seconda query su SecurityGroupContent dove vado a ricavare i gruppi che hanno la voce di menu esclusa
    conditionsSGC.add(EntityCondition.makeCondition("contentId", v.legacy));
    List<GenericValue> securityGroupContentList = delegator.findList("SecurityGroupContent", EntityCondition.makeCondition(conditionsSGC), null, null, null, false);

    // variabile link che indica il nome della prima parole del lnk che serve per ottere il permesso che troveremo nella variabile value_perm
    def link;
    contentAttributeList.each{ content ->
        String attrValue = content.getString("attrValue") 
        if (attrValue != null) {
            link = attrValue.split("/")[1]
        } else {
            println "attrValue is null"
        }
    }

   

    // variabile value_perm che indica il nome del permesso della voce legacy
    def value_perm;

    // lista a cui andremo a inserire i gruppi associati al permesso della voce legacy
    List<String> groupIdList = FastList.newInstance();

    map_permission.each{ key, value-> 

        Debug.logInfo("- dataMigGN6470 :: Ciclo su MAPPA PERMESSI con link : " + value.link.toString() + " e valore : " + value.perm.toString() + "\n", "dataMigGN6470");
        if(link.equals(value.link)){
            value_perm = value.perm;

            // lista delle condizioni che serve per fare la query sulla tabella SecurityGroupPermission per ricavare i gruppi ai quali è associato il permesso della voce di menu legacy
            List<EntityCondition> conditionsLike = FastList.newInstance();
            conditionsLike.add(EntityCondition.makeCondition("permissionId", EntityOperator.LIKE, "%" + value.perm + "%" ));
            List<GenericValue> securityGroupPermissionList = delegator.findList("SecurityGroupPermission", EntityCondition.makeCondition(conditionsLike), null, null, null, false);

            Debug.logInfo("- dataMigGN6470 :: Cerco i gruppi su SecurityGroupPermission al quale è associato il permesso : " + value.perm + " e ottengo :" + "\n", "dataMigGN6470");
            
            securityGroupPermissionList.each{ record -> 
                Debug.logInfo("- dataMigGN6470 :: Gruppo : " + record.groupId + "\n", "dataMigGN6470");
                if (!groupIdList.contains(record.groupId)) {
                    groupIdList.push(record.groupId)
                }
            }
            conditionsLike.clear();
        }
    }

    // lista delle condizioni che serve per fare la query sulla tabella SecurityPermission per ricavare il permesso della voce di menu gzoom2
    List<EntityCondition> conditionsLike = FastList.newInstance();
    conditionsLike.add(EntityCondition.makeCondition("permissionId", EntityOperator.LIKE, "%ADMIN%" ));
    conditionsLike.add(EntityCondition.makeCondition("description", EntityOperator.LIKE, "%" + v.gzoom2 + "%" ));
    List<GenericValue> securityPermissionList = delegator.findList("SecurityPermission", EntityCondition.makeCondition(conditionsLike), null, null, null, false);
    conditionsLike.clear();

    Debug.logInfo("- dataMigGN6470 :: Cerco su SecurityPermission permesso voce gzoom2 : " + v.gzoom2 + "\n", "dataMigGN6470");
    
    // ciclo su tutti i gruppi a cui è associato il permesso della voce di menu legacy escludendo quelli che in cui è esclusa la voce di menù, 
    // con l'obiettivo di inserire il permesso della corrispondente voce di menu gzoom2 dentro agli stessi gruppi

    Debug.logInfo("- dataMigGN6470 :: Ciclo su Lista dei gruppi a cui è associato il permesso della voce legacy   : " + v.legacy + "\n", "dataMigGN6470");

    groupIdList.each{ group -> 

        Debug.logInfo("- dataMigGN6470 :: Gruppo  : " + group + "\n", "dataMigGN6470");

        // verifico che il gruppo non sia tra quelli che hanno la voce di menu esclusa 
        Boolean control = true;
        securityGroupContentList.each{ e -> 
            if(group.equals(e.groupId)){
                control = false;
                Debug.logInfo("- dataMigGN6470 :: Il gruppo " + group + " ha la voce legacy esclusa : " + v.legacy + "\n", "dataMigGN6470");
            }
        }

        // nel caso non ci sia tra i gruppi in cui la voce di menu è esclusa posso procedere a completare l'obiettivo
        if(control){

            Debug.logInfo("- dataMigGN6470 :: Il gruppo " + group + " non ha la voce legacy esclusa : " + v.legacy + "\n", "dataMigGN6470");
        
            // la lista ha sempre un solo elemento in teoria
            securityPermissionList.each{ value -> 

                // lista delle condizioni che serve per fare la query sulla tabella SecurityGroupPermission per ricavare i gruppi a cui è associato il permesso della voce di menu gzoom2
                List<EntityCondition> conditionsSGP = FastList.newInstance(); 
                conditionsSGP.add(EntityCondition.makeCondition("permissionId", value.permissionId));
                List<GenericValue> securityGroupPermissionList = delegator.findList("SecurityGroupPermission", EntityCondition.makeCondition(conditionsSGP), null, null, null, false);
                
                Debug.logInfo("- dataMigGN6470 :: Cerco su SecurityGroupPermission i gruppi a cui è associato il permesso della voce gzoom2 :" + v.gzoom2 + " \n", "dataMigGN6470");

                // se il permesso non è associato a quel gruppo lo associa, altrimenti no.
                Boolean notExist = true;
                securityGroupPermissionList.each{ e -> 
                    if(group.equals(e.groupId)){
                        Debug.logInfo("- dataMigGN6470 :: Gruppo  : " + e.groupId + " già associato a permesso \n", "dataMigGN6470");
                        notExist = false
                    }
                }
                
                // creo e inserisco record nella tabella
                if(notExist){
                    Debug.logInfo("- dataMigGN6470 :: Gruppo  : " + group + " non associato a permesso" + "\n", "dataMigGN6470");
                    Debug.logInfo("- dataMigGN6470 :: Inserisco record in SecurityGroupId: " + value.permissionId + "\n", "dataMigGN6470");
                    def record = new Record(groupId: group, permissionId: value.permissionId)
                    aggiungiSeNonPresente(listRecordRecordInseriti, record)
                }
                conditionsSGP.clear();
            }
        }
    }

    conditionsSGC.clear();
    conditionsCA.clear();

    Debug.logInfo("Fine elaborazione per menu con contentId: " + v.legacy, "dataMigGN6470");
}


map_menu.each{ k, v ->


    // delete from SecurityGroupContent
    List<EntityCondition> conditionsSecurityGroupContent = FastList.newInstance();
    conditionsSecurityGroupContent.add(EntityCondition.makeCondition("contentId",v.legacy));
    securityGroupList = delegator.findList("SecurityGroupContent", EntityCondition.makeCondition(conditionsSecurityGroupContent), null, null, null, false);

    if(UtilValidate.isNotEmpty(securityGroupList)) {
        Debug.log("Elimino securityGroupList...");
        delegator.removeAll(securityGroupList);
    }

    // delete from ContentAttribute
    List<EntityCondition> conditionContentAttribute = FastList.newInstance();
    conditionContentAttribute.add(EntityCondition.makeCondition("contentId", v.legacy));
    contentAttributeList = delegator.findList("ContentAttribute", EntityCondition.makeCondition(conditionContentAttribute), null, null, null, false);

    

    if(UtilValidate.isNotEmpty(contentAttributeList)) {
        Debug.log("Elimino contentAttributeList...");
        delegator.removeAll(contentAttributeList);
    }

    // delete from ContentAssoc
    List<EntityCondition> conditionsContentAssoc = FastList.newInstance();
    conditionsContentAssoc.add(EntityCondition.makeCondition("contentIdTo", v.legacy));
    contentAssocList = delegator.findList("ContentAssoc", EntityCondition.makeCondition(conditionsContentAssoc), null, null, null, false);

    

    if(UtilValidate.isNotEmpty(contentAssocList)) {
        Debug.log("Elimino contentAssocList...");
        delegator.removeAll(contentAssocList);
    }

    // delete from Content
    List<EntityCondition> conditionsContent = FastList.newInstance();
    conditionsContent.add(EntityCondition.makeCondition("contentId", v.legacy));
    contentList = delegator.findList("Content", EntityCondition.makeCondition(conditionsContent), null, null, null, false);


    if(UtilValidate.isNotEmpty(contentList)) {
        Debug.log("Elimino contentList...");
        delegator.removeAll(contentList);
    }


    
}
return result;
