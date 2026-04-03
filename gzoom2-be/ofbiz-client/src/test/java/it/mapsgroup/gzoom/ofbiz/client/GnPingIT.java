// Ping2Test.java, created on 14/dic/2012
package it.mapsgroup.gzoom.ofbiz.client;

import it.memelabs.gn.services.login.LoginSourceOfbiz;
import it.mapsgroup.gzoom.ofbiz.client.impl.AuthenticationOfBizClientImpl;
import it.mapsgroup.gzoom.ofbiz.client.impl.GnPingOfBizClientImpl;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Callable;

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
public class GnPingIT extends AbstractOfBizTest {

    private static final Logger log = LoggerFactory
            .getLogger(GnPingIT.class);

    private GnPingOfBizClient pingClient;
    private AuthenticationOfBizClient loginClient;

    @Before
    public void setUp() throws Exception {
        pingClient = new GnPingOfBizClientImpl(config, connectionManager);
        loginClient = new AuthenticationOfBizClientImpl(config, connectionManager);
    }

    /**
     * Test Company creation
     */
    @Ignore
    @Test
    @SuppressWarnings("unchecked")
    public void testPingMulti() {
        final Integer[] count = {0};
        Callable task = new Callable() {

            @Override
            public Object call() throws Exception {
                //wait one second between each test
              /*  synchronized (count) {
                    count[0] = count[0] + 1;
                }
                Thread.sleep(count[0]  * 5000);*/
                //Thread.sleep(Math.round(100 * Math.random()));
                try {
                    for (int i = 0; i < 100; i++) {
                        testPing2();
                        Thread.sleep(300);
                    }
                } catch (Exception e) {
                    log.error("error on thread", e);
                }
                return null;
            }
        };
        executeMultiThreadedTest(10, task);
    }

    @Ignore
    @Test
    public void testPing2_100() {
        for (int i = 0; i < 100; i++) {
            testPing2();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testPing() {
        Map<String, Object> response = loginClient.login("gnadmin", "ofbiz", LoginSourceOfbiz.GN_LOG_SRC_WEB_UI.name());
        String sessionId = (String) response.get("sessionId");
        assertThat("returned sessionId", sessionId, is(not(nullValue())));
        String reply = pingClient.ping(sessionId, "Blah blah blahhhh!!!");
        assertThat("ping echos the message", reply, is("Blah blah blahhhh!!!"));

        //String logoutReply = loginClient.logout(sessionId);
        //assertThat("logout message", logoutReply, is("success"));

        try {
            reply = pingClient.ping(sessionId, "Blah blah blahhhh!!!");
            assertTrue("service doesn't return an exception [" + reply + "]", false);
        } catch (OfBizClientException e) {
            System.out.println("Catch of OfBizClientException: logout succeded.");
            assertThat("ping echos the message", e.getMessage(), containsString("User authorization is required for this service: gnPing"));
        }

    }

    @Test
    public void testPing2() {
        Map<String, Object> response = loginClient.login("gnadmin", "ofbiz", LoginSourceOfbiz.GN_LOG_SRC_WEB_UI.name());
        String sessionId = (String) response.get("sessionId");
        assertThat("returned sessionId", sessionId, is(not(nullValue())));

        String reply = pingClient.ping(sessionId, "Blah blah blahhhh!!!");
        assertThat("ping echos the message", reply, is("Blah blah blahhhh!!!"));


      /*  for (int i = 0; i < 200; i++) {
            String reply = pingClient.ping(sessionId, "Blah blah blahhhh!!!");
            assertThat("ping echos the message", reply, is("Blah blah blahhhh!!!"));

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

        //String logoutReply = loginClient.logout(sessionId);
        //assertThat("logout message", logoutReply, is("success"));
    }
}
