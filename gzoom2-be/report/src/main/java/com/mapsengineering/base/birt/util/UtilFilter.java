package com.mapsengineering.base.birt.util;


import it.mapsgroup.gzoom.mybatis.service.PermissionService;
import it.mapsgroup.gzoom.mybatis.util.ContextPermissionPrefixEnum;
import it.memelabs.smartnebula.spring.boot.config.ApplicationContextProvider;

//TODO RISCRITTO
public class UtilFilter {

    public static final String MODULE = UtilFilter.class.getName();
/*
    OLD PERMISSION

    private String MGR_ADMIN = "MGR_ADMIN";
    private String ORG_ADMIN = "ORG_ADMIN";
    private String ROLE_ADMIN = "ROLE_ADMIN";
    private String SUP_ADMIN = "SUP_ADMIN";
    private String TOP_ADMIN = "TOP_ADMIN";

    private String FULLADMIN = "FULLADMIN";

 */


    private String filterInnerJoin = " INNER JOIN \n" +
            "      ( SELECT FIL_UL.PARTY_ID AS UL_PARTY_ID, FIL_ULVR.USER_LOGIN_ID AS ULVR_USER_LOGIN_ID, FIL_ULVR.PARTY_ID AS ULVR_PARTY_ID, FIL_ULVR.ROLE_TYPE_ID AS ULVR_ROLE_TYPE_ID \n" +
            "        FROM USER_LOGIN_VALID_PARTY_ROLE FIL_ULVR \n" +
            "        INNER JOIN USER_LOGIN FIL_UL ON FIL_ULVR.USER_LOGIN_ID = FIL_UL.USER_LOGIN_ID) \n" +
            "              FIL_U ON FIL_W.ORGANIZATION_ID = FIL_U.ULVR_PARTY_ID AND FIL_U.ULVR_ROLE_TYPE_ID = 'INTERNAL_ORGANIZATIO' \n";


