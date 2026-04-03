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

userLoginListToDisable = delegator.findList("UserLoginSecurityGroup"
        , EntityCondition.makeCondition(EntityCondition.makeCondition("groupId", "FULLADMIN"))
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
        if (UtilValidate.isNotEmpty(userLoginToDisable) && userToDisable.userLoginId.indexOf("gzoom.") < 0 ) {
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

List<Entry<String, String>> userLogins = UtilProperties.getPropertyValueList("GzoomTeam", "userLogin.gzoom");
for (Entry<String, String> map : userLogins) {
    String key = map.getKey();
    String userLoginId = key.substring(key.indexOf(".") + 1);
    Debug.log("userLoginId " + userLoginId);
    List<String> valueList = StringUtil.split(map.getValue(), "|");
    
    // <Party partyId="gzoom.dain" partyTypeId="PERSON" statusId="PARTY_ENABLED"/>
    GenericValue party = delegator.makeValue("Party");
    party.put("partyId", userLoginId);
    party.put("partyTypeId", "PERSON");
    party.put("statusId", "PARTY_ENABLED" );
    party.put("partyName", valueList[0] + " " + valueList[1]);
    delegator.createOrStore(party);
    
    // <Person firstName="Daina" lastName="Interrante" middleName="-" partyId="gzoom.dain"/>
    GenericValue person = delegator.makeValue("Person");
    person.put("partyId", userLoginId);
    person.put("firstName", valueList[0]);
    person.put("lastName", valueList[1]);
    person.put("middleName", "-");
    delegator.createOrStore(person);
    
    // <PartyStatus statusId="PARTY_ENABLED" partyId="gzoom.aletiv" statusDate="2000-01-01 00:00:00"/>
    GenericValue partyStatus = delegator.makeValue("PartyStatus");
    partyStatus.put("partyId", userLoginId);
    partyStatus.put("statusDate", new Timestamp(UtilDateTime.toDate(1, 1, 2000, 0, 0, 0).getTime()));
    partyStatus.put("statusId", "PARTY_ENABLED" );
    delegator.createOrStore(partyStatus);
    
    // <PartyParentRole organizationId="Company" roleTypeId="EMPLOYEE" partyId="gzoom.dain" parentRoleCode="gzoom.dain"/>
    GenericValue partyParentRole = delegator.makeValue("PartyParentRole");
    partyParentRole.put("partyId", userLoginId);
    partyParentRole.put("parentRoleCode", userLoginId);
    partyParentRole.put("organizationId", "Company");
    partyParentRole.put("roleTypeId", "EMPLOYEE");
    delegator.createOrStore(partyParentRole);

    // <PartyRole roleTypeId="EMPLOYEE" partyId="gzoom.dain"/>   
    GenericValue partyRole = delegator.makeValue("PartyRole");
    partyRole.put("partyId", userLoginId);
    partyRole.put("roleTypeId", "EMPLOYEE");
    delegator.createOrStore(partyRole);
    
    // <PartyRole parentRoleTypeId="EMPLOYEE" roleTypeId="EMPLOYEE" partyId="gzoom.dain"/>
    partyRole = delegator.makeValue("PartyRole");
    partyRole.put("partyId", userLoginId);
    partyRole.put("parentRoleTypeId", "EMPLOYEE");
    partyRole.put("roleTypeId", "EMPLOYEE");
    delegator.createOrStore(partyRole);

    // <UserLogin enabled="Y" partyId="gzoom.dain" userLoginId="gzoom.dain" currentPassword="{SHA}6bbdefb4512d72a496ccf11f9d0861467893092a" requirePasswordChange="N"/>
    GenericValue userLogin = delegator.makeValue("UserLogin");
    userLogin.put("partyId", userLoginId);
    userLogin.put("userLoginId", userLoginId);
    userLogin.put("enabled", "Y");
    userLoginToCreate = delegator.findOne("UserLogin", ["userLoginId": userLoginId], false);
    if (UtilValidate.isEmpty(userLoginToCreate)) {
        userLogin.put("requirePasswordChange", "Y");
        userLogin.put("currentPassword", "{SHA}6bbdefb4512d72a496ccf11f9d0861467893092a" );
    }
    delegator.createOrStore(userLogin);

    // <UserLoginSecurityGroup groupId="FULLADMIN" userLoginId="gzoom.dain" fromDate="2000-01-01 00:00:00"/>
    GenericValue userLoginSecurityGroup = delegator.makeValue("UserLoginSecurityGroup");
    userLoginSecurityGroup.put("groupId", "FULLADMIN");
    userLoginSecurityGroup.put("userLoginId", userLoginId);
    userLoginSecurityGroup.put("fromDate", new Timestamp(UtilDateTime.toDate(1, 1, 2000, 0, 0, 0).getTime()));
    delegator.createOrStore(userLoginSecurityGroup);

    // <UserLoginValidPartyRole userLoginId="gzoom.dain" partyId="Company" roleTypeId="INTERNAL_ORGANIZATIO"/>
    partyAcctgPreferenceList = delegator.findList("PartyAcctgPreference"
            , null, null, null, null, false);
    if (UtilValidate.isNotEmpty(partyAcctgPreferenceList)) {
        partyAcctgPreferenceList.each{ partyAcctgPreference ->
            GenericValue userLoginValidPartyRole = delegator.makeValue("UserLoginValidPartyRole");
            userLoginValidPartyRole.put("roleTypeId", "INTERNAL_ORGANIZATIO");
            userLoginValidPartyRole.put("userLoginId", userLoginId);
            userLoginValidPartyRole.put("partyId", partyAcctgPreference.partyId);
            delegator.createOrStore(userLoginValidPartyRole);
        }
    }
    
    // <ContactMech contactMechId="gzoom.dain" contactMechTypeId="EMAIL_ADDRESS" infoString="daina.interrante@mapsgroup.it"/>
    GenericValue contactMech = delegator.makeValue("ContactMech");
    contactMech.put("contactMechTypeId", "EMAIL_ADDRESS");
    contactMech.put("contactMechId", userLoginId);
    contactMech.put("infoString", valueList[2]);
    delegator.createOrStore(contactMech);

    // <PartyContactMech partyId="gzoom.dain" contactMechId="gzoom.dain" fromDate="2000-01-01 00:00:00.0" allowSolicitation="Y"/>
    GenericValue partyContactMech = delegator.makeValue("PartyContactMech");
    partyContactMech.put("allowSolicitation", "Y");
    partyContactMech.put("partyId", userLoginId);
    partyContactMech.put("contactMechId", userLoginId);
    partyContactMech.put("fromDate", new Timestamp(UtilDateTime.toDate(1, 1, 2000, 0, 0, 0).getTime()));
    delegator.createOrStore(partyContactMech);

    // <PartyContactMechPurpose contactMechPurposeTypeId="PRIMARY_EMAIL" partyId="gzoom.dain" contactMechId="gzoom.dain" fromDate="2000-01-01 00:00:00.0"/>
    GenericValue partyContactMechPurpose = delegator.makeValue("PartyContactMechPurpose");
    partyContactMechPurpose.put("contactMechPurposeTypeId", "PRIMARY_EMAIL");
    partyContactMechPurpose.put("partyId", userLoginId);
    partyContactMechPurpose.put("contactMechId", userLoginId);
    partyContactMechPurpose.put("fromDate", new Timestamp(UtilDateTime.toDate(1, 1, 2000, 0, 0, 0).getTime()));
    delegator.createOrStore(partyContactMechPurpose);
}

/** Esistono inoltre
    <!-- System UserLogin Account - see additional data in SecurityExtData -->
    <UserLogin userLoginId="system" enabled="N" isSystem="Y"/>
    <UserLoginSecurityGroup groupId="FULLADMIN" userLoginId="system" fromDate="2001-01-01 12:00:00.0"/>
    <!-- Anonymous UserLogin is referenced by services in various components -->
    <UserLogin userLoginId="anonymous" enabled="N"/>
 */
return result;
