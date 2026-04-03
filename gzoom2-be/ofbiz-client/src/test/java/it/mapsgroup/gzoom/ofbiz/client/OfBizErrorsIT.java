package it.mapsgroup.gzoom.ofbiz.client;

import it.memelabs.gn.services.OfbizErrors;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * 11/11/13
 *
 * @author Andrea Fossi
 */
public class OfBizErrorsIT extends AbstractOfBizTest {


   // private AuthorizationOfBizClient authOfBizClient;

    private static final Logger log = LoggerFactory.getLogger(OfBizErrorsIT.class);

    @Before
    public void setUp() {
        //authOfBizClient = new AuthorizationOfBizClientImpl(config, connectionManager);
    }

    /**
     * {@link OfbizErrors#INVALID_PARAMETERS}
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testInvalidParameters() {
        String sessionId = login("memepooperuser", "ofbiz");
        String contextId = "GN_C_UOSEDE_MMPO_PUB";

        try {
            try {
             //   Map<String, Object> authorization = AuthorizationResources.getAuthorization(AuthorizationTypesOfbiz.GN_AUTH_ALBO);
            //    authorization.remove("description");
             //   Map<String, Object> saveResult = authOfBizClient.save(authorization, sessionId, contextId);
                assertFalse("service should return an exception", true);
            } catch (OfBizClientException e) {
                assertThat("invalid parameters", e.getCode(), is(OfbizErrors.INVALID_PARAMETERS.ordinal()));
            }


        } finally {
            logOff(sessionId);
        }
    }

    /**
     * Test Authorization creation
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testSavePrivateAuthorization1() {
        final String sessionId = login("memepooperuser", "ofbiz");
        final String contextId = "GN_C_UOTERZ_MEMETOSC";

        try {
            try {
              //  Map<String, Object> authorization = AuthorizationResources.getPrivateAuthorization(AuthorizationTypesOfbiz.GN_AUTH_ALBO);
            //    MapUtil.setValue(authorization, "partyNodeTo.groupName", null);
            //    Map<String, Object> saveResult = authOfBizClient.save(authorization, sessionId, contextId);

                assertFalse("service should return an exception", true);
            } catch (OfBizClientException e) {
                assertThat("invalid parameters", e.getCode(), is(OfbizErrors.INVALID_PARAMETERS.ordinal()));
            }
        } finally {
            logOff(sessionId);
        }
    }

}
