// OfBizClientException.java, created on 05/dic/2012
package it.mapsgroup.gzoom.ofbiz.client;

import it.memelabs.gn.services.EnrichedXmlRpcException;

/**
 * Runtime exception thrown from the OfBiz client.
 *
 * @author Fabio Strozzi
 */
@SuppressWarnings("serial")
public class OfBizClientException extends RuntimeException {
    private int code;
    private Object data;

    /**
     *
     */
    public OfBizClientException() {
        super();
    }

    /**
     * @param message
     * @param cause
     */
    public OfBizClientException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public OfBizClientException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public OfBizClientException(Throwable cause) {
        super(cause);
    }

    public OfBizClientException(int code) {
        this.code = code;
    }

    public OfBizClientException(int code, String message) {
        super(message);
        this.code = code;
    }

    public OfBizClientException(int code, String message, Throwable th) {
        super(message, th);
        this.code = code;
        handleCause(th);
    }

    public OfBizClientException(int code, Throwable th) {
        super(th);
        this.code = code;
        handleCause(th);
    }

    public int getCode() {
        return code;
    }
    
    public Object getData() {
        return data;
    }
    
    private void handleCause(Throwable th) {
        if(th instanceof EnrichedXmlRpcException) {
            data = ((EnrichedXmlRpcException) th).getData();
        }
    }
}
