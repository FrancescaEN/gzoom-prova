package it.mapsgroup.gzoom.ofbiz.client;

import it.mapsgroup.gzoom.ofbiz.client.impl.AuthenticationOfBizClientImpl;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 29/08/13
 *
 * @author Andrea Fossi
 */
public class AbstractRollBackOfBizTest {

    private static Properties props = new Properties();
    private static OfBizClientConfig config;
    private AuthenticationOfBizClient loginClient;
    private static SimpleHttpConnectionManager connectionManager;

    private static final Logger log = LoggerFactory
            .getLogger(AbstractOfBizTest.class);

    /*@Before
    public void setUp() throws Exception {
        rollbackDb();
    }*/

    @After
    public void tearDown() throws Exception {
        rollbackDb();
        connectionManager.shutdown();
    }

    protected void rollbackDb() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        long start = System.currentTimeMillis();
        loginClient.rollbackDb();
        long time = System.currentTimeMillis() - start;
        log.info("Roll back done in [{}]ms", time);
    }

    @BeforeClass
    public static void setUpOnce2() throws IOException {
        // configure client properties
        props.load(AbstractOfBizTest.class.getResourceAsStream("/client.properties"));
        config = new OfbizClientConfigImpl(props);
    }


    @Before
    public void setUpLoginClient2() throws Exception {
        connectionManager = new SimpleHttpConnectionManager(true);
        loginClient = new AuthenticationOfBizClientImpl(config, connectionManager);
        rollbackDb();
    }
}