    private String filterLeftJoin = " INNER JOIN WORK_EFFORT_TYPE FIL_WT ON FIL_WT.WORK_EFFORT_TYPE_ID = FIL_W.WORK_EFFORT_TYPE_ID \n" +
            "LEFT OUTER JOIN PARTY_RELATIONSHIP FIL_PRM ON FIL_W.ORG_UNIT_ROLE_TYPE_ID = FIL_PRM.ROLE_TYPE_ID_FROM AND FIL_W.ORG_UNIT_ID = FIL_PRM.PARTY_ID_FROM \n " +
            "       AND (FIL_PRM.PARTY_RELATIONSHIP_TYPE_ID = 'ORG_RESPONSIBLE' OR \n" +
            "           (FIL_PRM.PARTY_RELATIONSHIP_TYPE_ID = 'ORG_DELEGATE' AND \n" +
            "               (FIL_PRM.CTX_ENABLED IS NULL \n" +
            "                   OR FIL_PRM.CTX_ENABLED LIKE CONCAT('%', CONCAT(FIL_WT.PARENT_TYPE_ID, '%')) \n" +
            "               ) \n" +
            "           ) \n" +
            "       ) \n" +
            "       AND FIL_PRM.FROM_DATE <= FIL_W.ESTIMATED_COMPLETION_DATE \n " +
            "       AND (FIL_PRM.THRU_DATE IS NULL OR FIL_PRM.THRU_DATE >= FIL_W.ESTIMATED_START_DATE) AND FIL_PRM.PARTY_ID_TO = FIL_U.UL_PARTY_ID \n " +
            "LEFT OUTER JOIN PARTY_RELATIONSHIP FIL_PRS ON FIL_W.ORG_UNIT_ROLE_TYPE_ID = FIL_PRS.ROLE_TYPE_ID_TO AND FIL_W.ORG_UNIT_ID = FIL_PRS.PARTY_ID_TO \n " +
            "       AND FIL_PRS.PARTY_RELATIONSHIP_TYPE_ID = 'GROUP_ROLLUP' AND FIL_PRS.FROM_DATE <= FIL_W.ESTIMATED_COMPLETION_DATE \n " +
            "       AND (FIL_PRS.THRU_DATE IS NULL OR FIL_PRS.THRU_DATE >= FIL_W.ESTIMATED_START_DATE) \n " +
            "LEFT OUTER JOIN PARTY_RELATIONSHIP FIL_PRS2 ON FIL_PRS.ROLE_TYPE_ID_FROM = FIL_PRS2.ROLE_TYPE_ID_FROM AND FIL_PRS.PARTY_ID_FROM = FIL_PRS2.PARTY_ID_FROM \n " +
            "       AND (FIL_PRS2.PARTY_RELATIONSHIP_TYPE_ID = 'ORG_RESPONSIBLE' OR \n" +
            "           (FIL_PRS2.PARTY_RELATIONSHIP_TYPE_ID = 'ORG_DELEGATE' AND \n" +
            "               (FIL_PRS2.CTX_ENABLED IS NULL \n" +
            "                   OR FIL_PRS2.CTX_ENABLED LIKE CONCAT('%', CONCAT(FIL_WT.PARENT_TYPE_ID, '%')) \n" +
            "               ) \n" +
            "           ) \n" +
            "       ) \n" +
            "       AND FIL_PRS2.FROM_DATE <= FIL_W.ESTIMATED_COMPLETION_DATE \n " +
            "       AND (FIL_PRS2.THRU_DATE IS NULL OR FIL_PRS2.THRU_DATE >= FIL_W.ESTIMATED_START_DATE) AND FIL_PRS2.PARTY_ID_TO = FIL_U.UL_PARTY_ID \n " +
            "LEFT OUTER JOIN PARTY_RELATIONSHIP FIL_Z2 ON FIL_PRS.ROLE_TYPE_ID_FROM = FIL_Z2.ROLE_TYPE_ID_TO AND FIL_PRS.PARTY_ID_FROM = FIL_Z2.PARTY_ID_TO \n " +
            "		AND FIL_Z2.PARTY_RELATIONSHIP_TYPE_ID = 'GROUP_ROLLUP' AND FIL_Z2.FROM_DATE <= FIL_W.ESTIMATED_COMPLETION_DATE \n " +
            "		AND (FIL_Z2.THRU_DATE IS NULL OR FIL_Z2.THRU_DATE >= FIL_W.ESTIMATED_START_DATE) \n " +
            "LEFT OUTER JOIN PARTY_RELATIONSHIP FIL_Y2 ON FIL_Z2.ROLE_TYPE_ID_FROM = FIL_Y2.ROLE_TYPE_ID_FROM AND FIL_Z2.PARTY_ID_FROM = FIL_Y2.PARTY_ID_FROM \n " +
            "       AND (FIL_Y2.PARTY_RELATIONSHIP_TYPE_ID = 'ORG_RESPONSIBLE' OR \n" +
            "           (FIL_Y2.PARTY_RELATIONSHIP_TYPE_ID = 'ORG_DELEGATE' AND \n" +
            "               (FIL_Y2.CTX_ENABLED IS NULL \n" +
            "                   OR FIL_Y2.CTX_ENABLED LIKE CONCAT('%', CONCAT(FIL_WT.PARENT_TYPE_ID, '%')) \n" +
            "               ) \n" +
            "           ) \n" +
            "       ) \n" +
            "       AND FIL_Y2.FROM_DATE <= FIL_W.ESTIMATED_COMPLETION_DATE \n " +
            " 		AND (FIL_Y2.THRU_DATE IS NULL OR FIL_Y2.THRU_DATE >= FIL_W.ESTIMATED_START_DATE) \n " +
            "		AND FIL_Y2.PARTY_ID_TO = FIL_U.UL_PARTY_ID \n " +
            "LEFT OUTER JOIN WORK_EFFORT_PARTY_ASSIGNMENT FIL_WEPA ON FIL_W.WORK_EFFORT_ID = FIL_WEPA.WORK_EFFORT_ID AND FIL_W.ESTIMATED_COMPLETION_DATE = FIL_WEPA.THRU_DATE \n " +
                                    "       AND FIL_WEPA.ROLE_TYPE_ID LIKE 'WEM%' AND FIL_WEPA.PARTY_ID = FIL_U.UL_PARTY_ID";

