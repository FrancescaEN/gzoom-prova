package it.mapsgroup.gzoom.security.controllers;

import it.mapsgroup.gzoom.mybatis.dao.UserLoginDao;
import it.mapsgroup.gzoom.mybatis.dto.Person;
import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import it.mapsgroup.gzoom.ofbiz.service.LoginResponseOfBiz;
import it.mapsgroup.gzoom.ofbiz.service.LoginServiceOfBiz;
import it.mapsgroup.gzoom.security.JwtOfBizLoginAuthenticationProvider;
import it.mapsgroup.gzoom.security.JwtService;
import it.mapsgroup.gzoom.security.PermitsStorage;
import it.mapsgroup.gzoom.security.dto.models.AuthRequest;
import it.mapsgroup.gzoom.security.dto.models.ExternalLoginKey;
import it.mapsgroup.gzoom.security.dto.models.TokenDto;
import it.mapsgroup.gzoom.security.model.JwtAuthentication;
import it.mapsgroup.gzoom.security.model.KeycloakConfig;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value="/api", produces= MediaType.APPLICATION_JSON_VALUE)
public class AuthController {
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);
    public static final String SECURITY_ENABLE_CHANGE_PASSWORD = "security.enableChangePassword";
    public static final String OFBIZ_SERVER_SSO_ONELOGIN_LOGOUT_URL = "ofbiz.server.sso.onelogin.logout.url";
    public static final String OFBIZ_SERVER_SSO_ONELOGIN_LOGIN_URL = "ofbiz.server.sso.onelogin.login.url";
    public static final String OFBIZ_SERVER_SSO_ONELOGIN_ENABLE = "ofbiz.server.sso.onelogin.enable";
    public static final String KEYCLOAK_LOGIN_ENABLE = "keycloak.login.enable";
    public static final String KEYCLOAK_URL = "keycloak.url";
    public static final String KEYCLOAK_REALM = "keycloak.realm";
    public static final String KEYCLOAK_CLIENT_ID = "keycloak.client.id";
    public static final String GZOOM_NATIVE_LOGIN = "GzoomNativeLogin";
    public static final String KEYCLOAK_LOGIN = "GzoomKeycloakLogin";
    public static final String ONE_LOGIN = "OneLogin";
    public static final String OFBIZ_SERVER_SSO_SIRAC_LOGOUT_USERNAME_HEADER_NAME = "ofbiz.server.sso.sirac.logout.username.header.name";
    public static final String GZOOM_2_APIKEY = "gzoom2apikey";
    public static final String OFBIZ_SERVER_XMLRPC_APIKEY = "ofbiz.server.xmlrpc.apikey";
    private final JwtService jwtService;
    private final LoginServiceOfBiz loginService;
    private final UserLoginDao userLoginDao;
    private final PermitsStorage permitsStorage;
    private final Environment env;

    @Autowired
    public AuthController(JwtOfBizLoginAuthenticationProvider jwtOfBizLoginAuthenticationProvider, JwtService jwtService, LoginServiceOfBiz loginService, UserLoginDao userLoginDao, PermitsStorage permitsStorage, Environment env){
        super();
        this.jwtService = jwtService;
        this.loginService = loginService;
        this.userLoginDao = userLoginDao;
        this.permitsStorage = permitsStorage;
        this.env =env;
    }


    @RequestMapping(value = "/getEnableChangePassword", method = RequestMethod.GET)
    public boolean getEnableChangePassword() {
        return this.env.getProperty(SECURITY_ENABLE_CHANGE_PASSWORD, Boolean.class, true);
    }


    @RequestMapping(value = "/getOneLogin-LogoutUrl", method = RequestMethod.GET)
    public String getOneLoginLogoutUrl() {
        String oneLoginSSOLogoutUrl = this.env.getProperty(OFBIZ_SERVER_SSO_ONELOGIN_LOGOUT_URL);
        if(oneLoginSSOLogoutUrl != null){
            return oneLoginSSOLogoutUrl;
        }
        else{
            LOG.error("no oneLoginSSOLogoutUrlPropertyName configuration found");
            return null;
        }
    }


    @RequestMapping(value = "/getOneLogin-LoginUrl", method = RequestMethod.GET)
    public String getOneLoginLoginUrl() {
        String oneLoginSSOLoginUrl = this.env.getProperty(OFBIZ_SERVER_SSO_ONELOGIN_LOGIN_URL);
        if(oneLoginSSOLoginUrl != null){
            return oneLoginSSOLoginUrl;
        }
        else{
            LOG.error("no oneLoginSSOLoginUrlPropertyName configuration found");
            return null;
        }
    }



    @RequestMapping(value = "/getLoginMethod", method = RequestMethod.GET)
    public String getLoginMethod() {
        String oneLoginSSOEnabled = this.env.getProperty(OFBIZ_SERVER_SSO_ONELOGIN_ENABLE);
        String keycloakLoginEnabled = this.env.getProperty(KEYCLOAK_LOGIN_ENABLE);

        if (keycloakLoginEnabled == null && oneLoginSSOEnabled == null) {
            LOG.error("no oneLoginSSOEnabledPropertyName and keycloakLoginEnabledPropertyName configuration found");
            return GZOOM_NATIVE_LOGIN;
        }

        if (keycloakLoginEnabled != null && keycloakLoginEnabled.equalsIgnoreCase("true")) {
            return KEYCLOAK_LOGIN;
        }

        if (oneLoginSSOEnabled != null && oneLoginSSOEnabled.equalsIgnoreCase("true")) {
            return ONE_LOGIN;
        }

        return GZOOM_NATIVE_LOGIN;
    }

    @RequestMapping(value = "/keycloak-config", method = RequestMethod.GET)
    public KeycloakConfig getKeycloakConfig() {
        String keycloakUrl = this.env.getProperty(KEYCLOAK_URL);
        String keycloakRealm = this.env.getProperty(KEYCLOAK_REALM);
        String keycloakClientId = this.env.getProperty(KEYCLOAK_CLIENT_ID);

        if(keycloakUrl != null && keycloakRealm != null && keycloakClientId != null ){
            return new KeycloakConfig(keycloakUrl, keycloakRealm, keycloakClientId);
        }
        else{
            LOG.error("no Keycloak configuration found (keycloak.url, keycloak.realm, keycloak.client.id)");
            return null;
        }

    }

    /*
        API utilizzata per eseguire il logout richiesto da un altro SP, ovvero SSO Global Logout
        lo username di cui effettuare il logout deve essere passato nell'header con nome configurato
        nella property :
     */
    @RequestMapping(value = "/doLogout", method = RequestMethod.POST)
    public String doLogout(@RequestHeader MultiValueMap<String, String> headers) {
        String err = null;
        String logoutUsernameHeader = this.env.getProperty(OFBIZ_SERVER_SSO_SIRAC_LOGOUT_USERNAME_HEADER_NAME);
        if(logoutUsernameHeader != null && !logoutUsernameHeader.isEmpty()){
            if(headers != null && !headers.isEmpty() && headers.getFirst(logoutUsernameHeader) != null){
                String userName = headers.getFirst(logoutUsernameHeader);
                if(userName != null && !userName.isEmpty()){
                    loginService.logout(userName);
                    return "Logout succeed";
                }
                else{
                    err = "no username found on request header " + logoutUsernameHeader;
                    LOG.error(err);
                }
            }
            else{
                err = "no request headers found or not request header found with name " + logoutUsernameHeader;
                LOG.error(err);
            }
        }
        else{
            err = "no value found for configuration property " + OFBIZ_SERVER_SSO_SIRAC_LOGOUT_USERNAME_HEADER_NAME;
            LOG.error(err);
        }
        return "Logout not succeed. " + err;
    }


    @PostMapping(value = "/getExternalKey")
    public ExternalLoginKey getExternalKey(@RequestBody AuthRequest credentials, @RequestHeader MultiValueMap<String, String> headers) {
        if(headers != null){
            String requestApiKey = headers.getFirst(GZOOM_2_APIKEY);
            if(requestApiKey != null){
                String confApiKey = this.env.getProperty(OFBIZ_SERVER_XMLRPC_APIKEY);
                if(confApiKey != null){
                    if(requestApiKey.equals(confApiKey)){
                        LoginResponseOfBiz response = loginService.loginWithOnlyUserLoginId(credentials.uid);
                        return new ExternalLoginKey(response.getExternalLoginKey());
                    } else {
                        LOG.error("wrong apikey found");
                        return null;
                    }
                } else {
                    LOG.error("no apikey configuration found");
                    return null;
                }
            } else {
                LOG.error("no apikey header found");
                return null;
            }
        } else {
            LOG.error("no headers found");
            return null;
        }
    }



    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    public TokenDto getToken(@RequestBody AuthRequest credentials,@RequestHeader MultiValueMap<String, String> headers) {
        if(headers != null){
            String requestApiKey = headers.getFirst(GZOOM_2_APIKEY);
            if(requestApiKey != null){
                String confApiKey = this.env.getProperty(OFBIZ_SERVER_XMLRPC_APIKEY);
                if(confApiKey != null){
                    if(requestApiKey.equals(confApiKey)){
                        UserLogin principal = new UserLogin();
                        principal.setUserLoginId(credentials.uid);
                        UserLogin profile = userLoginDao.getUserLogin(principal.getUserLoginId());
                        LoginResponseOfBiz response = loginService.loginWithOnlyUserLoginId(credentials.uid);
                        Person person = new Person();
                        person.setFirstName(response.getFirstName());
                        person.setLastName(response.getLastName());
                        principal.setPerson(person);
                        profile.setExternalLoginKey(response.getExternalLoginKey());
                        String token =null;
                        try {
                            token = jwtService.generate(profile);
                        } catch (JoseException e) {
                            e.printStackTrace();
                        }
                        permitsStorage.save(token, profile.getUsername());
                        JwtAuthentication auth = new JwtAuthentication(token, profile);
                        LOG.info("Authenticated with username : {}", credentials.uid);
                        return new TokenDto(token);
                    }
                    else{
                        LOG.error("wrong apikey found");
                        return null;
                    }
                }
                else{
                    LOG.error("no apikey configuration found");
                    return null;
                }
            }
            else{
                LOG.error("no apikey header found");
                return null;
            }
        }
        else{
            LOG.error("no headers found");
            return null;
        }
    }
}

