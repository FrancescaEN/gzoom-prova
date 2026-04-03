package it.mapsgroup.gzoom.ofbiz.client.impl;

import it.mapsgroup.gzoom.ofbiz.client.CatalogOfBizClient;
import it.mapsgroup.gzoom.ofbiz.client.OfBizClientConfig;
import it.mapsgroup.gzoom.ofbiz.client.OfBizClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class CatalogOfBizClientImpl extends OfBizClient implements CatalogOfBizClient {

    private static final Logger log = LoggerFactory.getLogger(CatalogOfBizClientImpl.class);

    public CatalogOfBizClientImpl(OfBizClientConfig config, HttpConnectionManager connectionManager) {
        super(config, connectionManager);
    }

    public CatalogOfBizClientImpl(OfBizClientConfig config) {
        super(config);
    }

    @Override
    public Map<String, Object> getRoles(Map<String, Object> paramMap, String sessionId, String contextId) {
        Map<String, Object> result = execute("gnFindSecurityGroups", sessionId, contextId, paramMap);
        return result;
    }

    @Override
    public Map<String, Object> getWastes(Map<String, Object> paramMap, String sessionId, String contextId) {
        Map<String, Object> result = execute("gnFindWasteCerCodes", sessionId, contextId, paramMap);
        return result;
    }

    @Override
    public Map<String, Object> getOperations(Map<String, Object> paramMap, String sessionId, String contextId) {
        Map<String, Object> result = execute("gnFindOperations", sessionId, contextId, paramMap);
        return result;
    }

    @Override
    public Map<String, Object> getPackagings(Map<String, Object> paramMap, String sessionId, String contextId) {
        Map<String, Object> result = execute("gnFindPackaging", sessionId, contextId, paramMap);
        return result;
    }

    @Override
    public Map<String, Object> getIssuers(Map<String, Object> paramMap, String sessionId, String contextId) {
        Map<String, Object> result = execute("gnFindIssuers", sessionId, contextId, paramMap);
        return result;
    }

    @Override
    public Map<String, Object> getCountries(Map<String, Object> paramMap, String sessionId, String contextId) {
        Map<String, Object> result = execute("gnFindCountries", sessionId, contextId, paramMap);
        return result;
    }

    @Override
    public Map<String, Object> getCategories(Map<String, Object> paramMap, String sessionId, String contextId) {
        Map<String, Object> result = execute("gnFindCategoriesAndClasses", sessionId, contextId, paramMap);
        return result;

    }
}
