package it.mapsgroup.gzoom.ofbiz.client.impl;

import it.mapsgroup.gzoom.ofbiz.client.VersionOfBizClient;
import it.mapsgroup.gzoom.ofbiz.client.OfBizClient;
import it.mapsgroup.gzoom.ofbiz.client.OfBizClientConfig;
import org.apache.commons.httpclient.HttpConnectionManager;

import java.util.Map;

public class VersionOfBizClientImpl extends OfBizClient implements VersionOfBizClient {

    public VersionOfBizClientImpl(OfBizClientConfig config, HttpConnectionManager connectionManager) {
        super(config, connectionManager);
    }

    public VersionOfBizClientImpl(OfBizClientConfig config) {
        super(config);
    }

    @Override
    public Map<String, Object> getVersions() {
        Map<String, Object> result = execute("gzVersions");
        return result;
    }
}
