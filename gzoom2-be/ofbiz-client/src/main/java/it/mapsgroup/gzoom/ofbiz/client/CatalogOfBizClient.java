package it.mapsgroup.gzoom.ofbiz.client;

import java.util.Map;

public interface CatalogOfBizClient {

    public Map<String, Object> getRoles(Map<String, Object> paramMap, String sessionId, String contextId);

    public Map<String, Object> getWastes(Map<String, Object> paramMap, String sessionId, String contextId);

    public Map<String, Object> getOperations(Map<String, Object> paramMap, String sessionId, String contextId);

    public Map<String, Object> getPackagings(Map<String, Object> paramMap, String sessionId, String contextId);

    public Map<String, Object> getIssuers(Map<String, Object> paramMap, String sessionId, String contextId);

    public Map<String, Object> getCountries(Map<String, Object> paramMap, String sessionId, String contextId);

    public Map<String, Object> getCategories(Map<String, Object> paramMap, String sessionId, String contextId);

}
