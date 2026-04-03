package it.memelabs.gn.services.event;

import it.memelabs.gn.services.event.type.*;
import it.memelabs.gn.services.job.JobResultOfbiz;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 16/01/14
 *
 * @author Andrea Fossi
 */
public abstract class AbstractEventMessageFactory {

    public EventMessageAuthArchived makeEventMessageAuthArchived() {
        return init(new EventMessageAuthArchived());
    }

    public EventMessageAuthArchived makeEventMessageAuthArchived(String nodeKey, String partyId, String nodeType, Map<String, Object> authorization) {
        EventMessageAuthArchived em = init(new EventMessageAuthArchived());
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setAuthorization(authorization);
        return em;

    }

    public EventMessageAuthArchived makeEventMessageAuthArchived(Map<String, Object> attributes) {
        return init(new EventMessageAuthArchived(), attributes);
    }

    //---

    public EventMessageCommunication makeEventMessageCommunication() {
        return init(new EventMessageCommunication());
    }

    public EventMessageCommunication makeEventMessageCommunication(Map<String, Object> attributes) {
        return init(new EventMessageCommunication(), attributes);
    }

    public EventMessageCommunication makeEventMessageCommunicationFromCommunication(Map<String, Object> communication) {
        EventMessageCommunication em = init(new EventMessageCommunication());
        em.setCommunicationEvent(communication);
        return em;
    }

    //---

    public EventMessageCommunicationAck makeEventMessageCommunicationAck() {
        return init(new EventMessageCommunicationAck());
    }

    public EventMessageCommunicationAck makeEventMessageCommunicationAck(String communicationId, boolean isDelivered) {
        EventMessageCommunicationAck em = init(new EventMessageCommunicationAck());
        em.setCommunicationEventId(communicationId);
        em.setDelivered(isDelivered);
        return em;
    }

    public EventMessageCommunicationAck makeEventMessageCommunicationAck(Map<String, Object> attributes) {
        return init(new EventMessageCommunicationAck(), attributes);
    }

    //---

    public EventMessageAuthArchivedAck makeEventMessageAuthArchivedAck() {
        return init(new EventMessageAuthArchivedAck());
    }

    public EventMessageAuthArchivedAck makeEventMessageAuthArchivedAck(String authorizationKey) {
        EventMessageAuthArchivedAck em = init(new EventMessageAuthArchivedAck());
        em.setAuthorizationKey(authorizationKey);
        return em;
    }

    public EventMessageAuthArchivedAck makeEventMessageAuthArchivedAck(Map<String, Object> attributes) {
        return init(new EventMessageAuthArchivedAck(), attributes);
    }

    public EventMessageAuthArchivedAck makeEventMessageAuthArchivedAck(String nodeKey, String partyId, String nodeType, String authorizationId) {
        EventMessageAuthArchivedAck em = init(new EventMessageAuthArchivedAck());
        em.setAuthorizationKey(authorizationId);
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        return em;
    }

    //

    public EventMessageAuthAudit makeEventMessageAuthAudit() {
        return init(new EventMessageAuthAudit());
    }

    public EventMessageAuthAudit makeEventMessageAuthAudit(Map<String, Object> attributes) {
        return init(new EventMessageAuthAudit(), attributes);
    }

    public EventMessageAuthAudit makeEventMessageAuthAudit(EventMessageAuthAudit.ActionTypeEnumOFBiz actionType, String contextPartyId, String userLoginId, String nodeKey, String partyId, String nodeType, Map<String, Object> authorization) {
        EventMessageAuthAudit em = init(new EventMessageAuthAudit());
        em.setActionType(actionType);
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setAuthorization(authorization);
        em.setContextPartyId(contextPartyId);
        em.setUserloginId(userLoginId);
        return em;

    }

    public EventMessageAuthAudit makeEventMessageAuthAudit(EventMessageAuthAudit.ActionTypeEnumOFBiz actionType, String contextPartyId, String userLoginId, String nodeKey, String partyId, String nodeType, Map<String, Object> authorization, Map<String, Object> previousAuthorization) {
        EventMessageAuthAudit em = init(new EventMessageAuthAudit());
        em.setActionType(actionType);
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setAuthorization(authorization);
        em.setContextPartyId(contextPartyId);
        em.setUserloginId(userLoginId);
        em.setPreviousAuthorization(previousAuthorization);
        return em;

    }

