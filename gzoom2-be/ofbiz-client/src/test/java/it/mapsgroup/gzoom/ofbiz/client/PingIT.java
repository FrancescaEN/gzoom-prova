// PingTest.java, created on 05/dic/2012
package it.mapsgroup.gzoom.ofbiz.client;

import it.mapsgroup.gzoom.ofbiz.client.impl.PingOfBizClientImpl;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the ping client.
 * <p/>
 * <em>Beware OfBiz has to be up and running before starting this test.</em>
 *
 * @author Fabio Strozzi
 */
public class PingIT {
    private static Properties props = new Properties();
    private static OfBizClientConfig config;
    private PingOfBizClient client;

    @BeforeClass
    public static void setUpOnce() throws IOException {
        // configure client properties
        props.load(PingIT.class.getResourceAsStream("/client.properties"));
        config = new OfbizClientConfigImpl(props);
    }

    @Before
    public void setUp() {
        client = new PingOfBizClientImpl(config, new SimpleHttpConnectionManager(true));
    }

    @Test
    public void testPing() {
        String reply = client.ping("Blah blah blahhhh!!!");
        assertThat("ping echos the message", reply, is("Blah blah blahhhh!!!"));
    }
}
