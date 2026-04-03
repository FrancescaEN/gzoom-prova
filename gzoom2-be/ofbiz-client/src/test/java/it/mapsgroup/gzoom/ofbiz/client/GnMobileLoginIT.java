package it.mapsgroup.gzoom.ofbiz.client;

import it.mapsgroup.gzoom.ofbiz.client.impl.AuthenticationOfBizClientImpl;
import it.mapsgroup.gzoom.ofbiz.client.impl.GnPingOfBizClientImpl;
import it.memelabs.gn.services.OfbizErrors;
import it.memelabs.gn.services.login.LoginSourceOfbiz;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests the ping client.
 * <p/>
 * <em>Beware OfBiz has to be up and running before starting this test.</em>
 *
 * @author Andrea Fossi
 */
public class GnMobileLoginIT extends AbstractOfBizTest {

    private static final Logger log = LoggerFactory
            .getLogger(GnMobileLoginIT.class);

    private GnPingOfBizClient pingClient;
    private AuthenticationOfBizClient loginClient;

    @Before
    public void setUp() throws Exception {
        pingClient = new GnPingOfBizClientImpl(config, connectionManager);
        loginClient = new AuthenticationOfBizClientImpl(config, connectionManager);
    }


    @Test
    public void testPing() {
        String deviceId = UUID.randomUUID().toString();
        //Map<String, Object> response = loginClient.login("admin", "MapsGzoom01", LoginSourceOfbiz.GN_LOG_SRC_MOBILE.name(), deviceId, "FOX_MOBILE", "UNIT_TEST", "V1");
        Map<String, Object> response = loginClient.login("admin", "MapsGzoom01", null);
        String sessionId = (String) response.get("sessionId");
        assertThat("returned sessionId", sessionId, is(not(nullValue())));
        // String reply = pingClient.ping(sessionId, "Blah blah blahhhh!!!");
        // assertThat("ping echos the message", reply, is("Blah blah blahhhh!!!"));

        //String logoutReply = loginClient.logout(sessionId);
        //assertThat("logout message", logoutReply, is("success"));

        Map<String, Object> response3 = loginClient.login("user1", "ofbiz", LoginSourceOfbiz.GN_LOG_SRC_MOBILE.name(), deviceId, "FOX_MOBILE", "UNIT_TEST", "V1");
        String sessionId3 = (String) response3.get("sessionId");
        assertThat("returned sessionId", sessionId3, is(not(nullValue())));
        // String reply = pingClient.ping(sessionId, "Blah blah blahhhh!!!");
        // assertThat("ping echos the message", reply, is("Blah blah blahhhh!!!"));

        //String logoutReply3 = loginClient.logout(sessionId3);
        //assertThat("logout message", logoutReply3, is("success"));

        String deviceId2 = UUID.randomUUID().toString();

        Map<String, Object> response2 = loginClient.login("user1", "ofbiz", LoginSourceOfbiz.GN_LOG_SRC_MOBILE.name(), deviceId2, "FOX_MOBILE", "UNIT_TEST", "V1");
        String sessionId2 = (String) response2.get("sessionId");
        assertThat("returned sessionId", sessionId2, is(not(nullValue())));
        // String reply = pingClient.ping(sessionId, "Blah blah blahhhh!!!");
        // assertThat("ping echos the message", reply, is("Blah blah blahhhh!!!"));

        //String logoutReply2 = loginClient.logout(sessionId2);
        try {
            Map<String, Object> response4 = loginClient.login("user1", "ofbiz", LoginSourceOfbiz.GN_LOG_SRC_MOBILE.name(), deviceId, "FOX_MOBILE", "UNIT_TEST", "V1");
            assertTrue(false);
        } catch (OfBizClientException e) {

            assertThat("device is invalid", e.getCode(), is(OfbizErrors.EXPIRED_DEVICE.ordinal()));
        }

       /* try {
            reply = pingClient.ping(sessionId, "Blah blah blahhhh!!!");
            assertTrue("service doesn't return an exception [" + reply + "]", false);
        } catch (OfBizClientException e) {
            System.out.println("Catch of OfBizClientException: logout succeded.");
            assertThat("ping echos the message", e.getMessage(), containsString("User authorization is required for this service: gnPing"));
        }*/

    }


}
