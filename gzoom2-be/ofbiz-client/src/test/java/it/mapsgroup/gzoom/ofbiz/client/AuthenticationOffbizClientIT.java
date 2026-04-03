package it.mapsgroup.gzoom.ofbiz.client;

import it.mapsgroup.gzoom.ofbiz.client.impl.AuthenticationOfBizClientImpl;
import it.mapsgroup.gzoom.ofbiz.client.impl.GnPingOfBizClientImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class AuthenticationOffbizClientIT extends AbstractOfBizTest {

    private AuthenticationOfBizClient loginClient;

    @Before
    public void setUp() throws Exception {
        loginClient = new AuthenticationOfBizClientImpl(config, connectionManager);
    }

    @Test
    public void testLoginNoPwd() {
        Map<String, Object> result = loginClient.login("gzoom.rodo","foo");
        assertNotNull(result.get("externalLoginKey"));
    }
}
