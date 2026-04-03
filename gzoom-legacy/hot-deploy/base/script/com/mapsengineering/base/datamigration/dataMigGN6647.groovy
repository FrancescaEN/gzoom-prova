import org.ofbiz.base.util.*;
import org.ofbiz.service.*;
import com.mapsengineering.base.datamigration.util.DatabaseUtil;

def result = ServiceUtil.returnSuccess();

def delegator = dctx.getDelegator();

def security = dctx.getSecurity();

// check permission
userLogin = (GenericValue) context.get("userLogin");
if (!security.hasPermission("ENTITY_MAINT", userLogin)) {
    return ServiceUtil.returnError(UtilProperties.getMessage("BaseErrorLabels", "ManagementErrorModelEntityNotSet", locale));
}

def groupName = DatabaseUtil.DEFAULT_GROUP_NAME;
def helperInfo = delegator.getGroupHelperInfo(groupName);
def dbUtil = new DatabaseUtil(helperInfo);
// ContactListCommStatus - communicationEventId - fk-name CNCT_LST_CST_CE
Debug.log("Removing index");
dbUtil.deleteForeignKeyAndIndex("CONTACT_LIST_COMM_STATUS", "CNCT_LST_CST_CE");
dbUtil.modifyColumnTypeAndRedefinePk("CONTACT_LIST_COMM_STATUS", "COMMUNICATION_EVENT_ID", "id-long-ne", ["CONTACT_LIST_ID", "COMMUNICATION_EVENT_ID", "CONTACT_MECH_ID"]);
dbUtil.addForeignKey("CONTACT_LIST_COMM_STATUS", "CNCT_LST_CST_CE", "COMMUNICATION_EVENT_ID", "COMMUNICATION_EVENT", "COMMUNICATION_EVENT_ID");

// CommunicationEventOrder - communicationEventId - fk-name="COMEV_ORDER_CMEV"
dbUtil.deleteForeignKeyAndIndex("COMMUNICATION_EVENT_ORDER", "COMEV_ORDER_CMEV");
dbUtil.modifyColumnTypeAndRedefinePk("COMMUNICATION_EVENT_ORDER", "COMMUNICATION_EVENT_ID", "id-long-ne", ["ORDER_ID", "COMMUNICATION_EVENT_ID"]);
dbUtil.addForeignKey("COMMUNICATION_EVENT_ORDER", "COMEV_ORDER_CMEV", "COMMUNICATION_EVENT_ID", "COMMUNICATION_EVENT", "COMMUNICATION_EVENT_ID");

// CustRequestCommEvent - communicationEventId - fk-name="CUSTREQ_CEV_CEV"
dbUtil.deleteForeignKeyAndIndex("CUST_REQUEST_COMM_EVENT", "CUSTREQ_CEV_CEV");
dbUtil.modifyColumnTypeAndRedefinePk("CUST_REQUEST_COMM_EVENT", "COMMUNICATION_EVENT_ID", "id-long-ne", ["CUST_REQUEST_ID", "COMMUNICATION_EVENT_ID"]);
dbUtil.addForeignKey("CUST_REQUEST_COMM_EVENT", "CUSTREQ_CEV_CEV", "COMMUNICATION_EVENT_ID", "COMMUNICATION_EVENT", "COMMUNICATION_EVENT_ID");

// CommEventContentAssoc fk-name="COMMEV_CA_COMMEV"
dbUtil.deleteForeignKeyAndIndex("COMM_EVENT_CONTENT_ASSOC", "COMMEV_CA_COMMEV");
dbUtil.modifyColumnTypeAndRedefinePk("COMM_EVENT_CONTENT_ASSOC", "COMMUNICATION_EVENT_ID", "id-long-ne", ["CONTENT_ID", "COMMUNICATION_EVENT_ID", "FROM_DATE"]);
dbUtil.addForeignKey("COMM_EVENT_CONTENT_ASSOC", "COMMEV_CA_COMMEV", "COMMUNICATION_EVENT_ID", "COMMUNICATION_EVENT", "COMMUNICATION_EVENT_ID");

// CommunicationEvent
dbUtil.modifyColumnTypeAndRedefinePk("COMMUNICATION_EVENT", "COMMUNICATION_EVENT_ID", "id-long-ne", ["COMMUNICATION_EVENT_ID"]);

