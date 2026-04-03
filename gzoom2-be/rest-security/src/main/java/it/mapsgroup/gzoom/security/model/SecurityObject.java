package it.mapsgroup.gzoom.security.model;

import com.google.common.collect.ImmutableMap;

import static it.mapsgroup.gzoom.commons.Enums.parseMap;

/**
 * @author Andrea Fossi.
 */
public enum SecurityObject {
    ACCIDENT(SecurityDomainObject.ACCIDENT, query("accident")),
    ADMIN(SecurityDomainObject.ADMIN, null),
    ANTIMAFIA_PROCESS(SecurityDomainObject.ANTIMAFIA_PROCESS, query("antimafia_process")),
    ANTIMAFIA_PROCESS_PHASE(SecurityDomainObject.ANTIMAFIA_PROCESS, Constants.PHASE_QUERY),
    ATI(SecurityDomainObject.COMPANY, query("company")),
    AUDITING(SecurityDomainObject.AUDITING, null),
    COMPANY(SecurityDomainObject.COMPANY, query("company")),
    CONSTRUCTION_SITE(SecurityDomainObject.JOB_ORDER, query("construction_site")),
    CONSTRUCTION_SITE_LOG(SecurityDomainObject.CONSTRUCTION_SITE_LOG, query("construction_site_log")),
    CONSTRUCTION_SITE_LOG_ACTIVITY(SecurityDomainObject.CONSTRUCTION_SITE_LOG, query("construction_site_log_activity")),
    CONTRACT(SecurityDomainObject.CONTRACT, query("contract")),
    EQUIPMENT(SecurityDomainObject.EQUIPMENT, query("equipment")),
    EQUIPMENT_EMPLOYMENT(SecurityDomainObject.EQUIPMENT_EMPLOYMENT, query("equipment_employment")),
    GALLERY(SecurityDomainObject.GALLERY, query("gallery_image")),
    JOB_ORDER(SecurityDomainObject.JOB_ORDER, query("job_order")),
    LOT(SecurityDomainObject.JOB_ORDER, query("lot")),
    PERSON(SecurityDomainObject.PERSON, query("person")),
    PERSON_EMPLOYMENT(SecurityDomainObject.PERSON_EMPLOYMENT, query("person_employment")),
    WBS(SecurityDomainObject.JOB_ORDER, query("wbs")),
    WEEKLY_WORK_LOG(SecurityDomainObject.WEEKLY_WORK_LOG, query("weekly_work_log")),
    WORK_LOG(SecurityDomainObject.WORK_LOG, query("work_log")),
    TIMESHEET(SecurityDomainObject.TIMESHEET, query("work_log")),
    TIMESHEET_PERSON_EVENT(SecurityDomainObject.TIMESHEET, Constants.TIMESHEET_PERSON_EVENT_QUERY),
    TIMESHEET_EQUIPMENT_EVENT(SecurityDomainObject.TIMESHEET, Constants.TIMESHEET_EQUIPMENT_EVENT_QUERY),
    //--
    ;

    SecurityObject(SecurityDomainObject permission, String query) {
        this.query = query;
        this.permission = permission;
    }

    private String query;
    private SecurityDomainObject permission;

    public String getQuery() {
        return query;
    }

    public SecurityDomainObject getPermission() {
        return permission;
    }

    private static final ImmutableMap<String, SecurityObject> STR2PERM = parseMap(SecurityObject.class);


    public static SecurityObject fromString(String perm) {
        return STR2PERM.get(perm);
    }


    private static String query(String tableName) {
        return "select count(1) from " + tableName + " where id = ? and owner_node_id = ?";
    }

    private static class Constants {
        private static final String PHASE_QUERY = "select count(1) from  antimafia_process_phase PH " +
                "inner join antimafia_process P on PH.antimafia_process_id = P.id " +
                "where PH.id = ? and P.owner_node_id = ?";
        private static final String TIMESHEET_PERSON_EVENT_QUERY = "select count(1) from work_log_person_event ev " +
                "join work_log wl on wl.id=ev.work_log_id where ev.id = ? and wl.owner_node_id = ?";
        private static final String TIMESHEET_EQUIPMENT_EVENT_QUERY = "select count(1) from work_log_equipment_event ev " +
                "join work_log wl on wl.id=ev.work_log_id where ev.id = ? and wl.owner_node_id = ?";
    }
}
