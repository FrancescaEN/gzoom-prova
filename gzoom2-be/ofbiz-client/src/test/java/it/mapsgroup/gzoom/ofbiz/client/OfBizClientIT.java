// PingTest.java, created on 05/dic/2012
package it.mapsgroup.gzoom.ofbiz.client;

import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests the ping client.
 * <p/>
 * <em>Beware OfBiz has to be up and running before starting this test.</em>
 *
 * @author Fabio Strozzi
 */
public class OfBizClientIT {
    private static Properties props = new Properties();
    private static OfBizClientConfig config;
    private OfBizClient client;

    @BeforeClass
    public static void setUpOnce() throws IOException {
        // configure client properties
        props.load(OfBizClientIT.class.getResourceAsStream("/client.properties"));
        config = new OfbizClientConfigImpl(props);
    }

    @Before
    public void setUp() {
        client = new OfBizClient(config, new SimpleHttpConnectionManager(true));
    }

    @Test
    public void testGnFind() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("login.username", "admin");
        paramMap.put("login.password", "ofbiz");
        //paramMap.put("VIEW_SIZE", (Integer)20);
        paramMap.put("entityName", "PartyAndUserLoginAndPerson");

        Map<String, Object> filterMap = new HashMap<String, Object>();
        filterMap.put("partyId", "sss");
        filterMap.put("statusId", "PARTY_ENABLED");
        List<Object> userLoginId = new ArrayList<Object>();
        userLoginId.add("sss1");
        userLoginId.add("sss2");
        filterMap.put("userLoginId", userLoginId);

        paramMap.put("entityFilterFields", filterMap);

        Map<String, Object> ret = client.execute("gnFindEntity", paramMap);
        ret.size();
        assertTrue("ok", true);
    }

    @Test
    public void testGnGetUserProfile() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("login.username", "admin");
        paramMap.put("login.password", "ofbiz");
        paramMap.put("userLoginId", "user1");
        //paramMap.put("VIEW_SIZE", (Integer)20);
//        paramMap.put("entityName", "PartyAndUserLoginAndPerson");
        Map<String, Object> ret = client.execute("gnGetUserProfile", paramMap);
        ret.size();
        assertTrue("ok", true);
    }

/*

    @Test
    public void testPing() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("login.username", "admin");
        paramMap.put("login.password", "ofbiz");
        //paramMap.put("VIEW_SIZE", (Integer)20);
        Map<String, Object> ret = client.execute("gnFindUsers", paramMap);
        ret.size();
        assertTrue("ok", true);
    }

    @Test
    public void testPing2() {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("login.username", "admin");
        paramMap.put("login.password", "ofbiz");
        //paramMap.put("VIEW_SIZE", (Integer)20);
        Map<String, Object> ret = client.execute("findParty", paramMap);
        ret.size();
        assertTrue("ok", true);
    }*/

}
