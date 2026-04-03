// Ping2OfBizClient.java, created on 14/dic/2012
package it.mapsgroup.gzoom.ofbiz.client.impl;

import it.mapsgroup.gzoom.ofbiz.client.GnPingOfBizClient;
import it.mapsgroup.gzoom.ofbiz.client.OfBizClient;
import it.mapsgroup.gzoom.ofbiz.client.OfBizClientConfig;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * OfBiz pinging client.
 *
 * @author Andrea Fossi
 */
public class GnPingOfBizClientImpl extends OfBizClient implements GnPingOfBizClient {
    private static final Logger log = LoggerFactory.getLogger(GnPingOfBizClientImpl.class);

    /**
     * @param config
     * @param connectionManager
     */
    public GnPingOfBizClientImpl(OfBizClientConfig config, HttpConnectionManager connectionManager) {
        super(config, connectionManager);
    }

    public GnPingOfBizClientImpl(OfBizClientConfig config) {
        super(config);
    }

    /* (non-Javadoc)
         * @see it.memelabs.greennebula.ofbiz.client.impl.GnPingOfBizClient#ping(java.lang.String, java.lang.String)
         */
    @Override
    public String ping(String sessionId, String message) {
        Map<String, String> paramMap = new HashMap<String, String>();

        paramMap.put("message", message);

        Map<String, Object> result = execute("gnPing", sessionId, paramMap);
        String resp = (String) result.get("message");
        log.debug("Ping reply message: {}", resp);

        return resp;
    }
}
