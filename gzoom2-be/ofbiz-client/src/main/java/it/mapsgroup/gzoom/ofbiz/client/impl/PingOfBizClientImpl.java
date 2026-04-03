// PingOfBizClient.java, created on 05/dic/2012
package it.mapsgroup.gzoom.ofbiz.client.impl;

import it.mapsgroup.gzoom.ofbiz.client.OfBizClientConfig;
import it.mapsgroup.gzoom.ofbiz.client.OfBizClient;
import it.mapsgroup.gzoom.ofbiz.client.PingOfBizClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * OfBiz pinging client.
 *
 * @author Fabio Strozzi
 */
public class PingOfBizClientImpl extends OfBizClient implements PingOfBizClient {
    private static final Logger log = LoggerFactory.getLogger(PingOfBizClientImpl.class);

    /**
     * @param config
     * @param connectionManager
     */
    public PingOfBizClientImpl(OfBizClientConfig config, HttpConnectionManager connectionManager) {
        super(config, connectionManager);
    }

    public PingOfBizClientImpl(OfBizClientConfig config) {
        super(config);
    }

    /* (non-Javadoc)
         * @see it.memelabs.greennebula.ofbiz.client.impl.PingOfBizClient#ping(java.lang.String)
         */
    @Override
    public String ping(String message) {
        Map<String, String> paramMap = new HashMap<String, String>();

        paramMap.put("message", message);

        Map<String, Object> result = execute("ping", paramMap);
        String resp = (String) result.get("message");
        log.debug("Ping reply message: {}", resp);

        return resp;
    }
}
