package it.mapsgroup.gzoom.ofbiz.client;

import com.thoughtworks.xstream.XStream;
import it.mapsgroup.gzoom.ofbiz.client.impl.AuthenticationOfBizClientImpl;
import it.memelabs.gn.services.login.LoginSourceOfbiz;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * 29/01/13
 *
 * @author Andrea Fossi
 */
public abstract class AbstractOfBizTest extends AbstractRollBackOfBizTest {
    protected static Properties props = new Properties();
    protected static OfBizClientConfig config;
    protected AuthenticationOfBizClient loginClient;

    protected MultiThreadedHttpConnectionManager connectionManager;


    private static final Logger log = LoggerFactory
            .getLogger(AbstractOfBizTest.class);


    @BeforeClass
    public static void setUpOnce() throws IOException {
        // configure client properties
        props.load(AbstractOfBizTest.class.getResourceAsStream("/client.properties"));
        config = new OfbizClientConfigImpl(props);
    }

    @Before
    public void setUpLoginClient() {
        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        params.setMaxTotalConnections(30);
        params.setDefaultMaxConnectionsPerHost(30);
        connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.setParams(params);
        loginClient = new AuthenticationOfBizClientImpl(config, connectionManager);
    }

    @After
    public void shutdown() {
        connectionManager.shutdown();
    }

    /**
     * login with default user credential
     *
     * @return
     */
    public String login() {
        return login("user1", "ofbiz", LoginSourceOfbiz.GN_LOG_SRC_WEB_UI.name());
    }

    /**
     * @param username
     * @param password
     * @return
     */
    public String login(String username, String password) {
        return login(username, password, LoginSourceOfbiz.GN_LOG_SRC_WEB_UI.name());
    }

    /**
     * @param username      username
     * @param password      pwd
     * @param loginSourceId see {@link LoginSourceOfbiz} values
     * @return sessionId
     */
    public String login(String username, String password, String loginSourceId) {
        Map<String, Object> response = loginClient.login(username, password, loginSourceId);
        String sessionId = (String) response.get("sessionId");
        assertThat("returned sessionId", sessionId, is(not(nullValue())));
        return sessionId;
    }

    public Object fromXml(String name) {
        InputStream r = Thread.currentThread().getClass().getResourceAsStream(name);
        XStream x = new XStream();
        return x.fromXML(r);
    }

    public String toXml(Object object) {
        //InputStream r = Thread.currentThread().getClass().getResourceAsStream(name);
        XStream x = new XStream();
        return x.toXML(object);
    }

    public void logOff(String sessionId) {
        //String logoutReply = loginClient.logout(sessionId);
        //assertThat("logout message", logoutReply, is("success"));
    }

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deepClone(T in) {
        XStream xStream = new XStream();
        return (T) xStream.fromXML(xStream.toXML(in));
    }

    protected <T> void executeMultiThreadedTest(int threadCount, Callable<T> task) {
        List<Callable<T>> tasks = Collections.nCopies(threadCount, task);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        try {
            List<Future<T>> futures = executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            log.error("multi thread execution error", e);
            assertTrue(false);
        }
    }
    /**

     @Test
     @SuppressWarnings("unchecked")
     public void testCreateUpdatePartyNode1Multi() {
     Callable task = new Callable() {
     @Override public Object call() throws Exception {
     testCreateUpdatePartyNode1();
     return null;
     }
     };
     executeMultiThreadedTest(10,task);
     }  */


}