// CommunicationEventProduct fk-name="COMEV_PROD_CMEV"
dbUtil.deleteForeignKeyAndIndex("COMMUNICATION_EVENT_PRODUCT", "COMEV_PROD_CMEV");
dbUtil.modifyColumnTypeAndRedefinePk("COMMUNICATION_EVENT_PRODUCT", "COMMUNICATION_EVENT_ID", "id-long-ne", ["COMMUNICATION_EVENT_ID", "PRODUCT_ID"]);
dbUtil.addForeignKey("COMMUNICATION_EVENT_PRODUCT", "COMEV_PROD_CMEV", "COMMUNICATION_EVENT_ID", "COMMUNICATION_EVENT", "COMMUNICATION_EVENT_ID");

// CommunicationEventPurpose fk-name="COM_EVNT_PRP_EVNT"
dbUtil.deleteForeignKeyAndIndex("COMMUNICATION_EVENT_PURPOSE", "COM_EVNT_PRP_EVNT");
dbUtil.modifyColumnTypeAndRedefinePk("COMMUNICATION_EVENT_PURPOSE", "COMMUNICATION_EVENT_ID", "id-long-ne", ["COMMUNICATION_EVENT_ID", "COMMUNICATION_EVENT_PRP_TYP_ID"]);
dbUtil.addForeignKey("COMMUNICATION_EVENT_PURPOSE", "COM_EVNT_PRP_EVNT", "COMMUNICATION_EVENT_ID", "COMMUNICATION_EVENT", "COMMUNICATION_EVENT_ID");

// CommunicationEventRole fk-name="COM_EVRL_CMEV"
dbUtil.deleteForeignKeyAndIndex("COMMUNICATION_EVENT_ROLE", "COM_EVRL_CMEV");
dbUtil.modifyColumnTypeAndRedefinePk("COMMUNICATION_EVENT_ROLE", "COMMUNICATION_EVENT_ID", "id-long-ne", ["COMMUNICATION_EVENT_ID", "PARTY_ID", "ROLE_TYPE_ID"]);
dbUtil.addForeignKey("COMMUNICATION_EVENT_ROLE", "COM_EVRL_CMEV", "COMMUNICATION_EVENT_ID", "COMMUNICATION_EVENT", "COMMUNICATION_EVENT_ID");

// PartyNeed fk-name="PARTY_NEED_CMEV"
dbUtil.deleteForeignKeyAndIndex("PARTY_NEED", "PARTY_NEED_CMEV");
dbUtil.modifyColumnType("PARTY_NEED", "COMMUNICATION_EVENT_ID", "id-long");
dbUtil.addForeignKey("PARTY_NEED", "PARTY_NEED_CMEV", "COMMUNICATION_EVENT_ID", "COMMUNICATION_EVENT", "COMMUNICATION_EVENT_ID");

// Subscription
dbUtil.modifyColumnType("SUBSCRIPTION", "COMMUNICATION_EVENT_ID", "id-long");

// SubscriptionCommEvent fk-name="SUBSC_COM_EVENT"
dbUtil.deleteForeignKeyAndIndex("SUBSCRIPTION_COMM_EVENT", "SUBSC_COM_EVENT");
dbUtil.modifyColumnTypeAndRedefinePk("SUBSCRIPTION_COMM_EVENT", "COMMUNICATION_EVENT_ID", "id-long-ne", ["SUBSCRIPTION_ID", "COMMUNICATION_EVENT_ID"]);
dbUtil.addForeignKey("SUBSCRIPTION_COMM_EVENT", "SUBSC_COM_EVENT", "COMMUNICATION_EVENT_ID", "COMMUNICATION_EVENT", "COMMUNICATION_EVENT_ID");

// CommunicationEventWorkEff fk-name="COMEV_WEFF_WEFF"
dbUtil.deleteForeignKeyAndIndex("COMMUNICATION_EVENT_WORK_EFF", "COMEV_WEFF_WEFF");
dbUtil.modifyColumnTypeAndRedefinePk("COMMUNICATION_EVENT_WORK_EFF", "COMMUNICATION_EVENT_ID", "id-long-ne", ["COMMUNICATION_EVENT_ID", "WORK_EFFORT_ID"]);
dbUtil.addForeignKey("COMMUNICATION_EVENT_WORK_EFF", "COMEV_WEFF_WEFF", "COMMUNICATION_EVENT_ID", "COMMUNICATION_EVENT", "COMMUNICATION_EVENT_ID");

return result;