    //

    public EventMessageAuthPropagate makeEventMessageAuthPropagate() {
        return init(new EventMessageAuthPropagate());
    }

    public EventMessageAuthPropagate makeEventMessageAuthPropagate(String nodeKey, String partyId, String nodeType, String authorizationKey) {
        EventMessageAuthPropagate em = init(new EventMessageAuthPropagate());
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setAuthorizationKey(authorizationKey);
        return em;
    }

    public EventMessageAuthPropagate makeEventMessageAuthPropagate(Map<String, Object> attributes) {
        return init(new EventMessageAuthPropagate(), attributes);
    }

    //

    public EventMessageAuthSend makeEventMessageAuthSend() {
        return init(new EventMessageAuthSend());
    }

    public EventMessageAuthSend makeEventMessageAuthSend(String nodeKey, String partyId, String nodeType, String authorizationKey) {
        EventMessageAuthSend em = init(new EventMessageAuthSend());
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setAuthorizationKey(authorizationKey);
        return em;
    }

    public EventMessageAuthSend makeEventMessageAuthSend(Map<String, Object> attributes) {
        return init(new EventMessageAuthSend(), attributes);
    }

    //

    public EventMessageAuthSendDelete makeEventMessageAuthSendDeleteDelete() {
        return init(new EventMessageAuthSendDelete());
    }

    public EventMessageAuthSendDelete makeEventMessageAuthSendDelete(String nodeKey, String partyId, String nodeType, String authorizationKey) {
        EventMessageAuthSendDelete em = init(new EventMessageAuthSendDelete());
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setAuthorizationKey(authorizationKey);
        return em;
    }

    public EventMessageAuthSendDelete makeEventMessageAuthSendDelete(Map<String, Object> attributes) {
        return init(new EventMessageAuthSendDelete(), attributes);
    }

    //

    public EventMessageAuthPropagateDelete makeEventMessageAuthPropagateDeleteDelete() {
        return init(new EventMessageAuthPropagateDelete());
    }

    public EventMessageAuthPropagateDelete makeEventMessageAuthPropagateDelete(String nodeKey, String partyId, String nodeType, String authorizationKey) {
        EventMessageAuthPropagateDelete em = init(new EventMessageAuthPropagateDelete());
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setAuthorizationKey(authorizationKey);
        return em;
    }

    public EventMessageAuthPropagateDelete makeEventMessageAuthPropagateDelete(Map<String, Object> attributes) {
        return init(new EventMessageAuthPropagateDelete(), attributes);
    }

    //

    public EventMessageAuthReceived makeEventMessageAuthReceived() {
        return init(new EventMessageAuthReceived());
    }

    public EventMessageAuthReceived makeEventMessageAuthReceived(String nodeKey, String partyId, String nodeType, String authorizationKey, String parentAuthorizationKey) {
        EventMessageAuthReceived em = init(new EventMessageAuthReceived());
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setAuthorizationKey(authorizationKey);
        em.setParentAuthorizationKey(parentAuthorizationKey);
        return em;
    }

    public EventMessageAuthReceived makeEventMessageAuthReceived(Map<String, Object> attributes) {
        return init(new EventMessageAuthReceived(), attributes);
    }

    ///

    public EventMessageAuthMerge makeEventMessageAuthMerge() {
        return init(new EventMessageAuthMerge());
    }

    public EventMessageAuthMerge makeEventMessageAuthMerge(String nodeKey, String partyId, String nodeType, String authorizationKey, String conflictingAuthorizationKey) {
        EventMessageAuthMerge em = init(new EventMessageAuthMerge());
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setAuthorizationKey(authorizationKey);
        em.setConflictingAuthorizationKey(conflictingAuthorizationKey);
        return em;
    }

    public EventMessageAuthMerge makeEventMessageAuthMerge(Map<String, Object> attributes) {
        return init(new EventMessageAuthMerge(), attributes);
    }

    ///

    public EventMessageAuthReceivedAck makeEventMessageAuthReceivedAck() {
        return init(new EventMessageAuthReceivedAck());
    }

