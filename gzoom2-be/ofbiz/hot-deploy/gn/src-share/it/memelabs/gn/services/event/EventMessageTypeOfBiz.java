package it.memelabs.gn.services.event;

/**
 * 10/01/14
 *
 * @author Andrea Fossi
 */
public enum EventMessageTypeOfBiz {

    PING,//in
    PONG,//out
    AUTH_AUDIT,//out
    AUTH_INDEXING,//out
    AUTH_INDEXING_DELETE,//out
    AUTH_ARCHIVED,//out
    AUTH_RECEIVED,//out
    AUTH_RECEIVED_ACK,//in
    AUTH_MERGE,//out
    AUTH_MERGE_ACK,//in
    AUTH_ARCHIVED_ACK,//in
    AUTH_PROPAGATE,//out
    AUTH_SEND,//in
    AUTH_SEND_DELETE,//in
    AUTH_PROPAGATE_DELETE,//out
    COMMUNICATION_EVENT,//out
    COMMUNICATION_ACK,//in
    PUBLIC_SITE_UPDATE,//out
    FILTER_APERTURE,//out
    FILTER_APERTURE_SEND,//in
    ENTITY_AUDIT,//out
    OPERATIONAL_CHECK_AUDIT,//out
    JOB_ACK,//in

}
