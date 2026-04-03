package it.mapsgroup.gzoom.ofbiz.client;

public interface GnPingOfBizClient {

    /**
     * Sample ping method.
     *
     * @param message
     * @param sessionId
     * @return
     */
    String ping(String sessionId, String message);

}