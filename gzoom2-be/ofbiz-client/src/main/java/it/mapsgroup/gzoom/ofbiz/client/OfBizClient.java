// OfBizClient.java, created on 05/dic/2012
package it.mapsgroup.gzoom.ofbiz.client;

import it.memelabs.gn.xmlrpc.TimestampParser;
import it.memelabs.gn.xmlrpc.TimestampSerializer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.ws.commons.util.NamespaceContextImpl;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.XmlRpcRequestConfig;
import org.apache.xmlrpc.client.*;
import org.apache.xmlrpc.common.TypeFactoryImpl;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.apache.xmlrpc.parser.ObjectArrayParser;
import org.apache.xmlrpc.parser.TypeParser;
import org.apache.xmlrpc.serializer.TypeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OfBiz RPC client.
 *
 * @author Fabio Strozzi
 */
public class OfBizClient {
    private static final Logger log = LoggerFactory.getLogger(OfBizClient.class);

    private OfBizClientConfig config;
    private XmlRpcClient client;
    private XmlRpcClientConfigImpl xmlRpcConfig;

    private final HttpConnectionManager connectionManager;

    public OfBizClient(OfBizClientConfig config) {
        this(config, null);
    }

    public OfBizClient(OfBizClientConfig config, HttpConnectionManager connectionManager) {
        this.config = config;

        this.connectionManager = connectionManager;

        this.client = new XmlRpcClient();
        this.client.setTypeFactory(new TypeFactoryImpl(client) {
            final TimestampSerializer timestampSerializer = new TimestampSerializer();

            @Override
            public TypeParser getParser(XmlRpcStreamConfig pConfig, NamespaceContextImpl pContext, String pURI, String pLocalName) {
                if (TimestampSerializer.TIMESTAMP_TAG.equals(pLocalName)) {
                    return new TimestampParser();
                }
                TypeParser parser = super.getParser(pConfig, pContext, pURI, pLocalName);
                if (parser instanceof ObjectArrayParser) {
                    parser = new it.memelabs.gn.xmlrpc.ObjectArrayParserProxy(parser);
                }
                return parser;
            }

            @Override
            public TypeSerializer getSerializer(XmlRpcStreamConfig pConfig, Object pObject) throws SAXException {
                if (pObject instanceof Timestamp || pObject instanceof Date) {
                    return timestampSerializer;
                } else
                    return super.getSerializer(pConfig, pObject);

            }
        });

        this.xmlRpcConfig = buildXmlRpcConfig(config);
        this.client.setConfig(xmlRpcConfig);


        XmlRpcTransportFactory xmlRpcTransportFactory = buildXmlRpcTransportFactory();
        this.client.setTransportFactory(xmlRpcTransportFactory);
    }

    private XmlRpcCommonsTransportFactory buildXmlRpcTransportFactory() {
        final XmlRpcCommonsTransportFactory xmlRpcCommonsTransportFactory = new XmlRpcCommonsTransportFactory(client) {


            @Override
            public XmlRpcTransport getTransport() {
                return new XmlRpcCommonsTransport(this) {
                    @Override
                    protected HttpClient newHttpClient() {

                        HttpClient httpClient;
                        if (connectionManager != null) {
                            httpClient = new HttpClient(connectionManager);
                        } else {
                            httpClient = new HttpClient();
                        }
                        return httpClient;
                    }

                    @Override
                    protected void initHttpHeaders(XmlRpcRequest pRequest) throws XmlRpcClientException {
                        super.initHttpHeaders(pRequest);
                        //method.getParams().setVersion(HttpVersion.HTTP_1_0);//disable  keep alive
                        if (pRequest instanceof LocalXmlRpcClientRequestImpl) {
                            String sessionId = ((LocalXmlRpcClientRequestImpl) pRequest).getSessionId();
                            if (sessionId != null && sessionId.length() > 0) {
                                log.trace("setting Cookie [JSESSIONID={}]", sessionId);
                                super.method.addRequestHeader("Cookie", "JSESSIONID=" + sessionId);
                            }
                            String contextId = ((LocalXmlRpcClientRequestImpl) pRequest).getContextId();
                            if (contextId != null && contextId.length() > 0) {
                                log.trace("setting contextId=[{}]", contextId);
                                super.method.addRequestHeader("contextId", contextId);
                            }
                        }
                    }
                };
            }
        };
        return xmlRpcCommonsTransportFactory;
    }

