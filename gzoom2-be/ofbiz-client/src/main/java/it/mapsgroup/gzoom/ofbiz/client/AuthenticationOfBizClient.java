package it.mapsgroup.gzoom.ofbiz.client;

import java.util.Map;
import java.util.Locale;

public interface AuthenticationOfBizClient {

    /**
     * Login method.
     * Return a token that will can be used by next request.
     *
     * @param username
     * @param password
     * @param loginSourceId {@link it.memelabs.gn.services.login.LoginSourceOfbiz} values
     * @return <ul>
     * <li>{@link String} sessionId</li>
     * <li>{@link Map} companyProfile (containing entries: rootPublicationEnabled, privateAuthorizationEnabled, privateAuthCheckEnabled)</li>
     * </ul>
     */
    public abstract Map<String, Object> login(String username, String password, String loginSourceId);



    /**
     * Login method.
     * Return a token that will can be used by next request.
     *
     * @param username
     * @param loginSourceId {@link it.memelabs.gn.services.login.LoginSourceOfbiz} values
     * @return <ul>
     * <li>{@link String} sessionId</li>
     * <li>{@link Map} companyProfile (containing entries: rootPublicationEnabled, privateAuthorizationEnabled, privateAuthCheckEnabled)</li>
     * </ul>
     */
    public abstract Map<String, Object> login(String username, String loginSourceId);


    /**
     * Login method.
     * Return a token that will can be used by next request.
     *
     * @param username
     * @param password
     * @param loginSourceId {@link it.memelabs.gn.services.login.LoginSourceOfbiz} values
     * @param deviceId-     device ID
     * @return <ul>
     * <li>{@link String} sessionId</li>
     * <li>{@link Map} companyProfile (containing entries: rootPublicationEnabled, privateAuthorizationEnabled, privateAuthCheckEnabled)</li>
     * </ul>
     */
    public abstract Map<String, Object> login(String username, String password, String loginSourceId, String deviceId, String deviceDescription, String appId, String appVersion);


    public abstract Map<String, Object> getSessionContext(Map<String, Object> session, String sessionId);

    /**
     * Logout method.
     *
     * @param username
     * @return
     */
    public abstract Map<String, Object> logout(String username, String logoutSourceId);


    public abstract Map<String, Object> rollbackDb();
    
    
   /**
    * Change password
    * @param username
    * @param password
    * @param newPassword
    * @return
    */
    public abstract Map<String, Object> changePassword(String sessionId, String username, String password, String newPassword, String locale);

    /**
     * Change Locale Session
     * @param externalLoginKey
     * @param locale
     * @return
     */
     public abstract Map<String, Object> changeSessionLocale(String externalLoginKey, String username, String locale);
}
