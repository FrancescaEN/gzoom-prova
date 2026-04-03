package it.memelabs.gn.xmlrpc;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.parser.TypeParser;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 11/01/13
 *
 * @author Andrea Fossi
 */
public class ObjectArrayParserProxy implements TypeParser {
    private TypeParser parser;

    public ObjectArrayParserProxy(TypeParser parser) {
        this.parser = parser;
    }

    @Override
    public Object getResult() throws XmlRpcException {
        Object result = parser.getResult();
        if (result != null && result instanceof Object[] && ((Object[]) result).length > 0) {
            return new ArrayList<Object>(Arrays.asList((Object[]) result));
        } else {
            return new ArrayList<Object>(0);
        }
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        parser.setDocumentLocator(locator);
    }

    @Override
    public void startDocument() throws SAXException {
        parser.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        parser.endDocument();
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        parser.startPrefixMapping(prefix, uri);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        parser.endPrefixMapping(prefix);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        parser.startElement(uri, localName, qName, atts);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        parser.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        parser.characters(ch, start, length);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        parser.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        parser.processingInstruction(target, data);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        parser.skippedEntity(name);
    }
}
