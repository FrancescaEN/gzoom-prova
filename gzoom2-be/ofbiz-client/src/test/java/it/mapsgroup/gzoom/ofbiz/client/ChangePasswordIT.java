package it.mapsgroup.gzoom.ofbiz.client;

import it.mapsgroup.gzoom.ofbiz.client.impl.AuthenticationOfBizClientImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Map;

/**
 * Tests the ping client.
 * <p/>
 * <em>Beware OfBiz has to be up and running before starting this test.</em>
 *
 * @author Assuntina Magnante
 */
public class ChangePasswordIT extends AbstractOfBizTest {

    private static final Logger log = LoggerFactory
            .getLogger(ChangePasswordIT.class);


    private AuthenticationOfBizClient loginClient;

    @Before
    public void setUp() throws Exception {
        loginClient = new AuthenticationOfBizClientImpl(config, connectionManager);
    }

    @Test
    public void testChangePassword() {      
    	//Map<String, Object> responseSession = loginClient.login("admin", "MapsGzoom01", null);
        String sessionId = "";//;(String) responseSession.get("sessionId");
        
        Map<String, Object> response = loginClient.changePassword(sessionId, "admin", "MapsGzoom01", "MapsGzoom02", Locale.ITALIAN.toString());
        log.info("testChangePassword "+ response);
        
       /* try {
            reply = pingClient.ping(sessionId, "Blah blah blahhhh!!!");
            assertTrue("service doesn't return an exception [" + reply + "]", false);
        } catch (OfBizClientException e) {
            System.out.println("Catch of OfBizClientException: logout succeded.");
            assertThat("ping echos the message", e.getMessage(), containsString("User authorization is required for this service: gnPing"));
        }*/

    }
}