    private String  filterLeftJoinGenovaNew = " INNER JOIN WORK_EFFORT_TYPE FIL_WT ON FIL_WT.WORK_EFFORT_TYPE_ID = FIL_W.WORK_EFFORT_TYPE_ID \n " +
            "LEFT OUTER JOIN PARTY_RELATIONSHIP FIL_PRM ON \n " +
            " ( \n " +
            "  (FIL_W.ORG_UNIT_ROLE_TYPE_ID = FIL_PRM.ROLE_TYPE_ID_FROM AND FIL_W.ORG_UNIT_ID = FIL_PRM.PARTY_ID_FROM \n " +
            "   AND FIL_PRM.FROM_DATE <= FIL_W.ESTIMATED_COMPLETION_DATE \n " +
            "  AND (FIL_PRM.THRU_DATE IS NULL OR FIL_PRM.THRU_DATE >= FIL_W.ESTIMATED_START_DATE) ) \n " +
            "          OR \n " +
            "   ( SO.ORG_UNIT_ROLE_TYPE_ID = FIL_PRM.ROLE_TYPE_ID_FROM AND SO.ORG_UNIT_ID = FIL_PRM.PARTY_ID_FROM \n " +
            "   AND FIL_PRM.FROM_DATE <= SO.ESTIMATED_COMPLETION_DATE \n " +
            "   AND (FIL_PRM.THRU_DATE IS NULL OR FIL_PRM.THRU_DATE >= SO.ESTIMATED_START_DATE)) \n " +
            "  )   \n " +
            "       AND (FIL_PRM.PARTY_RELATIONSHIP_TYPE_ID = 'ORG_RESPONSIBLE' OR \n" +
            "           (FIL_PRM.PARTY_RELATIONSHIP_TYPE_ID = 'ORG_DELEGATE' AND \n" +
            "               (FIL_PRM.CTX_ENABLED IS NULL \n" +
            "                   OR FIL_PRM.CTX_ENABLED LIKE CONCAT('%', CONCAT(FIL_WT.PARENT_TYPE_ID, '%')) \n" +
            "               ) \n" +
            "           ) \n" +
            "       ) \n" +
            "  AND FIL_PRM.PARTY_ID_TO = FIL_U.UL_PARTY_ID \n " +
            "LEFT OUTER JOIN PARTY_RELATIONSHIP FIL_PRS ON FIL_W.ORG_UNIT_ROLE_TYPE_ID = FIL_PRS.ROLE_TYPE_ID_TO AND FIL_W.ORG_UNIT_ID = FIL_PRS.PARTY_ID_TO \n " +
            "       AND FIL_PRS.PARTY_RELATIONSHIP_TYPE_ID = 'GROUP_ROLLUP' AND FIL_PRS.FROM_DATE <= FIL_W.ESTIMATED_COMPLETION_DATE \n " +
            "       AND (FIL_PRS.THRU_DATE IS NULL OR FIL_PRS.THRU_DATE >= FIL_W.ESTIMATED_START_DATE) \n " +
            "LEFT OUTER JOIN PARTY_RELATIONSHIP FIL_PRS2 ON FIL_PRS.ROLE_TYPE_ID_FROM = FIL_PRS2.ROLE_TYPE_ID_FROM AND FIL_PRS.PARTY_ID_FROM = FIL_PRS2.PARTY_ID_FROM \n " +
            "       AND (FIL_PRS2.PARTY_RELATIONSHIP_TYPE_ID = 'ORG_RESPONSIBLE' OR \n" +
            "           (FIL_PRS2.PARTY_RELATIONSHIP_TYPE_ID = 'ORG_DELEGATE' AND \n" +
            "               (FIL_PRS2.CTX_ENABLED IS NULL \n" +
            "                   OR FIL_PRS2.CTX_ENABLED LIKE CONCAT('%', CONCAT(FIL_WT.PARENT_TYPE_ID, '%')) \n" +
            "               ) \n" +
            "           ) \n" +
            "       ) \n" +
            "       AND FIL_PRS2.FROM_DATE <= FIL_W.ESTIMATED_COMPLETION_DATE \n " +
            "       AND (FIL_PRS2.THRU_DATE IS NULL OR FIL_PRS2.THRU_DATE >= FIL_W.ESTIMATED_START_DATE) AND FIL_PRS2.PARTY_ID_TO = FIL_U.UL_PARTY_ID \n " +
            "LEFT OUTER JOIN PARTY_RELATIONSHIP FIL_Z2 ON FIL_PRS.ROLE_TYPE_ID_FROM = FIL_Z2.ROLE_TYPE_ID_TO AND FIL_PRS.PARTY_ID_FROM = FIL_Z2.PARTY_ID_TO \n " +
            "		AND FIL_Z2.PARTY_RELATIONSHIP_TYPE_ID = 'GROUP_ROLLUP' AND FIL_Z2.FROM_DATE <= FIL_W.ESTIMATED_COMPLETION_DATE \n " +
            "		AND (FIL_Z2.THRU_DATE IS NULL OR FIL_Z2.THRU_DATE >= FIL_W.ESTIMATED_START_DATE) \n " +
            "LEFT OUTER JOIN PARTY_RELATIONSHIP FIL_Y2 ON FIL_Z2.ROLE_TYPE_ID_FROM = FIL_Y2.ROLE_TYPE_ID_FROM AND FIL_Z2.PARTY_ID_FROM = FIL_Y2.PARTY_ID_FROM \n " +
            "       AND (FIL_Y2.PARTY_RELATIONSHIP_TYPE_ID = 'ORG_RESPONSIBLE' OR \n" +
            "           (FIL_Y2.PARTY_RELATIONSHIP_TYPE_ID = 'ORG_DELEGATE' AND \n" +
            "               (FIL_Y2.CTX_ENABLED IS NULL \n" +
            "                   OR FIL_Y2.CTX_ENABLED LIKE CONCAT('%', CONCAT(FIL_WT.PARENT_TYPE_ID, '%')) \n" +
            "               ) \n" +
            "           ) \n" +
            "       ) \n" +
            "       AND FIL_Y2.FROM_DATE <= FIL_W.ESTIMATED_COMPLETION_DATE \n " +
            " 		AND (FIL_Y2.THRU_DATE IS NULL OR FIL_Y2.THRU_DATE >= FIL_W.ESTIMATED_START_DATE) \n " +
            "		AND FIL_Y2.PARTY_ID_TO = FIL_U.UL_PARTY_ID \n ";


