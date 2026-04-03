package it.mapsgroup.gzoom.ofbiz.service;

import it.mapsgroup.gzoom.ofbiz.client.AuthenticationOfBizClient;

import java.util.Map;
import java.util.Locale;

/**
 * @author 
 */
public class ChangePasswordServiceOfBiz {

    private final AuthenticationOfBizClient loginClient;

    public ChangePasswordServiceOfBiz(AuthenticationOfBizClient loginClient) {
        this.loginClient = loginClient;
    }

    public Map<String, Object> changePassword(String sessionId, String username, String password, String newPassword, String locale) {
    	return loginClient.changePassword(sessionId, username, password, newPassword, locale);
    }

    public Map<String, Object> changeSessionLocale(String externalLoginKey,String username, String locale) {
        return loginClient.changeSessionLocale(externalLoginKey,username,locale);
    }

}