    /**
     * @param config
     * @return
     */
    private XmlRpcClientConfigImpl buildXmlRpcConfig(OfBizClientConfig config) {
        XmlRpcClientConfigImpl xmlRpcConfig = new XmlRpcClientConfigImpl();
        xmlRpcConfig.setServerURL(config.getServerXmlRpcUrl());
        xmlRpcConfig.setEnabledForExceptions(true);
        xmlRpcConfig.setEnabledForExtensions(true);

        return xmlRpcConfig;
    }

    /**
     * Gets the OfBiz client configuration.
     *
     * @return the config
     */
    protected OfBizClientConfig getConfig() {
        return config;
    }

    /**
     * Execute rpc method using anonymous or standard OFBiz authentication
     * service.
     *
     * @param method rpc method name
     * @param params contains method call parameters
     * @return response
     */
    public Map<String, Object> execute(String method, Object... params) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = (Map<String, Object>) client.execute(method, params);
            return result;
        } catch (XmlRpcException e) {
            log.error("OfBiz client failed when calling method '{}' due to: {} code {}", method, e.getMessage(), e.code);
            throw new OfBizClientException(e.code, e);
        }
    }

    /**
     * Execute rpc method using sessionId provided from OFBiz login service.
     *
     * @param pMethodName rpc method name
     * @param sessionId   current OFBiz http sessionId
     * @param pParams
     * @return response
     */
    public Map<String, Object> execute(String pMethodName, String sessionId, Object... pParams) {
        return execute(pMethodName, sessionId, null, pParams);
    }

    /**
     * Execute rpc method using sessionId provided from OFBiz login service.
     *
     * @param pMethodName rpc method name
     * @param sessionId   current OFBiz http sessionId
     * @param contextId   active context user
     * @param pParams
     * @return response
     */
    public Map<String, Object> execute(String pMethodName, String sessionId, String contextId, Object... pParams) {
        try {
            XmlRpcClientRequestImpl req = new LocalXmlRpcClientRequestImpl(this.xmlRpcConfig, pMethodName, sessionId, contextId, pParams);
            log.debug("call service[{}] with contextId[{}] and sessionId[{}]", pMethodName, contextId, sessionId);
            long start = System.currentTimeMillis();
            @SuppressWarnings("unchecked")
            Map<String, Object> result = (Map<String, Object>) client.execute(req);
            long callTime = System.currentTimeMillis() - start;
            if (log.isDebugEnabled()) {
                log.debug("service[{}] finished in [{}] milliseconds", pMethodName, callTime);
            } else if (callTime > 200) {
                log.info("service[{}] finished in [{}] milliseconds", pMethodName, callTime);
            }
            return result;
        } catch (XmlRpcException e) {
            log.error("OfBiz client failed when calling method '{}' due to: {} code {}", pMethodName, e.getMessage(), e.code);
            throw new OfBizClientException(e.code, e.getMessage(), e);
        }
    }

    /**
     * Local XmlRpcClient that provide support to sessionId used by OFBiz
     */
    private static class LocalXmlRpcClientRequestImpl extends XmlRpcClientRequestImpl {
        private String sessionId;
        private String contextId;

        /**
         * @param pConfig
         * @param pMethodName
         * @param sessionId
         * @param pParams
         */
        private LocalXmlRpcClientRequestImpl(XmlRpcRequestConfig pConfig, String pMethodName, String sessionId, Object[] pParams) {
            super(pConfig, pMethodName, pParams);
            this.sessionId = sessionId;
        }

        /**
         * @param pConfig
         * @param pMethodName
         * @param sessionId
         * @param contextId
         * @param pParams
         */
        private LocalXmlRpcClientRequestImpl(XmlRpcRequestConfig pConfig, String pMethodName, String sessionId, String contextId, Object[] pParams) {
            super(pConfig, pMethodName, pParams);
            this.sessionId = sessionId;
            this.contextId = contextId;
        }

        /**
         * @param pConfig
         * @param pMethodName
         * @param sessionId
         * @param pParams
         */
        private LocalXmlRpcClientRequestImpl(XmlRpcRequestConfig pConfig, String pMethodName, String sessionId, @SuppressWarnings("rawtypes") List pParams) {
            super(pConfig, pMethodName, pParams);
            this.sessionId = sessionId;
        }

        public String getSessionId() {
            return sessionId;
        }

        public String getContextId() {
            return contextId;
        }
    }
}
