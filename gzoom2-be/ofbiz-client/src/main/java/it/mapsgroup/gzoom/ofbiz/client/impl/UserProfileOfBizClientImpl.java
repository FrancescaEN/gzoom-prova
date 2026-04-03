package it.mapsgroup.gzoom.ofbiz.client.impl;

import it.mapsgroup.gzoom.ofbiz.client.OfBizClient;
import it.mapsgroup.gzoom.ofbiz.client.OfBizClientConfig;
import it.mapsgroup.gzoom.ofbiz.client.UserProfileOfBizClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class UserProfileOfBizClientImpl extends OfBizClient implements UserProfileOfBizClient {

    private static final Logger log = LoggerFactory.getLogger(UserProfileOfBizClientImpl.class);

    public UserProfileOfBizClientImpl(OfBizClientConfig config, HttpConnectionManager connectionManager) {
        super(config, connectionManager);
    }

    public UserProfileOfBizClientImpl(OfBizClientConfig config) {
        super(config);
    }

    @Override
    public Map<String, Object> getUserProfile(Map<String, Object> paramMap, String sessionId, String contextId) {
        Map<String, Object> result = execute("gnGetUserProfile", sessionId, contextId, paramMap);
        return result;
    }

    @Override
    public Map<String, Object> findCompanies(Map<String, Object> paramMap, String sessionId, String contextId) {
        Map<String, Object> result = execute("gnUserProfileFindCompanies", sessionId, contextId, paramMap);
        return result;
    }

    @Override
    public Map<String, Object> findUsersByEmployingCompanyId(Map<String, Object> paramMap, String sessionId, String contextId) {
        Map<String, Object> result = execute("gnUserProfileFindUsers", sessionId, contextId, paramMap);
        return result;
    }

    @Override
    public Map<String, Object> findCompanyBases(Map<String, Object> paramMap, String sessionId, String contextId) {
        Map<String, Object> result = execute("gnUserProfileFindCompanyBases", sessionId, contextId, paramMap);
        return result;
    }

    @Override
    public Map<String, Object> hasPermission(Map<String, Object> paramMap, String sessionId, String contextId) {
        Map<String, Object> result = execute("gnHasPermission", sessionId, contextId, paramMap);
        return result;
    }
}