    public EventMessageAuthReceivedAck makeEventMessageAuthReceivedAck(String nodeKey, String partyId, String nodeType, String authorizationKey) {
        EventMessageAuthReceivedAck em = init(new EventMessageAuthReceivedAck());
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setAuthorizationKey(authorizationKey);
        return em;
    }

    public EventMessageAuthReceivedAck makeEventMessageAuthReceivedAck(Map<String, Object> attributes) {
        return init(new EventMessageAuthReceivedAck(), attributes);
    }

    ///

    public EventMessageAuthMergeAck makeEventMessageAuthMergeAck() {
        return init(new EventMessageAuthMergeAck());
    }

    public EventMessageAuthMergeAck makeEventMessageAuthMergeAck(String nodeKey, String partyId, String nodeType, String authorizationKey) {
        EventMessageAuthMergeAck em = init(new EventMessageAuthMergeAck());
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setAuthorizationKey(authorizationKey);
        return em;
    }

    public EventMessageAuthMergeAck makeEventMessageAuthMergeAck(Map<String, Object> attributes) {
        return init(new EventMessageAuthMergeAck(), attributes);
    }

    ///

    public EventMessagePing makeEventMessagePing() {
        return init(new EventMessagePing());
    }

    public EventMessagePing makeEventMessagePing(String message) {
        EventMessagePing em = init(new EventMessagePing());
        em.setMessage(message);
        return em;
    }

    public EventMessagePing makeEventMessagePing(Map<String, Object> attributes) {
        return init(new EventMessagePing(), attributes);
    }

    public EventMessagePong makeEventMessagePong() {
        return init(new EventMessagePong());
    }

    public EventMessagePong makeEventMessagePong(Map<String, Object> attributes) {
        return init(new EventMessagePong(), attributes);
    }

    //

    public EventMessagePublicSiteUpdate makeEventMessagePublicSiteUpdate() {
        return init(new EventMessagePublicSiteUpdate());
    }

    public EventMessagePublicSiteUpdate makeEventMessagePublicSiteUpdate(String shareEnable, Map<String, Object> sharerNode, List<Map<String, Object>> authorizationToShareList) {
        EventMessagePublicSiteUpdate em = init(new EventMessagePublicSiteUpdate());
        em.setShareEnable(shareEnable);
        em.setSharerNode(sharerNode);
        em.setAuthorizationToShareList(authorizationToShareList);
        return em;
    }

    public EventMessagePublicSiteUpdate makeEventMessagePublicSiteUpdate(String shareEnable, Map<String, Object> sharerNode) {
        EventMessagePublicSiteUpdate em = init(new EventMessagePublicSiteUpdate());
        em.setShareEnable(shareEnable);
        em.setSharerNode(sharerNode);
        em.setAuthorizationToShareList(Collections.<Map<String, Object>>emptyList());
        return em;
    }

    public EventMessagePublicSiteUpdate makeEventMessagePublicSiteUpdate(Map<String, Object> attributes) {
        return init(new EventMessagePublicSiteUpdate(), attributes);
    }

    //

    public EventMessageAuthIndexing makeEventMessageAuthIndexing() {
        return init(new EventMessageAuthIndexing());
    }

    public EventMessageAuthIndexing makeEventMessageAuthIndexing(String nodeKey, String partyId, String nodeType, Map<String, Object> authorization) {
        EventMessageAuthIndexing em = init(new EventMessageAuthIndexing());
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setAuthorization(authorization);
        return em;
    }

    public EventMessageAuthIndexing makeEventMessageAuthIndexing(Map<String, Object> attributes) {
        return init(new EventMessageAuthIndexing(), attributes);
    }

    //

    public EventMessageAuthIndexingDelete makeEventMessageAuthIndexingDelete() {
        return init(new EventMessageAuthIndexingDelete());
    }

    public EventMessageAuthIndexingDelete makeEventMessageAuthIndexingDelete(String nodeKey, String partyId, String nodeType, String authorizationKey) {
        EventMessageAuthIndexingDelete em = init(new EventMessageAuthIndexingDelete());
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setAuthorizationKey(authorizationKey);
        return em;
    }

    public EventMessageAuthIndexingDelete makeEventMessageAuthIndexingDelete(Map<String, Object> attributes) {
        return init(new EventMessageAuthIndexingDelete(), attributes);
    }

