package it.mapsgroup.gzoom.ofbiz.service;

import it.mapsgroup.gzoom.ofbiz.client.AuthenticationOfBizClient;

import java.util.Map;

/**
 * @author Andrea Fossi.
 */
public class LoginServiceOfBiz {

    private final AuthenticationOfBizClient loginClient;

    public LoginServiceOfBiz(AuthenticationOfBizClient loginClient) {
        this.loginClient = loginClient;
    }

    public LoginResponseOfBiz login(String username, String password) {
        Map<String, Object> response = loginClient.login(username, password, null);
        String externalLoginKey = (String) response.get("externalLoginKey");
        String firstName = (String) response.get("firstName");
        String lastName = (String) response.get("lastName");
        LoginResponseOfBiz loginResponse = new LoginResponseOfBiz();
        loginResponse.setExternalLoginKey(externalLoginKey);
        loginResponse.setFirstName(firstName);
        loginResponse.setLastName(lastName);
        return loginResponse;
    }


    public LoginResponseOfBiz loginWithOnlyUserLoginId(String username) {
        Map<String, Object> response = loginClient.login(username, null);
        String externalLoginKey = (String) response.get("externalLoginKey");
        String firstName = (String) response.get("firstName");
        String lastName = (String) response.get("lastName");
        LoginResponseOfBiz loginResponse = new LoginResponseOfBiz();
        loginResponse.setExternalLoginKey(externalLoginKey);
        loginResponse.setFirstName(firstName);
        loginResponse.setLastName(lastName);
        return loginResponse;
    }


    public LoginResponseOfBiz logout(String username) {
        Map<String, Object> response = loginClient.logout(username, null);
        String externalLoginKey = (String) response.get("externalLoginKey");
        String firstName = (String) response.get("firstName");
        String lastName = (String) response.get("lastName");
        LoginResponseOfBiz loginResponse = new LoginResponseOfBiz();
        loginResponse.setExternalLoginKey(externalLoginKey);
        loginResponse.setFirstName(firstName);
        loginResponse.setLastName(lastName);
        return loginResponse;
    }
}
