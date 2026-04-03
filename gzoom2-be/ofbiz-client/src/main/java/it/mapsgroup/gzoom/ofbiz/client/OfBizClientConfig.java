// OfBizClientConfig.java, created on 05/dic/2012
package it.mapsgroup.gzoom.ofbiz.client;

import java.net.URL;

/**
 * OfBiz client configuration interface.
 *
 * @author Fabio Strozzi
 */
public interface OfBizClientConfig {

    /**
     * Gets the XML-RPC server URL.
     *
     * @return
     */
    URL getServerXmlRpcUrl();

    default String getOfbizDefaultUser() {
        return null;
    }

    default String getOfbizDefaultPwd() {
        return null;
    }

    default String getOfbizServiceUser() {
        return null;
    }

    default String getOfbizServicePwd() {
        return null;
    }

}
