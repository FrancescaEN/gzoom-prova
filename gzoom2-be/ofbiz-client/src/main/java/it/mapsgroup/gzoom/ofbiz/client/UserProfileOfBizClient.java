package it.mapsgroup.gzoom.ofbiz.client;

import java.util.Map;

public interface UserProfileOfBizClient {

    public Map<String, Object> getUserProfile(Map<String, Object> paramMap, String sessionId, String contextId);

    public Map<String, Object> findCompanies(Map<String, Object> paramMap, String sessionId, String contextId);

    public Map<String, Object> findUsersByEmployingCompanyId(Map<String, Object> paramMap, String sessionId, String contextId);

    public Map<String, Object> findCompanyBases(Map<String, Object> paramMap, String sessionId, String contextId);

    /**
     * Input params:
     * <ul>
     * <li>{@link String} entityId</li>
     * <li>{@link String} entityTypeId</li>
     * <li>{@link String} permissionId</li>
     * </ul>
     * Output params:
     * <ul>
     * <li>{@link String} partyNodeKey (context node id)</li>
     * </ul>
     * <p/>
     *
     * @param paramMap
     * @param sessionId
     * @param contextId
     * @return
     */
    public Map<String, Object> hasPermission(Map<String, Object> paramMap, String sessionId, String contextId);
}