    //

    public EventMessageFilterAperture makeEventMessageFilterAperture() {
        return init(new EventMessageFilterAperture());
    }

    public EventMessageFilterAperture makeEventMessageFilterAperture(String nodeKey, String partyId, String nodeType,
                                                                     EventMessageDirectionOfbiz directionOfbiz, String senderNodeKey,
                                                                     String senderPartyId, String taxIdentificationNumber) {
        EventMessageFilterAperture em = init(new EventMessageFilterAperture());
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setDirection(directionOfbiz);
        em.setSenderNodeKey(senderNodeKey);
        em.setSenderPartyId(senderPartyId);
        em.setTaxIdentificationNumber(taxIdentificationNumber);
        return em;
    }

    public EventMessageFilterAperture makeEventMessageFilterAperture(Map<String, Object> attributes) {
        return init(new EventMessageFilterAperture(), attributes);
    }

    //

    public EventMessageFilterApertureSend makeEventMessageFilterApertureSend() {
        return init(new EventMessageFilterApertureSend());
    }

    public EventMessageFilterApertureSend makeEventMessageFilterApertureSend(String nodeKey, String partyId, String nodeType,
                                                                             EventMessageDirectionOfbiz directionOfbiz, String senderNodeKey,
                                                                             String senderPartyId, String taxIdentificationNumber) {
        EventMessageFilterApertureSend em = init(new EventMessageFilterApertureSend());
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setDirection(directionOfbiz);
        em.setSenderNodeKey(senderNodeKey);
        em.setSenderPartyId(senderPartyId);
        em.setTaxIdentificationNumber(taxIdentificationNumber);
        return em;
    }

    public EventMessageFilterApertureSend makeEventMessageFilterApertureSend(Map<String, Object> attributes) {
        return init(new EventMessageFilterApertureSend(), attributes);
    }

    //

    public EventMessageEntityAudit makeEventMessageEntityAudit() {
        return init(new EventMessageEntityAudit());
    }

    public EventMessageEntityAudit makeEventMessageEntityAudit(Map<String, Object> attributes) {
        return init(new EventMessageEntityAudit(), attributes);
    }

    public EventMessageEntityAudit makeEventMessageEntityAudit(Map<String, Object> auditContext, Date auditTimestamp, List<Map<String, Object>> entries) {
        EventMessageEntityAudit em = init(new EventMessageEntityAudit());
        em.setAuditContext(auditContext);
        em.setAuditTimestamp(auditTimestamp);
        em.setEntries(entries);
        return em;
    }

    //

    public EventMessageJobAck makeEventMessageJobAck() {
        return init(new EventMessageJobAck());
    }

    public EventMessageJobAck makeEventMessageJobAck(Map<String, Object> attributes) {
        return init(new EventMessageJobAck(), attributes);
    }

    public EventMessageJobAck makeEventMessageJobAck(String jobId, JobResultOfbiz jobResultId, String failMessage) {
        EventMessageJobAck em = init(new EventMessageJobAck());
        em.setJobId(jobId);
        em.setJobResultId(jobResultId);
        em.setFailMessage(failMessage);
        return em;
    }

    //

    public EventMessageOperationalCheckAudit makeEventMessageOperationalCheckAudit() {
        return init(new EventMessageOperationalCheckAudit());
    }

    public EventMessageOperationalCheckAudit makeEventMessageOperationalCheckAudit(String userLoginId, String nodeKey, String partyId, String nodeType, List<Map<String, Object>> operationalCheckDataList) {
        EventMessageOperationalCheckAudit em = init(new EventMessageOperationalCheckAudit());
        em.setUserloginId(userLoginId);
        em.setNodeType(nodeType);
        em.setNodeKey(nodeKey);
        em.setPartyId(partyId);
        em.setOperationalCheckDataList(operationalCheckDataList);
        return em;
    }

    public EventMessageOperationalCheckAudit makeEventMessageOperationalCheckAudit(Map<String, Object> attributes) {
        return init(new EventMessageOperationalCheckAudit(), attributes);
    }

    ///

    protected abstract <T extends EventMessage> T init(T em);

    protected abstract <T extends EventMessage> T init(T em, Map<String, Object> attributes);

}
