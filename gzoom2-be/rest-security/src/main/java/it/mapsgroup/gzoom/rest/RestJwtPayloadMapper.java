package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.mybatis.dto.UserLogin;
import it.mapsgroup.gzoom.security.JwtPayloadMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fabio G. Strozzi
 */
@Component
public class RestJwtPayloadMapper implements JwtPayloadMapper {
    private static final String PAYLOAD_ID = "id";
    private static final String PAYLOAD_USERNAME = "username";
    private static final String PAYLOAD_FIRST_NAME = "firstName";
    private static final String PAYLOAD_LAST_NAME = "lastName";
    private static final String PAYLOAD_AUTH_TYPE = "authenticationType";
    private static final String PAYLOAD_PERMISSIONS = "permissions";
    private static final String PAYLOAD_NODES = "nodes";
    private static final String PAYLOAD_DESCRIPTION = "description";
    private static final String PAYLOAD_ACL_PERMISSIONS = "aclPermissions";
    private static final String PAYLOAD_ACTIVE_NODE_ID = "activeNodeId";
    private static final String PAYLOAD_SESSION_ID = "sessionId";
    private static final String PAYLOAD_EXTERNAL_LOGIN_KEY = "externalLoginKey";
    
    @Override
    public Map<String, Object> map(UserLogin user) {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put(PAYLOAD_ID, user.getUserLoginId());
        payload.put(PAYLOAD_USERNAME, user.getUsername());
        payload.put(PAYLOAD_FIRST_NAME, user.getPerson().getFirstName());
        payload.put(PAYLOAD_LAST_NAME, user.getPerson().getLastName());
        // TODO potrebbe servire per ldap? payload.put(PAYLOAD_AUTH_TYPE, user.getAuthenticationType().toString());
        payload.put(PAYLOAD_PERMISSIONS, 1); //maskAll(fromList(user.getPermissions())));
        // TODO potrebbe servire per payload.put(PAYLOAD_NODES, mapNodes(user.getNodes()));
        // TODO potrebbe servire per payload.put(PAYLOAD_ACTIVE_NODE_ID, mapNodeId(user));
        payload.put(PAYLOAD_EXTERNAL_LOGIN_KEY, user.getExternalLoginKey());
        return payload;
    }


    @Override
    public String getUsername(Map<String, Object> payload) {
        return (String) payload.get(PAYLOAD_USERNAME);
    }

    @Override
    public Long getActiveNodeId(Map<String, Object> payload) {
        return (Long) payload.get(PAYLOAD_ACTIVE_NODE_ID);
    }


}