    private String filterWhereIsTop = " OR FIL_Y2.PARTY_ID_TO IS NOT NULL";
    private String filterWhereIsOrg = " OR FIL_PRM.PARTY_ID_TO IS NOT NULL"; //" OR (FIL_STATUS.MANAG_WE_STATUS_ENUM_ID = 'ORGMANAGER' AND FIL_PRM.PARTY_ID_TO IS NOT NULL) ";
    private String filterWhereIsSup = " OR FIL_PRS2.PARTY_ID_TO IS NOT NULL"; //" OR (FIL_STATUS.MANAG_WE_STATUS_ENUM_ID = 'SUPMANAGER' AND FIL_PRS2.PARTY_ID_TO IS NOT NULL) ";
    private String filterWhereIsRole = " OR  FIL_WEPA.PARTY_ID IS NOT NULL";  //" OR (FIL_STATUS.MANAG_WE_STATUS_ENUM_ID = 'ROLE' AND FIL_WEPA.ROLE_TYPE_ID = FIL_STATUS.MANAGEMENT_ROLE_TYPE_ID AND FIL_WEPA.PARTY_ID IS NOT NULL) ";
    private String filterUserLogin  = " AND FIL_U.ULVR_USER_LOGIN_ID = ";

    private boolean isFullAdmin = false;
    //  private Security security;
    private String localDispatcherName;
    private String userLoginId;

    private PermissionService permissionService;

       /*
    public UtilFilter(String userLoginId, String localDispatcherName) {

    	//UserLoginDao dao = ApplicationContextProvider.getApplicationContext().getBean(UserLoginDao.class);
    	//UserLogin user = dao.getUserLogin(userLoginId);
    	//System.out.println("User="+user.getUsername());

    }*/

    public UtilFilter(String userLoginId, String localDispatcherName) {
        permissionService = ApplicationContextProvider.getApplicationContext().getBean(PermissionService.class);
        setFullAdmin(userLoginId, localDispatcherName);
    }

    private void setFullAdmin(String userLoginId, String localDispatcherName) {
        this.localDispatcherName = localDispatcherName;
        this.userLoginId = userLoginId;

        String permission = permissionService.permissionLocalDispatcherName(localDispatcherName);
        ContextPermissionPrefixEnum context = ContextPermissionPrefixEnum.getByPermissionPrefix(permission);
        if (permissionService.isFullAdmin(userLoginId, context) || permissionService.isView(userLoginId, context)) {
            isFullAdmin = true;
        }
    }

    /**
     * Ritorna la query utilizzata per i filtri in inner join solo se non sono full admin
     * @return
     */
    public String getWorkEffortFilterInner() {
        if (isFullAdmin) {
            return "";
        }
        return filterInnerJoin;
    }


    /**
     * Ritorna la query utilizzata per i filtri in left outer join solo se non sono full admin
     * @return
     */
    public String getWorkEffortFilterLeftJoin() {
        if (isFullAdmin) {
            return "";
        }
        return filterLeftJoin;
    }

    public String getWorkEffortFilterLeftJoinGenovaNew() {
        if (isFullAdmin) {
            return "";
        }
        return filterLeftJoinGenovaNew;
    }


    /**
     * Ritorna la query utilizzata per i filtri in where solo se non sono full admin e in base al tipo di permesso
     * @return
     */
    public String getWorkEffortFilterWhere() {
        if (isFullAdmin) {
            return "";
        }

        String where = filterUserLogin + "'" + userLoginId +"'";
        where += " AND (1 = 0 " ;
        String permission = permissionService.permissionLocalDispatcherName(localDispatcherName);
        ContextPermissionPrefixEnum context = ContextPermissionPrefixEnum.getByPermissionPrefix(permission);
        if (permissionService.isOrgMgr(userLoginId, context)) {
            where += filterWhereIsOrg;
        }

        if (permissionService.isRole(userLoginId, context)) {
            where += filterWhereIsRole;
        }

        if (permissionService.isSup(userLoginId, context)) {
            where += filterWhereIsSup;
        }

        if (permissionService.isTop(userLoginId, context)) {
            where += filterWhereIsTop;
        }
        where +=  ") ";

        return  where;
    }

   // TODO Solo legacy
    public boolean hasUserProfile(String userLoginId, String localDispatcherName, String permissionAdmin) {
        String permission = permissionService.permissionLocalDispatcherName(localDispatcherName);
        return permissionService.hasPermission(permission + permissionAdmin, userLoginId);
    }


}
