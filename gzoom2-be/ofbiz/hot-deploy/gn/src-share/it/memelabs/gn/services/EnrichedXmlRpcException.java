//EnrichedXmlRpcException.java, created on 16/ott/2013
package it.memelabs.gn.services;

import org.apache.xmlrpc.XmlRpcException;

/**
*@author Aldo Figlioli
*/
public class EnrichedXmlRpcException extends XmlRpcException {
    private static final long serialVersionUID = -4182514830622178991L;
    
    private Object data;

    public EnrichedXmlRpcException(int code, String message) {
        super(code, message);
    }

    public EnrichedXmlRpcException(int code, String message, Object data) {
        this(code, message);
        this.data = data;
    }
    
    public EnrichedXmlRpcException(String message, Throwable t) {
        super(message, t);
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public Object getData() {
        return data;
    }
}
