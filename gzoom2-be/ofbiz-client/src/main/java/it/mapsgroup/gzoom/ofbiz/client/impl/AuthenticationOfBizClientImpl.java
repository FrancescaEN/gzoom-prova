// LoginOfBizClient.java, created on 14/dic/2012
package it.mapsgroup.gzoom.ofbiz.client.impl;

import it.mapsgroup.gzoom.ofbiz.client.AuthenticationOfBizClient;
import it.mapsgroup.gzoom.ofbiz.client.OfBizClient;
import it.mapsgroup.gzoom.ofbiz.client.OfBizClientConfig;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * OfBiz login service client.
 *
 * @author Andrea Fossi
 */
public class AuthenticationOfBizClientImpl extends OfBizClient implements AuthenticationOfBizClient {
    private static final Logger log = LoggerFactory
            .getLogger(AuthenticationOfBizClientImpl.class);

    /**
     * @param config
     * @param connectionManager
     */
    public AuthenticationOfBizClientImpl(OfBizClientConfig config, HttpConnectionManager connectionManager) {
        super(config, connectionManager);
    }

    public AuthenticationOfBizClientImpl(OfBizClientConfig config) {
        super(config);
    }

    @Override
    public Map<String, Object> login(String username, String password, String loginSourceId) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("login.username", username);
        paramMap.put("login.password", password);
        //paramMap.put("loginSourceId", loginSourceId);
        Map<String, Object> result = execute("gzSimpleLogin", paramMap);
        String sessionId = (String) result.get("sessionId");
        log.debug("Login reply sessionId: {}", sessionId);
        return result;
    }




    @Override
    public Map<String, Object> login(String username, String loginSourceId) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("login.username", username);
        //paramMap.put("login.password", "regFrm24334dc7M");
        Map<String, Object> result = execute("gzSimpleLoginWithOnlyUserLoginId", paramMap);
        String sessionId = (String) result.get("sessionId");
        log.debug("Login reply sessionId: {}", sessionId);
        return result;
    }




    public Map<String, Object> login(String username, String password, String loginSourceId,
                                     String deviceId,String deviceDescription,String appId,String appVersion) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("login.username", username);
        paramMap.put("login.password", password);
        
        Map<String, Object> result = execute("gzSimpleLogin", paramMap);
        String sessionId = (String) result.get("sessionId");
        log.debug("Login reply sessionId: {}", sessionId);
        return result;
    }


    /**
     * Logout method.
     *
     * @param username
     * @return
     */
    @Override
    public Map<String, Object> logout(String username, String logoutSourceId) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("login.username", username);
        Map<String, Object> result = execute("gzLogout", username, paramMap);
        //String resp = (String) result.get("result");
        //log.debug("Logout  reply message: {}", resp);
        //return resp;
        return result;
    }


    /**
     * Get paramMap context
     *
     * @param paramMap
     * @param sessionId
     * @return
     */
    @Override
    public Map<String, Object> getSessionContext(Map<String, Object> paramMap, String sessionId) {
        Map<String, Object> result = execute("gnFindContextById", sessionId, paramMap);
        String resp = (String) result.get("result");
        log.debug("Logout  reply message: {}", resp);
        return result;
    }

    public Map<String, Object> rollbackDb() {
        
        return null;
    }

    
    public Map<String, Object> changePassword(String sessionId, String username, String password, String newPassword, String locale) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("login.username", username);
        paramMap.put("login.password", password);
        
        paramMap.put("userLoginId", username);
        paramMap.put("currentPassword", password);
        paramMap.put("newPassword", newPassword);
        paramMap.put("newPasswordVerify", newPassword);
        
        paramMap.put("sessionId", sessionId);
        paramMap.put("locale", locale);
        Map<String, Object> result = execute("gzChangePassword", sessionId, paramMap);
        return result;
    }

    public Map<String, Object> changeSessionLocale(String externalLoginKey, String username, String locale) {
        Map<String, String> paramMap = new HashMap<>();
        //paramMap.put("login.username", username);
        //paramMap.put("login.password", "MapsGzoom01"); //TODO - sostituire con password che è in sha, non funziona attualmente

        paramMap.put("externalLoginKey", externalLoginKey);
        paramMap.put("userLoginId",username);
        paramMap.put("newLocale", locale);

        Map<String,Object> result = execute("gzChangeSessionLocale", externalLoginKey, paramMap);
        return result;
    }

}
